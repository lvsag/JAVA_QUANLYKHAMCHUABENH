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
}
