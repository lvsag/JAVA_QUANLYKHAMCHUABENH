package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.LichHen;
import com.java.BaoCaoDoAn.Service.LichHenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/lich-hen")
public class AdminLichHenController {

    @Autowired
    private LichHenService lichHenService;

    @Autowired
    private com.java.BaoCaoDoAn.Service.KhamBenhService khamBenhService;

    @Autowired
    private com.java.BaoCaoDoAn.Service.BenhNhanService benhNhanService;
    
    @Autowired
    private com.java.BaoCaoDoAn.Service.BacSiService bacSiService;

    @GetMapping
    public String danhSachLichHen(Model model) {
        model.addAttribute("danhSach", lichHenService.getAllLichHen());
        return "admin/lich-hen/danh-sach";
    }

    @GetMapping("/duyet/{id}")
    public String duyetLichHen(@PathVariable String id) {
        lichHenService.getLichHenById(id).ifPresent(lh -> {
            lh.setTrangThai("Đã xác nhận");
            lichHenService.saveLichHen(lh);
            if (lh.getBacSi() != null && lh.getBenhNhan() != null) {
                khamBenhService.taoPhieuKhamTuLichHen(lh);
            }
        });
        return "redirect:/admin/lich-hen";
    }

    @GetMapping("/huy/{id}")
    public String huyLichHen(@PathVariable String id) {
        lichHenService.getLichHenById(id).ifPresent(lh -> {
            lh.setTrangThai("Đã hủy");
            lichHenService.saveLichHen(lh);
        });
        return "redirect:/admin/lich-hen";
    }

    @GetMapping("/hoan-thanh/{id}")
    public String hoanThanhLichHen(@PathVariable String id) {
        lichHenService.getLichHenById(id).ifPresent(lh -> {
            lh.setTrangThai("Đã hoàn thành");
            lichHenService.saveLichHen(lh);
        });
        return "redirect:/admin/lich-hen";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("lichHen", new LichHen());
        model.addAttribute("benhNhans", benhNhanService.getAllBenhNhan());
        model.addAttribute("bacSis", bacSiService.getAllBacSi());
        return "admin/lich-hen/form";
    }

    @PostMapping("/them")
    public String luuLichHen(@ModelAttribute LichHen lichHen) {
        if (lichHen.getMaLichHen() == null || lichHen.getMaLichHen().isEmpty()) {
            lichHen.setMaLichHen("LH" + System.currentTimeMillis());
            lichHen.setTrangThai("Chờ xác nhận");
        }
        lichHenService.saveLichHen(lichHen);
        return "redirect:/admin/lich-hen";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable String id, Model model) {
        lichHenService.getLichHenById(id).ifPresent(lh -> model.addAttribute("lichHen", lh));
        model.addAttribute("benhNhans", benhNhanService.getAllBenhNhan());
        model.addAttribute("bacSis", bacSiService.getAllBacSi());
        return "admin/lich-hen/form";
    }

    @PostMapping("/sua/{id}")
    public String capNhatLichHen(@PathVariable String id, @ModelAttribute LichHen lichHen) {
        lichHen.setMaLichHen(id);
        lichHenService.saveLichHen(lichHen);
        return "redirect:/admin/lich-hen";
    }
}
