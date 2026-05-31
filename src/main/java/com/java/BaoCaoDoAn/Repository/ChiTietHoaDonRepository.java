package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer> {
    List<ChiTietHoaDon> findByHoaDon_MaHoaDon(String maHoaDon);
}
