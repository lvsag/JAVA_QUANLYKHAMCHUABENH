package com.java.BaoCaoDoAn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dich-vu")
public class AdminDichVuController {

    @GetMapping
    public String danhSachDichVu() {
        return "redirect:/admin/xet-nghiem";
    }

    @GetMapping("/them")
    public String hienThiFormThem() {
        return "redirect:/admin/xet-nghiem/them";
    }

    @PostMapping("/them")
    public String luuDichVu() {
        return "redirect:/admin/xet-nghiem";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable String id) {
        return "redirect:/admin/xet-nghiem/sua/" + id;
    }

    @PostMapping("/sua/{id}")
    public String capNhatDichVu(@PathVariable String id) {
        return "redirect:/admin/xet-nghiem/sua/" + id;
    }

    @GetMapping("/xoa/{id}")
    public String xoaDichVu(@PathVariable String id) {
        return "redirect:/admin/xet-nghiem";
    }
}
