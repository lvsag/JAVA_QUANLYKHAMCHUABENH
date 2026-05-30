package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.Thuoc;
import com.java.BaoCaoDoAn.Model.VatTuYTe;
import com.java.BaoCaoDoAn.Service.DichVuService;
import com.java.BaoCaoDoAn.Service.ThuocService;
import com.java.BaoCaoDoAn.Service.VatTuYTeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/vat-tu")
public class AdminVatTuController {

    @Autowired
    private ThuocService thuocService;

    @Autowired
    private VatTuYTeService vatTuYTeService;

    @Autowired
    private DichVuService dichVuService;

    // Hiển thị danh sách thuốc và vật tư y tế (MH21)
    @GetMapping
    public String danhSachVatTu(Model model) {
        var dsThuoc = thuocService.findAll();
        var dsVatTu = vatTuYTeService.findAll();
        var dsDichVu = dichVuService.getAllDichVu();

        model.addAttribute("danhSachThuoc", dsThuoc);
        model.addAttribute("danhSachVatTu", dsVatTu);
        model.addAttribute("danhSachDichVu", dsDichVu);

        // Thống kê
        long soDichVu = dsDichVu.size();
        long soMatHang = dsThuoc.size() + dsVatTu.size();
        long soSapHetHang = dsThuoc.stream().filter(t -> t.getTonKho() <= t.getNguongCanhBao()).count()
                          + dsVatTu.stream().filter(v -> v.getTonKho() <= v.getNguongCanhBao()).count();

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
    public String luuThuoc(@ModelAttribute("thuoc") Thuoc thuoc) {
        thuocService.save(thuoc);
        return "redirect:/admin/vat-tu";
    }

    @GetMapping("/thuoc/sua/{id}")
    public String hienThiFormSuaThuoc(@PathVariable("id") Integer id, Model model) {
        thuocService.findById(id).ifPresent(t -> model.addAttribute("thuoc", t));
        return "admin/vat-tu/form-thuoc";
    }

    @GetMapping("/thuoc/xoa/{id}")
    public String xoaThuoc(@PathVariable("id") Integer id) {
        thuocService.deleteById(id);
        return "redirect:/admin/vat-tu";
    }

    // --- QUẢN LÝ VẬT TƯ Y TẾ ---

    @GetMapping("/vattu/them")
    public String hienThiFormThemVatTu(Model model) {
        model.addAttribute("vatTuYTe", new VatTuYTe());
        return "admin/vat-tu/form-vattu";
    }

    @PostMapping("/vattu/luu")
    public String luuVatTu(@ModelAttribute("vatTuYTe") VatTuYTe vatTuYTe) {
        vatTuYTeService.save(vatTuYTe);
        return "redirect:/admin/vat-tu";
    }

    @GetMapping("/vattu/sua/{id}")
    public String hienThiFormSuaVatTu(@PathVariable("id") Integer id, Model model) {
        vatTuYTeService.findById(id).ifPresent(v -> model.addAttribute("vatTuYTe", v));
        return "admin/vat-tu/form-vattu";
    }

    @GetMapping("/vattu/xoa/{id}")
    public String xoaVatTu(@PathVariable("id") Integer id) {
        vatTuYTeService.deleteById(id);
        return "redirect:/admin/vat-tu";
    }
}
