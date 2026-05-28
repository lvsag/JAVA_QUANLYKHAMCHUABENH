package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.LichHen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichHenRepository extends JpaRepository<LichHen, String> {
    List<LichHen> findByBenhNhan_MaBenhNhanOrderByNgayHenDescGioHenDesc(String maBenhNhan);
}
