package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.TaiKhoan;
import com.java.BaoCaoDoAn.Repository.VaiTroRepository;
import com.java.BaoCaoDoAn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tai-khoan")
public class AdminTaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @GetMapping
    public String danhSachTaiKhoan(Model model) {
        model.addAttribute("danhSach", taiKhoanService.getAllTaiKhoan());
        return "admin/tai-khoan/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("taiKhoan", new TaiKhoan());
        model.addAttribute("vaiTros", vaiTroRepository.findAll());
        return "admin/tai-khoan/form";
    }

    @PostMapping("/them")
    public String luuTaiKhoan(@ModelAttribute TaiKhoan taiKhoan) {
        taiKhoanService.saveTaiKhoan(taiKhoan);
        return "redirect:/admin/tai-khoan";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Integer id, Model model) {
        taiKhoanService.getTaiKhoanById(id).ifPresent(tk -> model.addAttribute("taiKhoan", tk));
        model.addAttribute("vaiTros", vaiTroRepository.findAll());
        return "admin/tai-khoan/form";
    }

    @PostMapping("/sua/{id}")
    public String capNhatTaiKhoan(@PathVariable Integer id, @ModelAttribute TaiKhoan taiKhoan) {
        taiKhoan.setMaTaiKhoan(id);
        taiKhoanService.saveTaiKhoan(taiKhoan);
        return "redirect:/admin/tai-khoan";
    }

    @PostMapping("/khoa/{id}")
    public String khoaTaiKhoan(@PathVariable Integer id) {
        taiKhoanService.toggleTrangThai(id);
        return "redirect:/admin/tai-khoan";
    }
}
