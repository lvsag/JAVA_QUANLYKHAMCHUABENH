package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.ChuyenKhoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuyenKhoaRepository extends JpaRepository<ChuyenKhoa, String> {
    List<ChuyenKhoa> findByTrangThai(String trangThai);
    List<ChuyenKhoa> findByTenChuyenKhoaContainingIgnoreCase(String tenChuyenKhoa);
}
