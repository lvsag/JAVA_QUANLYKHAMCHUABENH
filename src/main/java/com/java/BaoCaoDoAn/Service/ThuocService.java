package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.Thuoc;
import com.java.BaoCaoDoAn.Repository.ThuocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ThuocService {
    private final ThuocRepository thuocRepository;

    public ThuocService(ThuocRepository thuocRepository) {
        this.thuocRepository = thuocRepository;
    }

    public List<Thuoc> findAll() {
        return thuocRepository.findAll();
    }

    public Optional<Thuoc> findById(Integer id) {
        return thuocRepository.findById(id);
    }

    public Thuoc save(Thuoc thuoc) {
        return thuocRepository.save(thuoc);
    }

    public void deleteById(Integer id) {
        thuocRepository.deleteById(id);
    }

    public List<Thuoc> getAllThuoc() {
        return findAll();
    }

    public Optional<Thuoc> getThuoc(Integer maThuoc) {
        return findById(maThuoc);
    }

    public List<Thuoc> searchThuoc(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllThuoc();
        }
        return thuocRepository.findByTenThuocContainingIgnoreCase(keyword);
    }
}
