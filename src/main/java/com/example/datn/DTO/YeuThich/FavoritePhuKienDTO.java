package com.example.datn.DTO.YeuThich;



import com.example.datn.Model.PhuKien;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritePhuKienDTO {

    private Integer maPhuKien;
    private String tenPhuKien;
    // private String thuongHieu; // <-- ĐÃ XÓA
    private String tenDanhMucPhuKien; // <-- TRƯỜNG MỚI
    private BigDecimal gia;

    // Constructor để chuyển đổi từ Entity PhuKien sang DTO
    public FavoritePhuKienDTO(PhuKien entity) {
        this.maPhuKien = entity.getMaPhuKien();
        this.tenPhuKien = entity.getTenPhuKien();
        // this.thuongHieu = entity.getThuongHieu(); // <-- ĐÃ XÓA
        this.gia = entity.getGia();

        // Thêm logic để lấy tenDanhMucPhuKien
        // Giả sử model PhuKien của bạn có liên kết đến DanhMucPhuKien
        if (entity.getDanhMucPhuKien() != null) {
            this.tenDanhMucPhuKien = entity.getDanhMucPhuKien().getTenDanhMucPhuKien();
        } else {
            this.tenDanhMucPhuKien = null; // Hoặc "Chưa phân loại"
        }
    }

    // Đưa hàm getGiaHienThi vào DTO (giữ nguyên)
    public String getGiaHienThi() {
        if (gia == null) return "0 đ";
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        formatter.setGroupingUsed(true);
        formatter.setGroupingSize(3);
        return formatter.format(gia).replace(",", ".") + " đ";
    }
}