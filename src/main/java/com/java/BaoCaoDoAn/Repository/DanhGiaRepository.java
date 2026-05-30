package com.java.BaoCaoDoAn.Repository;



import com.java.BaoCaoDoAn.Model.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    List<DanhGia> findByMaBacSiOrderByNgayTaoDesc(String maBacSi);
}
