package com.example.datn.Service;

import com.example.datn.Model.UserVoucher;
import com.example.datn.Model.Users;
import com.example.datn.Model.Voucher;
import com.example.datn.Repository.UserVoucherRepository;
import com.example.datn.Repository.UsersRepository;
import com.example.datn.Repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserVoucherService {

    private final UserVoucherRepository userVoucherRepo;


    public List<UserVoucher> getActiveVouchersByUser(Integer userId) {
        // Lấy danh sách UserVoucher có trạng thái = 1 (active)
        return userVoucherRepo.findByUserIdAndTrangThai(userId, 1);
    }

    public List<UserVoucher> getVouchersByUserWithStatuses(Integer userId) {
        // Tạo một danh sách chứa các trạng thái bạn muốn lấy (1 và 2)
        List<Integer> desiredStatuses = Arrays.asList(1, 2);

        // Gọi phương thức mới trong repo và truyền danh sách trạng thái vào
        return userVoucherRepo.findByUserIdAndTrangThaiIn(userId, desiredStatuses);
    }


    private final UsersRepository usersRepository;
    private final VoucherRepository voucherRepository;


//    @Transactional
//    public UserVoucher addUserVoucher(String codeVoucher) {
//        // 1. Lấy username của người dùng đang đăng nhập từ Security Context
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Users currentUser = usersRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));
//
//        // 2. Tìm voucher theo code
//        Voucher voucher = voucherRepository.findByCodeVoucher(codeVoucher)
//                .orElseThrow(() -> new RuntimeException("Mã voucher không tồn tại: " + codeVoucher));
//
//        // 3. Kiểm tra các điều kiện của voucher
//        if (voucher.getTrangThai() != 1) {
//            throw new RuntimeException("Voucher này không còn hoạt động.");
//        }
//        if (voucher.getSoLanSuDung() != null && voucher.getSoLanSuDung() <= 0) {
//            throw new RuntimeException("Voucher đã hết lượt sử dụng.");
//        }
//
//        // 4. Kiểm tra xem người dùng đã sở hữu voucher này chưa
//        boolean alreadyExists = userVoucherRepo.existsByUserIdAndVoucherId(currentUser.getId(), voucher.getId());
//        if (alreadyExists) {
//            throw new RuntimeException("Bạn đã sở hữu voucher này rồi.");
//        }
//
//        // 5. Tạo và lưu UserVoucher mới
//        UserVoucher newUserVoucher = new UserVoucher();
//        newUserVoucher.setUser(currentUser);
//        newUserVoucher.setVoucher(voucher);
//        newUserVoucher.setSoLanSuDung(0); // 0 = chưa sử dụng
//        newUserVoucher.setTrangThai(1); // 1 = hoạt động
//
//        // Giảm số lần sử dụng của voucher gốc nếu có giới hạn
//        if (voucher.getSoLanSuDung() != null) {
//            voucher.setSoLanSuDung(voucher.getSoLanSuDung() - 1);
//            voucherRepository.save(voucher);
//        }
//
//        return userVoucherRepo.save(newUserVoucher);
//    }



    @Transactional
    public UserVoucher addUserVoucher(String codeVoucher) {
        // 1. Lấy username (Giữ nguyên)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));

        // 2. Tìm voucher VÀ KHÓA LẠI (THAY ĐỔI)
        // Phải dùng hàm có Lock để đảm bảo an toàn khi nhiều user cùng add
        Voucher voucher = voucherRepository.findByCodeVoucherForUpdate(codeVoucher)
                .orElseThrow(() -> new RuntimeException("Mã voucher không tồn tại: " + codeVoucher));

        // 3. Kiểm tra các điều kiện của voucher (Giữ nguyên)
        if (voucher.getTrangThai() != 1) {
            // Nếu trạng thái đã là 0 (do hết lượt, hoặc admin tắt) thì báo lỗi
            throw new RuntimeException("Voucher này không còn hoạt động hoặc đã hết lượt nhận.");
        }

        // 4. (BỎ ĐI) - Logic cũ về soLanSuDung không đúng ở đây
        // if (voucher.getSoLanSuDung() != null && voucher.getSoLanSuDung() <= 0) { ... }

        // 4. (THÊM MỚI) - Kiểm tra logic số lượng nhận TỐI ĐA
        if (voucher.getSoLuongToiDa() != null) { // Check xem voucher có giới hạn không
            if (voucher.getSoLuongDaNhan() >= voucher.getSoLuongToiDa()) {
                // Nếu đã đủ số lượt nhận -> Báo lỗi
                throw new RuntimeException("Voucher đã hết lượt nhận.");
            }
        }

        // 5. Kiểm tra xem người dùng đã sở hữu voucher này chưa (Giữ nguyên)
        boolean alreadyExists = userVoucherRepo.existsByUserIdAndVoucherId(currentUser.getId(), voucher.getId());
        if (alreadyExists) {
            throw new RuntimeException("Bạn đã sở hữu voucher này rồi.");
        }

        // 6. Tạo và lưu UserVoucher mới (Giữ nguyên)
        UserVoucher newUserVoucher = new UserVoucher();
        newUserVoucher.setUser(currentUser);
        newUserVoucher.setVoucher(voucher);
        newUserVoucher.setSoLanSuDung(0); // 0 = chưa sử dụng
        newUserVoucher.setTrangThai(1); // 1 = hoạt động

        // 7. (BỎ ĐI) - Logic cũ về soLanSuDung
        // if (voucher.getSoLanSuDung() != null) { ... }

        // 7. (BỔ SUNG) - Cập nhật số lượng đã nhận và trạng thái voucher
        int soLuongMoi = voucher.getSoLuongDaNhan() + 1;
        voucher.setSoLuongDaNhan(soLuongMoi);

        // Kiểm tra nếu đây là lượt nhận cuối cùng -> Tắt voucher đi
        if (voucher.getSoLuongToiDa() != null && soLuongMoi >= voucher.getSoLuongToiDa()) {
            voucher.setTrangThai(0); // 0 = Hết lượt nhận
        }

        // 8. Lưu cả hai
        voucherRepository.save(voucher); // Lưu thay đổi của voucher (soLuongDaNhan, trangThai)
        return userVoucherRepo.save(newUserVoucher); // Lưu và trả về UserVoucher mới
    }
}
