package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.ChuyenKhoa;
import com.java.BaoCaoDoAn.Repository.ChuyenKhoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChuyenKhoaService {
    @Autowired
    private ChuyenKhoaRepository chuyenKhoaRepository;

    public List<ChuyenKhoa> getAllChuyenKhoa() {
        return chuyenKhoaRepository.findAll();
    }

    public Optional<ChuyenKhoa> getChuyenKhoaById(String id) {
        return chuyenKhoaRepository.findById(id);
    }

    public ChuyenKhoa saveChuyenKhoa(ChuyenKhoa chuyenKhoa) {
        if (chuyenKhoa.getMaChuyenKhoa() == null || chuyenKhoa.getMaChuyenKhoa().isEmpty()) {
            long count = chuyenKhoaRepository.count() + 1;
            chuyenKhoa.setMaChuyenKhoa(String.format("CK%02d", count));
        }
        return chuyenKhoaRepository.save(chuyenKhoa);
    }
}
