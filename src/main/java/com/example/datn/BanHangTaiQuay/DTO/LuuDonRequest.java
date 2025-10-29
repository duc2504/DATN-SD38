package com.example.datn.BanHangTaiQuay.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LuuDonRequest {
    private Integer userId; // ID của nhân viên xử lý đơn hàng
    private Integer maDonHang; // Mã đơn hàng cần cập nhật (nếu có)
    private Boolean isUpdate; // true = cập nhật đơn hiện tại, false = tạo đơn mới
    private BigDecimal tongTien;
    private String diaChiGiaoHang;
    private String soDienThoai;
    private String phuongThucThanhToan;
    private String ghiChu;
    private Integer userVoucherId;
    private List<ChiTietDonHangBanHangTaiQuayDTO> chiTietDonHangs;
    
    // ✅ THÊM: Các trường mới để xử lý số lượng
    private Boolean updateProductQuantities; // Có trừ số lượng sản phẩm/phụ kiện không
    private Boolean updateVoucherQuantities; // Có trừ số lượng voucher không
}
