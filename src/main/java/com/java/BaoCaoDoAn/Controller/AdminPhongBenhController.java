package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.GiuongBenh;
import com.java.BaoCaoDoAn.Model.PhongBenh;
import com.java.BaoCaoDoAn.Service.BacSiService;
import com.java.BaoCaoDoAn.Service.ChuyenKhoaService;
import com.java.BaoCaoDoAn.Service.GiuongBenhService;
import com.java.BaoCaoDoAn.Service.PhongBenhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/phong-benh")
public class AdminPhongBenhController {

    @Autowired
    private PhongBenhService phongBenhService;

    @Autowired
    private GiuongBenhService giuongBenhService;

    @Autowired
    private BacSiService bacSiService;

    @Autowired
    private ChuyenKhoaService chuyenKhoaService;

    // MH19 - Danh sách phòng bệnh
    @GetMapping("")
    public String danhSachPhong(@RequestParam(value = "maChuyenKhoa", required = false) String maChuyenKhoa,
                                Model model) {
        List<PhongBenh> list;
        if (maChuyenKhoa != null && !maChuyenKhoa.isEmpty() && !"all".equals(maChuyenKhoa)) {
            list = phongBenhService.findByChuyenKhoa(maChuyenKhoa);
        } else {
            list = phongBenhService.findAll();
        }
        
        // Cập nhật số giường đang dùng thực tế từ bảng GiuongBenh cho từng phòng
        for (PhongBenh p : list) {
            long occupied = giuongBenhService.countByPhongBenhAndTrangThai(p, "Có người");
            p.setSoGiuongDangDung((int) occupied);
        }
        
        model.addAttribute("danhSachPhong", list);
        model.addAttribute("listChuyenKhoa", chuyenKhoaService.getAllChuyenKhoa());
        model.addAttribute("selectedMaChuyenKhoa", maChuyenKhoa);
        
        // Tính toán thống kê cho MH19 (Thống kê trên toàn bộ phòng, không theo filter - thông thường là vậy, 
        // nhưng nếu muốn thống kê theo filter thì tính từ `list`. Theo yêu cầu thì không nói rõ, 
        // nhưng card thống kê thường là toàn cục. Tuy nhiên, để khớp với danh sách đang hiển thị, 
        // có lẽ nên tính từ list nếu đang filter? 
        // Yêu cầu 1 nói "danh sách phòng chỉ hiển thị phòng thuộc chuyên khoa đó", 
        // không nói thay đổi card thống kê. Tôi sẽ giữ thống kê toàn cục để admin bao quát.)
        List<PhongBenh> allRooms = phongBenhService.findAll();
        long tongSoPhong = allRooms.size();
        long soPhongHoatDong = allRooms.stream().filter(p -> "Còn nhận".equals(p.getTrangThai())).count();
        long soPhongBaoTri = allRooms.stream().filter(p -> "Bảo trì".equals(p.getTrangThai())).count();
        
        model.addAttribute("tongSoPhong", tongSoPhong);
        model.addAttribute("soPhongHoatDong", soPhongHoatDong);
        model.addAttribute("soPhongBaoTri", soPhongBaoTri);
        
        return "admin/phong-benh/danh-sach";
    }

    // Form thêm phòng
    @GetMapping("/them")
    public String themPhong(Model model) {
        model.addAttribute("phongBenh", new PhongBenh());
        model.addAttribute("listBacSi", bacSiService.getAllBacSi());
        model.addAttribute("listChuyenKhoa", chuyenKhoaService.getAllChuyenKhoa());
        return "admin/phong-benh/form-phong";
    }

    // Form sửa phòng
    @GetMapping("/sua/{id}")
    public String suaPhong(@PathVariable("id") String id, Model model) {
        Optional<PhongBenh> phongOpt = phongBenhService.findById(id);
        if (phongOpt.isPresent()) {
            model.addAttribute("phongBenh", phongOpt.get());
            model.addAttribute("listBacSi", bacSiService.getAllBacSi());
            model.addAttribute("listChuyenKhoa", chuyenKhoaService.getAllChuyenKhoa());
            return "admin/phong-benh/form-phong";
        }
        return "redirect:/admin/phong-benh";
    }

