package com.example.datn.DTO.TrangMuaHang;

// package com.yourcompany.dto;
import java.util.List;

public class KetQuaTimKiemDTO {
    private List<TrangChuSanPham> sanPham;
    private List<PhuKienSearchDTO> phuKien;

    // Getters and Setters
    public List<TrangChuSanPham> getSanPham() { return sanPham; }
    public void setSanPham(List<TrangChuSanPham> sanPham) { this.sanPham = sanPham; }
    public List<PhuKienSearchDTO> getPhuKien() { return phuKien; }
    public void setPhuKien(List<PhuKienSearchDTO> phuKien) { this.phuKien = phuKien; }
}