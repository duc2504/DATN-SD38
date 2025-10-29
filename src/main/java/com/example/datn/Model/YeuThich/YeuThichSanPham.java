package com.example.datn.Model.YeuThich;



import com.example.datn.Model.SanPham;
import com.example.datn.Model.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "YeuThichSanPham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Báo cho JPA biết lớp nào sẽ dùng làm Id phức hợp
@IdClass(YeuThichSanPhamId.class)
public class YeuThichSanPham {

    // Cả 2 trường này đều là @Id
    @Id
    @Column(name = "userId")
    private Integer userId;

    @Id
    @Column(name = "maSanPham")
    private Integer maSanPham;

    @Column(name = "ngayThem")
    private LocalDateTime ngayThem;

    // --- Định nghĩa mối quan hệ ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false) // Không cho phép insert/update cột này qua đây
    @JsonBackReference // Tránh lặp vô hạn khi serialize JSON
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maSanPham", insertable = false, updatable = false) // Không cho phép insert/update cột này qua đây
    @JsonBackReference // Tránh lặp vô hạn khi serialize JSON
    private SanPham sanPham;

    // Tự động gán ngày giờ khi tạo mới
    @PrePersist
    protected void onCreate() {
        ngayThem = LocalDateTime.now();
    }
}