    // Lưu phòng bệnh
    @PostMapping("/luu")
    public String luuPhong(@ModelAttribute("phongBenh") PhongBenh phongBenh,
                           @RequestParam("maChuyenKhoa") String maChuyenKhoa,
                           @RequestParam(value = "maBacSiPhuTrach", required = false) String maBacSiPhuTrach,
                           RedirectAttributes redirectAttributes) {
        
        boolean isNew = true;
        if (phongBenh.getMaPhong() != null) {
            phongBenh.setMaPhong(phongBenh.getMaPhong().trim());
            if (phongBenhService.findById(phongBenh.getMaPhong()).isPresent()) {
                isNew = false;
            }
        }
        
        // Kiểm tra giới hạn khi sửa phòng
        if (!isNew) {
            Optional<PhongBenh> oldPhongOpt = phongBenhService.findById(phongBenh.getMaPhong());
            if (oldPhongOpt.isPresent()) {
                PhongBenh oldPhong = oldPhongOpt.get();
                long soGiuongThucTe = giuongBenhService.countByPhongBenh(oldPhong);
                long soGiuongDangDungThucTe = giuongBenhService.countByPhongBenhAndTrangThai(oldPhong, "Có người");
                
                // Luôn tính lại số giường đang dùng thực tế, không tin form
                phongBenh.setSoGiuongDangDung((int) soGiuongDangDungThucTe);

                // 1. Số giường tối đa phải >= 1
                if (phongBenh.getSoGiuongToiDa() < 1) {
                    redirectAttributes.addFlashAttribute("error", "Số giường tối đa phải lớn hơn hoặc bằng 1.");
                    return "redirect:/admin/phong-benh/sua/" + phongBenh.getMaPhong();
                }

                // 2. Không cho SoGiuongToiDa nhỏ hơn SoGiuongDangDung
                if (phongBenh.getSoGiuongToiDa() < soGiuongDangDungThucTe) {
                    redirectAttributes.addFlashAttribute("error", "Số giường tối đa không được nhỏ hơn số giường đang dùng thực tế (" + soGiuongDangDungThucTe + ")");
                    return "redirect:/admin/phong-benh/sua/" + phongBenh.getMaPhong();
                }
                
                // 3. Không cho SoGiuongToiDa nhỏ hơn số giường thực tế đang có trong bảng GiuongBenh
                if (phongBenh.getSoGiuongToiDa() < soGiuongThucTe) {
                    redirectAttributes.addFlashAttribute("error", "Số giường tối đa không được nhỏ hơn số giường thực tế hiện có trong database (" + soGiuongThucTe + ")");
                    return "redirect:/admin/phong-benh/sua/" + phongBenh.getMaPhong();
                }
            }
        } else {
            // Thêm mới mặc định SoGiuongDangDung = 0
            phongBenh.setSoGiuongDangDung(0);
        }
        
        // Load Chuyên khoa thật từ DB
        chuyenKhoaService.getChuyenKhoaById(maChuyenKhoa).ifPresent(phongBenh::setChuyenKhoa);
        
        // Load Bác sĩ thật từ DB nếu có
        if (maBacSiPhuTrach != null && !maBacSiPhuTrach.isEmpty()) {
            bacSiService.getBacSiById(maBacSiPhuTrach).ifPresent(phongBenh::setBacSiPhuTrach);
        } else {
            phongBenh.setBacSiPhuTrach(null);
        }
        
        PhongBenh savedPhong = phongBenhService.save(phongBenh);
        
        // Nếu là thêm mới, tự động tạo giường
        if (isNew) {
            giuongBenhService.syncGiuongChoPhong(savedPhong.getMaPhong());
        }
        
        return "redirect:/admin/phong-benh";
    }

    // Đồng bộ giường cho phòng
    @GetMapping("/dong-bo-giuong/{maPhong}")
    public String dongBoGiuong(@PathVariable("maPhong") String maPhong) {
        giuongBenhService.syncGiuongChoPhong(maPhong);
        return "redirect:/admin/phong-benh";
    }

