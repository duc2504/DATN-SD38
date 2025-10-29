package com.example.datn.Model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codeVoucher", length = 50, nullable = false, unique = true)
    private String codeVoucher;

    @Column(name = "tenVoucher", length = 100)
    private String tenVoucher;

    @Column(name = "soLanSuDung")
    private Integer soLanSuDung;

    @Column(name = "moTa", length = 255)
    private String moTa;


    @Column(name = "loaiGiam")
    private Integer loaiGiam;


    @Column(name = "giaTriGiam")
    private BigDecimal giaTriGiam;

    @Column(name = "dieuKienGiam")
    private BigDecimal dieuKienGiam;

    @Column(name = "giamToiDa")
    private BigDecimal giamToiDa;

    @Column(name = "ngayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngayKetThuc")
    private LocalDateTime ngayKetThuc;


    @Column(name = "soLuongToiDa")
    private Integer soLuongToiDa; // Dùng Integer để cho phép giá trị NULL

    @Column(name = "soLuongDaNhan")
    private Integer soLuongDaNhan = 0; // Đặt giá trị mặc định là 0
    @Column(name = "trangThai")
    private Integer trangThai ;

    // Quan hệ 1 Voucher có thể phát cho nhiều User
    @OneToMany(mappedBy = "voucher")
    private List<UserVoucher> userVouchers;


}
