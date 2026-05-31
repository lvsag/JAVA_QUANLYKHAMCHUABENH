package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.*;
import com.java.BaoCaoDoAn.Service.ChiDinhService;
import com.java.BaoCaoDoAn.Service.KetQuaXetNghiemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/nhap-ket-qua")
public class AdminNhapKetQuaController {

    @Autowired
    private ChiDinhService chiDinhService;

    @Autowired
    private KetQuaXetNghiemService ketQuaXetNghiemService;

    @GetMapping("")
    public String danhSachCho(Model model) {
        List<ChiTietChiDinh> danhSachCho = chiDinhService.getChiTietChoThucHien();
        model.addAttribute("danhSachCho", danhSachCho);
        model.addAttribute("activeMenu", "nhap-ket-qua");
        return "admin/nhap-ket-qua/danh-sach";
    }

    @GetMapping("/{id}")
    public String hienThiFormNhap(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ChiTietChiDinh> opt = chiDinhService.getChiTietById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy yêu cầu dịch vụ này.");
            return "redirect:/admin/nhap-ket-qua";
        }
        ChiTietChiDinh chiTiet = opt.get();
        if (!"Cho".equals(chiTiet.getTrangThai())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Dịch vụ này đã được nhập kết quả.");
            return "redirect:/admin/nhap-ket-qua";
        }

        KetQuaXetNghiem ketQua = new KetQuaXetNghiem();
        ketQua.setChiTietChiDinh(chiTiet);

        model.addAttribute("chiTiet", chiTiet);
        model.addAttribute("ketQua", ketQua);
        model.addAttribute("activeMenu", "nhap-ket-qua");
        return "admin/nhap-ket-qua/form";
    }

    @PostMapping("/luu")
    public String luuKetQua(@RequestParam("idChiTiet") Long idChiTiet,
                            @ModelAttribute KetQuaXetNghiem ketQua,
                            RedirectAttributes redirectAttributes) {
        Optional<ChiTietChiDinh> opt = chiDinhService.getChiTietById(idChiTiet);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi dữ liệu. Không tìm thấy dịch vụ.");
            return "redirect:/admin/nhap-ket-qua";
        }

        ChiTietChiDinh chiTiet = opt.get();
        PhieuChiDinh pcd = chiTiet.getPhieuChiDinh();

        // 1. Cập nhật trạng thái ChiTietChiDinh thành "Đã xong"
        chiTiet.setTrangThai("Da xong");
        chiDinhService.saveChiTietChiDinh(chiTiet);

        // 2. Lưu KetQuaXetNghiem
        ketQua.setPhieuChiDinh(pcd);
        ketQua.setBenhNhan(pcd.getBenhNhan());
        ketQua.setBacSi(pcd.getBacSi());
        ketQua.setDichVu(chiTiet.getDichVu());
        ketQua.setTrangThai("Moi");
        
        ketQuaXetNghiemService.saveKetQua(ketQua);

        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu kết quả thành công cho bệnh nhân " + pcd.getBenhNhan().getHoTen());
        return "redirect:/admin/nhap-ket-qua";
    }
}
