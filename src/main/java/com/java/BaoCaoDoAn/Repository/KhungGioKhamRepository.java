package com.java.BaoCaoDoAn.Repository;



import com.java.BaoCaoDoAn.Model.KhungGioKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface KhungGioKhamRepository extends JpaRepository<KhungGioKham, Integer> {

    // SỬA Ở ĐÂY: Thay l.maBacSi thành l.bacSi.maBacSi
    @Query("SELECT k FROM KhungGioKham k JOIN k.lichLamViec l WHERE l.bacSi.maBacSi = :maBacSi AND k.ngayKham BETWEEN :startDate AND :endDate ORDER BY k.ngayKham ASC, k.gioBatDau ASC")
    List<KhungGioKham> findKhungGioByBacSiAndDateRange(
            @Param("maBacSi") String maBacSi,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}