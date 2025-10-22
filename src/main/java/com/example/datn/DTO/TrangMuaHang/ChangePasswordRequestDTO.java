package com.example.datn.DTO.TrangMuaHang;

// Trong package com.example.datn.DTO


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {
    // Dùng String username để xác định user,
    // hoặc bạn có thể lấy user từ context bảo mật (nếu dùng Spring Security)
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}