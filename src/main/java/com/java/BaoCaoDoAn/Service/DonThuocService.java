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
    private final ThuocService thuocService;

    public DonThuocService(DonThuocRepository donThuocRepository, ThuocService thuocService) {
        this.donThuocRepository = donThuocRepository;
        this.thuocService = thuocService;
    }

    public List<DonThuoc> getAllDonThuoc() {
        return donThuocRepository.findAll();
    }

    public List<DonThuoc> getDonThuocByBenhNhan(String maBenhNhan) {
        return donThuocRepository.findByBenhNhan_MaBenhNhanOrderByNgayKeDesc(maBenhNhan);
    }

    public List<DonThuoc> getPaidDonThuocByBenhNhan(String maBenhNhan) {
        return donThuocRepository.findPaidDonThuocByBenhNhan(maBenhNhan);
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
            Integer maThuoc;
            try {
                maThuoc = Integer.valueOf(tenThuoc.get(i));
            } catch (NumberFormatException ex) {
                continue;
            }
            Thuoc thuoc = thuocService.getThuoc(maThuoc).orElse(null);
            if (thuoc == null) {
                continue;
            }
            ChiTietDonThuoc chiTiet = new ChiTietDonThuoc();
            chiTiet.setDonThuoc(donThuoc);
            // Added: selected form value is MaThuoc so the detail row saves the FK to Thuoc.
            chiTiet.setThuoc(thuoc);
            chiTiet.setLieuDung(getValue(lieuDung, i));
            chiTiet.setSoLanTrongNgay(getValue(soLanTrongNgay, i));
            
            int soLuongThuoc = parseInt(getValue(soLuong, i), 1);
            chiTiet.setSoLuong(soLuongThuoc);
            
            // Deduct from inventory
            if (thuoc.getTonKho() != null) {
                thuoc.setTonKho(Math.max(0, thuoc.getTonKho() - soLuongThuoc));
                thuocService.save(thuoc);
            }
            
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

    private Integer parseInt(String value, int fallback) {
        try {
            return value == null || value.isBlank() ? fallback : Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }
}
