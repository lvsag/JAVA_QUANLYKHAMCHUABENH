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
            "(:ngay IS NULL OR :ngay = '' OR FUNCTION('DATE', k.ngayKetQua) = FUNCTION('DATE', :ngay)) " +
            "ORDER BY k.ngayKetQua DESC")
    List<KetQuaXetNghiem> filterKetQua(
            @org.springframework.data.repository.query.Param("trangThai") String trangThai,
            @org.springframework.data.repository.query.Param("ngay") String ngay
    );
}
