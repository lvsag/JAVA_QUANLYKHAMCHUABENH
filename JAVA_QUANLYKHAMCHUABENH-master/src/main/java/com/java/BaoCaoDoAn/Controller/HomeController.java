package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.DTO.DatLichRequestDTO;
import com.java.BaoCaoDoAn.Model.BenhNhan;
import com.java.BaoCaoDoAn.Model.LichHen;
import com.java.BaoCaoDoAn.Service.KhungGioGeneratorService;
import com.java.BaoCaoDoAn.Service.LichHenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("datLichSession") // QUAN TRỌNG: Giữ dữ liệu DTO qua 4 bước
public class HomeController {

    @Autowired
    private com.java.BaoCaoDoAn.Repository.BenhNhanRepository benhNhanRepository;

    @Autowired
    private com.java.BaoCaoDoAn.Repository.BacSiRepository bacSiRepository;

    @Autowired
    private com.java.BaoCaoDoAn.Repository.ChuyenKhoaRepository chuyenKhoaRepository;

    @Autowired
    private com.java.BaoCaoDoAn.Repository.DichVuRepository dichVuRepository;

    @Autowired
    private LichHenService lichHenService;

    @Autowired
    private com.java.BaoCaoDoAn.Repository.LichHenRepository lichHenRepository;

    @Autowired
    private KhungGioGeneratorService khungGioGeneratorService;

    @ModelAttribute("datLichSession")
    public DatLichRequestDTO setUpDatLichForm() {
        return new DatLichRequestDTO();
    }

    // ===================================================================
    // 1. TRANG CHỦ & TỔNG QUAN
    // ===================================================================
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("totalPatients", benhNhanRepository.count());
        model.addAttribute("totalDoctors", bacSiRepository.count());
        model.addAttribute("totalSpecialties", chuyenKhoaRepository.count());

