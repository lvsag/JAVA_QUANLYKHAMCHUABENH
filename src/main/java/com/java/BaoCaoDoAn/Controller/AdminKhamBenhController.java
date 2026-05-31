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
// Added/updated: routes in this controller are doctor-workflow routes, so they use /bac-si/*
// and render templates from templates/bac-si/* instead of templates/admin/bac-si/*.
@RequestMapping("/bac-si")
public class AdminKhamBenhController {
    private final KhamBenhService khamBenhService;
    private final ChiDinhService chiDinhService;
    private final DonThuocService donThuocService;
    private final KetQuaXetNghiemService ketQuaXetNghiemService;
    private final DichVuService dichVuService;
    private final ThuocService thuocService;
    private final BenhNhanService benhNhanService;
    private final BacSiService bacSiService;

    public AdminKhamBenhController(KhamBenhService khamBenhService,
                                   ChiDinhService chiDinhService,
                                   DonThuocService donThuocService,
                                   KetQuaXetNghiemService ketQuaXetNghiemService,
                                   DichVuService dichVuService,
                                   ThuocService thuocService,
                                   BenhNhanService benhNhanService,
                                   BacSiService bacSiService) {
        this.khamBenhService = khamBenhService;
        this.chiDinhService = chiDinhService;
        this.donThuocService = donThuocService;
        this.ketQuaXetNghiemService = ketQuaXetNghiemService;
        this.dichVuService = dichVuService;
        this.thuocService = thuocService;
        this.benhNhanService = benhNhanService;
        this.bacSiService = bacSiService;
    }

    @GetMapping("/kham-benh")
    public String khamBenh(@RequestParam(value = "maPhieuKham", required = false) String maPhieuKham,
                           Model model,
                           HttpSession session) {
        // Added to connect kham-benh.html with the examination queue and selected examination detail.
        BacSi currentBacSi = resolveLoggedInBacSi(session);
        List<PhieuKham> phieuKhams = currentBacSi != null
                ? khamBenhService.getPhieuKhamByBacSi(currentBacSi.getMaBacSi())
                : java.util.Collections.emptyList();
        PhieuKham selected = maPhieuKham != null
                ? khamBenhService.getPhieuKham(maPhieuKham).orElse(null)
                : (phieuKhams.isEmpty() ? null : phieuKhams.get(0));
        if (selected != null) {
            boolean selectedInQueue = false;
            for (PhieuKham item : phieuKhams) {
                if (item.getMaPhieuKham().equals(selected.getMaPhieuKham())) {
                    selectedInQueue = true;
                    break;
                }
            }
            if (!selectedInQueue) {
                selected = phieuKhams.isEmpty() ? null : phieuKhams.get(0);
            }
        }
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
        // Added: expose selected exam for dynamic doctor workflow actions.
        model.addAttribute("selectedPhieuKham", selected);
        return "bac-si/kham-benh/kham-benh";
    }

    @PostMapping("/kham-benh/luu")
    public String luuKhamBenh(@ModelAttribute PhieuKham phieuKham, RedirectAttributes redirectAttributes) {
        // Added to save examination notes from kham-benh.html.
        if (phieuKham.getMaPhieuKham() == null || phieuKham.getMaPhieuKham().isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thiếu mã phiếu khám, không thể lưu thông tin khám.");
            return "redirect:/bac-si/kham-benh";
        }
        if (phieuKham.getBenhNhan() == null || phieuKham.getBenhNhan().getMaBenhNhan() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cần chọn bệnh nhân trước khi lưu phiếu khám.");
            return "redirect:/bac-si/kham-benh";
        }
        if (isBlank(phieuKham.getTrieuChung()) && isBlank(phieuKham.getChanDoanBanDau())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cần nhập ít nhất triệu chứng hoặc chẩn đoán ban đầu.");
            return "redirect:/bac-si/kham-benh?maPhieuKham=" + phieuKham.getMaPhieuKham();
        }
        khamBenhService.savePhieuKham(phieuKham);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu thông tin khám bệnh.");
        return "redirect:/bac-si/kham-benh?maPhieuKham=" + phieuKham.getMaPhieuKham();
    }

    @GetMapping("/chi-dinh")
    public String lapPhieuChiDinh(@RequestParam(value = "maPhieuKham", required = false) String maPhieuKham, Model model) {
        // Added to connect phieu-chi-dinh.html with real examination and service data.
        PhieuKham phieuKham = maPhieuKham != null ? khamBenhService.getPhieuKham(maPhieuKham).orElse(null) : null;
        // Added: combobox source for selecting an examination if the page is opened directly.
        model.addAttribute("phieuKhams", khamBenhService.getAllPhieuKham());
        model.addAttribute("phieuKham", phieuKham);
        model.addAttribute("dichVus", dichVuService.getAllDichVu());
        model.addAttribute("phieuChiDinh", new PhieuChiDinh());
        // Added: selected services are posted back to create PhieuChiDinh + ChiTietChiDinh rows.
        return "bac-si/chi-dinh/phieu-chi-dinh";
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
        if (phieuKham == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cần chọn phiếu khám trước khi lập chỉ định dịch vụ.");
            return "redirect:/bac-si/chi-dinh";
        }
        BenhNhan benhNhan = resolveBenhNhan(phieuKham, maBenhNhan);
        BacSi bacSi = resolveBacSi(phieuKham, maBacSi);
        if (benhNhan == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bệnh nhân cho phiếu chỉ định.");
            return "redirect:/bac-si/chi-dinh?maPhieuKham=" + phieuKham.getMaPhieuKham();
        }
        if (bacSi == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bác sĩ chỉ định.");
            return "redirect:/bac-si/chi-dinh?maPhieuKham=" + phieuKham.getMaPhieuKham();
        }
        if (maDichVu == null || maDichVu.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cần chọn ít nhất một dịch vụ cận lâm sàng.");
            return "redirect:/bac-si/chi-dinh?maPhieuKham=" + phieuKham.getMaPhieuKham();
        }
        PhieuChiDinh saved = chiDinhService.taoPhieuChiDinh(phieuKham, benhNhan, bacSi, maDichVu, ghiChu);
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo phiếu chỉ định dịch vụ.");
        return "redirect:/bac-si/chi-dinh?maPhieuKham=" + (saved.getPhieuKham() != null ? saved.getPhieuKham().getMaPhieuKham() : "");
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
        return "redirect:/bac-si/ket-qua";
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
        // Added: results are loaded from KetQuaDichVu via KetQuaXetNghiem mapping.
        return "bac-si/ket-qua/xem-ket-qua-xet-nghiem";
    }

