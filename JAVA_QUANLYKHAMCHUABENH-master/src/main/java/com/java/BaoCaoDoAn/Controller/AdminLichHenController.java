package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Service.LichHenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// ===================================================================
// BUG 3 FIX: AdminLichHenController cũ chỉ có GET danh sách.
// Thêm endpoints xác nhận, cập nhật trạng thái (UI 24)
// ===================================================================
@Controller
@RequestMapping("/admin/lich-hen")
public class AdminLichHenController {

    @Autowired
    private LichHenService lichHenService;

    /** UI 24 - Danh sách tất cả lịch hẹn, lọc theo trạng thái */
    @GetMapping
    public String danhSachLichHen(
            @RequestParam(value = "trangThai", required = false) String trangThai,
            Model model) {
        model.addAttribute("danhSach",   lichHenService.getLichHenByTrangThai(trangThai));
        model.addAttribute("trangThaiFilter", trangThai);
        return "admin/lich-hen/danh-sach";
    }

    /** Admin duyệt lịch: Chờ xác nhận → Đã xác nhận */
    @PostMapping("/{maLichHen}/xac-nhan")
    public String xacNhanLich(@PathVariable String maLichHen, RedirectAttributes ra) {
        try {
            lichHenService.xacNhanLich(maLichHen);
            ra.addFlashAttribute("success", "Đã xác nhận lịch hẹn " + maLichHen);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/lich-hen";
    }

    /** Admin cập nhật trạng thái (Đang khám / Hoàn thành / Đã hủy) */
    @PostMapping("/{maLichHen}/cap-nhat")
    public String capNhatTrangThai(
            @PathVariable String maLichHen,
            @RequestParam("trangThai") String trangThai,
            RedirectAttributes ra) {
        try {
            lichHenService.capNhatTrangThai(maLichHen, trangThai);
            ra.addFlashAttribute("success", "Cập nhật trạng thái thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/lich-hen";
    }
}
