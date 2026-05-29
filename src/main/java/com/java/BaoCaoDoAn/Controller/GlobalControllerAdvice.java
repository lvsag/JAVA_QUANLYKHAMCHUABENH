package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.CaiDatHeThong;
import com.java.BaoCaoDoAn.Model.ChuyenKhoa;
import com.java.BaoCaoDoAn.Repository.CaiDatHeThongRepository;
import com.java.BaoCaoDoAn.Repository.ChuyenKhoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CaiDatHeThongRepository caiDatHeThongRepository;

    @Autowired
    private ChuyenKhoaRepository chuyenKhoaRepository;

    @ModelAttribute("siteSettings")
    public Map<String, String> getSiteSettings() {
        List<CaiDatHeThong> settingsList = caiDatHeThongRepository.findAll();
        Map<String, String> settingsMap = new HashMap<>();
        for (CaiDatHeThong setting : settingsList) {
            settingsMap.put(setting.getTenCaiDat(), setting.getGiaTri());
        }
        return settingsMap;
    }

    @ModelAttribute("footerSpecialties")
    public List<ChuyenKhoa> getFooterSpecialties() {
        // Lấy 5 chuyên khoa đầu tiên đang hoạt động cho footer
        return chuyenKhoaRepository.findAll().stream()
                .filter(ck -> "Hoạt động".equals(ck.getTrangThai()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
