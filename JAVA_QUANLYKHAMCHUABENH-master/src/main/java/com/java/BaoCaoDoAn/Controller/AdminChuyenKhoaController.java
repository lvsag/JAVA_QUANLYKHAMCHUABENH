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

    @GetMapping
    public String danhSachChuyenKhoa(Model model) {
        model.addAttribute("danhSach", chuyenKhoaService.getAllChuyenKhoa());
        return "admin/chuyen-khoa/danh-sach";
    }
}
