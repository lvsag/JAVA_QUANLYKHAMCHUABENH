package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.Thuoc;
import com.java.BaoCaoDoAn.Repository.ThuocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ThuocService {

    @Autowired
    private ThuocRepository thuocRepository;

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
    
    public List<Thuoc> searchByName(String name) {
        return thuocRepository.findByTenThuocContainingIgnoreCase(name);
    }
}
