//package com.example.datn.Model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//@Entity
//@Table(name = "Users")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Users implements UserDetails {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    private String tenHienThi;
//    private String username;
//    private String passwords;
//    private String hoTen;
//    private Integer gioiTinh; // 1=Nam, 0=Nữ
//    private String email;
//    private String soDienThoai;
//    private String diaChiGiaoHang;
//
//    @ManyToOne
//    @JoinColumn(name = "roleID")
//    private Role role;
//
//    @OneToMany(mappedBy = "user")
//    private List<SanPham> sanPhamList;
//
//    @OneToMany(mappedBy = "user")
//    private List<GioHang> gioHangList;
//
//
//    @OneToMany(mappedBy = "user")
//    private List<DonHang> donHangList;
//
//
//    @OneToMany(mappedBy = "user")
//    private List<UserVoucher> userVoucherList;
//
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // Trả về role dạng GrantedAuthority
//        return Collections.singleton(() -> "ROLE_" + role.getRoleName());
//    }
//
//
//    @Override
//    public String getPassword() {
//        return passwords;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    // Các phương thức còn lại (mặc định true)
//    @Override
//    public boolean isAccountNonExpired() { return true; }
//
//    @Override
//    public boolean isAccountNonLocked() { return true; }
//
//    @Override
//    public boolean isCredentialsNonExpired() { return true; }
//
//    @Override
//    public boolean isEnabled() { return true; }
//}



package com.example.datn.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate; // Sử dụng LocalDate cho kiểu 'date'
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // --- Thông tin đăng nhập & Hiển thị ---
    @Column(name = "username")
    private String username;

    @Column(name = "passwords")
    private String passwords;

    @Column(name = "tenHienThi")
    private String tenHienThi;

    // --- Thông tin cá nhân ---
    @Column(name = "hoTen")
    private String hoTen;

    @Column(name = "ngaySinh") // <-- BỔ SUNG TRƯỜNG CÒN THIẾU
    private LocalDate ngaySinh;

    @Column(name = "gioiTinh")
    private Integer gioiTinh; // 1=Nam, 0=Nữ

    // --- Thông tin liên lạc ---
    @Column(name = "email")
    private String email;

    @Column(name = "soDienThoai")
    private String soDienThoai;

    @Column(name = "diaChiGiaoHang")
    private String diaChiGiaoHang;

    // --- Quan hệ Role (Bảo mật) ---
    @ManyToOne
    @JoinColumn(name = "roleID")
    private Role role;

    // --- Các quan hệ Một-Nhiều ---
    @OneToMany(mappedBy = "user")
    private List<SanPham> sanPhamList;

    @OneToMany(mappedBy = "user")
    private List<GioHang> gioHangList;

    @OneToMany(mappedBy = "user")
    private List<DonHang> donHangList;

    @OneToMany(mappedBy = "user")
    private List<UserVoucher> userVoucherList;


    // ======================================================
    // === Triển khai các phương thức của UserDetails (Spring Security) ===
    // ======================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Thêm kiểm tra null để tránh NullPointerException
        if (role == null) {
            return Collections.emptyList();
        }
        // Trả về role dạng GrantedAuthority
        return Collections.singleton(() -> "ROLE_" + role.getRoleName());
    }

    @Override
    public String getPassword() {
        return passwords;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Các phương thức còn lại (mặc định true)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}