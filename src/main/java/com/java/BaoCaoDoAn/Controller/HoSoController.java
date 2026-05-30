package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.BenhNhan;
import com.java.BaoCaoDoAn.Model.TaiKhoan;
import com.java.BaoCaoDoAn.Repository.BenhNhanRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/ho-so")
public class HoSoController {

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private com.java.BaoCaoDoAn.Repository.TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private com.java.BaoCaoDoAn.Repository.LichHenRepository lichHenRepository;

    @Autowired
    private com.java.BaoCaoDoAn.Service.DonThuocService donThuocService;

    @Autowired
    private com.java.BaoCaoDoAn.Service.KetQuaXetNghiemService ketQuaXetNghiemService;

    @GetMapping("/lich-su-kham")
    public String showHistory(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<BenhNhan> bnOpt = benhNhanRepository.findByTaiKhoan_MaTaiKhoan(user.getMaTaiKhoan());
        if (bnOpt.isPresent()) {
            BenhNhan bn = bnOpt.get();
            model.addAttribute("benhNhan", bn);
            model.addAttribute("lichHens", lichHenRepository.findByBenhNhan_MaBenhNhanOrderByNgayHenDescGioHenDesc(bn.getMaBenhNhan()));
        }
        return "public/lich-su-kham";
    }

    @GetMapping("/don-thuoc")
    public String showPrescriptions(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<BenhNhan> bnOpt = benhNhanRepository.findByTaiKhoan_MaTaiKhoan(user.getMaTaiKhoan());
        if (bnOpt.isPresent()) {
            // Added to connect public/don-thuoc.html with prescriptions created by doctors.
            BenhNhan bn = bnOpt.get();
            model.addAttribute("benhNhan", bn);
            model.addAttribute("donThuocs", donThuocService.getDonThuocByBenhNhan(bn.getMaBenhNhan()));
        }
        return "public/don-thuoc";
    }

    @GetMapping("/xet-nghiem")
    public String showTestResults(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<BenhNhan> bnOpt = benhNhanRepository.findByTaiKhoan_MaTaiKhoan(user.getMaTaiKhoan());
        if (bnOpt.isPresent()) {
            // Added to keep the old /ho-so/xet-nghiem route backed by saved test results.
            BenhNhan bn = bnOpt.get();
            model.addAttribute("benhNhan", bn);
            model.addAttribute("ketQuas", ketQuaXetNghiemService.getKetQuaByBenhNhan(bn.getMaBenhNhan()));
        }
        return "public/xet-nghiem";
    }

    @GetMapping("/ket-qua-xet-nghiem")
    public String showKetQuaXetNghiem(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<BenhNhan> bnOpt = benhNhanRepository.findByTaiKhoan_MaTaiKhoan(user.getMaTaiKhoan());
        if (bnOpt.isPresent()) {
            // Added to connect public/ket-qua-xet-nghiem.html with saved test results.
            BenhNhan bn = bnOpt.get();
            model.addAttribute("benhNhan", bn);
            model.addAttribute("ketQuas", ketQuaXetNghiemService.getKetQuaByBenhNhan(bn.getMaBenhNhan()));
        }
        return "public/ket-qua-xet-nghiem";
    }

    @GetMapping("/bao-mat")
    public String showSecurity(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<BenhNhan> bnOpt = benhNhanRepository.findByTaiKhoan_MaTaiKhoan(user.getMaTaiKhoan());
        if (bnOpt.isPresent()) {
            model.addAttribute("benhNhan", bnOpt.get());
        }
        return "public/bao-mat";
    }

    @PostMapping("/doi-mat-khau")
    public String changePassword(@org.springframework.web.bind.annotation.RequestParam String oldPassword,
                                 @org.springframework.web.bind.annotation.RequestParam String newPassword,
                                 @org.springframework.web.bind.annotation.RequestParam String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<TaiKhoan> tkOpt = taiKhoanRepository.findById(user.getMaTaiKhoan());
        if (tkOpt.isPresent()) {
            TaiKhoan tk = tkOpt.get();
            
            if (!org.mindrot.jbcrypt.BCrypt.checkpw(oldPassword, tk.getMatKhau())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu hiện tại không đúng.");
                return "redirect:/ho-so/bao-mat";
            }
            
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp.");
                return "redirect:/ho-so/bao-mat";
            }
            
            if (!newPassword.matches(".*\\d.*") || !newPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới phải chứa ít nhất 1 chữ số và 1 ký tự đặc biệt.");
                return "redirect:/ho-so/bao-mat";
            }

            tk.setMatKhau(org.mindrot.jbcrypt.BCrypt.hashpw(newPassword, org.mindrot.jbcrypt.BCrypt.gensalt()));
            taiKhoanRepository.save(tk);
            
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        }

