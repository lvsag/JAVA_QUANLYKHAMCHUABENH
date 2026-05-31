package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.LichLamViec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichLamViecRepository extends JpaRepository<LichLamViec, Integer> {
    List<LichLamViec> findByBacSi_MaBacSi(String maBacSi);
    List<LichLamViec> findByBacSi_MaBacSiAndTrangThai(String maBacSi, String trangThai);
}
