package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.VatTuYTe;
import com.java.BaoCaoDoAn.Repository.VatTuYTeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VatTuYTeService {

    @Autowired
    private VatTuYTeRepository vatTuYTeRepository;

    public List<VatTuYTe> findAll() {
        return vatTuYTeRepository.findAll();
    }

    public Optional<VatTuYTe> findById(Integer id) {
        return vatTuYTeRepository.findById(id);
    }

    public VatTuYTe save(VatTuYTe vatTuYTe) {
        return vatTuYTeRepository.save(vatTuYTe);
    }

    public void deleteById(Integer id) {
        vatTuYTeRepository.deleteById(id);
    }

    public List<VatTuYTe> searchByName(String name) {
        return vatTuYTeRepository.findByTenVatTuContainingIgnoreCase(name);
    }
}
