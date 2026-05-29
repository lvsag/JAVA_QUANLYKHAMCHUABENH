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
