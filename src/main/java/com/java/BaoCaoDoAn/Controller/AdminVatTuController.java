package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.DichVu;
import com.java.BaoCaoDoAn.Model.Thuoc;
import com.java.BaoCaoDoAn.Model.VatTuYTe;
import com.java.BaoCaoDoAn.Service.ChuyenKhoaService;
import com.java.BaoCaoDoAn.Service.DichVuService;
import com.java.BaoCaoDoAn.Service.ThuocService;
import com.java.BaoCaoDoAn.Service.VatTuYTeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin/vat-tu")
public class AdminVatTuController {

    @Autowired
    private ThuocService thuocService;

    @Autowired
    private VatTuYTeService vatTuYTeService;

    @Autowired
    private DichVuService dichVuService;

    @Autowired
    private ChuyenKhoaService chuyenKhoaService;

    // Hiển thị danh sách dịch vụ, thuốc và vật tư y tế (MH21)
    @GetMapping
    public String danhSachVatTu(
            @RequestParam(value = "keywordDichVu", required = false) String keywordDichVu,
            @RequestParam(value = "keywordKho", required = false) String keywordKho,
            Model model) {
        
        List<DichVu> dsDichVu = dichVuService.searchDichVu(keywordDichVu);
        List<Thuoc> dsThuoc = thuocService.searchThuoc(keywordKho);
        List<VatTuYTe> dsVatTu = vatTuYTeService.searchVatTu(keywordKho);

        model.addAttribute("danhSachThuoc", dsThuoc);
        model.addAttribute("danhSachVatTu", dsVatTu);
        model.addAttribute("danhSachDichVu", dsDichVu);
        model.addAttribute("keywordDichVu", keywordDichVu);
        model.addAttribute("keywordKho", keywordKho);

        // Thống kê (Dựa trên toàn bộ dữ liệu hoặc dữ liệu lọc tùy logic, ở đây giữ logic cũ là toàn bộ cho khớp stats)
        long soDichVu = dichVuService.getAllDichVu().size();
        long soMatHang = thuocService.findAll().size() + vatTuYTeService.findAll().size();
        long soSapHetHang = thuocService.findAll().stream().filter(t -> t.getTonKho() <= t.getNguongCanhBao()).count()
                          + vatTuYTeService.findAll().stream().filter(v -> v.getTonKho() <= v.getNguongCanhBao()).count();

        model.addAttribute("soDichVuDangDung", soDichVu);
        model.addAttribute("soMatHang", soMatHang);
        model.addAttribute("soSapHetHang", soSapHetHang);

        return "admin/vat-tu/danh-sach";
    }

    // --- QUẢN LÝ THUỐC ---

    @GetMapping("/thuoc/them")
    public String hienThiFormThemThuoc(Model model) {
        model.addAttribute("thuoc", new Thuoc());
        return "admin/vat-tu/form-thuoc";
    }

    @PostMapping("/thuoc/luu")
    public String luuThuoc(@ModelAttribute("thuoc") Thuoc thuoc, Model model, RedirectAttributes ra) {
        if (thuoc.getTonKho() != null && thuoc.getTonKho() < 0 || 
            thuoc.getNguongCanhBao() != null && thuoc.getNguongCanhBao() < 0 || 
            thuoc.getGiaNhap() != null && thuoc.getGiaNhap().compareTo(BigDecimal.ZERO) < 0 || 
            thuoc.getGiaBan() != null && thuoc.getGiaBan().compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("error", "Số lượng và giá không được âm!");
            return "admin/vat-tu/form-thuoc";
        }
        thuocService.save(thuoc);
        ra.addFlashAttribute("message", "Lưu thuốc thành công!");
        return "redirect:/admin/vat-tu";
    }

    @GetMapping("/thuoc/sua/{id}")
    public String hienThiFormSuaThuoc(@PathVariable("id") Integer id, Model model) {
        thuocService.findById(id).ifPresent(t -> model.addAttribute("thuoc", t));
        return "admin/vat-tu/form-thuoc";
    }

