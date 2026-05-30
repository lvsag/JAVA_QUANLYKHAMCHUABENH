package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.ChuyenKhoa;
import com.java.BaoCaoDoAn.Model.PhongBenh;
import com.java.BaoCaoDoAn.Repository.PhongBenhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PhongBenhService {

    @Autowired
    private PhongBenhRepository phongBenhRepository;

    @Autowired
    private ChuyenKhoaService chuyenKhoaService;

    public List<PhongBenh> findByChuyenKhoa(String maChuyenKhoa) {
        Optional<ChuyenKhoa> ckOpt = chuyenKhoaService.getChuyenKhoaById(maChuyenKhoa);
        if (ckOpt.isPresent()) {
            return phongBenhRepository.findByChuyenKhoa(ckOpt.get());
        }
        return new ArrayList<>();
    }

    public List<PhongBenh> findByTrangThai(String trangThai) {
        return phongBenhRepository.findByTrangThai(trangThai);
    }

    public List<PhongBenh> findAll() {
        return phongBenhRepository.findAll();
    }

    public Optional<PhongBenh> findById(String id) {
        return phongBenhRepository.findById(id);
    }

    public PhongBenh save(PhongBenh phongBenh) {
        return phongBenhRepository.save(phongBenh);
    }

    public void deleteById(String id) {
        phongBenhRepository.deleteById(id);
    }
}
