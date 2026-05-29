package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.LichHen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// ===================================================================
// BUG FIX: ID type was Integer but LichHen.maLichHen is String (VARCHAR(20))
//           → Changed JpaRepository<LichHen, Integer> to <LichHen, String>
// ===================================================================
@Repository
public interface LichHenRepository extends JpaRepository<LichHen, String> {

    // Lấy danh sách lịch hẹn theo bệnh nhân (màn hình lịch sử - UI 09)
    List<LichHen> findByBenhNhan_MaBenhNhanOrderByNgayHenDesc(String maBenhNhan);

    // Lấy danh sách lịch hẹn theo bác sĩ (màn hình bác sĩ - UI 17)
    List<LichHen> findByBacSi_MaBacSiAndTrangThai(String maBacSi, String trangThai);

    // Admin: lọc theo trạng thái
    List<LichHen> findByTrangThaiOrderByNgayHenDesc(String trangThai);

    // Đếm lịch hẹn trong ngày để sinh mã
    @Query("SELECT COUNT(l) FROM LichHen l WHERE l.ngayHen = CURRENT_DATE")
    long countTodayAppointments();

    // Kiểm tra khung giờ đã bị đặt chưa (tránh double-booking)
    @Query("SELECT COUNT(l) FROM LichHen l WHERE l.maKhungGio = :maKhungGio AND l.trangThai NOT IN ('Đã hủy')")
    long countBookedSlot(@Param("maKhungGio") Integer maKhungGio);
}