        BigDecimal averageRating = bacSiRepository.getAverageRating();
        model.addAttribute("averageRating", averageRating != null ? averageRating : BigDecimal.ZERO);
        model.addAttribute("specialties", chuyenKhoaRepository.findAll());
        model.addAttribute("doctors", bacSiRepository.findAll());
        return "public/home";
    }

    // ===================================================================
    // 2. DANH SÁCH BÁC SĨ & CHI TIẾT
    // ===================================================================
    @GetMapping("/bac-si")
    public String doctors(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "maChuyenKhoa", required = false) String maChuyenKhoa,
            @RequestParam(value = "hocVi", required = false) List<String> hocViList,
            @RequestParam(value = "rating", required = false) BigDecimal rating,
            @RequestParam(value = "sort", required = false, defaultValue = "phu-hop") String sortParam,
            Model model) {

        if (hocViList != null && hocViList.isEmpty()) {
            hocViList = null;
        }

        Sort sortObj = Sort.unsorted();
        if ("danh-gia".equals(sortParam)) {
            sortObj = Sort.by(Sort.Direction.DESC, "diemDanhGiaTB");
        } else if ("lich-hen".equals(sortParam)) {
            sortObj = Sort.by(Sort.Direction.DESC, "soBenhNhanDaKham");
        } else {
            sortObj = Sort.by(Sort.Direction.ASC, "hoTen");
        }

        List<com.java.BaoCaoDoAn.Model.BacSi> doctors = bacSiRepository.searchBacSi(keyword, maChuyenKhoa, hocViList, rating, sortObj);
        model.addAttribute("doctors", doctors);
        model.addAttribute("specialties", chuyenKhoaRepository.findAll());
        model.addAttribute("totalDoctors", doctors.size());
        model.addAttribute("selectedHocVi", hocViList != null ? hocViList : new ArrayList<>());

        return "public/bac-si";
    }

    @GetMapping("/bac-si/{maBacSi}")
    public String doctorDetail(@PathVariable String maBacSi, Model model) {
        com.java.BaoCaoDoAn.Model.BacSi bacSi = bacSiRepository.findById(maBacSi).orElse(null);
        if (bacSi == null) return "redirect:/bac-si";
        model.addAttribute("bacSi", bacSi);

        // Trả về file giao diện bac-si-detail.html
        return "public/bac-si-detail";
    }

    // ===================================================================
    // 3. LUỒNG ĐẶT LỊCH WIZARD 4 BƯỚC
    // ===================================================================

    // --- BƯỚC 1: CHỌN DỊCH VỤ ---
    @GetMapping("/dat-lich/buoc-1-dich-vu")
    public String step1DichVu(
            @RequestParam(value = "maBacSi", required = false) String maBacSi,
            @ModelAttribute("datLichSession") DatLichRequestDTO dto,
            HttpSession session, RedirectAttributes redirectAttrs, Model model) {

        Object user = session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttrs.addFlashAttribute("error", "Vui lòng đăng nhập trước khi đặt lịch.");
            return "redirect:/login";
        }

        if (maBacSi != null && !maBacSi.isEmpty()) {
            dto.setMaBacSi(maBacSi);
        }
        model.addAttribute("danhSachDichVu", dichVuRepository.findAll());
        return "public/dich-vu";
    }

    // BỔ SUNG HÀM BỊ THIẾU: POST BƯỚC 1
    @PostMapping("/dat-lich/buoc-1-dich-vu")
    public String step1Submit(@ModelAttribute("datLichSession") DatLichRequestDTO dto) {
        return "redirect:/dat-lich/buoc-2-thong-tin";
    }

    // --- BƯỚC 2: THÔNG TIN ---
    @GetMapping("/dat-lich/buoc-2-thong-tin")
    public String step2ThongTin(
            @ModelAttribute("datLichSession") DatLichRequestDTO dto,
            Model model, HttpSession session) {

        // 1. Ép kiểu chuẩn xác thành TaiKhoan
        com.java.BaoCaoDoAn.Model.TaiKhoan tk = (com.java.BaoCaoDoAn.Model.TaiKhoan) session.getAttribute("loggedInUser");
        if (tk == null) {
            return "redirect:/login";
        }

        // 2. TỪ TÀI KHOẢN -> LẤY RA BỆNH NHÂN
        // LƯU Ý: Dòng chữ "tk.getBenhNhan()" phụ thuộc vào cách bạn thiết kế Entity TaiKhoan.
        // - Nếu Class TaiKhoan có biến "private BenhNhan benhNhan;", thì dùng lệnh này:


        // - Nếu báo đỏ ở getBenhNhan(), nghĩa là bạn đang liên kết bằng ID (Ví dụ: tên đăng nhập).
        // Khi đó hãy XÓA dòng trên và BỎ COMMENT dòng dưới đây:
        BenhNhan bn = benhNhanRepository.findByTaiKhoan(tk);

        khungGioGeneratorService.generateSlotsForAll7Days();
        model.addAttribute("doctors", bacSiRepository.findAll());
        model.addAttribute("specialties", chuyenKhoaRepository.findAll());

        // Nếu tìm thấy bệnh nhân, tự động điền họ tên và BHYT
        if (dto.getHoTenNguoiKham() == null || dto.getHoTenNguoiKham().isEmpty()) {
            if (bn != null) {
                // Nếu có hồ sơ bệnh nhân thì lấy data từ bệnh nhân
                dto.setHoTenNguoiKham(bn.getHoTen());
                dto.setSoBHYT(bn.getSoBHYT());
            } else {
                // Nếu chưa có hồ sơ, lấy tạm Tên từ Tài khoản
                dto.setHoTenNguoiKham(tk.getHoTen());
            }
        }

        return "public/dat-lich";
    }

    // BỔ SUNG HÀM BỊ THIẾU: POST BƯỚC 2
    @PostMapping("/dat-lich/buoc-2-thong-tin")
    public String step2Submit(@ModelAttribute("datLichSession") DatLichRequestDTO dto) {
        return "redirect:/dat-lich/buoc-3-thanh-toan";
    }

    // --- BƯỚC 3: THANH TOÁN ---
    // BỔ SUNG HÀM BỊ THIẾU: GET BƯỚC 3
    @GetMapping("/dat-lich/buoc-3-thanh-toan")
    public String step3ThanhToan(@ModelAttribute("datLichSession") DatLichRequestDTO dto, Model model) {
        // 1. Lấy Tên Bác Sĩ từ DB đẩy lên UI
        if (dto.getMaBacSi() != null && !dto.getMaBacSi().isEmpty()) {
            bacSiRepository.findById(dto.getMaBacSi()).ifPresent(bs -> model.addAttribute("bacSi", bs));
        }

        // 2. Tính Tổng Tiền từ danh sách dịch vụ đẩy lên UI
        double tongTien = lichHenService.tinhTongTien(dto.getDanhSachMaDichVu(), dto.getMaKhuyenMai(), dto.getMaBacSi());
        model.addAttribute("tongTien", tongTien);
        return "public/thanh-toan";
    }

    @PostMapping("/dat-lich/buoc-3-thanh-toan")
    public String step3Submit(
            @ModelAttribute("datLichSession") DatLichRequestDTO dto,
            HttpSession session,
            RedirectAttributes redirectAttrs) {

        // 1. Ép kiểu chuẩn xác thành TaiKhoan
        com.java.BaoCaoDoAn.Model.TaiKhoan tk = (com.java.BaoCaoDoAn.Model.TaiKhoan) session.getAttribute("loggedInUser");
        if (tk == null) {
            return "redirect:/login";
        }

        // 2. Lấy Bệnh nhân tương tự như Bước 2
        //BenhNhan bn = tk.getBenhNhan(); // Hoặc dùng benhNhanRepository.findById(...) như hướng dẫn ở trên
        BenhNhan bn = benhNhanRepository.findByTaiKhoan(tk);
        if (bn == null) {
            redirectAttrs.addFlashAttribute("error", "Không tìm thấy hồ sơ Bệnh nhân của tài khoản này!");
            return "redirect:/dat-lich/buoc-2-thong-tin";
        }

        try {
            LichHen saved = lichHenService.datLich(dto, bn.getMaBenhNhan());
            session.removeAttribute("datLichSession");
            return "redirect:/dat-lich/dat-lich-thanh-cong?maLichHen=" + saved.getMaLichHen();

        } catch (Exception e) {
            System.out.println("========== LỖI KHI LƯU ĐẶT LỊCH ==========");
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/dat-lich/buoc-3-thanh-toan";
        }
    }

    // --- BƯỚC 4: THÀNH CÔNG ---
    @GetMapping("/dat-lich/dat-lich-thanh-cong")
    public String step4ThanhCong(@RequestParam("maLichHen") String maLichHen, Model model) {
        LichHen lh = lichHenRepository.findById(maLichHen).orElse(null);
        model.addAttribute("lichHen", lh);
        model.addAttribute("maLichHen", maLichHen);
        return "public/dat-lich-thanh-cong";
    }

    // ===================================================================
    // 4. LỊCH SỬ & HỦY LỊCH
    // ===================================================================
    // ===================================================================
    // 4. LỊCH SỬ & HỦY LỊCH
    // ===================================================================
    @GetMapping("/lich-su-dat-lich")
    public String lichSuDatLich(Model model, HttpSession session, RedirectAttributes redirectAttrs) {

        // 1. Lấy Tài khoản từ Session
        com.java.BaoCaoDoAn.Model.TaiKhoan tk = (com.java.BaoCaoDoAn.Model.TaiKhoan) session.getAttribute("loggedInUser");
        if (tk == null) return "redirect:/login";

        // 2. Tìm Bệnh nhân thông qua Tài khoản
        BenhNhan bn = benhNhanRepository.findByTaiKhoan(tk);

        // Nếu Tài khoản này chưa từng tạo hồ sơ bệnh nhân
        if (bn == null) {
            redirectAttrs.addFlashAttribute("error", "Bạn chưa có hồ sơ Bệnh nhân, vui lòng cập nhật hồ sơ hoặc đặt lịch khám trước.");
            return "redirect:/home";
        }

        // 3. Tìm danh sách lịch hẹn của bệnh nhân này
        model.addAttribute("danhSach", lichHenService.getLichHenByBenhNhan(bn.getMaBenhNhan()));
        return "public/lich-su-dat-lich";
    }

    @PostMapping("/dat-lich/{maLichHen}/huy")
    public String huyLich(
            @PathVariable String maLichHen,
            @RequestParam(value = "lyDoHuy", required = false) String lyDoHuy,
            HttpSession session,
            RedirectAttributes redirectAttrs) {

        // 1. Lấy Tài khoản từ Session
        com.java.BaoCaoDoAn.Model.TaiKhoan tk = (com.java.BaoCaoDoAn.Model.TaiKhoan) session.getAttribute("loggedInUser");
        if (tk == null) return "redirect:/login";

        // 2. Tìm Bệnh nhân
        BenhNhan bn = benhNhanRepository.findByTaiKhoan(tk);
        if (bn == null) {
            redirectAttrs.addFlashAttribute("error", "Lỗi: Không tìm thấy hồ sơ Bệnh nhân.");
            return "redirect:/lich-su-dat-lich";
        }

        try {
            // 3. Thực hiện hủy lịch
            lichHenService.huyLich(maLichHen, lyDoHuy, bn.getMaBenhNhan());
            redirectAttrs.addFlashAttribute("success", "Hủy lịch thành công.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/lich-su-dat-lich";
    }

    @GetMapping("/dat-lich/tao-moi")
    public String taoMoiDatLich(HttpSession session, @RequestParam(value = "maBacSi", required = false) String maBacSi) {
        // "Quét sạch" giỏ hàng cũ
        session.removeAttribute("datLichSession");

        // Điều hướng sang Bước 1 (Nếu có truyền mã bác sĩ thì mang theo)
        if (maBacSi != null && !maBacSi.isEmpty()) {
            return "redirect:/dat-lich/buoc-1-dich-vu?maBacSi=" + maBacSi;
        }
        return "redirect:/dat-lich/buoc-1-dich-vu";
    }
}