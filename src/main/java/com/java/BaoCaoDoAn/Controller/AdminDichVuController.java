package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.DichVu;
import com.java.BaoCaoDoAn.Service.ChuyenKhoaService;
import com.java.BaoCaoDoAn.Service.DichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/dich-vu")
public class AdminDichVuController {

    @Autowired
    private DichVuService dichVuService;

    @Autowired
    private ChuyenKhoaService chuyenKhoaService;

    @GetMapping
    public String danhSachDichVu(Model model) {
        model.addAttribute("danhSach", dichVuService.getAllDichVu());
        return "admin/dich-vu/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("dichVu", new DichVu());
        model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
        return "admin/dich-vu/form";
    }

    @PostMapping("/them")
    public String luuDichVu(@ModelAttribute DichVu dichVu) {
        if (dichVu.getMaDichVu() == null || dichVu.getMaDichVu().isEmpty()) {
            dichVu.setMaDichVu("DV" + System.currentTimeMillis());
        }
        dichVuService.saveDichVu(dichVu);
        return "redirect:/admin/dich-vu";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable String id, Model model) {
        DichVu dv = dichVuService.getDichVuById(id);
        if(dv != null) {
            model.addAttribute("dichVu", dv);
        } else {
            return "redirect:/admin/dich-vu";
        }
        model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
        return "admin/dich-vu/form";
    }

    @PostMapping("/sua/{id}")
    public String capNhatDichVu(@PathVariable String id, @ModelAttribute DichVu dichVu) {
        dichVu.setMaDichVu(id);
        dichVuService.saveDichVu(dichVu);
        return "redirect:/admin/dich-vu";
    }

    @GetMapping("/xoa/{id}")
    public String xoaDichVu(@PathVariable String id) {
        DichVu dv = dichVuService.getDichVuById(id);
        if(dv != null) {
            dv.setTrangThai("Ngừng cung cấp");
            dichVuService.saveDichVu(dv);
        }
        return "redirect:/admin/dich-vu";
    }
}
