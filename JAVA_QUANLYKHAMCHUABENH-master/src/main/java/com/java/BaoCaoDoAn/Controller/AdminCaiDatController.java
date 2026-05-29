package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Service.CaiDatHeThongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping("/admin/cai-dat")
public class AdminCaiDatController {

    @Autowired
    private CaiDatHeThongService caiDatHeThongService;

    @GetMapping
    public String hienThiCaiDat(Model model) {
        model.addAttribute("settings", caiDatHeThongService.getAllSettings());
        return "admin/cai-dat/index";
    }

    @PostMapping("/luu")
    public String luuCaiDat(@RequestParam Map<String, String> allParams) {
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (!entry.getKey().equals("_csrf")) {
                caiDatHeThongService.saveSetting(entry.getKey(), entry.getValue(), null);
            }
        }
        return "redirect:/admin/cai-dat";
    }
}
