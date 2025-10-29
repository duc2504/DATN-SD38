package com.example.datn.Controller;



// 2. IMPORT Service (đường dẫn của bạn)
import com.example.datn.DTO.YeuThich.FavoritePhuKienDTO;
import com.example.datn.DTO.YeuThich.FavoriteSanPhamDTO;
import com.example.datn.Service.YeuThich.YeuThichService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/yeu-thich") // Đường dẫn chung cho API yêu thích
@RequiredArgsConstructor
public class YeuThichController {

    private final YeuThichService yeuThichService;

    /**
     * Endpoint lấy danh sách SẢN PHẨM yêu thích của user đang đăng nhập.
     * User phải gửi kèm Bearer Token trong header.
     */
    @GetMapping("/san-pham")
    // 3. THAY ĐỔI: Kiểu trả về là List<FavoriteSanPhamDTO>
    public ResponseEntity<List<FavoriteSanPhamDTO>> getMyFavoriteProducts(Authentication authentication) {
        String username = authentication.getName();

        // 4. THAY ĐỔI: Service của bạn (từ bước trước) giờ đã trả về đúng DTO
        List<FavoriteSanPhamDTO> favoriteProducts = yeuThichService.getFavoriteProducts(username);
        return ResponseEntity.ok(favoriteProducts);
    }

    /**
     * Endpoint lấy danh sách PHỤ KIỆN yêu thích của user đang đăng nhập.
     * User phải gửi kèm Bearer Token trong header.
     */
    @GetMapping("/phu-kien")
    // 5. THAY ĐỔI: Kiểu trả về là List<FavoritePhuKienDTO>
    public ResponseEntity<List<FavoritePhuKienDTO>> getMyFavoriteAccessories(Authentication authentication) {
        String username = authentication.getName();

        // 6. THAY ĐỔI: Service của bạn (từ bước trước) giờ đã trả về đúng DTO
        List<FavoritePhuKienDTO> favoriteAccessories = yeuThichService.getFavoriteAccessories(username);
        return ResponseEntity.ok(favoriteAccessories);
    }

















    // === BỔ SUNG: CÁC ENDPOINT THÊM/XÓA ===

    /**
     * API để THÊM một sản phẩm vào danh sách yêu thích
     */
    @PostMapping("/san-pham/{maSanPham}")
    public ResponseEntity<Void> addFavoriteProduct(
            @PathVariable Integer maSanPham,
            Authentication authentication) {

        String username = authentication.getName();
        yeuThichService.addFavoriteProduct(username, maSanPham);
        // 201 Created (hoặc 200 OK cũng được)
        return ResponseEntity.status(201).build();
    }

    /**
     * API để XÓA một sản phẩm khỏi danh sách yêu thích
     */
    @DeleteMapping("/san-pham/{maSanPham}")
    public ResponseEntity<Void> removeFavoriteProduct(
            @PathVariable Integer maSanPham,
            Authentication authentication) {

        String username = authentication.getName();
        yeuThichService.removeFavoriteProduct(username, maSanPham);
        // 204 No Content (thành công, không trả về body)
        return ResponseEntity.noContent().build();
    }

    /**
     * API để THÊM một phụ kiện vào danh sách yêu thích
     */
    @PostMapping("/phu-kien/{maPhuKien}")
    public ResponseEntity<Void> addFavoriteAccessory(
            @PathVariable Integer maPhuKien,
            Authentication authentication) {

        String username = authentication.getName();
        yeuThichService.addFavoriteAccessory(username, maPhuKien);
        return ResponseEntity.status(201).build();
    }

    /**
     * API để XÓA một phụ kiện khỏi danh sách yêu thích
     */
    @DeleteMapping("/phu-kien/{maPhuKien}")
    public ResponseEntity<Void> removeFavoriteAccessory(
            @PathVariable Integer maPhuKien,
            Authentication authentication) {

        String username = authentication.getName();
        yeuThichService.removeFavoriteAccessory(username, maPhuKien);
        return ResponseEntity.noContent().build();
    }
}