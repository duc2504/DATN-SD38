package com.example.datn.DTO.TrangMuaHang;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDTO {
    private Integer id;
    private String codeVoucher;
    private String tenVoucher;
    private Integer soLanSuDung;
    private String moTa;
    private Integer loaiGiam;

    private BigDecimal giaTriGiam;
    private BigDecimal dieuKienGiam;
    private BigDecimal giamToiDa;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;

    private Integer trangThai;

    // --- BỔ SUNG 2 TRƯỜNG MỚI ---
    private Integer soLuongToiDa;
    private Integer soLuongDaNhan;

    public VoucherDTO(Integer id, String codeVoucher, String tenVoucher, Integer soLanSuDung, String moTa, Integer loaiGiam, BigDecimal giaTriGiam, BigDecimal dieuKienGiam, BigDecimal giamToiDa, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai) {
        this.id = id;
        this.codeVoucher = codeVoucher;
        this.tenVoucher = tenVoucher;
        this.soLanSuDung = soLanSuDung;
        this.moTa = moTa;
        this.loaiGiam = loaiGiam;
        this.giaTriGiam = giaTriGiam;
        this.dieuKienGiam = dieuKienGiam;
        this.giamToiDa = giamToiDa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }
}