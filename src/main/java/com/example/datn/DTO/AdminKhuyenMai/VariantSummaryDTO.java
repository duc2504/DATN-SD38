//package com.example.datn.DTO.AdminKhuyenMai;
//
//
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class VariantSummaryDTO {
//    private String maSKU;
//    private String tenSanPham;
//}

package com.example.datn.DTO.AdminKhuyenMai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class VariantSummaryDTO {
    private String maSKU;
    private String tenHienThi; // Gộp cả tên danh mục, tên sản phẩm và SKU
    private BigDecimal gia; // Giá sau khi áp dụng khuyến mãi (nếu có)
    private BigDecimal giaKhongKhuyenMai; // Giá gốc

    public VariantSummaryDTO(String maSKU, String tenHienThi) {
        this.maSKU = maSKU;
        this.tenHienThi = tenHienThi;
    }
}