package com.example.datn.DTO.YeuThich;


import com.example.datn.Model.SanPham;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteSanPhamDTO {

    private Integer maSanPham;
    private String tenSanPham;
    // private String thuongHieu; // <-- ĐÃ XÓA
    private String tenDanhMuc; // <-- TRƯỜNG MỚI
    private BigDecimal gia;

    // Constructor để chuyển đổi từ Entity SanPham sang DTO
    public FavoriteSanPhamDTO(SanPham entity) {
        this.maSanPham = entity.getMaSanPham();
        this.tenSanPham = entity.getTenSanPham();
        // this.thuongHieu = entity.getThuongHieu(); // <-- ĐÃ XÓA
        this.gia = entity.getGia();

        // Thêm logic để lấy tenDanhMuc
        if (entity.getDanhMuc() != null) {
            this.tenDanhMuc = entity.getDanhMuc().getTenDanhMuc();
        } else {
            this.tenDanhMuc = null; // Hoặc "Không có danh mục"
        }
    }

    // Đưa hàm getGiaHienThi vào DTO
    // (Hàm này giữ nguyên)
    public String getGiaHienThi() {
        if (gia == null) return "0 đ";
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        formatter.setGroupingUsed(true);
        formatter.setGroupingSize(3);
        // Thay dấu phẩy bằng dấu chấm
        return formatter.format(gia).replace(",", ".") + " đ";
    }
}