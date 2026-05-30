package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.NhapVienNoiTru;
import com.java.BaoCaoDoAn.Model.BenhNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NhapVienNoiTruRepository extends JpaRepository<NhapVienNoiTru, String> {
    List<NhapVienNoiTru> findByBenhNhan(BenhNhan benhNhan);
    List<NhapVienNoiTru> findByTrangThai(String trangThai);
    boolean existsByBenhNhan_MaBenhNhanAndTrangThai(String maBenhNhan, String trangThai);
    List<NhapVienNoiTru> findByBenhNhan_MaBenhNhan(String maBenhNhan);
    void deleteByBenhNhan_MaBenhNhan(String maBenhNhan);
}
