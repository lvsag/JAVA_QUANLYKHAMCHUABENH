package com.java.BaoCaoDoAn.Config;

import com.java.BaoCaoDoAn.Model.TaiKhoan;
import com.java.BaoCaoDoAn.Repository.TaiKhoanRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public void run(String... args) throws Exception {
        List<TaiKhoan> taiKhoans = taiKhoanRepository.findAll();
        for (TaiKhoan tk : taiKhoans) {
            // Check if the password is not a valid BCrypt hash length (60 chars)
            if (tk.getMatKhau() == null || tk.getMatKhau().length() != 60) {
                // If it's a sample hash like "$2a$10$hashed_admin_password", replace it with "admin123" for admin,
                // or just "123456" for others.
                String defaultPassword = tk.getTenDangNhap().equals("admin") ? "admin123" : "123456";
                String hashed = BCrypt.hashpw(defaultPassword, BCrypt.gensalt());
                tk.setMatKhau(hashed);
                taiKhoanRepository.save(tk);
                System.out.println("Đã cập nhật mật khẩu cho tài khoản: " + tk.getTenDangNhap() + " -> " + defaultPassword);
            }
        }
    }
}
