package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.DonThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonThuocRepository extends JpaRepository<DonThuoc, String> {
    List<DonThuoc> findByBenhNhan_MaBenhNhanOrderByNgayKeDesc(String maBenhNhan);

    List<DonThuoc> findByBacSi_MaBacSiOrderByNgayKeDesc(String maBacSi);
}
