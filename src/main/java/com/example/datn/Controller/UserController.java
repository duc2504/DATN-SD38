//package com.example.datn.Controller;
//
//
//
//import com.example.datn.Config.JwtUtil;
//import com.example.datn.DTO.TrangMuaHang.UserDTO;
//
//import com.example.datn.Service.UserService;
//import io.jsonwebtoken.Claims;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    private final UserService userService;
//
//    private final JwtUtil jwtUtil;
//
//
//    // Lấy tất cả user
//    @GetMapping
//    public ResponseEntity<List<UserDTO>> getAll() {
//        return ResponseEntity.ok(userService.findAll());
//    }
//
//    // Lấy user theo id
//    @GetMapping("/{id}")
//    public ResponseEntity<UserDTO> getById(@PathVariable Integer id) {
//        return ResponseEntity.ok(userService.findById(id));
//    }
//
//    // Thêm mới user
//    @PostMapping
//    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
//        return ResponseEntity.ok(userService.save(dto));
//    }
//
//    // Cập nhật user
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDTO> update(@PathVariable Integer id, @RequestBody UserDTO dto) {
//        dto.setId(id); // gán id để update
//        return ResponseEntity.ok(userService.save(dto));
//    }
//
//    // Xóa user
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Integer id) {
//        userService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//    // ✅ Lấy thông tin user từ token
//    @GetMapping("/me")
//    public ResponseEntity<Object> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
//        try {
//            String token = authHeader.replace("Bearer ", "");
//            Claims claims = jwtUtil.extractAllClaims(token);
//
//            String username = claims.getSubject();
//            String role = claims.get("role", String.class);
//
//            UserDTO userDTO = userService.findByUsername(username);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("username", username);
//            response.put("role", role);
//            response.put("issuedAt", claims.getIssuedAt());
//            response.put("expiration", claims.getExpiration());
//            response.put("user", userDTO);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");
//        }
//    }
//}


package com.example.datn.Controller;

import com.example.datn.Config.JwtUtil;

import com.example.datn.DTO.TrangMuaHang.ChangePasswordRequestDTO;
import com.example.datn.DTO.TrangMuaHang.UserDTO;
import com.example.datn.Service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Lấy tất cả user (Không thay đổi)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // Lấy user theo id (Không thay đổi)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // Thêm mới user (Không thay đổi)
    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        UserDTO createdUser = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Cập nhật user (Không thay đổi)
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Integer id, @RequestBody UserDTO dto) {
        UserDTO updatedUser = userService.update(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    // Xóa user (Không thay đổi)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    // Lấy thông tin user từ token (Không thay đổi)
    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Claims claims = jwtUtil.extractAllClaims(token);

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            UserDTO userDTO = userService.findByUsername(username);

            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("role", role);
            response.put("issuedAt", claims.getIssuedAt());
            response.put("expiration", claims.getExpiration());
            response.put("user", userDTO);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ!");
        }
    }

    // ===================================
    // === ENDPOINT ĐỔI MẬT KHẨU (MỚI) ===
    // ===================================

    /**
     * Endpoint để người dùng tự đổi mật khẩu.
     * Lấy user từ token (SecurityContext) nên an toàn.
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO dto) {
        try {
            userService.changePassword(dto);
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } catch (IllegalArgumentException | UsernameNotFoundException e) {
            // Bắt các lỗi validate hoặc nghiệp vụ
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Bắt các lỗi chung
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }
}