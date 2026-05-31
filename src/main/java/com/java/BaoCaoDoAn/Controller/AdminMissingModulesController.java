package com.java.BaoCaoDoAn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminMissingModulesController {

    @GetMapping("/bao-cao")
    public String showBaoCao(Model model) {
        model.addAttribute("activeMenu", "bao-cao");
        return "admin/bao-cao/index";
    }

    @GetMapping("/lich-lam-viec")
    public String showLichLamViec(Model model) {
        model.addAttribute("activeMenu", "lich-lam-viec");
        return "admin/lich-lam-viec/index";
    }

    @GetMapping("/tin-tuc")
    public String showTinTuc(Model model) {
        model.addAttribute("activeMenu", "tin-tuc");
        return "admin/tin-tuc/index";
    }

    @GetMapping("/thong-bao")
    public String showThongBao(Model model) {
        model.addAttribute("activeMenu", "thong-bao");
        return "admin/thong-bao/index";
    }

    @GetMapping("/nhat-ky")
    public String showNhatKy(Model model) {
        model.addAttribute("activeMenu", "nhat-ky");
        return "admin/nhat-ky/index";
    }
}
