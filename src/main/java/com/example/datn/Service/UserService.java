
package com.example.datn.Service;

import com.example.datn.DTO.TrangMuaHang.ChangePasswordRequestDTO; // <-- BỔ SUNG IMPORT
import com.example.datn.DTO.TrangMuaHang.UserDTO;
import com.example.datn.Model.Role;
import com.example.datn.Model.Users;
import com.example.datn.Repository.RoleRepository;
import com.example.datn.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder; // <-- BỔ SUNG IMPORT
import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- BỔ SUNG IMPORT
import org.springframework.security.crypto.password.PasswordEncoder; // <-- BỔ SUNG IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UsersRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // <-- BỔ SUNG DEPENDENCY

    // ===================================
    // === PHƯƠNG THỨC VALIDATE (GIỮ NGUYÊN) ===
    // ===================================

    // ... (Tất cả các hàm validateString, validateUserDTO, validateUniqueness giữ nguyên) ...

    /**
     * Định nghĩa các hằng số REGEX
     */
    // Chỉ cho phép chữ, số, và các ký tự an toàn như _ . -
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_.-]*$";
    // Cho phép chữ cái (bao gồm tiếng Việt có dấu) và số
    // 2. Cho phép chữ cái (bao gồm tiếng Việt có dấu) và số (SỬA LỖI Ở ĐÂY)
    private static final String NAME_REGEX = "^[a-zA-Z0-9\\p{L} ]*$";
    // 3. Cho phép các ký tự thông dụng trong địa chỉ (/, . , -) (SỬA LỖI Ở ĐÂY)
    private static final String ADDRESS_REGEX = "^[a-zA-Z0-9\\p{L} .,/-]*$";
    // Bắt buộc phải là @gmail.com
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
    // Chỉ cho phép 10 số, bắt đầu bằng 0
    private static final String PHONE_REGEX = "^0\\d{9}$";

    /**
     * Hàm helper để validate các trường String
     */
    private void validateString(String value, String fieldName, int maxLength, String regex, String regexErrorMsg) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " không được rỗng");
        }
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " không quá " + maxLength + " kí tự");
        }
        if (regex != null && !value.matches(regex)) {
            throw new IllegalArgumentException(regexErrorMsg);
        }
    }

    /**
     * Validate toàn bộ DTO
     */
    private void validateUserDTO(UserDTO dto) {
        // Validate các trường String
        validateString(dto.getUsername(), "Username", 100, USERNAME_REGEX, "Username không được chứa kí tự đặc biệt");
        validateString(dto.getTenHienThi(), "Tên hiển thị", 100, NAME_REGEX, "Tên hiển thị không chứa kí tự đặc biệt");
        validateString(dto.getHoTen(), "Họ tên", 100, NAME_REGEX, "Họ tên không chứa kí tự đặc biệt");
        validateString(dto.getDiaChiGiaoHang(), "Địa chỉ giao hàng", 100, ADDRESS_REGEX, "Địa chỉ chứa kí tự không hợp lệ");

        // Validate Email (có regex riêng)
        validateString(dto.getEmail(), "Email", 100, EMAIL_REGEX, "Email phải có đuôi @gmail.com");

        // Validate Số điện thoại (có logic riêng)
        if (dto.getSoDienThoai() == null || dto.getSoDienThoai().isBlank()) {
            throw new IllegalArgumentException("Số điện thoại không được rỗng");
        }
        if (dto.getSoDienThoai().length() != 10) {
            throw new IllegalArgumentException("Số điện thoại phải có đúng 10 chữ số");
        }
        if (!dto.getSoDienThoai().matches(PHONE_REGEX)) {
            throw new IllegalArgumentException("Số điện thoại phải là 10 số, bắt đầu bằng 0 và không chứa chữ");
        }

        // Validate các trường không phải String
        if (dto.getNgaySinh() == null) {
            throw new IllegalArgumentException("Ngày sinh không được rỗng");
        }
        if (dto.getGioiTinh() == null) {
            throw new IllegalArgumentException("Giới tính không được rỗng");
        }
        if (dto.getRoleId() == null) {
            throw new IllegalArgumentException("ID Role không được rỗng");
        }
    }

    /**
     * Validate logic nghiệp vụ (như tính duy nhất)
     * @param id ID của user (null nếu là 'create', có giá trị nếu là 'update')
     */
    private void validateUniqueness(UserDTO dto, Integer id) {
        // Kiểm tra username đã tồn tại
        userRepository.findByUsername(dto.getUsername()).ifPresent(user -> {
            // Nếu (đang tạo mới) HOẶC (đang cập nhật VÀ user tìm thấy có ID khác user đang sửa)
            if (id == null || !user.getId().equals(id)) {
                throw new IllegalArgumentException("Username '" + dto.getUsername() + "' đã tồn tại");
            }
        });

        // Kiểm tra email đã tồn tại (Cần thêm findByEmail vào Repository)
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            if (id == null || !user.getId().equals(id)) {
                throw new IllegalArgumentException("Email '" + dto.getEmail() + "' đã tồn tại");
            }
        });
    }

    // ===================================
    // === MAPPING (GIỮ NGUYÊN) ===
    // ===================================

    // Entity -> DTO (Giữ nguyên)
    private UserDTO toDTO(Users e) {
        return new UserDTO(e);
    }

    // mapDtoToEntity (Giữ nguyên)
    private void mapDtoToEntity(UserDTO dto, Users e) {
        e.setTenHienThi(dto.getTenHienThi());
        e.setUsername(dto.getUsername());
        e.setHoTen(dto.getHoTen());
        e.setNgaySinh(dto.getNgaySinh());
        e.setGioiTinh(dto.getGioiTinh());
        e.setEmail(dto.getEmail());
        e.setSoDienThoai(dto.getSoDienThoai());
        e.setDiaChiGiaoHang(dto.getDiaChiGiaoHang());

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy role id = " + dto.getRoleId()));
            e.setRole(role);
        }
    }


    // ===================================
    // === CRUD (GIỮ NGUYÊN) ===
    // ===================================

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    public UserDTO findById(Integer id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user id = " + id));
    }

    @Transactional
    public UserDTO create(UserDTO dto) {
        // --- BƯỚC VALIDATE ---
        validateUserDTO(dto); // 1. Validate định dạng
        validateUniqueness(dto, null); // 2. Validate nghiệp vụ (null = tạo mới)
        // --- KẾT THÚC VALIDATE ---

        Users newUser = new Users();
        mapDtoToEntity(dto, newUser);
        // !!! LƯU Ý: Bạn cần xử lý set mật khẩu ban đầu ở đây
        // Ví dụ: newUser.setPasswords(passwordEncoder.encode("passwordMacDinh"));
        // Hoặc thêm trường password vào UserDTO (không khuyến khích)
        // Tốt nhất là dùng một RegisterDTO riêng cho việc tạo user.
        return toDTO(userRepository.save(newUser));
    }

    @Transactional
    public UserDTO update(Integer id, UserDTO dto) {
        // --- BƯỚC VALIDATE ---
        validateUserDTO(dto); // 1. Validate định dạng
        validateUniqueness(dto, id); // 2. Validate nghiệp vụ (truyền id để update)
        // --- KẾT THÚC VALIDATE ---

        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user id = " + id));

        mapDtoToEntity(dto, existingUser);
        return toDTO(userRepository.save(existingUser));
    }


    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy user id = " + id + " để xóa");
        }
        userRepository.deleteById(id);
    }

    // ===================================
    // === PHƯƠNG THỨC ĐỔI MẬT KHẨU (MỚI) ===
    // ===================================

    @Transactional
    public void changePassword(ChangePasswordRequestDTO dto) {
        // 1. Lấy username của người dùng đang đăng nhập
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));

        // 2. Validate mật khẩu mới
        if (dto.getNewPassword() == null || dto.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("Mật khẩu mới không được rỗng");
        }
        if (dto.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        // 3. Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác");
        }

        // 4. Mã hóa và cập nhật mật khẩu mới
        user.setPasswords(passwordEncoder.encode(dto.getNewPassword()));

        // 5. Lưu lại
        userRepository.save(user);
    }


    // ===================
    // === CÁC PHƯƠNG THỨC KHÁC (GIỮ NGUYÊN) ===
    // ===================

    // Phân trang (Giữ nguyên)
    public Page<UserDTO> getPage(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .map(this::toDTO);
    }

    // Tìm theo username (Giữ nguyên)
    public UserDTO findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với username = " + username));
    }

}