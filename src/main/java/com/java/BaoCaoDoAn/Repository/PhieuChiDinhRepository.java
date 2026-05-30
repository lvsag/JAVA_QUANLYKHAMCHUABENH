package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.PhieuChiDinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhieuChiDinhRepository extends JpaRepository<PhieuChiDinh, String> {
    List<PhieuChiDinh> findByBenhNhan_MaBenhNhanOrderByNgayChiDinhDesc(String maBenhNhan);

    List<PhieuChiDinh> findByBacSi_MaBacSiOrderByNgayChiDinhDesc(String maBacSi);

    List<PhieuChiDinh> findByTrangThaiOrderByNgayChiDinhAsc(String trangThai);
}
