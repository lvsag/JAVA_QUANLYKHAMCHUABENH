package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.BacSi;
import com.java.BaoCaoDoAn.Service.BacSiService;
import com.java.BaoCaoDoAn.Service.ChuyenKhoaService;
import com.java.BaoCaoDoAn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/bac-si")
public class AdminBacSiController {

    @Autowired
    private BacSiService bacSiService;

    @Autowired
    private ChuyenKhoaService chuyenKhoaService;
    
    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private com.java.BaoCaoDoAn.Repository.LichLamViecRepository lichLamViecRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @GetMapping
    public String danhSachBacSi(@RequestParam(value = "selectedMaBacSi", required = false) String selectedMaBacSi, Model model) {
        // Lấy danh sách bác sĩ kèm lịch làm việc hôm nay
        java.util.List<java.util.Map<String, Object>> danhSachBS = jdbcTemplate.queryForList(
            "SELECT bs.MaBacSi, bs.HoTen, ck.TenChuyenKhoa, bs.TrangThai, " +
            "(SELECT CaLamViec FROM LichLamViec WHERE MaBacSi = bs.MaBacSi AND NgayTrongTuan = DAYOFWEEK(CURRENT_DATE) LIMIT 1) as CaHomNay, " +
            "(SELECT SoSlotToiDa FROM LichLamViec WHERE MaBacSi = bs.MaBacSi AND NgayTrongTuan = DAYOFWEEK(CURRENT_DATE) LIMIT 1) as SlotHomNay " +
            "FROM BacSi bs " +
            "LEFT JOIN ChuyenKhoa ck ON bs.MaChuyenKhoa = ck.MaChuyenKhoa"
        );

        // Chuẩn hóa key của Map để tránh lỗi phân biệt chữ hoa chữ thường giữa các HĐH/Database driver
        java.util.List<java.util.Map<String, Object>> standardizedList = new java.util.ArrayList<>();
        for (java.util.Map<String, Object> row : danhSachBS) {
            java.util.Map<String, Object> standardRow = new java.util.HashMap<>();
            for (java.util.Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (key.equalsIgnoreCase("MaBacSi")) {
                    standardRow.put("maBacSi", entry.getValue());
                    standardRow.put("MaBacSi", entry.getValue());
                } else if (key.equalsIgnoreCase("HoTen")) {
                    standardRow.put("hoTen", entry.getValue());
                    standardRow.put("HoTen", entry.getValue());
                } else if (key.equalsIgnoreCase("TenChuyenKhoa")) {
                    standardRow.put("tenChuyenKhoa", entry.getValue());
                    standardRow.put("TenChuyenKhoa", entry.getValue());
                } else if (key.equalsIgnoreCase("TrangThai")) {
                    standardRow.put("trangThai", entry.getValue());
                    standardRow.put("TrangThai", entry.getValue());
                } else if (key.equalsIgnoreCase("CaHomNay")) {
                    standardRow.put("caHomNay", entry.getValue());
                    standardRow.put("CaHomNay", entry.getValue());
                } else if (key.equalsIgnoreCase("SlotHomNay")) {
                    standardRow.put("slotHomNay", entry.getValue());
                    standardRow.put("SlotHomNay", entry.getValue());
                } else {
                    standardRow.put(key, entry.getValue());
                }
            }
            standardizedList.add(standardRow);
        }

        model.addAttribute("danhSach", standardizedList);
        if (selectedMaBacSi == null && !standardizedList.isEmpty()) {
            Object val = standardizedList.get(0).get("maBacSi");
            if (val != null) {
                selectedMaBacSi = val.toString();
            }
        }
        
        model.addAttribute("selectedMaBacSi", selectedMaBacSi);

        if (selectedMaBacSi != null) {
            java.util.List<com.java.BaoCaoDoAn.Model.LichLamViec> lichTuan = lichLamViecRepository.findByBacSi_MaBacSi(selectedMaBacSi);
            
            // Đảm bảo có đủ 7 ngày (Thứ 2 = 2, ..., CN = 8)
            java.util.Map<Integer, com.java.BaoCaoDoAn.Model.LichLamViec> lichMap = new java.util.HashMap<>();
            for (com.java.BaoCaoDoAn.Model.LichLamViec l : lichTuan) {
                lichMap.put(l.getNgayTrongTuan(), l);
            }
            
            java.util.List<com.java.BaoCaoDoAn.Model.LichLamViec> hienThiLich = new java.util.ArrayList<>();
            for (int i = 2; i <= 8; i++) {
                if (lichMap.containsKey(i)) {
                    hienThiLich.add(lichMap.get(i));
                } else {
                    com.java.BaoCaoDoAn.Model.LichLamViec l = new com.java.BaoCaoDoAn.Model.LichLamViec();
                    l.setNgayTrongTuan(i);
                    l.setCaLamViec("Nghỉ");
                    l.setSoSlotToiDa(0);
                    hienThiLich.add(l);
                }
            }
            model.addAttribute("lichTuan", hienThiLich);
        }

        return "admin/bac-si/danh-sach";
    }

