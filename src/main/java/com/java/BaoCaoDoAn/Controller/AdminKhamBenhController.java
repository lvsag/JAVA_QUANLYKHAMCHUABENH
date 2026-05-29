package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.*;
import com.java.BaoCaoDoAn.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/bac-si")
public class AdminKhamBenhController {
    private final KhamBenhService khamBenhService;
    private final ChiDinhService chiDinhService;
    private final DonThuocService donThuocService;
    private final KetQuaXetNghiemService ketQuaXetNghiemService;
    private final DichVuService dichVuService;
    private final BenhNhanService benhNhanService;
    private final BacSiService bacSiService;

    public AdminKhamBenhController(KhamBenhService khamBenhService,
                                   ChiDinhService chiDinhService,
                                   DonThuocService donThuocService,
                                   KetQuaXetNghiemService ketQuaXetNghiemService,
                                   DichVuService dichVuService,
                                   BenhNhanService benhNhanService,
                                   BacSiService bacSiService) {
        this.khamBenhService = khamBenhService;
        this.chiDinhService = chiDinhService;
        this.donThuocService = donThuocService;
        this.ketQuaXetNghiemService = ketQuaXetNghiemService;
        this.dichVuService = dichVuService;
        this.benhNhanService = benhNhanService;
        this.bacSiService = bacSiService;
    }

    @GetMapping("/kham-benh")
    public String khamBenh(@RequestParam(value = "maPhieuKham", required = false) String maPhieuKham, Model model) {
        // Added to connect kham-benh.html with the examination queue and selected examination detail.
        List<PhieuKham> phieuKhams = khamBenhService.getAllPhieuKham();
        PhieuKham selected = maPhieuKham != null
                ? khamBenhService.getPhieuKham(maPhieuKham).orElse(null)
                : (phieuKhams.isEmpty() ? null : phieuKhams.get(0));
        java.util.List<java.util.Map<String, String>> patientQueue = new java.util.ArrayList<>();
        for (int i = 0; i < phieuKhams.size(); i++) {
            PhieuKham item = phieuKhams.get(i);
            java.util.Map<String, String> row = new java.util.HashMap<>();
            row.put("id", item.getMaPhieuKham());
            row.put("stt", String.format("%02d", i + 1));
            row.put("name", item.getBenhNhan() != null ? item.getBenhNhan().getHoTen() : "Chua co benh nhan");
            row.put("time", item.getNgayKham() != null ? new java.text.SimpleDateFormat("HH:mm").format(item.getNgayKham()) : "");
            row.put("status", item.getTrangThai() != null ? item.getTrangThai() : "");
            row.put("examCode", item.getMaPhieuKham());
            patientQueue.add(row);
        }
        model.addAttribute("phieuKhams", phieuKhams);
        model.addAttribute("patientQueue", patientQueue);
        model.addAttribute("phieuKham", selected);
        return "admin/bac-si/kham-benh/kham-benh";
    }

    @PostMapping("/kham-benh/luu")
    public String luuKhamBenh(@ModelAttribute PhieuKham phieuKham, RedirectAttributes redirectAttributes) {
        // Added to save examination notes from kham-benh.html.
        if (phieuKham.getBenhNhan() == null || phieuKham.getBenhNhan().getMaBenhNhan() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Can chon benh nhan truoc khi luu phieu kham.");
            return "redirect:/admin/bac-si/kham-benh";
        }
        khamBenhService.savePhieuKham(phieuKham);
        redirectAttributes.addFlashAttribute("successMessage", "Da luu phieu kham.");
        return "redirect:/admin/bac-si/kham-benh?maPhieuKham=" + phieuKham.getMaPhieuKham();
    }

    @GetMapping("/chi-dinh")
    public String lapPhieuChiDinh(@RequestParam(value = "maPhieuKham", required = false) String maPhieuKham, Model model) {
        // Added to connect phieu-chi-dinh.html with real examination and service data.
        PhieuKham phieuKham = maPhieuKham != null ? khamBenhService.getPhieuKham(maPhieuKham).orElse(null) : null;
        model.addAttribute("phieuKham", phieuKham);
        model.addAttribute("dichVus", dichVuService.getAllDichVu());
        model.addAttribute("phieuChiDinh", new PhieuChiDinh());
        return "admin/bac-si/chi-dinh/phieu-chi-dinh";
    }

