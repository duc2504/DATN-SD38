package com.example.datn.Service.YeuThich;


import com.example.datn.DTO.YeuThich.FavoritePhuKienDTO;
import com.example.datn.DTO.YeuThich.FavoriteSanPhamDTO;
import com.example.datn.Model.PhuKien;
import com.example.datn.Model.SanPham;
import com.example.datn.Model.Users;
import com.example.datn.Model.YeuThich.YeuThichPhuKien;
import com.example.datn.Model.YeuThich.YeuThichSanPham;
import com.example.datn.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YeuThichService {

    private final YeuThichSanPhamRepository yeuThichSanPhamRepo;
    private final YeuThichPhuKienRepository yeuThichPhuKienRepo;
    private final UsersRepository usersRepository;

    private Users getUserByUsername(String username) {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user với username: " + username));
    }

    // Sửa kiểu trả về thành List<FavoriteSanPhamDTO>
    @Transactional(readOnly = true)
    public List<FavoriteSanPhamDTO> getFavoriteProducts(String username) {
        Users user = getUserByUsername(username);
        List<YeuThichSanPham> favorites = yeuThichSanPhamRepo.findByUserId(user.getId());

        // Chuyển đổi từ List<YeuThichSanPham> -> List<SanPham> -> List<FavoriteSanPhamDTO>
        return favorites.stream()
                .map(YeuThichSanPham::getSanPham) // Lấy SanPham
                .map(FavoriteSanPhamDTO::new)     // Chuyển đổi sang DTO
                .collect(Collectors.toList());
    }

    // Sửa kiểu trả về thành List<FavoritePhuKienDTO>
    @Transactional(readOnly = true)
    public List<FavoritePhuKienDTO> getFavoriteAccessories(String username) {
        Users user = getUserByUsername(username);
        List<YeuThichPhuKien> favorites = yeuThichPhuKienRepo.findByUserId(user.getId());

        // Chuyển đổi từ List<YeuThichPhuKien> -> List<PhuKien> -> List<FavoritePhuKienDTO>
        return favorites.stream()
                .map(YeuThichPhuKien::getPhuKien) // Lấy PhuKien
                .map(FavoritePhuKienDTO::new)    // Chuyển đổi sang DTO
                .collect(Collectors.toList());
    }















private final SanPhamRepository sanPhamRepository ;
    // === BỔ SUNG: CÁC HÀM THÊM/XÓA ===

    /**
     * Thêm một sản phẩm vào danh sách yêu thích.
     */
    @Transactional
    public void addFavoriteProduct(String username, Integer maSanPham) {
        Users user = getUserByUsername(username);

        // 1. Kiểm tra xem sản phẩm có tồn tại không
        SanPham sanPham = sanPhamRepository.findById(maSanPham)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + maSanPham));

        // 2. Kiểm tra xem đã yêu thích chưa
        Optional<YeuThichSanPham> existing = yeuThichSanPhamRepo.findByUserIdAndMaSanPham(user.getId(), maSanPham);
        if (existing.isPresent()) {
            // Nếu đã tồn tại, không làm gì cả (hoặc có thể ném lỗi)
            return;
        }

        // 3. Tạo bản ghi yêu thích mới
        YeuThichSanPham favorite = new YeuThichSanPham();
        favorite.setUserId(user.getId());
        favorite.setMaSanPham(sanPham.getMaSanPham());
        favorite.setUser(user);
        favorite.setSanPham(sanPham);
        favorite.setNgayThem(LocalDateTime.now()); // Cập nhật ngày thêm

        yeuThichSanPhamRepo.save(favorite);
    }

    /**
     * Xóa một sản phẩm khỏi danh sách yêu thích.
     */
    @Transactional
    public void removeFavoriteProduct(String username, Integer maSanPham) {
        Users user = getUserByUsername(username);

        // Tìm bản ghi yêu thích
        YeuThichSanPham favorite = yeuThichSanPhamRepo.findByUserIdAndMaSanPham(user.getId(), maSanPham)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy mục yêu thích cho sản phẩm: " + maSanPham));

        // Xóa
        yeuThichSanPhamRepo.delete(favorite);
    }

    /**
     * Thêm một phụ kiện vào danh sách yêu thích.
     */


    private final PhuKienRepository phuKienRepository ;
    @Transactional
    public void addFavoriteAccessory(String username, Integer maPhuKien) {
        Users user = getUserByUsername(username);

        PhuKien phuKien = phuKienRepository.findById(maPhuKien)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phụ kiện với ID: " + maPhuKien));

        Optional<YeuThichPhuKien> existing = yeuThichPhuKienRepo.findByUserIdAndMaPhuKien(user.getId(), maPhuKien);
        if (existing.isPresent()) {
            return;
        }

        YeuThichPhuKien favorite = new YeuThichPhuKien();
        favorite.setUserId(user.getId());
        favorite.setMaPhuKien(phuKien.getMaPhuKien());
        favorite.setUser(user);
        favorite.setPhuKien(phuKien);
        favorite.setNgayThem(LocalDateTime.now());

        yeuThichPhuKienRepo.save(favorite);
    }

    /**
     * Xóa một phụ kiện khỏi danh sách yêu thích.
     */
    @Transactional
    public void removeFavoriteAccessory(String username, Integer maPhuKien) {
        Users user = getUserByUsername(username);

        YeuThichPhuKien favorite = yeuThichPhuKienRepo.findByUserIdAndMaPhuKien(user.getId(), maPhuKien)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy mục yêu thích cho phụ kiện: " + maPhuKien));

        yeuThichPhuKienRepo.delete(favorite);
    }
}