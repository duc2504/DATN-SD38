package com.example.datn.Repository;


import com.example.datn.Model.YeuThich.YeuThichSanPham;
import com.example.datn.Model.YeuThich.YeuThichSanPhamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YeuThichSanPhamRepository extends JpaRepository<YeuThichSanPham, YeuThichSanPhamId> {

    // Tìm tất cả các mục yêu thích theo userId
    List<YeuThichSanPham> findByUserId(Integer userId);

    // 3. Thêm hàm mới: Tìm 1 mục yêu thích cụ thể bằng userId và maSanPham
    Optional<YeuThichSanPham> findByUserIdAndMaSanPham(Integer userId, Integer maSanPham);
}