    @PostMapping("/chi-dinh/luu")
    public String luuPhieuChiDinh(@RequestParam(value = "maPhieuKham", required = false) String maPhieuKham,
                                  @RequestParam(value = "maBenhNhan", required = false) String maBenhNhan,
                                  @RequestParam(value = "maBacSi", required = false) String maBacSi,
                                  @RequestParam(value = "maDichVu", required = false) List<String> maDichVu,
                                  @RequestParam(value = "ghiChu", required = false) String ghiChu,
                                  RedirectAttributes redirectAttributes) {
        // Added to create a clinical service order from selected services.
        PhieuKham phieuKham = maPhieuKham != null && !maPhieuKham.isBlank()
                ? khamBenhService.getPhieuKham(maPhieuKham).orElse(null)
                : null;
        BenhNhan benhNhan = resolveBenhNhan(phieuKham, maBenhNhan);
        BacSi bacSi = resolveBacSi(phieuKham, maBacSi);
        if (benhNhan == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khong tim thay benh nhan cho phieu chi dinh.");
            return "redirect:/admin/bac-si/chi-dinh";
        }
        PhieuChiDinh saved = chiDinhService.taoPhieuChiDinh(phieuKham, benhNhan, bacSi, maDichVu, ghiChu);
        redirectAttributes.addFlashAttribute("successMessage", "Da tao phieu chi dinh.");
        return "redirect:/admin/bac-si/chi-dinh/danh-sach?maPhieuChiDinh=" + saved.getMaPhieuChiDinh();
    }

    @GetMapping("/chi-dinh/danh-sach")
    public String danhSachPhieuChiDinh(@RequestParam(value = "maPhieuChiDinh", required = false) String maPhieuChiDinh, Model model) {
        // Added to connect danh-sach-phieu-chi-dinh.html with saved clinical service orders.
        List<PhieuChiDinh> danhSach = chiDinhService.getAllPhieuChiDinh();
        PhieuChiDinh selected = maPhieuChiDinh != null
                ? chiDinhService.getPhieuChiDinh(maPhieuChiDinh).orElse(null)
                : (danhSach.isEmpty() ? null : danhSach.get(0));
        model.addAttribute("danhSach", danhSach);
        model.addAttribute("phieuChiDinh", selected);
        model.addAttribute("chiTiets", selected == null ? List.of() : chiDinhService.getChiTiet(selected.getMaPhieuChiDinh()));
        model.addAttribute("ketQua", new KetQuaXetNghiem());
        return "admin/bac-si/chi-dinh/danh-sach-phieu-chi-dinh";
    }

    @PostMapping("/chi-dinh/ket-qua")
    public String luuKetQuaChiDinh(@ModelAttribute KetQuaXetNghiem ketQua,
                                   @RequestParam(value = "maPhieuChiDinh", required = false) String maPhieuChiDinh,
                                   RedirectAttributes redirectAttributes) {
        // Added to let the result-entry panel save a test/service result.
        if (maPhieuChiDinh != null && !maPhieuChiDinh.isBlank()) {
            chiDinhService.getPhieuChiDinh(maPhieuChiDinh).ifPresent(phieu -> {
                ketQua.setPhieuChiDinh(phieu);
                ketQua.setBenhNhan(phieu.getBenhNhan());
                ketQua.setBacSi(phieu.getBacSi());
            });
        }
        ketQuaXetNghiemService.saveKetQua(ketQua);
        redirectAttributes.addFlashAttribute("successMessage", "Da luu ket qua.");
        return "redirect:/admin/bac-si/chi-dinh/danh-sach";
    }

    @GetMapping("/ket-qua")
    public String xemKetQua(@RequestParam(value = "maKetQua", required = false) String maKetQua, Model model) {
        // Added to connect xem-ket-qua-xet-nghiem.html with results entered by technicians/admin.
        List<KetQuaXetNghiem> ketQuas = ketQuaXetNghiemService.getAllKetQua();
        KetQuaXetNghiem selected = maKetQua != null
                ? ketQuaXetNghiemService.getKetQua(maKetQua).orElse(null)
                : (ketQuas.isEmpty() ? null : ketQuas.get(0));
        model.addAttribute("ketQuas", ketQuas);
        model.addAttribute("ketQua", selected);
        return "admin/bac-si/ket-qua/xem-ket-qua-xet-nghiem";
    }

