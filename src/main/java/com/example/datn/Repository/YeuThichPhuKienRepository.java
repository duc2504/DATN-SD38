package com.example.datn.Repository;


import com.example.datn.Model.YeuThich.YeuThichPhuKien;
import com.example.datn.Model.YeuThich.YeuThichPhuKienId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YeuThichPhuKienRepository extends JpaRepository<YeuThichPhuKien, YeuThichPhuKienId> {

    // Tìm tất cả các mục yêu thích theo userId
    List<YeuThichPhuKien> findByUserId(Integer userId);

    Optional<YeuThichPhuKien> findByUserIdAndMaPhuKien(Integer userId, Integer maPhuKien);
}