    // Xóa phòng bệnh
    @GetMapping("/xoa/{id}")
    public String xoaPhong(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        Optional<PhongBenh> phongOpt = phongBenhService.findById(id);
        if (phongOpt.isPresent()) {
            PhongBenh phong = phongOpt.get();
            long soGiuongThucTe = giuongBenhService.countByPhongBenh(phong);
            
            if (soGiuongThucTe == 0) {
                // Không có giường, xóa thẳng
                phongBenhService.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Đã xóa phòng bệnh thành công.");
            } else {
                long soGiuongCoNguoi = giuongBenhService.countByPhongBenhAndTrangThai(phong, "Có người");
                long soGiuongBaoTri = giuongBenhService.countByPhongBenhAndTrangThai(phong, "Bảo trì");
                
                if (soGiuongCoNguoi > 0) {
                    // Có giường có người, không xóa cứng, chuyển trạng thái
                    phong.setTrangThai("Ngừng sử dụng");
                    phongBenhService.save(phong);
                    redirectAttributes.addFlashAttribute("success", "Phòng đang có giường có người, hệ thống đã chuyển sang trạng thái 'Ngừng sử dụng'.");
                } else if (soGiuongBaoTri > 0) {
                    // Có giường bảo trì, không xóa cứng
                    phong.setTrangThai("Ngừng sử dụng");
                    phongBenhService.save(phong);
                    redirectAttributes.addFlashAttribute("success", "Phòng có giường đang bảo trì, hệ thống đã chuyển sang trạng thái 'Ngừng sử dụng'.");
                } else {
                    // Tất cả giường đều trống, xóa sạch giường rồi xóa phòng
                    giuongBenhService.deleteByPhongBenh(phong);
                    phongBenhService.deleteById(id);
                    redirectAttributes.addFlashAttribute("success", "Đã xóa phòng và các giường trống liên quan.");
                }
            }
        }
        return "redirect:/admin/phong-benh";
    }

    // MH20 - Chi tiết phòng và sơ đồ giường
    @GetMapping("/chi-tiet/{maPhong}")
    public String chiTietPhong(@PathVariable("maPhong") String maPhong, Model model) {
        Optional<PhongBenh> phongOpt = phongBenhService.findById(maPhong);
        if (phongOpt.isPresent()) {
            PhongBenh phong = phongOpt.get();
            List<GiuongBenh> listGiuong = giuongBenhService.findByPhongBenh(phong);
            
            model.addAttribute("phongBenh", phong);
            model.addAttribute("danhSachGiuong", listGiuong);
            model.addAttribute("giuongBenh", new GiuongBenh()); // Cho modal thêm giường
            
            // Tính toán thống kê cho MH20
            long soGiuongTrong = listGiuong.stream().filter(g -> "Trống".equals(g.getTrangThai())).count();
            long soGiuongCoNguoi = listGiuong.stream().filter(g -> "Có người".equals(g.getTrangThai())).count();
            long soGiuongBaoTri = listGiuong.stream().filter(g -> "Bảo trì".equals(g.getTrangThai())).count();
            
            model.addAttribute("soGiuongTrong", soGiuongTrong);
            model.addAttribute("soGiuongCoNguoi", soGiuongCoNguoi);
            model.addAttribute("soGiuongBaoTri", soGiuongBaoTri);
            
            return "admin/phong-benh/chi-tiet";
        }
        return "redirect:/admin/phong-benh";
    }

    // Quản lý giường - Lưu/Sửa (MH20A)
    @PostMapping("/giuong/luu")
    public String luuGiuong(@ModelAttribute("giuongBenh") GiuongBenh giuongBenh, 
                            @RequestParam("maPhong") String maPhong) {
        Optional<PhongBenh> phongOpt = phongBenhService.findById(maPhong);
        if (phongOpt.isPresent()) {
            giuongBenh.setPhongBenh(phongOpt.get());
            giuongBenhService.save(giuongBenh);
        }
        return "redirect:/admin/phong-benh/chi-tiet/" + maPhong;
    }

    // Quản lý giường - Xóa
    @GetMapping("/giuong/xoa/{id}")
    public String xoaGiuong(@PathVariable("id") Integer id, 
                            @RequestParam("maPhong") String maPhong) {
        giuongBenhService.deleteById(id);
        return "redirect:/admin/phong-benh/chi-tiet/" + maPhong;
    }
}
