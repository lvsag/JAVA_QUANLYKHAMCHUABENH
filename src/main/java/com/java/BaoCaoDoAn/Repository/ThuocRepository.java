package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.Thuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThuocRepository extends JpaRepository<Thuoc, Integer> {
    List<Thuoc> findByTrangThaiNotOrderByTenThuocAsc(String trangThai);
    List<Thuoc> findByTenThuocContainingIgnoreCase(String keyword);
}
