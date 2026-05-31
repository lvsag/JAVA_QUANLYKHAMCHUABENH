package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.BacSi;
import com.java.BaoCaoDoAn.Repository.BacSiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BacSiService {
    @Autowired
    private BacSiRepository bacSiRepository;

    public List<BacSi> getAllBacSi() {
        return bacSiRepository.findAll();
    }

    public Optional<BacSi> getBacSiById(String id) {
        return bacSiRepository.findById(id);
    }

    public BacSi saveBacSi(BacSi bacSi) {
        if (bacSi.getMaBacSi() == null || bacSi.getMaBacSi().isEmpty()) {
            long count = bacSiRepository.count() + 1;
            bacSi.setMaBacSi(String.format("BS%03d", count));
        }
        return bacSiRepository.save(bacSi);
    }
}
