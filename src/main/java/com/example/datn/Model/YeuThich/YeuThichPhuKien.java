package com.example.datn.Model.YeuThich;



import com.example.datn.Model.PhuKien;
import com.example.datn.Model.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "YeuThichPhuKien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Báo cho JPA biết lớp nào sẽ dùng làm Id phức hợp
@IdClass(YeuThichPhuKienId.class)
public class YeuThichPhuKien {

    // Cả 2 trường này đều là @Id
    @Id
    @Column(name = "userId")
    private Integer userId;

    @Id
    @Column(name = "maPhuKien")
    private Integer maPhuKien;

    @Column(name = "ngayThem")
    private LocalDateTime ngayThem;

    // --- Định nghĩa mối quan hệ ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    @JsonBackReference
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maPhuKien", insertable = false, updatable = false)
    @JsonBackReference
    private PhuKien phuKien; // Giả sử bạn có model tên là PhuKien

    // Tự động gán ngày giờ khi tạo mới
    @PrePersist
    protected void onCreate() {
        ngayThem = LocalDateTime.now();
    }
}