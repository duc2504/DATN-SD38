package com.example.datn.DTO.DanhMucPhuKien;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrangChuPhuKien {

    private Integer maPhuKien;
    private String tenPhuKien;
    private String thuongHieu;
    private String moTa;
    private Integer soLuong;
    private BigDecimal gia; // Giá của biến thể rẻ nhất

    // Danh mục phụ kiện
    private Integer maDanhMucPhuKien;
    private String tenDanhMucPhuKien;

    // Thông tin biến thể rẻ nhất
    private String maSKUPhuKien;

    // Danh sách các thuộc tính
    private List<PhuKienThuocTinhDTO> chiTietThuocTinh;
}