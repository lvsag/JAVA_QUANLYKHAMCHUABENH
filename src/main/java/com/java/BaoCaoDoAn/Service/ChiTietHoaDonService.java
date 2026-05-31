package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.ChiTietHoaDon;
import com.java.BaoCaoDoAn.Repository.ChiTietHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiTietHoaDonService {

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    public List<ChiTietHoaDon> findByMaHoaDon(String maHoaDon) {
        return chiTietHoaDonRepository.findByHoaDon_MaHoaDon(maHoaDon);
    }

    public ChiTietHoaDon save(ChiTietHoaDon chiTiet) {
        return chiTietHoaDonRepository.save(chiTiet);
    }

    public List<ChiTietHoaDon> saveAll(List<ChiTietHoaDon> list) {
        return chiTietHoaDonRepository.saveAll(list);
    }
}
