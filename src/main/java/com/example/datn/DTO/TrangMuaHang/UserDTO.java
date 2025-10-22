//package com.example.datn.DTO.TrangMuaHang;
//
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import lombok.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class UserDTO {
//    private Integer id;
//
//    private String tenHienThi;
//    private String username;
//    private String hoTen;
//    private Integer gioiTinh;
//    private String email;
//    private String soDienThoai;
//    private String diaChiGiaoHang;
//    private Integer roleId;   // chỉ lấy ID role
//
//    private String roleName;
//
//    public UserDTO(Integer id,String tenHienThi, String username, String hoTen, Integer gioiTinh, String email, String soDienThoai, String diaChiGiaoHang, String roleName) {
//        this.id = id;
//        this.tenHienThi = tenHienThi;
//        this.username = username;
//        this.hoTen = hoTen;
//        this.gioiTinh = gioiTinh;
//        this.email = email;
//        this.soDienThoai = soDienThoai;
//        this.diaChiGiaoHang = diaChiGiaoHang;
//        this.roleName = roleName;
//    }
//
//
//
//
//
//}


package com.example.datn.DTO.TrangMuaHang;

import com.example.datn.Model.Users; // Import class entity
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate; // Sử dụng LocalDate cho ngày sinh

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Integer id;

    // --- Thông tin đăng nhập & Hiển thị ---
    private String username;
    private String tenHienThi;

    // --- Thông tin cá nhân ---
    private String hoTen;
    private LocalDate ngaySinh; // <-- BỔ SUNG TRƯỜNG CÒN THIẾU
    private Integer gioiTinh; // 1=Nam, 0=Nữ

    // --- Thông tin liên lạc ---
    private String email;
    private String soDienThoai;
    private String diaChiGiaoHang;

    // --- Thông tin vai trò ---
    private Integer roleId;   // Dùng khi cập nhật
    private String roleName; // Dùng khi hiển thị

    /**
     * Constructor tùy chỉnh (đã cập nhật)
     */
    public UserDTO(Integer id, String tenHienThi, String username, String hoTen, LocalDate ngaySinh, Integer gioiTinh, String email, String soDienThoai, String diaChiGiaoHang, String roleName) {
        this.id = id;
        this.tenHienThi = tenHienThi;
        this.username = username;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh; // <-- Cập nhật
        this.gioiTinh = gioiTinh;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.roleName = roleName;
    }

    /**
     * Constructor tiện ích: Chuyển đổi từ Entity Users sang UserDTO
     * (Rất hữu ích trong tầng Service)
     */
    public UserDTO(Users user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.tenHienThi = user.getTenHienThi();
        this.hoTen = user.getHoTen();
        this.ngaySinh = user.getNgaySinh();
        this.gioiTinh = user.getGioiTinh();
        this.email = user.getEmail();
        this.soDienThoai = user.getSoDienThoai();
        this.diaChiGiaoHang = user.getDiaChiGiaoHang();
        if (user.getRole() != null) {
            this.roleId = user.getRole().getId();
            this.roleName = user.getRole().getRoleName();
        }
    }
}