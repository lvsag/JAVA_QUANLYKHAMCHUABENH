package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.KhuyenMai;
import com.java.BaoCaoDoAn.Repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
