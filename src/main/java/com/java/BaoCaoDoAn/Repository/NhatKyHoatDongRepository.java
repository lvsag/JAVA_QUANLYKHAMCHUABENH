package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.NhatKyHoatDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhatKyHoatDongRepository extends JpaRepository<NhatKyHoatDong, Integer> {
}
