package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.KhuyenMai;
import com.java.BaoCaoDoAn.Service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/khuyen-mai")
public class AdminKhuyenMaiController {

    @Autowired
    private KhuyenMaiService khuyenMaiService;

    @GetMapping("")
    public String listKhuyenMai(Model model) {
        List<KhuyenMai> ds = khuyenMaiService.findAll();
        model.addAttribute("danhSachKhuyenMai", ds);
        model.addAttribute("activeMenu", "khuyen-mai");
        model.addAttribute("title", "Quản Lý Khuyến Mãi");
        return "admin/khuyen-mai/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        KhuyenMai km = new KhuyenMai();
        km.setSoLuotDaDung(0);
        km.setTrangThai("Hoạt động");
        model.addAttribute("khuyenMai", km);
        model.addAttribute("activeMenu", "khuyen-mai");
        model.addAttribute("title", "Tạo Khuyến Mãi");
        return "admin/khuyen-mai/form";
    }

    @GetMapping("/sua/{maKhuyenMai}")
    public String hienThiFormSua(@PathVariable("maKhuyenMai") Integer maKhuyenMai, Model model, RedirectAttributes ra) {
        Optional<KhuyenMai> kmOpt = khuyenMaiService.findById(maKhuyenMai);
        if (!kmOpt.isPresent()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy mã khuyến mãi!");
            return "redirect:/admin/khuyen-mai";
        }
        model.addAttribute("khuyenMai", kmOpt.get());
        model.addAttribute("activeMenu", "khuyen-mai");
        model.addAttribute("title", "Cập Nhật Khuyến Mãi");
        return "admin/khuyen-mai/form";
    }

    @PostMapping("/luu")
    public String luuKhuyenMai(@ModelAttribute("khuyenMai") KhuyenMai khuyenMai, org.springframework.validation.BindingResult bindingResult, Model model, RedirectAttributes ra) {
        // Handle model binding/parsing errors (preventing 400 Whitelabel)
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Định dạng dữ liệu không hợp lệ. Vui lòng kiểm tra lại thông tin nhập (ngày bắt đầu/kết thúc phải đúng định dạng ngày)!");
            model.addAttribute("activeMenu", "khuyen-mai");
            model.addAttribute("title", khuyenMai.getMaKhuyenMai() == null ? "Tạo Khuyến Mãi" : "Cập Nhật Khuyến Mãi");
            return "admin/khuyen-mai/form";
        }

        // Form validations
        if (khuyenMai.getMaCode() == null || khuyenMai.getMaCode().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Mã code không được rỗng!");
            model.addAttribute("activeMenu", "khuyen-mai");
            model.addAttribute("title", khuyenMai.getMaKhuyenMai() == null ? "Tạo Khuyến Mãi" : "Cập Nhật Khuyến Mãi");
            return "admin/khuyen-mai/form";
        }

        String maCodeTrim = khuyenMai.getMaCode().trim().toUpperCase();
        khuyenMai.setMaCode(maCodeTrim);

        // Unique validation
        Optional<KhuyenMai> existing = khuyenMaiService.findByMaCode(maCodeTrim);
        if (existing.isPresent()) {
            if (khuyenMai.getMaKhuyenMai() == null || !existing.get().getMaKhuyenMai().equals(khuyenMai.getMaKhuyenMai())) {
                model.addAttribute("errorMessage", "Mã code '" + maCodeTrim + "' đã tồn tại trong hệ thống!");
                model.addAttribute("activeMenu", "khuyen-mai");
                model.addAttribute("title", khuyenMai.getMaKhuyenMai() == null ? "Tạo Khuyến Mãi" : "Cập Nhật Khuyến Mãi");
                return "admin/khuyen-mai/form";
            }
        }

        if (khuyenMai.getGiaTriGiam() == null || khuyenMai.getGiaTriGiam().doubleValue() < 0) {
            model.addAttribute("errorMessage", "Giá trị giảm phải lớn hơn hoặc bằng 0!");
            model.addAttribute("activeMenu", "khuyen-mai");
            model.addAttribute("title", khuyenMai.getMaKhuyenMai() == null ? "Tạo Khuyến Mãi" : "Cập Nhật Khuyến Mãi");
            return "admin/khuyen-mai/form";
        }

        if (khuyenMai.getSoLuotToiDa() != null && khuyenMai.getSoLuotToiDa() < 0) {
            model.addAttribute("errorMessage", "Số lượt tối đa phải lớn hơn hoặc bằng 0!");
            model.addAttribute("activeMenu", "khuyen-mai");
            model.addAttribute("title", khuyenMai.getMaKhuyenMai() == null ? "Tạo Khuyến Mãi" : "Cập Nhật Khuyến Mãi");
            return "admin/khuyen-mai/form";
        }

        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayKetThuc() != null) {
            if (khuyenMai.getNgayKetThuc().before(khuyenMai.getNgayBatDau())) {
                model.addAttribute("errorMessage", "Ngày kết thúc không được nhỏ hơn ngày bắt đầu!");
                model.addAttribute("activeMenu", "khuyen-mai");
                model.addAttribute("title", khuyenMai.getMaKhuyenMai() == null ? "Tạo Khuyến Mãi" : "Cập Nhật Khuyến Mãi");
                return "admin/khuyen-mai/form";
            }
        }

        if (khuyenMai.getSoLuotDaDung() == null) {
            khuyenMai.setSoLuotDaDung(0);
        }

        if (khuyenMai.getTrangThai() == null || khuyenMai.getTrangThai().trim().isEmpty()) {
            khuyenMai.setTrangThai("Hoạt động");
        }

        try {
            khuyenMaiService.save(khuyenMai);
            ra.addFlashAttribute("successMessage", "Lưu mã khuyến mãi thành công!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("activeMenu", "khuyen-mai");
            model.addAttribute("title", khuyenMai.getMaKhuyenMai() == null ? "Tạo Khuyến Mãi" : "Cập Nhật Khuyến Mãi");
            return "admin/khuyen-mai/form";
        }

        return "redirect:/admin/khuyen-mai";
    }

    @GetMapping("/xoa/{maKhuyenMai}")
    public String xoaKhuyenMai(@PathVariable("maKhuyenMai") Integer maKhuyenMai, RedirectAttributes ra) {
        try {
            khuyenMaiService.deleteById(maKhuyenMai);
            ra.addFlashAttribute("successMessage", "Đã xóa khuyến mãi thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa khuyến mãi do có ràng buộc dữ liệu: " + e.getMessage());
        }
        return "redirect:/admin/khuyen-mai";
    }
}
