package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.ChiTietDonThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietDonThuocRepository extends JpaRepository<ChiTietDonThuoc, Long> {
    List<ChiTietDonThuoc> findByDonThuoc_MaDonThuoc(String maDonThuoc);
}