        return "redirect:/ho-so/bao-mat";
    }

    @GetMapping
    public String showProfile(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<BenhNhan> bnOpt = benhNhanRepository.findByTaiKhoan_MaTaiKhoan(user.getMaTaiKhoan());
        if (bnOpt.isPresent()) {
            model.addAttribute("benhNhan", bnOpt.get());
        } else {
            // Should not happen if registered correctly, but just in case
            model.addAttribute("benhNhan", new BenhNhan());
        }

        return "public/ho-so";
    }

    @PostMapping("/cap-nhat")
    public String updateProfile(@ModelAttribute("benhNhan") BenhNhan formBenhNhan, 
                                HttpSession session, 
                                RedirectAttributes redirectAttributes) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Optional<BenhNhan> bnOpt = benhNhanRepository.findByTaiKhoan_MaTaiKhoan(user.getMaTaiKhoan());
        if (bnOpt.isPresent()) {
            if (formBenhNhan.getChieuCao() != null && formBenhNhan.getChieuCao().compareTo(java.math.BigDecimal.ZERO) < 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Chiều cao không được âm.");
                return "redirect:/ho-so";
            }
            if (formBenhNhan.getCanNang() != null && formBenhNhan.getCanNang().compareTo(java.math.BigDecimal.ZERO) < 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cân nặng không được âm.");
                return "redirect:/ho-so";
            }
            if (formBenhNhan.getSoCCCD() != null && !formBenhNhan.getSoCCCD().isEmpty() && !formBenhNhan.getSoCCCD().matches("^\\d{12}$")) {
                redirectAttributes.addFlashAttribute("errorMessage", "CCCD phải bao gồm đúng 12 chữ số.");
                return "redirect:/ho-so";
            }
            if (formBenhNhan.getHoTenNguoiThan() != null && !formBenhNhan.getHoTenNguoiThan().isEmpty() && !formBenhNhan.getHoTenNguoiThan().matches("^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Họ tên người thân không được chứa số hoặc ký tự đặc biệt.");
                return "redirect:/ho-so";
            }
            if (formBenhNhan.getSdtNguoiThan() != null && !formBenhNhan.getSdtNguoiThan().isEmpty() && !formBenhNhan.getSdtNguoiThan().matches("^\\d{10}$")) {
                redirectAttributes.addFlashAttribute("errorMessage", "SĐT người thân phải bao gồm đúng 10 chữ số.");
                return "redirect:/ho-so";
            }

            BenhNhan bn = bnOpt.get();
            // Update Basic Info
            bn.setHoTen(formBenhNhan.getHoTen());
            bn.setNgaySinh(formBenhNhan.getNgaySinh());
            bn.setGioiTinh(formBenhNhan.getGioiTinh());
            bn.setQuocTich(formBenhNhan.getQuocTich());
            bn.setSoCCCD(formBenhNhan.getSoCCCD());
            bn.setDiaChi(formBenhNhan.getDiaChi());
            
            // Note: Email and Phone are tied to TaiKhoan or can be updated separately here if allowed.
            // For now, allow update in BenhNhan table.
            bn.setEmail(formBenhNhan.getEmail());
            bn.setSoDienThoai(formBenhNhan.getSoDienThoai());

            // Update Medical Info
            bn.setNhomMau(formBenhNhan.getNhomMau());
            bn.setChieuCao(formBenhNhan.getChieuCao());
            bn.setCanNang(formBenhNhan.getCanNang());
            bn.setSoBHYT(formBenhNhan.getSoBHYT());
            bn.setNgayHetHanBHYT(formBenhNhan.getNgayHetHanBHYT());
            bn.setDiUngThuoc(formBenhNhan.getDiUngThuoc());
            bn.setTienSuBenhManTinh(formBenhNhan.getTienSuBenhManTinh());
            bn.setGhiChuYTe(formBenhNhan.getGhiChuYTe());

            // Update Emergency Contact
            bn.setHoTenNguoiThan(formBenhNhan.getHoTenNguoiThan());
            bn.setQuanHeNguoiThan(formBenhNhan.getQuanHeNguoiThan());
            bn.setSdtNguoiThan(formBenhNhan.getSdtNguoiThan());

            // Update Notifications
            bn.setNhanEmailNhacLich(formBenhNhan.getNhanEmailNhacLich() != null ? formBenhNhan.getNhanEmailNhacLich() : false);
            bn.setNhanSMSNhacLich(formBenhNhan.getNhanSMSNhacLich() != null ? formBenhNhan.getNhanSMSNhacLich() : false);
            bn.setNhanEmailKetQua(formBenhNhan.getNhanEmailKetQua() != null ? formBenhNhan.getNhanEmailKetQua() : false);
            bn.setNhanTinTucSucKhoe(formBenhNhan.getNhanTinTucSucKhoe() != null ? formBenhNhan.getNhanTinTucSucKhoe() : false);

            benhNhanRepository.save(bn);
            
            // Also update the session user's HoTen just in case it changed
            user.setHoTen(bn.getHoTen());
            session.setAttribute("loggedInUser", user);
            
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hồ sơ bệnh nhân!");
        }

        return "redirect:/ho-so";
    }
}
