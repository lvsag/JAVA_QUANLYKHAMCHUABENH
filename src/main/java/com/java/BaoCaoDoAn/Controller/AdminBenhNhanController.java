package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.BenhNhan;
import com.java.BaoCaoDoAn.Service.BenhNhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/benh-nhan")
public class AdminBenhNhanController {

    @Autowired
    private BenhNhanService benhNhanService;

    @GetMapping
    public String danhSachBenhNhan(Model model) {
        model.addAttribute("danhSach", benhNhanService.getAllBenhNhan());
        return "admin/benh-nhan/danh-sach";
    }

    @GetMapping("/chi-tiet/{id}")
    public String chiTietBenhNhan(@PathVariable String id, Model model) {
        benhNhanService.getBenhNhanById(id).ifPresent(bn -> model.addAttribute("benhNhan", bn));
        return "admin/benh-nhan/chi-tiet";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("benhNhan", new BenhNhan());
        return "admin/benh-nhan/form";
    }

    @PostMapping("/them")
    public String luuBenhNhan(@ModelAttribute BenhNhan benhNhan) {
        if (benhNhan.getMaBenhNhan() == null || benhNhan.getMaBenhNhan().isEmpty()) {
            benhNhan.setMaBenhNhan("BN" + System.currentTimeMillis());
        }
        benhNhanService.saveBenhNhan(benhNhan);
        return "redirect:/admin/benh-nhan";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable String id, Model model) {
        benhNhanService.getBenhNhanById(id).ifPresent(bn -> model.addAttribute("benhNhan", bn));
        return "admin/benh-nhan/form";
    }

    @PostMapping("/sua/{id}")
    public String capNhatBenhNhan(@PathVariable String id, @ModelAttribute BenhNhan benhNhan) {
        benhNhan.setMaBenhNhan(id);
        benhNhanService.saveBenhNhan(benhNhan);
        return "redirect:/admin/benh-nhan";
    }

    @GetMapping("/xoa/{id}")
    public String xoaBenhNhan(@PathVariable String id) {
        benhNhanService.deleteBenhNhan(id);
        return "redirect:/admin/benh-nhan";
    }
}
