package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.*;
import com.java.BaoCaoDoAn.Repository.DonThuocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DonThuocService {
    private final DonThuocRepository donThuocRepository;

    public DonThuocService(DonThuocRepository donThuocRepository) {
        this.donThuocRepository = donThuocRepository;
    }

    public List<DonThuoc> getAllDonThuoc() {
        return donThuocRepository.findAll();
    }

    public List<DonThuoc> getDonThuocByBenhNhan(String maBenhNhan) {
        return donThuocRepository.findByBenhNhan_MaBenhNhanOrderByNgayKeDesc(maBenhNhan);
    }

    public Optional<DonThuoc> getDonThuoc(String maDonThuoc) {
        return donThuocRepository.findById(maDonThuoc);
    }

    @Transactional
    public DonThuoc taoDonThuoc(PhieuKham phieuKham, BenhNhan benhNhan, BacSi bacSi, String chanDoan, String loiDan,
                                List<String> tenThuoc, List<String> lieuDung, List<String> soLanTrongNgay,
                                List<String> soLuong, List<String> ghiChu) {
        DonThuoc donThuoc = new DonThuoc();
        donThuoc.setMaDonThuoc("DT" + System.currentTimeMillis());
        donThuoc.setPhieuKham(phieuKham);
        donThuoc.setBenhNhan(benhNhan);
        donThuoc.setBacSi(bacSi);
        donThuoc.setChanDoan(chanDoan);
        donThuoc.setLoiDan(loiDan);
        donThuoc.setTrangThai("Dang dung");

        List<ChiTietDonThuoc> chiTietList = new ArrayList<>();
        int size = tenThuoc == null ? 0 : tenThuoc.size();
        for (int i = 0; i < size; i++) {
            if (tenThuoc.get(i) == null || tenThuoc.get(i).isBlank()) {
                continue;
            }
            ChiTietDonThuoc chiTiet = new ChiTietDonThuoc();
            chiTiet.setDonThuoc(donThuoc);
            chiTiet.setTenThuoc(tenThuoc.get(i));
            chiTiet.setLieuDung(getValue(lieuDung, i));
            chiTiet.setSoLanTrongNgay(getValue(soLanTrongNgay, i));
            chiTiet.setSoLuong(getValue(soLuong, i));
            chiTiet.setGhiChu(getValue(ghiChu, i));
            chiTietList.add(chiTiet);
        }
        donThuoc.setChiTietDonThuocs(chiTietList);
        return donThuocRepository.save(donThuoc);
    }

    public DonThuoc saveDonThuoc(DonThuoc donThuoc) {
        return donThuocRepository.save(donThuoc);
    }

    private String getValue(List<String> values, int index) {
        return values != null && index < values.size() ? values.get(index) : "";
    }
}
