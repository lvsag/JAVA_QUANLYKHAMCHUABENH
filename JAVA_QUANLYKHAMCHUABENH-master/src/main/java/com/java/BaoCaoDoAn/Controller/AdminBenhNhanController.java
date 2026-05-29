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
}
