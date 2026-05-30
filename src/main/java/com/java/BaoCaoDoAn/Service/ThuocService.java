package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.Thuoc;
import com.java.BaoCaoDoAn.Repository.ThuocRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThuocService {
    private final ThuocRepository thuocRepository;

    public ThuocService(ThuocRepository thuocRepository) {
        this.thuocRepository = thuocRepository;
    }

    // Added: provides medicine catalog and stock data for the doctor prescription screen.
    public List<Thuoc> getAllThuoc() {
        return thuocRepository.findAll();
    }

    public Optional<Thuoc> getThuoc(Integer maThuoc) {
        return thuocRepository.findById(maThuoc);
    }

    public List<Thuoc> searchThuoc(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllThuoc();
        }
        return thuocRepository.findByTenThuocContainingIgnoreCase(keyword);
    }

    public List<Thuoc> findAll() {
        return thuocRepository.findAll();
    }

    public Thuoc save(Thuoc thuoc) {
        return thuocRepository.save(thuoc);
    }

    public Optional<Thuoc> findById(Integer id) {
        return thuocRepository.findById(id);
    }

    public void deleteById(Integer id) {
        thuocRepository.deleteById(id);
    }
}