    @PostMapping("/ket-qua/ket-luan")
    public String luuKetLuan(@RequestParam String maKetQua,
                             @RequestParam(value = "ketLuanBacSi", required = false) String ketLuanBacSi,
                             RedirectAttributes redirectAttributes) {
        // Added to let the doctor save a conclusion after viewing a result.
        ketQuaXetNghiemService.getKetQua(maKetQua).ifPresent(ketQua -> {
            ketQua.setKetLuanBacSi(ketLuanBacSi);
            ketQua.setTrangThai("Da xem");
            ketQuaXetNghiemService.saveKetQua(ketQua);
        });
        redirectAttributes.addFlashAttribute("successMessage", "Da luu ket luan bac si.");
        return "redirect:/admin/bac-si/ket-qua?maKetQua=" + maKetQua;
    }

    @GetMapping("/ke-thuoc")
    public String keThuoc(@RequestParam(value = "maPhieuKham", required = false) String maPhieuKham, Model model) {
        // Added to connect ke-thuoc.html with selected examination data.
        PhieuKham phieuKham = maPhieuKham != null ? khamBenhService.getPhieuKham(maPhieuKham).orElse(null) : null;
        model.addAttribute("phieuKham", phieuKham);
        model.addAttribute("donThuoc", new DonThuoc());
        return "admin/bac-si/ke-thuoc/ke-thuoc";
    }

    @PostMapping("/ke-thuoc/luu")
    public String luuDonThuoc(@RequestParam(value = "maPhieuKham", required = false) String maPhieuKham,
                              @RequestParam(value = "maBenhNhan", required = false) String maBenhNhan,
                              @RequestParam(value = "maBacSi", required = false) String maBacSi,
                              @RequestParam(value = "chanDoan", required = false) String chanDoan,
                              @RequestParam(value = "loiDan", required = false) String loiDan,
                              @RequestParam(value = "tenThuoc", required = false) List<String> tenThuoc,
                              @RequestParam(value = "lieuDung", required = false) List<String> lieuDung,
                              @RequestParam(value = "soLanTrongNgay", required = false) List<String> soLanTrongNgay,
                              @RequestParam(value = "soLuong", required = false) List<String> soLuong,
                              @RequestParam(value = "ghiChu", required = false) List<String> ghiChu,
                              RedirectAttributes redirectAttributes) {
        // Added to create a prescription from ke-thuoc.html.
        PhieuKham phieuKham = maPhieuKham != null && !maPhieuKham.isBlank()
                ? khamBenhService.getPhieuKham(maPhieuKham).orElse(null)
                : null;
        BenhNhan benhNhan = resolveBenhNhan(phieuKham, maBenhNhan);
        BacSi bacSi = resolveBacSi(phieuKham, maBacSi);
        if (benhNhan == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khong tim thay benh nhan cho don thuoc.");
            return "redirect:/admin/bac-si/ke-thuoc";
        }
        DonThuoc saved = donThuocService.taoDonThuoc(phieuKham, benhNhan, bacSi, chanDoan, loiDan, tenThuoc, lieuDung, soLanTrongNgay, soLuong, ghiChu);
        redirectAttributes.addFlashAttribute("successMessage", "Da luu don thuoc.");
        return "redirect:/ho-so/don-thuoc?maDonThuoc=" + saved.getMaDonThuoc();
    }

    private BenhNhan resolveBenhNhan(PhieuKham phieuKham, String maBenhNhan) {
        if (phieuKham != null && phieuKham.getBenhNhan() != null) {
            return phieuKham.getBenhNhan();
        }
        return maBenhNhan == null || maBenhNhan.isBlank() ? null : benhNhanService.getBenhNhanById(maBenhNhan).orElse(null);
    }

    private BacSi resolveBacSi(PhieuKham phieuKham, String maBacSi) {
        if (phieuKham != null && phieuKham.getBacSi() != null) {
            return phieuKham.getBacSi();
        }
        return maBacSi == null || maBacSi.isBlank() ? null : bacSiService.getBacSiById(maBacSi).orElse(null);
    }
}
