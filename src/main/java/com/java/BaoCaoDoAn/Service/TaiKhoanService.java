package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.TaiKhoan;
import com.java.BaoCaoDoAn.Repository.TaiKhoanRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanRepository.findAll();
    }

    public Optional<TaiKhoan> getTaiKhoanById(Integer id) {
        return taiKhoanRepository.findById(id);
    }

    public TaiKhoan saveTaiKhoan(TaiKhoan taiKhoan) {
        if (taiKhoan.getMatKhau() != null && !taiKhoan.getMatKhau().startsWith("$2a$")) {
            taiKhoan.setMatKhau(BCrypt.hashpw(taiKhoan.getMatKhau(), BCrypt.gensalt()));
        }
        return taiKhoanRepository.save(taiKhoan);
    }

    public void toggleTrangThai(Integer id) {
        taiKhoanRepository.findById(id).ifPresent(tk -> {
            if ("Hoạt động".equals(tk.getTrangThai())) {
                tk.setTrangThai("Tạm khóa");
            } else {
                tk.setTrangThai("Hoạt động");
            }
            taiKhoanRepository.save(tk);
        });
    }

    public boolean checkLogin(String username, String rawPassword) {
        Optional<TaiKhoan> tk = taiKhoanRepository.findByTenDangNhap(username);
        if (tk.isPresent() && "Hoạt động".equals(tk.get().getTrangThai())) {
            return BCrypt.checkpw(rawPassword, tk.get().getMatKhau());
        }
        return false;
    }
}
