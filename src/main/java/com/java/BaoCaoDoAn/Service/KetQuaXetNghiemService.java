package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.KetQuaXetNghiem;
import com.java.BaoCaoDoAn.Repository.KetQuaXetNghiemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KetQuaXetNghiemService {
    private final KetQuaXetNghiemRepository ketQuaXetNghiemRepository;

    public KetQuaXetNghiemService(KetQuaXetNghiemRepository ketQuaXetNghiemRepository) {
        this.ketQuaXetNghiemRepository = ketQuaXetNghiemRepository;
    }

    public List<KetQuaXetNghiem> getAllKetQua() {
        return ketQuaXetNghiemRepository.findAll();
    }

    public List<KetQuaXetNghiem> getKetQuaMoi() {
        return ketQuaXetNghiemRepository.findByTrangThaiOrderByNgayKetQuaDesc("Moi");
    }

    public List<KetQuaXetNghiem> getKetQuaByBenhNhan(String maBenhNhan) {
        return ketQuaXetNghiemRepository.findByBenhNhan_MaBenhNhanOrderByNgayKetQuaDesc(maBenhNhan);
    }

    public Optional<KetQuaXetNghiem> getKetQua(String maKetQua) {
        return ketQuaXetNghiemRepository.findById(maKetQua);
    }

    public KetQuaXetNghiem saveKetQua(KetQuaXetNghiem ketQua) {
        if (ketQua.getMaKetQua() == null || ketQua.getMaKetQua().isBlank()) {
            ketQua.setMaKetQua("KQ" + System.currentTimeMillis());
        }
        return ketQuaXetNghiemRepository.save(ketQua);
    }
}
