package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.ChuyenKhoa;
import com.java.BaoCaoDoAn.Service.ChuyenKhoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/chuyen-khoa")
public class AdminChuyenKhoaController {

    @Autowired
    private ChuyenKhoaService chuyenKhoaService;

    @Autowired
    private com.java.BaoCaoDoAn.Repository.BacSiRepository bacSiRepository;

    @GetMapping
    public String danhSachChuyenKhoa(@RequestParam(value = "selectedMaChuyenKhoa", required = false) String selectedMaChuyenKhoa, Model model) {
        java.util.List<ChuyenKhoa> danhSachCK = chuyenKhoaService.getAllChuyenKhoa();
        model.addAttribute("danhSach", danhSachCK);
        
        if (selectedMaChuyenKhoa == null && !danhSachCK.isEmpty()) {
            selectedMaChuyenKhoa = danhSachCK.get(0).getMaChuyenKhoa();
        }
        
        model.addAttribute("selectedMaChuyenKhoa", selectedMaChuyenKhoa);
        
        if (selectedMaChuyenKhoa != null) {
            java.util.List<com.java.BaoCaoDoAn.Model.BacSi> danhSachBacSi = bacSiRepository.findByChuyenKhoa_MaChuyenKhoa(selectedMaChuyenKhoa);
            model.addAttribute("danhSachBacSi", danhSachBacSi);
            
            // Get selected ChuyenKhoa info
            ChuyenKhoa ck = chuyenKhoaService.getChuyenKhoaById(selectedMaChuyenKhoa).orElse(null);
            model.addAttribute("chuyenKhoaInfo", ck);
        }
        
        return "admin/chuyen-khoa/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("chuyenKhoa", new ChuyenKhoa());
        return "admin/chuyen-khoa/form";
    }

    @PostMapping("/them")
    public String luuChuyenKhoa(@ModelAttribute ChuyenKhoa chuyenKhoa) {
        chuyenKhoaService.saveChuyenKhoa(chuyenKhoa);
        return "redirect:/admin/chuyen-khoa";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable String id, Model model) {
        chuyenKhoaService.getChuyenKhoaById(id).ifPresent(ck -> model.addAttribute("chuyenKhoa", ck));
        return "admin/chuyen-khoa/form";
    }

    @PostMapping("/sua/{id}")
    public String capNhatChuyenKhoa(@PathVariable String id, @ModelAttribute ChuyenKhoa chuyenKhoa) {
        chuyenKhoa.setMaChuyenKhoa(id);
        chuyenKhoaService.saveChuyenKhoa(chuyenKhoa);
        return "redirect:/admin/chuyen-khoa";
    }
}
