package com.example.datn.Service;

import com.example.datn.DTO.TrangMuaHang.VoucherDTO;
import com.example.datn.Model.Voucher;
import com.example.datn.Repository.VoucherRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

    /**
     * Phương thức này chứa logic chính để gọi repository
     * và cập nhật trạng thái của tất cả voucher.
     */
    public void capNhatTrangThaiVoucher() {
        int rows = voucherRepository.capNhatTrangThaiTheoNgayVoucher();
        System.out.println("Đã quét và cập nhật trạng thái cho " + rows + " voucher.");
    }

    /**
     * Scheduler sẽ tự động chạy vào lúc 00:05 (12 giờ 5 phút đêm) hàng ngày
     * để gọi phương thức capNhatTrangThaiVoucher() ở trên.
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public void autoUpdateTrangThaiVoucher() {
        System.out.println("Scheduler đang chạy để cập nhật trạng thái voucher...");
        this.capNhatTrangThaiVoucher(); // Gọi phương thức cập nhật trong cùng lớp
    }


    public List<VoucherDTO> getActiveVouchers() {
        // Gọi phương thức mới trong repository và truyền vào trạng thái 1 (hoạt động)
        return voucherRepository.findActiveVouchersAsDTO(1);
    }

    public List<VoucherDTO> getUserVouchers(int userId) {
        return voucherRepository.findUserVouchers(userId);
    }







    public List<VoucherDTO> getAllVouchers() {
        // 1. Gọi repository để lấy tất cả Entity Voucher từ CSDL
        List<Voucher> vouchers = voucherRepository.findAll();

        // 2. Chuyển đổi danh sách List<Voucher> (Entity) sang List<VoucherDTO> (DTO)
        // Dùng Stream API của Java 8+ để việc chuyển đổi dễ dàng
        return vouchers.stream()
                .map(this::convertToDTO) // Gọi hàm chuyển đổi cho từng phần tử
                .collect(Collectors.toList()); // Thu thập kết quả về 1 List mới
    }
    // --- BỔ SUNG CÁC CHỨC NĂNG MỚI (THÊM, SỬA, XÓA) ---
    // =========================================================

    /**
     * Thêm mới một voucher
     * @param voucherDTO Dữ liệu voucher từ request
     * @return VoucherDTO đã được lưu (chứa ID mới)
     */
    @Transactional
    public VoucherDTO createVoucher(VoucherDTO voucherDTO) {
        // Chuyển DTO sang Entity
        Voucher voucher = convertToEntity(voucherDTO);

        // **QUAN TRỌNG**: Đặt ID là null để đảm bảo CSDL tự động tạo ID mới
        voucher.setId(null);

        // Set giá trị mặc định (nếu cần, mặc dù CSDL đã có default)
        if (voucher.getSoLuongDaNhan() == null) {
            voucher.setSoLuongDaNhan(0);
        }
        if (voucher.getTrangThai() == null) {
            voucher.setTrangThai(1); // Mặc định là 'hoạt động'
        }

        // Lưu vào CSDL
        Voucher savedVoucher = voucherRepository.save(voucher);

        // Chuyển lại sang DTO để trả về cho client
        return convertToDTO(savedVoucher);
    }

    /**
     * Cập nhật một voucher đã có
     * @param id ID của voucher cần cập nhật (từ đường dẫn URL)
     * @param voucherDTO Dữ liệu mới để cập nhật (từ body request)
     * @return VoucherDTO đã được cập nhật
     */
    @Transactional
    public VoucherDTO updateVoucher(Integer id, VoucherDTO voucherDTO) {
        // 1. Kiểm tra xem voucher có tồn tại không
        if (!voucherRepository.existsById(id)) {
            // Bạn nên tạo một Exception tùy chỉnh (ví dụ: ResourceNotFoundException)
            throw new RuntimeException("Không tìm thấy voucher với id: " + id);
        }

        // 2. Chuyển DTO sang Entity
        Voucher voucherToUpdate = convertToEntity(voucherDTO);

        // 3. **QUAN TRỌNG**: Đặt ID cho entity từ tham số 'id'
        // Điều này đảm bảo JPA hiểu là bạn muốn "update" bản ghi có ID này
        voucherToUpdate.setId(id);

        // 4. Hàm save() của JPA sẽ tự động update nếu ID đã tồn tại
        Voucher updatedVoucher = voucherRepository.save(voucherToUpdate);

        // 5. Trả về DTO sau khi update
        return convertToDTO(updatedVoucher);
    }

    /**
     * Xóa một voucher theo ID
     * @param id ID của voucher cần xóa
     */
    @Transactional
    public void deleteVoucher(Integer id) {
        // 1. Kiểm tra xem voucher có tồn tại không
        if (!voucherRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy voucher với id: " + id);
        }

        // 2. Xóa voucher
        voucherRepository.deleteById(id);
    }

    /**
     * Phương thức private helper để chuyển đổi từ Entity sang DTO
     * @param voucher Entity
     * @return VoucherDTO
     */
    private VoucherDTO convertToDTO(Voucher voucher) {
        VoucherDTO dto = new VoucherDTO();
        dto.setId(voucher.getId());
        dto.setCodeVoucher(voucher.getCodeVoucher());
        dto.setTenVoucher(voucher.getTenVoucher());
        dto.setSoLanSuDung(voucher.getSoLanSuDung());
        dto.setMoTa(voucher.getMoTa());
        dto.setLoaiGiam(voucher.getLoaiGiam());
        dto.setGiaTriGiam(voucher.getGiaTriGiam());
        dto.setDieuKienGiam(voucher.getDieuKienGiam());
        dto.setGiamToiDa(voucher.getGiamToiDa());
        dto.setNgayBatDau(voucher.getNgayBatDau());
        dto.setNgayKetThuc(voucher.getNgayKetThuc());
        dto.setTrangThai(voucher.getTrangThai());

        // --- BỔ SUNG 2 TRƯỜNG MỚI ---
        dto.setSoLuongToiDa(voucher.getSoLuongToiDa());
        dto.setSoLuongDaNhan(voucher.getSoLuongDaNhan());

        return dto;
    }

    // (Tùy chọn) Hàm chuyển đổi ngược lại từ DTO sang Entity
// Sẽ cần thiết khi làm chức năng Thêm/Sửa
    private Voucher convertToEntity(VoucherDTO dto) {
        Voucher voucher = new Voucher();
        voucher.setId(dto.getId());
        voucher.setCodeVoucher(dto.getCodeVoucher());
        voucher.setTenVoucher(dto.getTenVoucher());
        voucher.setSoLanSuDung(dto.getSoLanSuDung());
        voucher.setMoTa(dto.getMoTa());
        voucher.setLoaiGiam(dto.getLoaiGiam());
        voucher.setGiaTriGiam(dto.getGiaTriGiam());
        voucher.setDieuKienGiam(dto.getDieuKienGiam());
        voucher.setGiamToiDa(dto.getGiamToiDa());
        voucher.setNgayBatDau(dto.getNgayBatDau());
        voucher.setNgayKetThuc(dto.getNgayKetThuc());
        voucher.setTrangThai(dto.getTrangThai());

        // --- BỔ SUNG 2 TRƯỜNG MỚI ---
        voucher.setSoLuongToiDa(dto.getSoLuongToiDa());
        voucher.setSoLuongDaNhan(dto.getSoLuongDaNhan());

        return voucher;
    }


}
