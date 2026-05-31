package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.DonThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonThuocRepository extends JpaRepository<DonThuoc, String> {
    List<DonThuoc> findByBenhNhan_MaBenhNhanOrderByNgayKeDesc(String maBenhNhan);

    List<DonThuoc> findByBacSi_MaBacSiOrderByNgayKeDesc(String maBacSi);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT d FROM DonThuoc d JOIN d.phieuKham pk WHERE d.benhNhan.maBenhNhan = :maBenhNhan AND EXISTS (SELECT 1 FROM HoaDon hd WHERE (hd.maLichHen = pk.lichHen.maLichHen OR hd.maLichHen = pk.maPhieuKham) AND hd.trangThai = 'Đã thanh toán') ORDER BY d.ngayKe DESC")
    List<DonThuoc> findPaidDonThuocByBenhNhan(@org.springframework.data.repository.query.Param("maBenhNhan") String maBenhNhan);
}
