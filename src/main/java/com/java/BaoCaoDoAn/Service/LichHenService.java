package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.LichHen;
import com.java.BaoCaoDoAn.Repository.LichHenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LichHenService {
    @Autowired
    private LichHenRepository lichHenRepository;

    public List<LichHen> getAllLichHen() {
        return lichHenRepository.findAll();
    }
}
