package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.GiuongBenh;
import com.java.BaoCaoDoAn.Model.PhongBenh;
import com.java.BaoCaoDoAn.Repository.GiuongBenhRepository;
import com.java.BaoCaoDoAn.Repository.PhongBenhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GiuongBenhService {

    @Autowired
    private GiuongBenhRepository giuongBenhRepository;

    @Autowired
    private PhongBenhRepository phongBenhRepository;

    public List<GiuongBenh> findAll() {
        return giuongBenhRepository.findAll();
    }

    public Optional<GiuongBenh> findById(Integer id) {
        return giuongBenhRepository.findById(id);
    }

    public GiuongBenh save(GiuongBenh giuongBenh) {
        return giuongBenhRepository.save(giuongBenh);
    }

    public void deleteById(Integer id) {
        giuongBenhRepository.deleteById(id);
    }

    public List<GiuongBenh> findByPhongBenh(PhongBenh phongBenh) {
        return giuongBenhRepository.findByPhongBenh(phongBenh);
    }

    public long countByPhongBenhAndTrangThai(PhongBenh phongBenh, String trangThai) {
        return giuongBenhRepository.countByPhongBenhAndTrangThai(phongBenh, trangThai);
    }

    public long countByPhongBenh(PhongBenh phongBenh) {
        return giuongBenhRepository.countByPhongBenh(phongBenh);
    }

    public List<GiuongBenh> findByTrangThai(String trangThai) {
        return giuongBenhRepository.findByTrangThai(trangThai);
    }

    /**
     * Đồng bộ giường cho phòng: Chỉ tạo thêm giường còn thiếu.
     * Tên giường: GD1, GD2, GD3...
     */
    public void syncGiuongChoPhong(String maPhong) {
        Optional<PhongBenh> phongOpt = phongBenhRepository.findById(maPhong);
        if (phongOpt.isPresent()) {
            PhongBenh phong = phongOpt.get();
            List<GiuongBenh> existingBeds = giuongBenhRepository.findByPhongBenh(phong);
            int maxBeds = (phong.getSoGiuongToiDa() != null) ? phong.getSoGiuongToiDa() : 0;

            for (int i = 1; i <= maxBeds; i++) {
                String bedName = "GD" + i;
                boolean exists = existingBeds.stream().anyMatch(b -> bedName.equals(b.getSoGiuong()));
                if (!exists) {
                    GiuongBenh newBed = new GiuongBenh();
                    newBed.setPhongBenh(phong);
                    newBed.setSoGiuong(bedName);
                    newBed.setTrangThai("Trống");
                    newBed.setGhiChu("Tự động tạo");
                    giuongBenhRepository.save(newBed);
                }
            }
        }
    }

    public void deleteByPhongBenh(PhongBenh phongBenh) {
        giuongBenhRepository.deleteByPhongBenh(phongBenh);
    }
}
