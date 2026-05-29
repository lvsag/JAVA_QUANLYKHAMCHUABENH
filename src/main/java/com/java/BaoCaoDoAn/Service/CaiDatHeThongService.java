package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.CaiDatHeThong;
import com.java.BaoCaoDoAn.Repository.CaiDatHeThongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CaiDatHeThongService {
    @Autowired
    private CaiDatHeThongRepository caiDatHeThongRepository;

    public List<CaiDatHeThong> getAllSettings() {
        return caiDatHeThongRepository.findAll();
    }

    public void saveSetting(String key, String value, String description) {
        Optional<CaiDatHeThong> existing = caiDatHeThongRepository.findByTenCaiDat(key);
        CaiDatHeThong setting = existing.orElse(new CaiDatHeThong());
        setting.setTenCaiDat(key);
        setting.setGiaTri(value);
        if (description != null) setting.setMoTa(description);
        caiDatHeThongRepository.save(setting);
    }
    
    public String getSettingValue(String key, String defaultValue) {
        return caiDatHeThongRepository.findByTenCaiDat(key)
                .map(CaiDatHeThong::getGiaTri)
                .orElse(defaultValue);
    }
}
