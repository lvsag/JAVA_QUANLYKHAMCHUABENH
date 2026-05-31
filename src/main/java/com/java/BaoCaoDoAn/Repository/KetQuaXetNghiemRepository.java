package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.KetQuaXetNghiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KetQuaXetNghiemRepository extends JpaRepository<KetQuaXetNghiem, String> {
    List<KetQuaXetNghiem> findByBenhNhan_MaBenhNhanOrderByNgayKetQuaDesc(String maBenhNhan);

    List<KetQuaXetNghiem> findByBacSi_MaBacSiOrderByNgayKetQuaDesc(String maBacSi);

    List<KetQuaXetNghiem> findByTrangThaiOrderByNgayKetQuaDesc(String trangThai);

    @org.springframework.data.jpa.repository.Query("SELECT k FROM KetQuaXetNghiem k WHERE " +
            "(:trangThai IS NULL OR :trangThai = '' OR k.trangThai = :trangThai) AND " +
            "(:ngay IS NULL OR :ngay = '' OR FUNCTION('DATE', k.ngayKetQua) = FUNCTION('DATE', :ngay)) AND " +
            "(:maBacSi IS NULL OR :maBacSi = '' OR k.bacSi.maBacSi = :maBacSi) " +
            "ORDER BY k.ngayKetQua DESC")
    List<KetQuaXetNghiem> filterKetQua(
            @org.springframework.data.repository.query.Param("trangThai") String trangThai,
            @org.springframework.data.repository.query.Param("ngay") String ngay,
            @org.springframework.data.repository.query.Param("maBacSi") String maBacSi
    );

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT k FROM KetQuaXetNghiem k JOIN k.phieuChiDinh pcd JOIN pcd.phieuKham pk WHERE k.benhNhan.maBenhNhan = :maBenhNhan AND EXISTS (SELECT 1 FROM HoaDon hd WHERE (hd.maLichHen = pk.lichHen.maLichHen OR hd.maLichHen = pk.maPhieuKham) AND hd.trangThai = 'Đã thanh toán') ORDER BY k.ngayKetQua DESC")
    List<KetQuaXetNghiem> findPaidKetQuaByBenhNhan(@org.springframework.data.repository.query.Param("maBenhNhan") String maBenhNhan);
}
