package com.example.datn.Model.YeuThich;



import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class YeuThichPhuKienId implements Serializable {
    private Integer userId;
    private Integer maPhuKien;
}