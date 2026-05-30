package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.BenhNhan;
import com.java.BaoCaoDoAn.Model.NhapVienNoiTru;
import com.java.BaoCaoDoAn.Service.BenhNhanService;
import com.java.BaoCaoDoAn.Service.NhapVienNoiTruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/admin/benh-nhan")
public class AdminBenhNhanController {

    @Autowired
    private BenhNhanService benhNhanService;

    @Autowired
    private NhapVienNoiTruService nhapVienNoiTruService;

    @GetMapping
    public String danhSachBenhNhan(Model model) {
        List<BenhNhan> danhSach = benhNhanService.getAllBenhNhan();
        Map<String, Boolean> benhNhanDangNoiTruMap = new HashMap<>();
        Map<String, String> trangThaiNoiTruMap = new HashMap<>();
        
        for (BenhNhan bn : danhSach) {
            String maBenhNhan = bn.getMaBenhNhan();
            boolean isDangNoiTru = nhapVienNoiTruService.isBenhNhanDangNoiTru(maBenhNhan);
            benhNhanDangNoiTruMap.put(maBenhNhan, isDangNoiTru);
            
            List<NhapVienNoiTru> records = nhapVienNoiTruService.findByBenhNhan(maBenhNhan);
            if (records.isEmpty()) {
                trangThaiNoiTruMap.put(maBenhNhan, "Chưa nội trú");
            } else if (isDangNoiTru) {
                trangThaiNoiTruMap.put(maBenhNhan, "Đang nội trú");
            } else {
                trangThaiNoiTruMap.put(maBenhNhan, "Đã xuất viện");
            }
        }
        
        model.addAttribute("danhSach", danhSach);
        model.addAttribute("benhNhanDangNoiTruMap", benhNhanDangNoiTruMap);
        model.addAttribute("trangThaiNoiTruMap", trangThaiNoiTruMap);
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
    public String xoaBenhNhan(@PathVariable String id, RedirectAttributes ra) {
        try {
            // 1. Kiểm tra bệnh nhân đang điều trị nội trú
            if (nhapVienNoiTruService.isBenhNhanDangNoiTru(id)) {
                ra.addFlashAttribute("errorMessage", "Bệnh nhân đang điều trị nội trú, không thể xóa.");
                return "redirect:/admin/benh-nhan";
            }
            
            // 2. Kiểm tra nếu có hồ sơ nội trú đã xuất viện
            List<NhapVienNoiTru> dsNoiTru = nhapVienNoiTruService.findByBenhNhan(id);
            boolean hasNhapVienRecords = !dsNoiTru.isEmpty();
            
            if (hasNhapVienRecords) {
                // Xóa hồ sơ nội trú liên quan trước
                nhapVienNoiTruService.deleteByBenhNhan(id);
            }
            
            // 3. Xóa bệnh nhân
            benhNhanService.deleteBenhNhan(id);
            
            if (hasNhapVienRecords) {
                ra.addFlashAttribute("successMessage", "Đã xóa bệnh nhân và hồ sơ xuất viện liên quan.");
            } else {
                ra.addFlashAttribute("successMessage", "Đã xóa bệnh nhân thành công.");
            }
            
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa bệnh nhân do có dữ liệu liên quan khác (lịch hẹn, hóa đơn, v.v.) chưa được xử lý.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình xóa bệnh nhân: " + e.getMessage());
        }
        return "redirect:/admin/benh-nhan";
    }
}
