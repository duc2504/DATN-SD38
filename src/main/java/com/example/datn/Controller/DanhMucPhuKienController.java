package com.example.datn.Controller;


import com.example.datn.DTO.CRUDphukien.DanhMucPhuKienDTO;

import com.example.datn.DTO.DanhMucPhuKien.TrangChuPhuKien;
import com.example.datn.Service.DanhMucPhuKienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/danh-muc-phu-kien") // Đổi path API
public class DanhMucPhuKienController {

    private final DanhMucPhuKienService danhMucPhuKienService;

    @GetMapping
    public List<DanhMucPhuKienDTO> getAllDanhMucPhuKien() {
        return danhMucPhuKienService.getAllDanhMucPhuKienWithCount();
    }


    @GetMapping("/{id}/DanhMucPhuKien")
    public ResponseEntity<List<TrangChuPhuKien>> getAccessoriesByCategoryId(
            @PathVariable Integer id
    ) {
        // Giả sử id luôn được cung cấp, có thể thêm logic kiểm tra null nếu cần
        List<TrangChuPhuKien> accessories = danhMucPhuKienService.getPhuKienByDanhMuc(id);

        if (accessories == null || accessories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(accessories);
    }
}
