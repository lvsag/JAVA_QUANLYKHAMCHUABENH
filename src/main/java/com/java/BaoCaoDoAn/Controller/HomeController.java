package com.java.BaoCaoDoAn.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private com.java.BaoCaoDoAn.Repository.BenhNhanRepository benhNhanRepository;
    
    @Autowired
    private com.java.BaoCaoDoAn.Repository.BacSiRepository bacSiRepository;
    
    @Autowired
    private com.java.BaoCaoDoAn.Repository.ChuyenKhoaRepository chuyenKhoaRepository;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(org.springframework.ui.Model model) {
        // Load stats
        long totalPatients = benhNhanRepository.count();
        long totalDoctors = bacSiRepository.count();
        long totalSpecialties = chuyenKhoaRepository.count();
        
        // Load lists
        java.util.List<com.java.BaoCaoDoAn.Model.ChuyenKhoa> specialties = chuyenKhoaRepository.findAll();
        java.util.List<com.java.BaoCaoDoAn.Model.BacSi> doctors = bacSiRepository.findAll();
        
        // Limit to top 5 or 6 for the homepage if necessary, but we'll pass all and let Thymeleaf slice it
        java.math.BigDecimal averageRating = bacSiRepository.getAverageRating();
        
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("totalDoctors", totalDoctors);
        model.addAttribute("totalSpecialties", totalSpecialties);
        model.addAttribute("averageRating", averageRating != null ? averageRating : java.math.BigDecimal.ZERO);
        model.addAttribute("specialties", specialties);
        model.addAttribute("doctors", doctors);
        
        return "public/home";
    }

    @GetMapping("/bac-si")
    public String doctors(
            @org.springframework.web.bind.annotation.RequestParam(value = "keyword", required = false) String keyword,
            @org.springframework.web.bind.annotation.RequestParam(value = "maChuyenKhoa", required = false) String maChuyenKhoa,
            @org.springframework.web.bind.annotation.RequestParam(value = "hocVi", required = false) java.util.List<String> hocViList,
            @org.springframework.web.bind.annotation.RequestParam(value = "rating", required = false) java.math.BigDecimal rating,
            @org.springframework.web.bind.annotation.RequestParam(value = "sort", required = false, defaultValue = "phu-hop") String sortParam,
            org.springframework.ui.Model model) {
        
        if (hocViList != null && hocViList.isEmpty()) {
            hocViList = null;
        }
        
        org.springframework.data.domain.Sort sortObj = org.springframework.data.domain.Sort.unsorted();
        if ("danh-gia".equals(sortParam)) {
            sortObj = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "diemDanhGiaTB");
        } else if ("lich-hen".equals(sortParam)) {
            sortObj = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "soBenhNhanDaKham");
        } else {
            sortObj = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "hoTen");
        }
        
        java.util.List<com.java.BaoCaoDoAn.Model.BacSi> doctors = bacSiRepository.searchBacSi(keyword, maChuyenKhoa, hocViList, rating, sortObj);
        java.util.List<com.java.BaoCaoDoAn.Model.ChuyenKhoa> specialties = chuyenKhoaRepository.findAll();
        
        model.addAttribute("doctors", doctors);
        model.addAttribute("specialties", specialties);
        model.addAttribute("totalDoctors", doctors.size());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedMaChuyenKhoa", maChuyenKhoa);
        model.addAttribute("selectedHocVi", hocViList != null ? hocViList : new java.util.ArrayList<>());
        model.addAttribute("selectedRating", rating);
        model.addAttribute("selectedSort", sortParam);
        
        return "public/bac-si";
    }
}
