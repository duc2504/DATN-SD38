package com.example.datn.BanHangTaiQuay.Repo;

import com.example.datn.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersBanHangTaiQuayRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.role.roleName = :roleName")
    List<Users> findByRoleName(@Param("roleName") String roleName);

    @Query("SELECT u FROM Users u WHERE u.role.roleName = 'USER'")
    List<Users> findAllUsers();

    @Query("SELECT u FROM Users u WHERE u.role.roleName = 'ADMIN'")
    List<Users> findAllAdmins();

    // Tìm kiếm khách hàng theo số điện thoại (cho bán hàng tại quầy)
    List<Users> findBySoDienThoaiContainingIgnoreCase(String soDienThoai);

}
