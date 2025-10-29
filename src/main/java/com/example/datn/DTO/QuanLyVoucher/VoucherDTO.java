package com.example.datn.DTO.QuanLyVoucher;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private Integer trangThai;

    // Bạn có thể thêm các trường tùy chỉnh ở đây nếu cần
    // Ví dụ: một trường String để hiển thị loại giảm giá
    public String getLoaiGiamText() {
        if (loaiGiam == null) {
            return "Không xác định";
        }
        return loaiGiam == 1 ? "Giảm theo %" : "Giảm tiền mặt";
    }
}