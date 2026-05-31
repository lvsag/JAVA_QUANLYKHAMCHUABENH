package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.LichHen;
import com.java.BaoCaoDoAn.Model.PhieuKham;
import com.java.BaoCaoDoAn.Repository.PhieuKhamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public List<PhieuKham> getPhieuKhamByBacSi(String maBacSi) {
        return phieuKhamRepository.findByBacSi_MaBacSiOrderByNgayKhamDesc(maBacSi);
    }

    public Optional<PhieuKham> getPhieuKham(String maPhieuKham) {
        return phieuKhamRepository.findById(maPhieuKham);
    }

    public PhieuKham taoPhieuKhamTuLichHen(LichHen lichHen) {
        if (lichHen == null || lichHen.getMaLichHen() == null) {
            throw new IllegalArgumentException("Khong tim thay lich hen de tao phieu kham.");
        }
        Optional<PhieuKham> existing = phieuKhamRepository.findByLichHen_MaLichHen(lichHen.getMaLichHen());
        if (existing.isPresent()) {
            return existing.get();
        }
        PhieuKham phieuKham = new PhieuKham();
        phieuKham.setMaPhieuKham("PK" + System.currentTimeMillis());
        phieuKham.setLichHen(lichHen);
        phieuKham.setBenhNhan(lichHen.getBenhNhan());
        phieuKham.setBacSi(lichHen.getBacSi());
        phieuKham.setPhongKham(lichHen.getPhongKham() != null ? lichHen.getPhongKham()
                : (lichHen.getBacSi() != null ? lichHen.getBacSi().getPhongKham() : null));
        phieuKham.setNgayKham(toNgayKham(lichHen));
        phieuKham.setTrieuChung(lichHen.getGhiChuTrieuChung());
        phieuKham.setTrangThai("Cho kham");
        return phieuKhamRepository.save(phieuKham);
    }

    public PhieuKham savePhieuKham(PhieuKham phieuKham) {
        if (phieuKham.getMaPhieuKham() == null || phieuKham.getMaPhieuKham().isBlank()) {
            phieuKham.setMaPhieuKham("PK" + System.currentTimeMillis());
        }
        return phieuKhamRepository.save(phieuKham);
    }

    public List<PhieuKham> getPhieuKhamByBenhNhan(String maBenhNhan) {
        return phieuKhamRepository.findByBenhNhan_MaBenhNhanOrderByNgayKhamDesc(maBenhNhan);
    private Date toNgayKham(LichHen lichHen) {
        if (lichHen.getNgayHen() == null) {
            return new Date();
        }
        long time = lichHen.getNgayHen().getTime();
        if (lichHen.getGioHen() != null) {
            time += lichHen.getGioHen().getTime() % (24 * 60 * 60 * 1000);
        }
        return new Date(time);
    }
}
