package com.java.BaoCaoDoAn.Repository;



import com.java.BaoCaoDoAn.Model.KhungGioKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
@Repository
public interface KhungGioKhamRepository extends JpaRepository<KhungGioKham, Integer> {

    // SỬA Ở ĐÂY: Thay l.maBacSi thành l.bacSi.maBacSi
    @Query("SELECT k FROM KhungGioKham k JOIN k.lichLamViec l WHERE l.bacSi.maBacSi = :maBacSi AND k.ngayKham BETWEEN :startDate AND :endDate ORDER BY k.ngayKham ASC, k.gioBatDau ASC")
    List<KhungGioKham> findKhungGioByBacSiAndDateRange(
            @Param("maBacSi") String maBacSi,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT k FROM KhungGioKham k JOIN k.lichLamViec l WHERE l.bacSi.maBacSi = :maBacSi AND k.ngayKham = :ngayKham AND k.trangThai = :trangThai ORDER BY k.gioBatDau ASC")
    List<KhungGioKham> findAvailableSlots(
            @Param("maBacSi") String maBacSi,
            @Param("ngayKham") LocalDate ngayKham,
            @Param("trangThai") String trangThai);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT k FROM KhungGioKham k WHERE k.maKhungGio = :maKhungGio")
    Optional<KhungGioKham> findByIdWithLock(@Param("maKhungGio") Integer maKhungGio);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    void deleteByLichLamViecIn(List<com.java.BaoCaoDoAn.Model.LichLamViec> lichLamViecs);
}