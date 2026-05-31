package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.ChiSoXetNghiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiSoXetNghiemRepository extends JpaRepository<ChiSoXetNghiem, Integer> {
    List<ChiSoXetNghiem> findByKetQuaXetNghiem_MaKetQua(String maKetQua);
}