    @GetMapping("/thuoc/xoa/{id}")
    public String xoaThuoc(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            thuocService.deleteById(id);
            ra.addFlashAttribute("message", "Xóa thuốc thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa thuốc này!");
        }
        return "redirect:/admin/vat-tu";
    }

    // --- QUẢN LÝ VẬT TƯ Y TẾ ---

    @GetMapping("/vattu/them")
    public String hienThiFormThemVatTu(Model model) {
        model.addAttribute("vatTuYTe", new VatTuYTe());
        return "admin/vat-tu/form-vattu";
    }

    @PostMapping("/vattu/luu")
    public String luuVatTu(@ModelAttribute("vatTuYTe") VatTuYTe vatTuYTe, Model model, RedirectAttributes ra) {
        if (vatTuYTe.getTonKho() != null && vatTuYTe.getTonKho() < 0 || 
            vatTuYTe.getNguongCanhBao() != null && vatTuYTe.getNguongCanhBao() < 0 || 
            vatTuYTe.getGiaNhap() != null && vatTuYTe.getGiaNhap().compareTo(BigDecimal.ZERO) < 0 || 
            vatTuYTe.getGiaBan() != null && vatTuYTe.getGiaBan().compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("error", "Số lượng và giá không được âm!");
            return "admin/vat-tu/form-vattu";
        }
        vatTuYTeService.save(vatTuYTe);
        ra.addFlashAttribute("message", "Lưu vật tư thành công!");
        return "redirect:/admin/vat-tu";
    }

    @GetMapping("/vattu/sua/{id}")
    public String hienThiFormSuaVatTu(@PathVariable("id") Integer id, Model model) {
        vatTuYTeService.findById(id).ifPresent(v -> model.addAttribute("vatTuYTe", v));
        return "admin/vat-tu/form-vattu";
    }

    @GetMapping("/vattu/xoa/{id}")
    public String xoaVatTu(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            vatTuYTeService.deleteById(id);
            ra.addFlashAttribute("message", "Xóa vật tư thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa vật tư này!");
        }
        return "redirect:/admin/vat-tu";
    }

    // --- QUẢN LÝ DỊCH VỤ (Tích hợp MH21) ---

    @GetMapping("/dich-vu/them")
    public String hienThiFormThemDichVu(Model model) {
        model.addAttribute("dichVu", new DichVu());
        model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
        return "admin/vat-tu/form-dich-vu";
    }

    @PostMapping("/dich-vu/luu")
    public String luuDichVu(@ModelAttribute DichVu dichVu, @RequestParam("maChuyenKhoa") String maChuyenKhoa, RedirectAttributes ra) {
        if (dichVu.getMaDichVu() == null || dichVu.getMaDichVu().isEmpty()) {
            dichVu.setMaDichVu("DV" + System.currentTimeMillis() % 100000000); // Rút ngắn mã
        }
        chuyenKhoaService.getChuyenKhoaById(maChuyenKhoa).ifPresent(dichVu::setChuyenKhoa);
        dichVuService.saveDichVu(dichVu);
        ra.addFlashAttribute("message", "Lưu dịch vụ thành công!");
        return "redirect:/admin/vat-tu";
    }

    @GetMapping("/dich-vu/sua/{id}")
    public String hienThiFormSuaDichVu(@PathVariable("id") String id, Model model) {
        DichVu dv = dichVuService.getDichVuById(id);
        if (dv != null) {
            model.addAttribute("dichVu", dv);
            model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
            return "admin/vat-tu/form-dich-vu";
        }
        return "redirect:/admin/vat-tu";
    }

    @GetMapping("/dich-vu/xoa/{id}")
    public String xoaDichVu(@PathVariable("id") String id, RedirectAttributes ra) {
        try {
            // Xử lý xóa mềm hoặc kiểm tra khóa ngoại
            DichVu dv = dichVuService.getDichVuById(id);
            if (dv != null) {
                // Thử xóa cứng trước, nếu lỗi thì chuyển sang xóa mềm
                try {
                    dichVuService.deleteDichVu(id);
                    ra.addFlashAttribute("message", "Xóa dịch vụ thành công!");
                } catch (Exception e) {
                    dv.setTrangThai("Ngừng dùng");
                    dichVuService.saveDichVu(dv);
                    ra.addFlashAttribute("message", "Dịch vụ đã có dữ liệu liên quan nên chỉ chuyển sang trạng thái 'Ngừng dùng'.");
                }
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi xóa dịch vụ!");
        }
        return "redirect:/admin/vat-tu";
    }
}
