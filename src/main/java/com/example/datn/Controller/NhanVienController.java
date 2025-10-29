package com.example.datn.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import com.example.datn.DTO.TrangMuaHang.UserDTO;
import com.example.datn.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Controller này quản lý các API CRUD cho Nhân viên (Role ID = 3) và Admin (Role ID = 2)
 */
@RestController
@RequestMapping("/api/nhanvien")
@RequiredArgsConstructor
public class NhanVienController {

    private final UserService userService;

    // Định nghĩa các Role ID được phép quản lý trong controller này
    private static final Integer ADMIN_ROLE_ID = 2;
    private static final Integer STAFF_ROLE_ID = 3;
    private static final List<Integer> ALLOWED_ROLES = Arrays.asList(ADMIN_ROLE_ID, STAFF_ROLE_ID);

    /**
     * Helper private để kiểm tra Role ID
     */
    private boolean isAllowedRole(Integer roleId) {
        return roleId != null && ALLOWED_ROLES.contains(roleId);
    }

    /**
     * CREATE: Tạo một nhân viên/admin mới.
     * Endpoint: POST /api/nhanvien
     * Bắt buộc roleId phải là 2 hoặc 3.
     */
    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody UserDTO dto) {
        try {
            // Quan trọng: Kiểm tra Role ID phải là 2 hoặc 3
            if (!isAllowedRole(dto.getRoleId())) {
                return ResponseEntity.badRequest()
                        .body("Tạo thất bại: Role ID phải là " + ADMIN_ROLE_ID + " (ADMIN) hoặc " + STAFF_ROLE_ID + " (STAFF).");
            }

            // !!! LƯU Ý: Bạn cần set mật khẩu mặc định trong hàm create() của Service
            UserDTO newUser = userService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (IllegalArgumentException e) {
            // Lỗi validate (vd: trùng email, username, sai định dạng)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server khi tạo nhân viên: " + e.getMessage());
        }
    }

    /**
     * READ (All): Lấy tất cả nhân viên và admin (role = 2, 3).
     * Endpoint: GET /api/nhanvien
     * Sử dụng hàm 'findAllUsers_RoleAdminAndStaff' từ Service.
     */
    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        try {
            List<UserDTO> employees = userService.findAllUsers_RoleAdminAndStaff();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy danh sách nhân viên: " + e.getMessage());
        }
    }

    /**
     * READ (One): Lấy thông tin chi tiết của 1 nhân viên/admin.
     * Endpoint: GET /api/nhanvien/{id}
     * Chỉ trả về nếu user tìm thấy có role = 2 hoặc 3.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Integer id) {
        try {
            UserDTO user = userService.findById(id);

            // Kiểm tra: Phải đúng là nhân viên hoặc admin
            if (!isAllowedRole(user.getRoleId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy nhân viên với ID: " + id);
            }

            return ResponseEntity.ok(user);
        } catch (RuntimeException e) { // Bắt lỗi "Không tìm thấy" từ service
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * UPDATE: Cập nhật thông tin nhân viên/admin.
     * Endpoint: PUT /api/nhanvien/{id}
     * Chỉ cho phép cập nhật nếu user là role = 2 hoặc 3.
     */


    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer id, @RequestBody UserDTO dto) {
        try {
            // Bước 1: Kiểm tra user tồn tại có phải là nhân viên/admin không
            UserDTO existingUser = userService.findById(id);
            if (!isAllowedRole(existingUser.getRoleId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy nhân viên với ID: " + id);
            }

            // Bước 2: Kiểm tra DTO gửi lên cũng phải có role 2 hoặc 3
            if (!isAllowedRole(dto.getRoleId())) {
                return ResponseEntity.badRequest()
                        .body("Cập nhật thất bại: Role ID phải là " + ADMIN_ROLE_ID + " hoặc " + STAFF_ROLE_ID + ".");
            }

            UserDTO updatedUser = userService.update(id, dto);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) { // <-- SỬA 1: ĐƯA LÊN TRÊN
            // Lỗi validate (vd: trùng email, sai định dạng)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) { // <-- SỬA 2: ĐƯA XUỐNG DƯỚI
            // Lỗi không tìm thấy (từ userService.findById)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * DELETE: Xóa một nhân viên/admin.
     * Endpoint: DELETE /api/nhanvien/{id}
     * Chỉ cho phép xóa nếu user là role = 2 hoặc 3.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer id) {
        try {
            // Bước 1: Kiểm tra user tồn tại có phải là nhân viên/admin không
            UserDTO existingUser = userService.findById(id);
            if (!isAllowedRole(existingUser.getRoleId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy nhân viên với ID: " + id);
            }

            // Bước 2: Tiến hành xóa
            userService.delete(id);
            return ResponseEntity.ok("Đã xóa thành công nhân viên ID: " + id);
        } catch (RuntimeException e) { // Lỗi không tìm thấy
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /* * API GET /api/nhanvien/customers đã bị xóa khỏi đây.
     * Nó nên nằm trong CustomerController hoặc một controller quản lý chung.
     * Giữ controller này chỉ tập trung vào Nhân viên (Role 2, 3).
     */
}