package com.example.datn.Controller;





        import com.example.datn.DTO.TrangMuaHang.UserDTO;
        import com.example.datn.Service.UserService;
        import lombok.RequiredArgsConstructor;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

/**
 * Controller này quản lý các API CRUD cho Khách hàng (Users có Role ID = 1)
 */
@RestController
@RequestMapping("/api/khachhang")
@RequiredArgsConstructor
public class KhachHangController {



    private final UserService userService;

    // Định nghĩa Role ID cho Khách hàng (USER)
    private static final Integer CUSTOMER_ROLE_ID = 1;

    /**
     * CREATE: Tạo một khách hàng mới.
     * Endpoint: POST /api/customers
     * Luôn ép roleId = 1 khi tạo.
     */
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody UserDTO dto) {
        try {
            // Quan trọng: Ép Role ID là 1 (Customer) để đảm bảo an toàn
            dto.setRoleId(CUSTOMER_ROLE_ID);

            // !!! LƯU Ý: Bạn cần set mật khẩu mặc định trong hàm create() của Service
            // Ví dụ: passwordEncoder.encode("passwordMacDinh")
            UserDTO newUser = userService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (IllegalArgumentException e) {
            // Lỗi validate (vd: trùng email, username, sai định dạng)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server khi tạo khách hàng: " + e.getMessage());
        }
    }

    /**
     * READ (All): Lấy tất cả khách hàng (role = 1).
     * Endpoint: GET /api/customers
     * Sử dụng hàm 'findAllUsers_RoleUser' từ Service.
     */
    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<UserDTO> customers = userService.findAllUsers_RoleUser();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
    }

    /**
     * READ (One): Lấy thông tin chi tiết của 1 khách hàng.
     * Endpoint: GET /api/customers/{id}
     * Chỉ trả về nếu user tìm thấy có role = 1.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        try {
            UserDTO user = userService.findById(id);

            // Kiểm tra: Phải đúng là khách hàng
            if (!user.getRoleId().equals(CUSTOMER_ROLE_ID)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy khách hàng với ID: " + id);
            }

            return ResponseEntity.ok(user);
        } catch (RuntimeException e) { // Bắt lỗi "Không tìm thấy" từ service
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * UPDATE: Cập nhật thông tin khách hàng.
     * Endpoint: PUT /api/customers/{id}
     * Chỉ cho phép cập nhật nếu user là role = 1.
     */
    /**
     * UPDATE: Cập nhật thông tin khách hàng.
     * Endpoint: PUT /api/customers/{id}
     * Chỉ cho phép cập nhật nếu user là role = 1.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody UserDTO dto) {
        try {
            // Bước 1: Kiểm tra xem user tồn tại có phải là khách hàng không
            UserDTO existingUser = userService.findById(id);

            // SỬA LỖI LOGIC: Thêm dấu "!" (NOT)
            // Phải là: "Nếu user KHÔNG PHẢI là khách hàng, thì báo lỗi"
            if (!existingUser.getRoleId().equals(CUSTOMER_ROLE_ID)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy khách hàng với ID: " + id);
            }

            // Bước 2: Ép roleId = 1 trong DTO gửi lên để ngăn việc "nâng quyền"
            dto.setRoleId(CUSTOMER_ROLE_ID);

            UserDTO updatedUser = userService.update(id, dto);
            return ResponseEntity.ok(updatedUser);

            // SỬA LỖI THỨ TỰ CATCH: Đảo 2 khối catch
        } catch (IllegalArgumentException e) { // <-- ĐƯA LÊN TRÊN
            // Lỗi validate
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) { // <-- ĐƯA XUỐNG DƯỚI
            // Lỗi không tìm thấy
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * DELETE: Xóa một khách hàng.
     * Endpoint: DELETE /api/customers/{id}
     * Chỉ cho phép xóa nếu user là role = 1.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
        try {
            // Bước 1: Kiểm tra xem user tồn tại có phải là khách hàng không
            UserDTO existingUser = userService.findById(id);
            if (!existingUser.getRoleId().equals(CUSTOMER_ROLE_ID)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy khách hàng với ID: " + id);
            }

            // Bước 2: Tiến hành xóa
            userService.delete(id);
            return ResponseEntity.ok("Đã xóa thành công khách hàng ID: " + id);
        } catch (RuntimeException e) { // Lỗi không tìm thấy
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}