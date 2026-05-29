package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.DichVu;
import com.java.BaoCaoDoAn.Repository.DichVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DichVuService {

    @Autowired
    private DichVuRepository dichVuRepository;

    public List<DichVu> getAllDichVu() {
        return dichVuRepository.findAll();
    }

    public DichVu saveDichVu(DichVu dichVu) {
        return dichVuRepository.save(dichVu);
    }

    public DichVu getDichVuById(String maDichVu) {
        return dichVuRepository.findById(maDichVu).orElse(null);
    }

    public void deleteDichVu(String maDichVu) {
        dichVuRepository.deleteById(maDichVu);
    }
}
