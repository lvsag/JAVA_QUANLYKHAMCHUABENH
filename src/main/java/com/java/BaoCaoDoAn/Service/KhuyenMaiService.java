package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.KhuyenMai;
import com.java.BaoCaoDoAn.Repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    public List<KhuyenMai> findAll() {
        return khuyenMaiRepository.findAll();
    }

    public Optional<KhuyenMai> findById(Integer id) {
        return khuyenMaiRepository.findById(id);
    }

    public Optional<KhuyenMai> findByMaCode(String maCode) {
        if (maCode == null) return Optional.empty();
        return khuyenMaiRepository.findByMaCode(maCode.trim().toUpperCase());
    }

    public KhuyenMai save(KhuyenMai khuyenMai) {
        return khuyenMaiRepository.save(khuyenMai);
    }

    public void deleteById(Integer id) {
        khuyenMaiRepository.deleteById(id);
    }

    public boolean existsByMaCode(String maCode) {
        if (maCode == null) return false;
        return khuyenMaiRepository.existsByMaCode(maCode.trim().toUpperCase());
    }

    public KhuyenMai layKhuyenMaiHopLe(String maCode) throws Exception {
        if (maCode == null || maCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khuyến mãi không được để trống.");
        }
        
        String code = maCode.trim().toUpperCase();
        KhuyenMai km = khuyenMaiRepository.findByMaCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Mã khuyến mãi không tồn tại."));
                
        if (!"Hoạt động".equals(km.getTrangThai())) {
            throw new IllegalStateException("Mã khuyến mãi này hiện đang bị khóa hoặc ngưng hoạt động.");
        }
        
        LocalDate today = LocalDate.now();
        LocalDate start = toLocalDate(km.getNgayBatDau());
        LocalDate end = toLocalDate(km.getNgayKetThuc());
        
        if (start != null && today.isBefore(start)) {
            throw new IllegalStateException("Mã khuyến mãi chưa đến thời gian áp dụng.");
        }
        
        if (end != null && today.isAfter(end)) {
            throw new IllegalStateException("Mã khuyến mãi đã hết hạn.");
        }
        
        if (km.getSoLuotToiDa() != null && km.getSoLuotDaDung() != null && km.getSoLuotDaDung() >= km.getSoLuotToiDa()) {
            throw new IllegalStateException("Mã khuyến mãi đã đạt số lượt sử dụng tối đa.");
        }
        
        return km;
    }

    public double tinhGiamGiaHopLe(String maCode, double tongTien) {
        try {
            KhuyenMai km = layKhuyenMaiHopLe(maCode);
            double discount = km.getGiaTriGiam() != null ? km.getGiaTriGiam().doubleValue() : 0.0;
            return Math.min(tongTien, discount);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        if (date instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) date).toLocalDateTime().toLocalDate();
        }
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
}
