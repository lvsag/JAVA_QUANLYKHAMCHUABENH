package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.PhieuKham;
import com.java.BaoCaoDoAn.Repository.PhieuKhamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KhamBenhService {
    private final PhieuKhamRepository phieuKhamRepository;

    public KhamBenhService(PhieuKhamRepository phieuKhamRepository) {
        this.phieuKhamRepository = phieuKhamRepository;
    }

    public List<PhieuKham> getHangDoiKham() {
        return phieuKhamRepository.findByTrangThaiOrderByNgayKhamAsc("Dang kham");
    }

    public List<PhieuKham> getAllPhieuKham() {
        return phieuKhamRepository.findAll();
    }

    public Optional<PhieuKham> getPhieuKham(String maPhieuKham) {
        return phieuKhamRepository.findById(maPhieuKham);
    }

    public PhieuKham savePhieuKham(PhieuKham phieuKham) {
        if (phieuKham.getMaPhieuKham() == null || phieuKham.getMaPhieuKham().isBlank()) {
            phieuKham.setMaPhieuKham("PK" + System.currentTimeMillis());
        }
        return phieuKhamRepository.save(phieuKham);
    }
}
