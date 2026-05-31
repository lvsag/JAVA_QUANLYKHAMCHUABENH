package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    List<HoaDon> findByBenhNhan_MaBenhNhan(String maBenhNhan);
    Optional<HoaDon> findByNhapVienNoiTru_MaNoiTru(String maNoiTru);
}
