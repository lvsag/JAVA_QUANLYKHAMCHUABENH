package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.DichVu;
import com.java.BaoCaoDoAn.Repository.DichVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class DichVuService {

    @Autowired
    private DichVuRepository dichVuRepository;

    public List<DichVu> getAllDichVu() {
        return dichVuRepository.findAll();
    }

    public List<DichVu> getAllXetNghiem() {
        return dichVuRepository.findAll().stream()
                .filter(this::isXetNghiem)
                .sorted(Comparator.comparing(DichVu::getMaDichVu, Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList();
    }

    public List<DichVu> searchXetNghiem(String keyword, String trangThai) {
        String normalizedKeyword = normalize(keyword);
        String normalizedTrangThai = normalize(trangThai);

        return getAllXetNghiem().stream()
                .filter(dv -> normalizedKeyword.isBlank()
                        || normalize(dv.getMaDichVu()).contains(normalizedKeyword)
                        || normalize(dv.getTenDichVu()).contains(normalizedKeyword)
                        || normalize(dv.getPhongThucHien()).contains(normalizedKeyword))
                .filter(dv -> normalizedTrangThai.isBlank()
                        || normalize(dv.getTrangThai()).equals(normalizedTrangThai))
                .toList();
    }

    private boolean isXetNghiem(DichVu dichVu) {
        String loai = normalize(dichVu.getLoaiDichVu());
        String ten = normalize(dichVu.getTenDichVu());
        return loai.contains("xét nghiệm") || loai.contains("xet nghiem")
                || ten.contains("xét nghiệm") || ten.contains("xet nghiem");
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
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
