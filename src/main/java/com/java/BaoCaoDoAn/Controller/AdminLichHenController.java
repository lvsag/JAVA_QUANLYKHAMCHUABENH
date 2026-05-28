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

    @GetMapping
    public String danhSachLichHen(Model model) {
        model.addAttribute("danhSach", lichHenService.getAllLichHen());
        return "admin/lich-hen/danh-sach";
    }
}
