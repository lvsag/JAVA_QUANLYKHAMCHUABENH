package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.*;
import com.java.BaoCaoDoAn.Repository.ChiTietChiDinhRepository;
import com.java.BaoCaoDoAn.Repository.PhieuChiDinhRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChiDinhService {
    private final PhieuChiDinhRepository phieuChiDinhRepository;
    private final ChiTietChiDinhRepository chiTietChiDinhRepository;
    private final DichVuService dichVuService;

    public ChiDinhService(PhieuChiDinhRepository phieuChiDinhRepository,
                          ChiTietChiDinhRepository chiTietChiDinhRepository,
                          DichVuService dichVuService) {
        this.phieuChiDinhRepository = phieuChiDinhRepository;
        this.chiTietChiDinhRepository = chiTietChiDinhRepository;
        this.dichVuService = dichVuService;
    }

    public List<PhieuChiDinh> getAllPhieuChiDinh() {
        return phieuChiDinhRepository.findAll();
    }

    public List<PhieuChiDinh> getPhieuChoThucHien() {
        return phieuChiDinhRepository.findByTrangThaiOrderByNgayChiDinhAsc("Cho thuc hien");
    }

    public Optional<PhieuChiDinh> getPhieuChiDinh(String maPhieuChiDinh) {
        return phieuChiDinhRepository.findById(maPhieuChiDinh);
    }

    public List<ChiTietChiDinh> getChiTiet(String maPhieuChiDinh) {
        return chiTietChiDinhRepository.findByPhieuChiDinh_MaPhieuChiDinh(maPhieuChiDinh);
    }

    @Transactional
    public PhieuChiDinh taoPhieuChiDinh(PhieuKham phieuKham, BenhNhan benhNhan, BacSi bacSi,
                                        List<String> maDichVuList, String ghiChu) {
        PhieuChiDinh phieu = new PhieuChiDinh();
        phieu.setMaPhieuChiDinh("CD" + System.currentTimeMillis());
        phieu.setPhieuKham(phieuKham);
        phieu.setBenhNhan(benhNhan);
        phieu.setBacSi(bacSi);
        phieu.setGhiChu(ghiChu);
        phieu.setTrangThai("Cho thuc hien");

        List<ChiTietChiDinh> chiTiets = new ArrayList<>();
        BigDecimal tongTien = BigDecimal.ZERO;
        if (maDichVuList != null) {
            for (String maDichVu : maDichVuList) {
                DichVu dichVu = dichVuService.getDichVuById(maDichVu);
                if (dichVu == null) {
                    continue;
                }
                ChiTietChiDinh chiTiet = new ChiTietChiDinh();
                chiTiet.setPhieuChiDinh(phieu);
                chiTiet.setDichVu(dichVu);
                chiTiet.setTrangThai("Cho");
                // Added: persist required schema fields for ordered service rows.
                chiTiet.setDonGia(dichVu.getGiaGiam() != null ? dichVu.getGiaGiam() : dichVu.getGiaDichVu());
                chiTiet.setPhongThucHien(dichVu.getPhongThucHien());
                chiTiets.add(chiTiet);
                if (dichVu.getGiaDichVu() != null) {
                    tongTien = tongTien.add(dichVu.getGiaGiam() != null ? dichVu.getGiaGiam() : dichVu.getGiaDichVu());
                }
            }
        }
        phieu.setTongTien(tongTien);
        phieu.setChiTietChiDinhs(chiTiets);
        return phieuChiDinhRepository.save(phieu);
    }

    public PhieuChiDinh savePhieuChiDinh(PhieuChiDinh phieuChiDinh) {
        return phieuChiDinhRepository.save(phieuChiDinh);
    }
}
