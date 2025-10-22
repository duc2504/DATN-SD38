package com.example.datn.DTO.DanhMucPhuKien;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Cần thiết để kiểm tra contains() trong List
public class PhuKienThuocTinhDTO {
    private String tenThuocTinh;
    private String giaTriThuocTinh;
}