package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.ChuyenKhoa;
import com.java.BaoCaoDoAn.Model.PhongBenh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhongBenhRepository extends JpaRepository<PhongBenh, String> {
    List<PhongBenh> findByChuyenKhoa(ChuyenKhoa chuyenKhoa);
    List<PhongBenh> findByTrangThai(String trangThai);
}
