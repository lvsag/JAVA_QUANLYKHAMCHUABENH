package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.*;
import com.java.BaoCaoDoAn.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/noi-tru")
public class AdminNoiTruController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
    private NhapVienNoiTruService nhapVienNoiTruService;

    @Autowired
    private BenhNhanService benhNhanService;

    @Autowired
    private PhongBenhService phongBenhService;

    @Autowired
    private GiuongBenhService giuongBenhService;

    @Autowired
    private BacSiService bacSiService;

    @Autowired
    private ChuyenKhoaService chuyenKhoaService;

    @GetMapping("")
    public String danhSachNoiTru(Model model) {
        model.addAttribute("danhSachNoiTru", nhapVienNoiTruService.findAll());
        model.addAttribute("activeMenu", "noi-tru");
        return "admin/noi-tru/danh-sach";
    }

    @GetMapping("/nhap-vien/{maBenhNhan}")
    public String formNhapVien(@PathVariable("maBenhNhan") String maBenhNhan, Model model, RedirectAttributes ra) {
        Optional<BenhNhan> bnOpt = benhNhanService.getBenhNhanById(maBenhNhan);
        if (!bnOpt.isPresent()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy bệnh nhân.");
            return "redirect:/admin/benh-nhan";
        }

        if (nhapVienNoiTruService.isBenhNhanDangNoiTru(maBenhNhan)) {
            ra.addFlashAttribute("errorMessage", "Bệnh nhân này đang điều trị nội trú, không thể nhập viện thêm.");
            return "redirect:/admin/benh-nhan";
        }

        NhapVienNoiTru nhapVien = new NhapVienNoiTru();
        nhapVien.setBenhNhan(bnOpt.get());
        nhapVien.setNgayNhapVien(new Date());
        nhapVien.setTrangThai("Đang điều trị");

        model.addAttribute("benhNhan", bnOpt.get());
        model.addAttribute("nhapVienNoiTru", nhapVien);
        model.addAttribute("danhSachPhong", phongBenhService.findByTrangThai("Còn nhận"));
        model.addAttribute("danhSachGiuongTrong", giuongBenhService.findByTrangThai("Trống"));
        model.addAttribute("danhSachBacSi", bacSiService.getAllBacSi());
        model.addAttribute("danhSachChuyenKhoa", chuyenKhoaService.getAllChuyenKhoa());
        model.addAttribute("activeMenu", "benh-nhan");
        
        return "admin/noi-tru/nhap-vien";
    }

    @PostMapping("/luu")
    @Transactional
    public String luuNhapVien(@ModelAttribute("nhapVienNoiTru") NhapVienNoiTru nhapVien,
                              @RequestParam(value = "maBenhNhan", required = false) String maBenhNhan,
                              @RequestParam(value = "maPhong", required = false) String maPhong,
                              @RequestParam(value = "maGiuong", required = false) Integer maGiuong,
                              @RequestParam(value = "maBacSiDieuTri", required = false) String maBacSiDieuTri,
                              @RequestParam(value = "maChuyenKhoa", required = false) String maChuyenKhoa,
                              Model model, RedirectAttributes ra) {
        
        if (maBenhNhan == null || maBenhNhan.trim().isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy thông tin bệnh nhân.");
            return "redirect:/admin/benh-nhan";
        }

        // 1. Kiểm tra bệnh nhân
        Optional<BenhNhan> bnOpt = benhNhanService.getBenhNhanById(maBenhNhan);
        if (!bnOpt.isPresent()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy bệnh nhân.");
            return "redirect:/admin/benh-nhan";
        }

        if (nhapVienNoiTruService.isBenhNhanDangNoiTru(maBenhNhan)) {
            ra.addFlashAttribute("errorMessage", "Bệnh nhân này đang điều trị nội trú, không thể nhập viện thêm.");
            return "redirect:/admin/benh-nhan";
        }
        
        if (maPhong == null || maPhong.trim().isEmpty()) {
            return returnToFormWithError(maBenhNhan, nhapVien, "Vui lòng chọn phòng bệnh.", model);
        }
        
        if (maGiuong == null) {
            return returnToFormWithError(maBenhNhan, nhapVien, "Vui lòng chọn giường bệnh.", model);
        }
        
        if (maChuyenKhoa == null || maChuyenKhoa.trim().isEmpty()) {
            return returnToFormWithError(maBenhNhan, nhapVien, "Vui lòng chọn khoa điều trị.", model);
        }

        if (maBacSiDieuTri == null || maBacSiDieuTri.trim().isEmpty()) {
            return returnToFormWithError(maBenhNhan, nhapVien, "Vui lòng chọn bác sĩ phụ trách.", model);
        }

        // 2. Kiểm tra phòng
        Optional<PhongBenh> pbOpt = phongBenhService.findById(maPhong);
        if (!pbOpt.isPresent()) {
            return returnToFormWithError(maBenhNhan, nhapVien, "Phòng bệnh không tồn tại.", model);
        }
        PhongBenh phong = pbOpt.get();
        
        // 3. Kiểm tra giường
        Optional<GiuongBenh> gbOpt = giuongBenhService.findById(maGiuong);
        if (!gbOpt.isPresent()) {
            return returnToFormWithError(maBenhNhan, nhapVien, "Giường bệnh không tồn tại.", model);
        }
        GiuongBenh giuong = gbOpt.get();
        
        // 4. Kiểm tra giường thuộc phòng
        if (!giuong.getPhongBenh().getMaPhong().equals(maPhong)) {
            return returnToFormWithError(maBenhNhan, nhapVien, "Giường không thuộc phòng đã chọn.", model);
        }
        
        // 5. Kiểm tra giường trống
        if (!"Trống".equals(giuong.getTrangThai())) {
            return returnToFormWithError(maBenhNhan, nhapVien, "Giường đã có người hoặc đang bảo trì.", model);
        }
        
        // 6. Tạo hồ sơ NhapVienNoiTru
        nhapVien.setBenhNhan(bnOpt.get());
        nhapVien.setPhongBenh(phong);
        nhapVien.setGiuongBenh(giuong);
        
        Optional<BacSi> bsOpt = bacSiService.getBacSiById(maBacSiDieuTri);
        bsOpt.ifPresent(nhapVien::setBacSiDieuTri);
        
        Optional<ChuyenKhoa> ckOpt = chuyenKhoaService.getChuyenKhoaById(maChuyenKhoa);
        ckOpt.ifPresent(nhapVien::setChuyenKhoa);
        
        // Tạo mã nội trú tự động nếu chưa có
        if (nhapVien.getMaNoiTru() == null || nhapVien.getMaNoiTru().isEmpty()) {
            String maNoiTru = "NT" + System.currentTimeMillis() / 1000;
            nhapVien.setMaNoiTru(maNoiTru);
        }
        
        nhapVienNoiTruService.save(nhapVien);
        
        // 7. Cập nhật GiuongBenh.TrangThai = "Có người"
        giuong.setTrangThai("Có người");
        giuongBenhService.save(giuong);
        
        // 8. Cập nhật lại SoGiuongDangDung của PhongBenh
        long soGiuongDangDung = giuongBenhService.countByPhongBenhAndTrangThai(phong, "Có người");
        phong.setSoGiuongDangDung((int) soGiuongDangDung);
        phongBenhService.save(phong);
        
        ra.addFlashAttribute("successMessage", "Lập phiếu nhập viện thành công cho bệnh nhân " + bnOpt.get().getHoTen());
        return "redirect:/admin/noi-tru";
    }

    @GetMapping("/xuat-vien/{maNoiTru}")
    public String formXuatVien(@PathVariable("maNoiTru") String maNoiTru, Model model, RedirectAttributes ra) {
        Optional<NhapVienNoiTru> ntOpt = nhapVienNoiTruService.findById(maNoiTru);
        if (!ntOpt.isPresent()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy hồ sơ nội trú.");
            return "redirect:/admin/noi-tru";
        }
        
        NhapVienNoiTru nt = ntOpt.get();
        if ("Đã xuất viện".equals(nt.getTrangThai())) {
            ra.addFlashAttribute("errorMessage", "Bệnh nhân này đã xuất viện trước đó.");
            return "redirect:/admin/noi-tru";
        }
        
        model.addAttribute("nhapVienNoiTru", nt);
        model.addAttribute("ngayXuatVienMacDinh", new Date());
        model.addAttribute("activeMenu", "noi-tru");
        
        return "admin/noi-tru/xuat-vien";
    }

    @PostMapping("/xuat-vien")
    @Transactional
    public String luuXuatVien(@RequestParam("maNoiTru") String maNoiTru,
                              @RequestParam("tinhTrangRaVien") String tinhTrangRaVien,
                              @RequestParam(value = "ngayXuatVien", required = false) Date ngayXuatVien,
                              RedirectAttributes ra) {
        Optional<NhapVienNoiTru> ntOpt = nhapVienNoiTruService.findById(maNoiTru);
        if (!ntOpt.isPresent()) {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy hồ sơ nội trú.");
            return "redirect:/admin/noi-tru";
        }
        
        NhapVienNoiTru nt = ntOpt.get();
        if ("Đã xuất viện".equals(nt.getTrangThai())) {
            ra.addFlashAttribute("errorMessage", "Hồ sơ này đã được xuất viện.");
            return "redirect:/admin/noi-tru";
        }
        
        // Cập nhật NhapVienNoiTru
        nt.setTrangThai("Đã xuất viện");
        nt.setTinhTrangRaVien(tinhTrangRaVien);
        nt.setNgayXuatVien(ngayXuatVien != null ? ngayXuatVien : new Date());
        nhapVienNoiTruService.save(nt);
        
        // Giải phóng GiuongBenh
        GiuongBenh giuong = nt.getGiuongBenh();
        if (giuong != null) {
            giuong.setTrangThai("Trống");
            giuongBenhService.save(giuong);
            
            // Cập nhật lại SoGiuongDangDung của PhongBenh
            PhongBenh phong = nt.getPhongBenh();
            if (phong != null) {
                long soGiuongDangDung = giuongBenhService.countByPhongBenhAndTrangThai(phong, "Có người");
                phong.setSoGiuongDangDung((int) soGiuongDangDung);
                phongBenhService.save(phong);
            }
        }
        
        ra.addFlashAttribute("successMessage", "Bệnh nhân " + nt.getBenhNhan().getHoTen() + " đã xuất viện thành công.");
        return "redirect:/admin/noi-tru";
    }
    
    private String returnToFormWithError(String maBenhNhan, NhapVienNoiTru nhapVien, String error, Model model) {
        model.addAttribute("errorMessage", error);
        model.addAttribute("benhNhan", benhNhanService.getBenhNhanById(maBenhNhan).get());
        model.addAttribute("nhapVienNoiTru", nhapVien);
        model.addAttribute("danhSachPhong", phongBenhService.findByTrangThai("Còn nhận"));
        model.addAttribute("danhSachGiuongTrong", giuongBenhService.findByTrangThai("Trống"));
        model.addAttribute("danhSachBacSi", bacSiService.getAllBacSi());
        model.addAttribute("danhSachChuyenKhoa", chuyenKhoaService.getAllChuyenKhoa());
        model.addAttribute("activeMenu", "benh-nhan");
        return "admin/noi-tru/nhap-vien";
    }
}
