package com.example.datn.Model.YeuThich;



import lombok.*;
import java.io.Serializable;

// Cần @EqualsAndHashCode cho các lớp Id phức hợp
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class YeuThichSanPhamId implements Serializable {
    private Integer userId;
    private Integer maSanPham;
}