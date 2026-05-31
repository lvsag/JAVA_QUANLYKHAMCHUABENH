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

    @Autowired
    private com.java.BaoCaoDoAn.Repository.KhungGioKhamRepository khungGioKhamRepository;

    @GetMapping
    public String danhSachLichHen(Model model) {
        // Lấy tất cả lịch hẹn nhưng LỌC BỎ những lịch bị hủy bởi Bệnh nhân (có nhập Lý do hủy)
        java.util.List<LichHen> danhSachHoatDong = lichHenService.getAllLichHen().stream()
                .filter(lh -> lh.getLyDoHuy() == null || lh.getLyDoHuy().trim().isEmpty())
                .collect(java.util.stream.Collectors.toList());
                
        model.addAttribute("danhSach", danhSachHoatDong);
        return "admin/lich-hen/danh-sach";
    }

    @GetMapping("/debug")
    @ResponseBody
    public java.util.List<LichHen> debugLichHen() {
        return lichHenService.getAllLichHen();
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
            
            // Giải phóng khung giờ khám
            if (lh.getMaKhungGio() != null) {
                khungGioKhamRepository.findById(lh.getMaKhungGio()).ifPresent(slot -> {
                    slot.setTrangThai("Còn chỗ");
                    khungGioKhamRepository.save(slot);
                });
            }
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