    @PostMapping("/phan-lich")
    public String phanLich(@RequestParam("maBacSi") String maBacSi, 
                           @RequestParam("ngayTrongTuan") java.util.List<Integer> ngayTrongTuan,
                           @RequestParam("caLamViec") java.util.List<String> caLamViec,
                           @RequestParam(value = "gioBatDau", required = false) java.util.List<String> gioBatDauStr,
                           @RequestParam(value = "gioKetThuc", required = false) java.util.List<String> gioKetThucStr,
                           @RequestParam("soSlotToiDa") java.util.List<Integer> soSlotToiDa) {
        
        BacSi bs = bacSiService.getBacSiById(maBacSi).orElse(null);
        if (bs != null) {
            // Xóa lịch cũ
            java.util.List<com.java.BaoCaoDoAn.Model.LichLamViec> oldLich = lichLamViecRepository.findByBacSi_MaBacSi(maBacSi);
            lichLamViecRepository.deleteAll(oldLich);
            
            // Lưu lịch mới
            for (int i = 0; i < ngayTrongTuan.size(); i++) {
                if (!"Nghỉ".equals(caLamViec.get(i))) {
                    com.java.BaoCaoDoAn.Model.LichLamViec newLich = new com.java.BaoCaoDoAn.Model.LichLamViec();
                    newLich.setBacSi(bs);
                    newLich.setNgayTrongTuan(ngayTrongTuan.get(i));
                    newLich.setCaLamViec(caLamViec.get(i));
                    newLich.setSoSlotToiDa(soSlotToiDa.get(i));
                    
                    try {
                        if (gioBatDauStr != null && i < gioBatDauStr.size() && !gioBatDauStr.get(i).isEmpty()) {
                            String timeStr = gioBatDauStr.get(i);
                            if (timeStr.length() == 5) timeStr += ":00"; // convert HH:mm to HH:mm:ss
                            newLich.setGioBatDau(java.sql.Time.valueOf(timeStr));
                        } else {
                            newLich.setGioBatDau(java.sql.Time.valueOf("07:30:00"));
                        }
                        
                        if (gioKetThucStr != null && i < gioKetThucStr.size() && !gioKetThucStr.get(i).isEmpty()) {
                            String timeStr = gioKetThucStr.get(i);
                            if (timeStr.length() == 5) timeStr += ":00";
                            newLich.setGioKetThuc(java.sql.Time.valueOf(timeStr));
                        } else {
                            newLich.setGioKetThuc(java.sql.Time.valueOf("11:30:00"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        newLich.setGioBatDau(java.sql.Time.valueOf("07:30:00"));
                        newLich.setGioKetThuc(java.sql.Time.valueOf("11:30:00"));
                    }
                    
                    lichLamViecRepository.save(newLich);
                }
            }
        }
        
        return "redirect:/admin/bac-si?selectedMaBacSi=" + maBacSi;
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        BacSi bs = new BacSi();
        bs.setChuyenKhoa(new com.java.BaoCaoDoAn.Model.ChuyenKhoa());
        bs.setTaiKhoan(new com.java.BaoCaoDoAn.Model.TaiKhoan());
        model.addAttribute("bacSi", bs);
        model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
        model.addAttribute("taiKhoans", taiKhoanService.getAllTaiKhoan()); 
        return "admin/bac-si/form";
    }

    @PostMapping("/them")
    public String luuBacSi(@ModelAttribute BacSi bacSi) {
        bacSiService.saveBacSi(bacSi);
        return "redirect:/admin/bac-si";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable String id, Model model) {
        bacSiService.getBacSiById(id).ifPresent(bs -> {
            if (bs.getChuyenKhoa() == null) bs.setChuyenKhoa(new com.java.BaoCaoDoAn.Model.ChuyenKhoa());
            if (bs.getTaiKhoan() == null) bs.setTaiKhoan(new com.java.BaoCaoDoAn.Model.TaiKhoan());
            model.addAttribute("bacSi", bs);
        });
        model.addAttribute("chuyenKhoas", chuyenKhoaService.getAllChuyenKhoa());
        model.addAttribute("taiKhoans", taiKhoanService.getAllTaiKhoan());
        return "admin/bac-si/form";
    }

    @PostMapping("/sua/{id}")
    public String capNhatBacSi(@PathVariable String id, @ModelAttribute BacSi bacSi) {
        bacSi.setMaBacSi(id);
        bacSiService.saveBacSi(bacSi);
        return "redirect:/admin/bac-si";
    }

    @GetMapping("/xoa/{id}")
    public String xoaBacSi(@PathVariable String id, @RequestHeader(value = "referer", required = false) String referer) {
        bacSiService.getBacSiById(id).ifPresent(bs -> {
            bs.setTrangThai("Đã nghỉ việc"); // Soft delete
            bacSiService.saveBacSi(bs);
        });
        
        // Return to the previous page (could be ChuyenKhoa list or BacSi list)
        if (referer != null) {
            return "redirect:" + referer;
        }
        return "redirect:/admin/bac-si";
    }
}
