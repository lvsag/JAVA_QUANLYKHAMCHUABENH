package com.java.BaoCaoDoAn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminXetNghiemController {

    @GetMapping("/xet-nghiem")
    public String ketQuaXetNghiem() {
        // Added/updated: admin sidebar compatibility route now forwards to the doctor workflow area.
        return "redirect:/bac-si/ket-qua";
    }
}
