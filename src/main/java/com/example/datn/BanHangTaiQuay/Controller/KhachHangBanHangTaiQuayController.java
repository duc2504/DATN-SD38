package com.example.datn.BanHangTaiQuay.Controller;

import com.example.datn.BanHangTaiQuay.DTO.KhachHangBanHangTaiQuayDTO;
import com.example.datn.BanHangTaiQuay.Service.KhachHangBanHangTaiQuayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ban-hang-tai-quay/khach-hang")
@CrossOrigin("*")
public class KhachHangBanHangTaiQuayController {

    @Autowired
    private KhachHangBanHangTaiQuayService khachHangBanHangTaiQuayService;

    // Tìm kiếm khách hàng theo số điện thoại
    @GetMapping("/tim-kiem")
    public ResponseEntity<List<KhachHangBanHangTaiQuayDTO>> timKiemKhachHang(
            @RequestParam("soDienThoai") String soDienThoai) {
        
        List<KhachHangBanHangTaiQuayDTO> khachHangList = khachHangBanHangTaiQuayService.timKiemKhachHangTheoSoDienThoai(soDienThoai);
        
        if (khachHangList == null || khachHangList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(khachHangList);
    }
}
