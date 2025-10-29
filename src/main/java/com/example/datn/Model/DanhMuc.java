package com.example.datn.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "DanhMuc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maDanhMuc;

    private String tenDanhMuc;
    private String moTa;

    @OneToMany(mappedBy = "danhMuc")
//    @JsonBackReference // <-- THÊM ANNOTATION NÀY VÀO
    private List<SanPham> sanPhamList;

    @OneToMany(mappedBy = "danhMuc")
    private List<LoaiThongSo> loaiThongSoList;
}
