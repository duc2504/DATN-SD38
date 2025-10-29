package com.example.datn.DTO.TrangMuaHang;

// package com.yourcompany.dto;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
@Data
public class PhuKienSearchDTO {
    private int maPhuKien;
    private String tenPhuKien;
    private String thuongHieu;
    private String moTa;
    private BigDecimal gia;
    private Integer soLuong;
    private String tenDanhMucPhuKien; // Giả sử bảng DanhMucPhuKien có cột tenDanhMuc

    // Thêm getters and setters ...

    // Ví dụ (bạn tự generate đầy đủ):

}