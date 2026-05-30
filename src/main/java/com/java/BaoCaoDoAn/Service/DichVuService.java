package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.DichVu;
import com.java.BaoCaoDoAn.Repository.DichVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DichVuService {

    @Autowired
    private DichVuRepository dichVuRepository;

    public List<DichVu> getAllDichVu() {
        return dichVuRepository.findAll();
    }

    // Added for service catalog screens: reusable search without hard-coding data in HTML.
    public List<DichVu> searchDichVu(String keyword, String maChuyenKhoa) {
        return searchDichVu(keyword, maChuyenKhoa, null, null);
    }

    // Added for public service catalog comboboxes: keyword + specialty + price range + service type.
    public List<DichVu> searchDichVu(String keyword, String maChuyenKhoa, String mucGia, String loaiDichVu) {
        java.util.List<DichVu> result;
        if (keyword != null && !keyword.isBlank()) {
            result = dichVuRepository.findByTenDichVuContainingIgnoreCase(keyword.trim());
        } else if (maChuyenKhoa != null && !maChuyenKhoa.isBlank()) {
            result = dichVuRepository.findByChuyenKhoa_MaChuyenKhoa(maChuyenKhoa);
        } else {
            result = dichVuRepository.findAll();
        }

        return result.stream()
                .filter(dv -> maChuyenKhoa == null || maChuyenKhoa.isBlank()
                        || (dv.getChuyenKhoa() != null && maChuyenKhoa.equals(dv.getChuyenKhoa().getMaChuyenKhoa())))
                .filter(dv -> loaiDichVu == null || loaiDichVu.isBlank()
                        || (dv.getLoaiDichVu() != null && dv.getLoaiDichVu().equalsIgnoreCase(loaiDichVu)))
                .filter(dv -> matchesMucGia(dv, mucGia))
                .toList();
    }

    private boolean matchesMucGia(DichVu dichVu, String mucGia) {
        if (mucGia == null || mucGia.isBlank()) {
            return true;
        }
        java.math.BigDecimal gia = dichVu.getGiaGiam() != null ? dichVu.getGiaGiam() : dichVu.getGiaDichVu();
        if (gia == null) {
            return false;
        }
        return switch (mucGia) {
            case "duoi-200" -> gia.compareTo(new java.math.BigDecimal("200000")) < 0;
            case "200-500" -> gia.compareTo(new java.math.BigDecimal("200000")) >= 0
                    && gia.compareTo(new java.math.BigDecimal("500000")) <= 0;
            case "tren-500" -> gia.compareTo(new java.math.BigDecimal("500000")) > 0;
            default -> true;
        };
    }

    public DichVu saveDichVu(DichVu dichVu) {
        return dichVuRepository.save(dichVu);
    }

    public DichVu getDichVuById(String maDichVu) {
        return dichVuRepository.findById(maDichVu).orElse(null);
    }

    public void deleteDichVu(String maDichVu) {
        dichVuRepository.deleteById(maDichVu);
    }
}
