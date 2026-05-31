package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.*;
import com.java.BaoCaoDoAn.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/hoa-don")
public class AdminHoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private ChiTietHoaDonService chiTietHoaDonService;

    @Autowired
    private NhapVienNoiTruService nhapVienNoiTruService;

    @GetMapping("")
    public String danhSachHoaDon(Model model) {
        List<HoaDon> ds = hoaDonService.findAll();
        model.addAttribute("danhSachHoaDon", ds);
        model.addAttribute("activeMenu", "hoa-don");
        return "admin/hoa-don/danh-sach";
    }

    @GetMapping("/chi-tiet/{maHoaDon}")
    public String chiTietHoaDon(@PathVariable("maHoaDon") String maHoaDon, Model model, RedirectAttributes ra) {
        Optional<HoaDon> hdOpt = hoaDonService.findById(maHoaDon);
        if (!hdOpt.isPresent()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn có mã: " + maHoaDon);
            return "redirect:/admin/hoa-don";
        }
        HoaDon hd = hdOpt.get();
        List<ChiTietHoaDon> details = chiTietHoaDonService.findByMaHoaDon(maHoaDon);

        model.addAttribute("hoaDon", hd);
        model.addAttribute("chiTietHoaDon", details);
        model.addAttribute("activeMenu", "hoa-don");
        return "admin/hoa-don/chi-tiet";
    }

    @GetMapping("/tao-tu-noi-tru/{maNoiTru}")
    public String taoTuNoiTru(@PathVariable("maNoiTru") String maNoiTru, RedirectAttributes ra) {
        Optional<NhapVienNoiTru> ntOpt = nhapVienNoiTruService.findById(maNoiTru);
        if (!ntOpt.isPresent()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy hồ sơ nội trú.");
            return "redirect:/admin/noi-tru";
        }

        NhapVienNoiTru nt = ntOpt.get();
        if (!"Đã xuất viện".equals(nt.getTrangThai())) {
            ra.addFlashAttribute("errorMessage", "Bệnh nhân cần làm thủ tục xuất viện trước khi thanh toán.");
            return "redirect:/admin/noi-tru";
        }

        // Kiểm tra xem đã có hóa đơn chưa
        Optional<HoaDon> hdOpt = hoaDonService.findByMaNoiTru(maNoiTru);
        if (hdOpt.isPresent()) {
            ra.addFlashAttribute("successMessage", "Hóa đơn cho hồ sơ nội trú này đã được tạo trước đó.");
            return "redirect:/admin/hoa-don/chi-tiet/" + hdOpt.get().getMaHoaDon();
        }

        try {
            HoaDon hd = hoaDonService.taoHoaDonTuNoiTru(maNoiTru);
            ra.addFlashAttribute("successMessage", "Tạo hóa đơn từ hồ sơ nội trú thành công.");
            return "redirect:/admin/hoa-don/chi-tiet/" + hd.getMaHoaDon();
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi khi tạo hóa đơn: " + e.getMessage());
            return "redirect:/admin/noi-tru";
        }
    }

    @PostMapping("/thanh-toan/{maHoaDon}")
    public String thanhToan(@PathVariable("maHoaDon") String maHoaDon,
            @RequestParam(value = "phuongThucThanhToan", defaultValue = "Tiền mặt") String phuongThucThanhToan,
            @RequestParam(value = "ghiChu", required = false) String ghiChu,
            RedirectAttributes ra) {
        Optional<HoaDon> hdOpt = hoaDonService.findById(maHoaDon);
        if (!hdOpt.isPresent()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn.");
            return "redirect:/admin/hoa-don";
        }

        HoaDon hd = hdOpt.get();
        if ("Đã thanh toán".equals(hd.getTrangThai())) {
            ra.addFlashAttribute("errorMessage", "Hóa đơn này đã được thanh toán trước đó.");
            return "redirect:/admin/hoa-don/chi-tiet/" + maHoaDon;
        }

        try {
            hd.setTrangThai("Đã thanh toán");
            hd.setSoTienDaThanhToan(hd.getTongCanThanhToan());
            hd.setNgayThanhToan(new Date());
            hd.setPhuongThucThanhToan(phuongThucThanhToan);
            hd.setNguoiThu("Quầy Thu Ngân");
            if (ghiChu != null && !ghiChu.trim().isEmpty()) {
                hd.setGhiChu(ghiChu);
            }
            hoaDonService.save(hd);

            ra.addFlashAttribute("successMessage", "Xác nhận thanh toán hóa đơn " + maHoaDon + " thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi khi thanh toán: " + e.getMessage());
        }

        return "redirect:/admin/hoa-don/chi-tiet/" + maHoaDon;
    }

    @PostMapping("/ap-dung-khuyen-mai/{maHoaDon}")
    public String apDungKhuyenMai(@PathVariable("maHoaDon") String maHoaDon,
                                   @RequestParam("maCode") String maCode,
                                   RedirectAttributes ra) {
        try {
            hoaDonService.apDungKhuyenMai(maHoaDon, maCode);
            ra.addFlashAttribute("successMessage", "Áp dụng mã khuyến mãi '" + maCode.trim().toUpperCase() + "' thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Không thể áp dụng khuyến mãi: " + e.getMessage());
        }
        return "redirect:/admin/hoa-don/chi-tiet/" + maHoaDon;
    }

    @GetMapping("/xoa/{maHoaDon}")
    public String xoaHoaDon(@PathVariable("maHoaDon") String maHoaDon, RedirectAttributes ra) {
        Optional<HoaDon> hdOpt = hoaDonService.findById(maHoaDon);
        if (!hdOpt.isPresent()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn.");
            return "redirect:/admin/hoa-don";
        }

        try {
            hoaDonService.deleteHoaDonVaChiTiet(maHoaDon);
            ra.addFlashAttribute("successMessage", "Đã xóa hóa đơn thành công.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi khi xóa hóa đơn: " + e.getMessage());
        }

        return "redirect:/admin/hoa-don";
    }
}
