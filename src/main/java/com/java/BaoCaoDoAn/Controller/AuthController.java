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
                // Added/updated: doctors use the separate /bac-si workflow area; admins keep /admin.
                if ("ADMIN".equalsIgnoreCase(role) || "Admin".equalsIgnoreCase(role)) {
                    return "redirect:/admin/dashboard";
                } else if ("BAC_SI".equalsIgnoreCase(role) || "Bác sĩ".equalsIgnoreCase(role)) {
                    return "redirect:/bac-si/kham-benh";
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

        if (!email.toLowerCase().endsWith("@gmail.com")) {
            model.addAttribute("error", "Vui lòng sử dụng địa chỉ email định dạng @gmail.com.");
            return "public/register";
        }

        if (!soDienThoai.matches("^\\d{10}$")) {
            model.addAttribute("error", "Số điện thoại phải bao gồm đúng 10 chữ số.");
            return "public/register";
        }

        if (!hoTen.matches("^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$")) {
            model.addAttribute("error", "Họ tên không được chứa số hoặc ký tự đặc biệt.");
            return "public/register";
        }

        if (!password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            model.addAttribute("error", "Mật khẩu phải chứa ít nhất 1 chữ số và 1 ký tự đặc biệt.");
            return "public/register";
        }

        if (ngaySinh != null && !ngaySinh.isEmpty()) {
            try {
                java.time.LocalDate dob = java.time.LocalDate.parse(ngaySinh);
                if (!dob.isBefore(java.time.LocalDate.now())) {
                    model.addAttribute("error", "Ngày sinh phải nhỏ hơn ngày hiện tại.");
                    return "public/register";
                }
            } catch (Exception e) {
                model.addAttribute("error", "Ngày sinh không hợp lệ.");
                return "public/register";
            }
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

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "public/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String soDienThoai,
                                        @RequestParam String ngaySinh,
                                        @RequestParam String soCCCD,
                                        HttpSession session,
                                        Model model) {
        try {
            java.sql.Date dob = java.sql.Date.valueOf(ngaySinh.trim());
            Optional<com.java.BaoCaoDoAn.Model.BenhNhan> bnOpt = benhNhanRepository.findBySoDienThoaiAndNgaySinhAndSoCCCD(soDienThoai.trim(), dob, soCCCD.trim());
            
            if (bnOpt.isPresent() && bnOpt.get().getTaiKhoan() != null) {
                // Save account ID to session for reset step
                session.setAttribute("resetAccountId", bnOpt.get().getTaiKhoan().getMaTaiKhoan());
                return "redirect:/reset-password";
            } else {
                model.addAttribute("error", "Thông tin không khớp với bất kỳ tài khoản nào trong hệ thống.");
                return "public/forgot-password";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Dữ liệu không hợp lệ.");
            return "public/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(HttpSession session) {
        if (session.getAttribute("resetAccountId") == null) {
            return "redirect:/forgot-password";
        }
        return "public/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       HttpSession session,
                                       Model model) {
        Integer accountId = (Integer) session.getAttribute("resetAccountId");
        if (accountId == null) {
            return "redirect:/forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp.");
            return "public/reset-password";
        }

        if (!newPassword.matches(".*\\d.*") || !newPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            model.addAttribute("error", "Mật khẩu phải chứa ít nhất 1 chữ số và 1 ký tự đặc biệt.");
            return "public/reset-password";
        }

        Optional<TaiKhoan> tkOpt = taiKhoanRepository.findById(accountId);
        if (tkOpt.isPresent()) {
            TaiKhoan tk = tkOpt.get();
            tk.setMatKhau(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            taiKhoanRepository.save(tk);
            
            // Clear session
            session.removeAttribute("resetAccountId");
            
            model.addAttribute("success", "Đổi mật khẩu thành công!");
            return "public/reset-password";
        }

        model.addAttribute("error", "Không tìm thấy tài khoản.");
        return "public/reset-password";
    }
}
