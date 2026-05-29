package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.BenhNhan;
import com.java.BaoCaoDoAn.Repository.BenhNhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BenhNhanService {
    @Autowired
    private BenhNhanRepository benhNhanRepository;

    public List<BenhNhan> getAllBenhNhan() {
        return benhNhanRepository.findAll();
    }

    public Optional<BenhNhan> getBenhNhanById(String id) {
        return benhNhanRepository.findById(id);
    }

    public BenhNhan saveBenhNhan(BenhNhan benhNhan) {
        if (benhNhan.getMaBenhNhan() == null || benhNhan.getMaBenhNhan().isEmpty()) {
            long count = benhNhanRepository.count() + 1;
            benhNhan.setMaBenhNhan(String.format("BN-2025-%04d", count));
        }
        return benhNhanRepository.save(benhNhan);
    }

    public void deleteBenhNhan(String id) {
        benhNhanRepository.deleteById(id);
    }
}