    @PostMapping("/ket-qua/ket-luan")
    public String luuKetLuan(@RequestParam String maKetQua,
                             @RequestParam(value = "ketLuanBacSi", required = false) String ketLuanBacSi,
                             RedirectAttributes redirectAttributes) {
        // Added to let the doctor save a conclusion after viewing a result.
        if (isBlank(maKetQua)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thiếu mã kết quả, không thể lưu kết luận.");
            return "redirect:/bac-si/ket-qua";
        }
        if (isBlank(ketLuanBacSi)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cần nhập kết luận bác sĩ trước khi lưu.");
            return "redirect:/bac-si/ket-qua?maKetQua=" + maKetQua;
        }
        KetQuaXetNghiem ketQua = ketQuaXetNghiemService.getKetQua(maKetQua).orElse(null);
        if (ketQua == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy kết quả dịch vụ cần kết luận.");
            return "redirect:/bac-si/ket-qua";
        }
        ketQua.setKetLuanBacSi(ketLuanBacSi);
        ketQua.setTrangThai("Da xem");
        ketQuaXetNghiemService.saveKetQua(ketQua);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu kết luận bác sĩ.");
        return "redirect:/bac-si/ket-qua?maKetQua=" + maKetQua;
    }

    @GetMapping("/ke-thuoc")
    public String keThuoc(@RequestParam(value = "maPhieuKham", required = false) String maPhieuKham, Model model) {
        // Added to connect ke-thuoc.html with selected examination data.
        PhieuKham phieuKham = maPhieuKham != null ? khamBenhService.getPhieuKham(maPhieuKham).orElse(null) : null;
        // Added: combobox source for choosing the examination when entering prescription directly.
        model.addAttribute("phieuKhams", khamBenhService.getAllPhieuKham());
        model.addAttribute("phieuKham", phieuKham);
        model.addAttribute("donThuoc", new DonThuoc());
        // Added: dynamic medicine catalog and stock data for prescription rows.
        model.addAttribute("thuocs", thuocService.getAllThuoc());
        return "bac-si/ke-thuoc/ke-thuoc";
    }

    @GetMapping("/bao-cao")
    public String baoCao() {
        // Added: placeholder route so the doctor sidebar has a distinct report page.
        return "bac-si/bao-cao/xem-bao-cao";
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
        if (phieuKham == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cần chọn phiếu khám trước khi kê đơn.");
            return "redirect:/bac-si/ke-thuoc";
        }
        BenhNhan benhNhan = resolveBenhNhan(phieuKham, maBenhNhan);
        BacSi bacSi = resolveBacSi(phieuKham, maBacSi);
        if (benhNhan == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bệnh nhân cho đơn thuốc.");
            return "redirect:/bac-si/ke-thuoc?maPhieuKham=" + phieuKham.getMaPhieuKham();
        }
        if (bacSi == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bác sĩ kê đơn.");
            return "redirect:/bac-si/ke-thuoc?maPhieuKham=" + phieuKham.getMaPhieuKham();
        }
        if (isBlank(chanDoan)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cần nhập chẩn đoán trước khi lưu đơn thuốc.");
            return "redirect:/bac-si/ke-thuoc?maPhieuKham=" + phieuKham.getMaPhieuKham();
        }
        if (tenThuoc == null || tenThuoc.stream().allMatch(this::isBlank)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cần chọn ít nhất một thuốc trong đơn.");
            return "redirect:/bac-si/ke-thuoc?maPhieuKham=" + phieuKham.getMaPhieuKham();
        }
        donThuocService.taoDonThuoc(phieuKham, benhNhan, bacSi, chanDoan, loiDan, tenThuoc, lieuDung, soLanTrongNgay, soLuong, ghiChu);
        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu đơn thuốc.");
        return "redirect:/bac-si/ke-thuoc?maPhieuKham=" + phieuKham.getMaPhieuKham();
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

    private BacSi resolveLoggedInBacSi(HttpSession session) {
        Object user = session.getAttribute("loggedInUser");
        if (!(user instanceof TaiKhoan taiKhoan) || taiKhoan.getMaTaiKhoan() == null) {
            return null;
        }
        return bacSiService.getBacSiByTaiKhoan(taiKhoan.getMaTaiKhoan()).orElse(null);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
