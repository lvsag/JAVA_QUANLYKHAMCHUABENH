package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.PhieuKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhieuKhamRepository extends JpaRepository<PhieuKham, String> {
    List<PhieuKham> findByBenhNhan_MaBenhNhanOrderByNgayKhamDesc(String maBenhNhan);

    List<PhieuKham> findByBacSi_MaBacSiOrderByNgayKhamDesc(String maBacSi);

    List<PhieuKham> findByTrangThaiOrderByNgayKhamAsc(String trangThai);
}
