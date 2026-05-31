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
<<<<<<< Updated upstream
        return thuocRepository.findById(maThuoc);
=======
        return findById(maThuoc);
    }

    public List<Thuoc> searchThuoc(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllThuoc();
        }
        return thuocRepository.findByTenThuocContainingIgnoreCase(keyword);
>>>>>>> Stashed changes
    }
}
