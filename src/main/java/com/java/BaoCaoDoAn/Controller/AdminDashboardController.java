package com.java.BaoCaoDoAn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @GetMapping("/dashboard")
    public String dashboard(org.springframework.ui.Model model) {
        // Lấy số liệu thống kê tổng quan
        Integer totalAppointments = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM LichHen WHERE DATE(NgayHen) = CURRENT_DATE", Integer.class);
        Integer totalPatients = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BenhNhan", Integer.class);
        java.math.BigDecimal revenueToday = jdbcTemplate.queryForObject("SELECT SUM(TongCanThanhToan) FROM HoaDon WHERE DATE(NgayThanhToan) = CURRENT_DATE", java.math.BigDecimal.class);
        if (revenueToday == null) revenueToday = java.math.BigDecimal.ZERO;
        
        Integer lowStockCount = jdbcTemplate.queryForObject(
            "SELECT (SELECT COUNT(*) FROM Thuoc WHERE TonKho <= NguongCanhBao) + (SELECT COUNT(*) FROM VatTuYTe WHERE TonKho <= NguongCanhBao)", Integer.class);

        // Lấy danh sách lịch hẹn hôm nay
        java.util.List<java.util.Map<String, Object>> todayAppointments = jdbcTemplate.queryForList(
            "SELECT lh.MaLichHen, bn.HoTen as TenBenhNhan, bs.HoTen as TenBacSi, lh.GioHen, lh.TrangThai " +
            "FROM LichHen lh " +
            "LEFT JOIN BenhNhan bn ON lh.MaBenhNhan = bn.MaBenhNhan " +
            "LEFT JOIN BacSi bs ON lh.MaBacSi = bs.MaBacSi " +
            "WHERE DATE(lh.NgayHen) = CURRENT_DATE LIMIT 10");

        // Lấy lịch làm việc bác sĩ
        java.util.List<java.util.Map<String, Object>> workingDoctors = jdbcTemplate.queryForList(
            "SELECT bs.HoTen as TenBacSi, ck.TenChuyenKhoa, bs.TrangThai " +
            "FROM BacSi bs " +
            "LEFT JOIN ChuyenKhoa ck ON bs.MaChuyenKhoa = ck.MaChuyenKhoa LIMIT 5");

        // Lấy cảnh báo tồn kho
        java.util.List<java.util.Map<String, Object>> lowStockItems = jdbcTemplate.queryForList(
            "SELECT TenThuoc as Ten, TonKho, NguongCanhBao FROM Thuoc WHERE TonKho <= NguongCanhBao " +
            "UNION ALL " +
            "SELECT TenVatTu as Ten, TonKho, NguongCanhBao FROM VatTuYTe WHERE TonKho <= NguongCanhBao LIMIT 5");

        model.addAttribute("totalAppointments", totalAppointments != null ? totalAppointments : 0);
        model.addAttribute("totalPatients", totalPatients != null ? totalPatients : 0);
        model.addAttribute("revenueToday", revenueToday);
        model.addAttribute("lowStockCount", lowStockCount != null ? lowStockCount : 0);
        model.addAttribute("todayAppointments", todayAppointments);
        model.addAttribute("workingDoctors", workingDoctors);
        model.addAttribute("lowStockItems", lowStockItems);

        return "admin/dashboard/index";
    }

    @GetMapping({"", "/"})
    public String index() {
        return "redirect:/admin/dashboard";
    }
}
