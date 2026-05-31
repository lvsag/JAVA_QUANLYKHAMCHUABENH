package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.DichVu;
import com.java.BaoCaoDoAn.Service.ChuyenKhoaService;
import com.java.BaoCaoDoAn.Service.DichVuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/xet-nghiem")
public class AdminXetNghiemController {

    private static final String LOAI_XET_NGHIEM = "Xét nghiệm";
    private static final String TRANG_THAI_DANG_DUNG = "Đang dùng";
    private static final String TRANG_THAI_NGUNG_CUNG_CAP = "Ngừng cung cấp";

    private final DichVuService dichVuService;
    private final ChuyenKhoaService chuyenKhoaService;

    public AdminXetNghiemController(DichVuService dichVuService, ChuyenKhoaService chuyenKhoaService) {
        this.dichVuService = dichVuService;
        this.chuyenKhoaService = chuyenKhoaService;
    }

    @GetMapping
    public String danhSachXetNghiem(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "trangThai", required = false) String trangThai,
            Model model) {

        model.addAttribute("danhSach", dichVuService.searchXetNghiem(keyword, trangThai));
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        return "admin/xet-nghiem/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        DichVu xetNghiem = new DichVu();
        xetNghiem.setLoaiDichVu(LOAI_XET_NGHIEM);
        xetNghiem.setTrangThai(TRANG_THAI_DANG_DUNG);
        model.addAttribute("dichVu", xetNghiem);
        model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
        return "admin/xet-nghiem/form";
    }

    @PostMapping("/them")
    public String luuXetNghiem(@ModelAttribute("dichVu") DichVu dichVu, RedirectAttributes redirectAttributes) {
        if (dichVu.getMaDichVu() == null || dichVu.getMaDichVu().isBlank()) {
            dichVu.setMaDichVu(generateMaXetNghiem());
        }
        prepareXetNghiem(dichVu);
        dichVuService.saveDichVu(dichVu);
        redirectAttributes.addFlashAttribute("success", "Thêm xét nghiệm thành công.");
        return "redirect:/admin/xet-nghiem";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        DichVu dichVu = dichVuService.getDichVuById(id);
        if (dichVu == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy xét nghiệm.");
            return "redirect:/admin/xet-nghiem";
        }

        model.addAttribute("dichVu", dichVu);
        model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
        return "admin/xet-nghiem/form";
    }

    @PostMapping("/sua/{id}")
    public String capNhatXetNghiem(
            @PathVariable String id,
            @ModelAttribute("dichVu") DichVu dichVu,
            RedirectAttributes redirectAttributes) {

        dichVu.setMaDichVu(id);
        prepareXetNghiem(dichVu);
        dichVuService.saveDichVu(dichVu);
        redirectAttributes.addFlashAttribute("success", "Cập nhật xét nghiệm thành công.");
        return "redirect:/admin/xet-nghiem";
    }

    @GetMapping("/xoa/{id}")
    public String xoaXetNghiem(@PathVariable String id, RedirectAttributes redirectAttributes) {
        DichVu dichVu = dichVuService.getDichVuById(id);
        if (dichVu != null) {
            dichVu.setTrangThai(TRANG_THAI_NGUNG_CUNG_CAP);
            prepareXetNghiem(dichVu);
            dichVuService.saveDichVu(dichVu);
            redirectAttributes.addFlashAttribute("success", "Đã ngừng cung cấp xét nghiệm.");
        }
        return "redirect:/admin/xet-nghiem";
    }

    private void prepareXetNghiem(DichVu dichVu) {
        dichVu.setLoaiDichVu(LOAI_XET_NGHIEM);
        if (dichVu.getTrangThai() == null || dichVu.getTrangThai().isBlank()) {
            dichVu.setTrangThai(TRANG_THAI_DANG_DUNG);
        }
    }

    private String generateMaXetNghiem() {
        String millis = String.valueOf(System.currentTimeMillis());
        return "XN" + millis.substring(millis.length() - 8);
    }
}
