package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.TaiKhoan;
import com.java.BaoCaoDoAn.Repository.TaiKhoanRepository;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "public/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        Optional<TaiKhoan> userOpt = taiKhoanRepository.findByTenDangNhap(username);
        
        if (userOpt.isEmpty()) {
            // Check if it's email
            userOpt = taiKhoanRepository.findByEmail(username);
        }

        if (userOpt.isPresent()) {
            TaiKhoan user = userOpt.get();
            if (BCrypt.checkpw(password, user.getMatKhau())) {
                if ("Khóa".equals(user.getTrangThai())) {
                    model.addAttribute("error", "Tài khoản đã bị khóa!");
                    return "public/login";
                }
                
                // Login success
                session.setAttribute("loggedInUser", user);
                session.setAttribute("role", user.getVaiTro().getTenVaiTro());
                
                String role = user.getVaiTro().getTenVaiTro();
                if ("ADMIN".equalsIgnoreCase(role) || "BAC_SI".equalsIgnoreCase(role) || "Admin".equalsIgnoreCase(role) || "Bác sĩ".equalsIgnoreCase(role)) {
                    return "redirect:/admin/dashboard"; // Redirect to dashboard
                } else {
                    return "redirect:/home";
                }
            }
        }

        model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
        return "public/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @Autowired
    private com.java.BaoCaoDoAn.Repository.BenhNhanRepository benhNhanRepository;
    
    @Autowired
    private com.java.BaoCaoDoAn.Repository.VaiTroRepository vaiTroRepository;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "public/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String hoTen,
                                  @RequestParam String email,
                                  @RequestParam String soDienThoai,
                                  @RequestParam(required = false) String ngaySinh,
                                  @RequestParam(required = false) String gioiTinh,
                                  @RequestParam(required = false) String diaChi,
                                  @RequestParam(required = false) String soCCCD,
                                  @RequestParam(required = false) String soBHYT,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  Model model) {
                                  
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp.");
            return "public/register";
        }

        if (taiKhoanRepository.findByEmail(email).isPresent() || taiKhoanRepository.findByTenDangNhap(email).isPresent()) {
            model.addAttribute("error", "Email này đã được sử dụng.");
            return "public/register";
        }

        try {
            com.java.BaoCaoDoAn.Model.VaiTro userRole = vaiTroRepository.findByTenVaiTro("USER")
                .orElseGet(() -> {
                    com.java.BaoCaoDoAn.Model.VaiTro role = new com.java.BaoCaoDoAn.Model.VaiTro();
                    role.setTenVaiTro("USER");
                    role.setMoTa("Người dùng");
                    return vaiTroRepository.save(role);
                });

            TaiKhoan tk = new TaiKhoan();
            tk.setTenDangNhap(email);
            tk.setEmail(email);
            tk.setHoTen(hoTen);
            tk.setMatKhau(BCrypt.hashpw(password, BCrypt.gensalt()));
            tk.setVaiTro(userRole);
            tk.setTrangThai("Hoạt động");
            taiKhoanRepository.save(tk);

            com.java.BaoCaoDoAn.Model.BenhNhan bn = new com.java.BaoCaoDoAn.Model.BenhNhan();
            bn.setMaBenhNhan("BN-" + System.currentTimeMillis());
            bn.setTaiKhoan(tk);
            bn.setHoTen(hoTen);
            bn.setEmail(email);
            bn.setSoDienThoai(soDienThoai);
            bn.setGioiTinh(gioiTinh);
            bn.setDiaChi(diaChi);
            bn.setSoCCCD(soCCCD);
            bn.setSoBHYT(soBHYT);
            if (ngaySinh != null && !ngaySinh.isEmpty()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                bn.setNgaySinh(sdf.parse(ngaySinh));
            }
            benhNhanRepository.save(bn);

            model.addAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
            return "public/register";
            
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "public/register";
        }
    }
}
