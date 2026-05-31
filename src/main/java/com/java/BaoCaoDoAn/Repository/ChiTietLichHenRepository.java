package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.ChiTietLichHen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietLichHenRepository extends JpaRepository<ChiTietLichHen, Integer> {
    List<ChiTietLichHen> findByLichHen_MaLichHen(String maLichHen);
}
