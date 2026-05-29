package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.BenhNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan, String> {
    Optional<BenhNhan> findByTaiKhoan_MaTaiKhoan(Integer maTaiKhoan);
    List<BenhNhan> findByHoTenContainingIgnoreCase(String hoTen);
    Optional<BenhNhan> findBySoDienThoai(String soDienThoai);
    Optional<BenhNhan> findBySoDienThoaiAndNgaySinhAndSoCCCD(String soDienThoai, java.util.Date ngaySinh, String soCCCD);
}
