package com.java.BaoCaoDoAn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard/index";
    }

    @GetMapping({"", "/"})
    public String index() {
        return "redirect:/admin/dashboard";
    }
}
