package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.NhapVienNoiTru;
import com.java.BaoCaoDoAn.Repository.NhapVienNoiTruRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NhapVienNoiTruService {

    @Autowired
    private NhapVienNoiTruRepository nhapVienNoiTruRepository;

    public List<NhapVienNoiTru> findAll() {
        return nhapVienNoiTruRepository.findAll();
    }

    public Optional<NhapVienNoiTru> findById(String id) {
        return nhapVienNoiTruRepository.findById(id);
    }

    public NhapVienNoiTru save(NhapVienNoiTru nhapVienNoiTru) {
        return nhapVienNoiTruRepository.save(nhapVienNoiTru);
    }

    public void deleteById(String id) {
        nhapVienNoiTruRepository.deleteById(id);
    }

    public List<NhapVienNoiTru> findByTrangThai(String trangThai) {
        return nhapVienNoiTruRepository.findByTrangThai(trangThai);
    }

    public boolean isBenhNhanDangNoiTru(String maBenhNhan) {
        return nhapVienNoiTruRepository.existsByBenhNhan_MaBenhNhanAndTrangThai(maBenhNhan, "Đang điều trị");
    }

    public List<NhapVienNoiTru> findByBenhNhan(String maBenhNhan) {
        return nhapVienNoiTruRepository.findByBenhNhan_MaBenhNhan(maBenhNhan);
    }

    public void deleteByBenhNhan(String maBenhNhan) {
        nhapVienNoiTruRepository.deleteByBenhNhan_MaBenhNhan(maBenhNhan);
    }
}
