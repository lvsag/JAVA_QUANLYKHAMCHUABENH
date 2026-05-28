package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.BacSi;
import com.java.BaoCaoDoAn.Service.BacSiService;
import com.java.BaoCaoDoAn.Service.ChuyenKhoaService;
import com.java.BaoCaoDoAn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/bac-si")
public class AdminBacSiController {

    @Autowired
    private BacSiService bacSiService;

    @Autowired
    private ChuyenKhoaService chuyenKhoaService;
    
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping
    public String danhSachBacSi(Model model) {
        model.addAttribute("danhSach", bacSiService.getAllBacSi());
        return "admin/bac-si/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("bacSi", new BacSi());
        model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
        model.addAttribute("taiKhoans", taiKhoanService.getAllTaiKhoan()); 
        return "admin/bac-si/form";
    }

    @PostMapping("/them")
    public String luuBacSi(@ModelAttribute BacSi bacSi) {
        bacSiService.saveBacSi(bacSi);
        return "redirect:/admin/bac-si";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable String id, Model model) {
        bacSiService.getBacSiById(id).ifPresent(bs -> model.addAttribute("bacSi", bs));
        model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
        model.addAttribute("taiKhoans", taiKhoanService.getAllTaiKhoan());
        return "admin/bac-si/form";
    }

    @PostMapping("/sua/{id}")
    public String capNhatBacSi(@PathVariable String id, @ModelAttribute BacSi bacSi) {
        bacSi.setMaBacSi(id);
        bacSiService.saveBacSi(bacSi);
        return "redirect:/admin/bac-si";
    }
}
