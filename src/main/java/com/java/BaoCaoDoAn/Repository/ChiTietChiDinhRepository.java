package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.ChiTietChiDinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietChiDinhRepository extends JpaRepository<ChiTietChiDinh, Long> {
    List<ChiTietChiDinh> findByPhieuChiDinh_MaPhieuChiDinh(String maPhieuChiDinh);
}
