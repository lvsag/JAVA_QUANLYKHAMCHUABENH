package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.*;
import com.java.BaoCaoDoAn.Repository.HoaDonRepository;
import com.java.BaoCaoDoAn.Repository.ChiTietHoaDonRepository;
import com.java.BaoCaoDoAn.Repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    @Autowired
    private NhapVienNoiTruService nhapVienNoiTruService;

    @Autowired
    private KhamBenhService khamBenhService;

    @Autowired
    private ChiTietHoaDonService chiTietHoaDonService;

    public List<HoaDon> findAll() {
        return hoaDonRepository.findAll();
    }

    public Optional<HoaDon> findById(String maHoaDon) {
        return hoaDonRepository.findById(maHoaDon);
    }

    public HoaDon save(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    public Optional<HoaDon> findByMaNoiTru(String maNoiTru) {
        return hoaDonRepository.findByNhapVienNoiTru_MaNoiTru(maNoiTru);
    }

    public Optional<HoaDon> findByMaLichHen(String maLichHen) {
        return hoaDonRepository.findByMaLichHen(maLichHen);
    }

    public boolean existsByMaNoiTru(String maNoiTru) {
        return hoaDonRepository.findByNhapVienNoiTru_MaNoiTru(maNoiTru).isPresent();
    }

    public String generateMaHoaDon() {
        return "HD" + System.currentTimeMillis();
    }

    public HoaDon taoHoaDonTuNoiTru(String maNoiTru) {
        // 1. Kiểm tra đã có hóa đơn theo maNoiTru chưa
        Optional<HoaDon> existingOpt = findByMaNoiTru(maNoiTru);
        if (existingOpt.isPresent()) {
            return existingOpt.get();
        }

        // 2. Tìm NhapVienNoiTru theo maNoiTru
        Optional<NhapVienNoiTru> ntOpt = nhapVienNoiTruService.findById(maNoiTru);
        if (!ntOpt.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy hồ sơ nội trú với mã: " + maNoiTru);
        }
        NhapVienNoiTru nt = ntOpt.get();

        // 3. Chỉ tạo hóa đơn nếu hồ sơ nội trú đã "Đã xuất viện"
        if (!"Đã xuất viện".equals(nt.getTrangThai())) {
            throw new IllegalStateException("Hồ sơ nội trú chưa xuất viện. Vui lòng lập phiếu xuất viện trước.");
        }

        // 4. Tạo HoaDon
        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(generateMaHoaDon());
        hd.setBenhNhan(nt.getBenhNhan());
        hd.setNhapVienNoiTru(nt);
        hd.setTrangThai("Chờ thanh toán");
        hd.setGiamBHYT(BigDecimal.ZERO);
        hd.setGiamUuDai(BigDecimal.ZERO);
        hd.setSoTienDaThanhToan(BigDecimal.ZERO);

        List<ChiTietHoaDon> details = new ArrayList<>();

        // 5. Tạo ChiTietHoaDon
        // Tiền giường/phòng
        PhongBenh phong = nt.getPhongBenh();
        GiuongBenh giuong = nt.getGiuongBenh();
        BigDecimal giaGiuong = new BigDecimal("300000"); // Mặc định 300.000 đ
        if (phong != null && phong.getLoaiPhong() != null && phong.getLoaiPhong().toUpperCase().contains("VIP")) {
            giaGiuong = new BigDecimal("500000"); // VIP 500.000 đ
        }

        Date ngayNhap = nt.getNgayNhapVien();
        Date ngayXuat = nt.getNgayXuatVien();
        if (ngayXuat == null) {
            ngayXuat = new Date();
        }
        long diff = ngayXuat.getTime() - ngayNhap.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        if (days <= 0) {
            days = 1;
        }

        BigDecimal tienGiuong = giaGiuong.multiply(new BigDecimal(days));
        ChiTietHoaDon ctGiuong = new ChiTietHoaDon();
        ctGiuong.setHoaDon(hd);
        ctGiuong.setKhoanMuc(
                "Tiền giường bệnh (" + (phong != null ? phong.getMaPhong() : "N/A") + " - " + days + " ngày)");
        ctGiuong.setSoLuong((int) days);
        ctGiuong.setDonGia(giaGiuong);
        ctGiuong.setThanhTien(tienGiuong);
        ctGiuong.setGhiChu("Phòng " + (phong != null ? phong.getLoaiPhong() : "thường") + ", giường "
                + (giuong != null ? giuong.getSoGiuong() : ""));
        details.add(ctGiuong);

        // Phí khám & bác sĩ điều trị
        BacSi bacSi = nt.getBacSiDieuTri();
        BigDecimal phiKham = new BigDecimal("150000"); // Mặc định 150.000 đ
        if (bacSi != null && bacSi.getPhiKham() != null) {
            phiKham = bacSi.getPhiKham();
        }
        ChiTietHoaDon ctKham = new ChiTietHoaDon();
        ctKham.setHoaDon(hd);
        ctKham.setKhoanMuc(
                "Phí khám & bác sĩ điều trị (" + (bacSi != null ? bacSi.getHoTen() : "Bác sĩ điều trị") + ")");
        ctKham.setSoLuong(1);
        ctKham.setDonGia(phiKham);
        ctKham.setThanhTien(phiKham);
        ctKham.setGhiChu("Bác sĩ phụ trách chính");
        details.add(ctKham);

        // Thuốc & vật tư y tế tạm tính
        BigDecimal tienThuoc = new BigDecimal("350000");
        ChiTietHoaDon ctThuoc = new ChiTietHoaDon();
        ctThuoc.setHoaDon(hd);
        ctThuoc.setKhoanMuc("Thuốc & vật tư y tế tiêu hao (Tạm tính)");
        ctThuoc.setSoLuong(1);
        ctThuoc.setDonGia(tienThuoc);
        ctThuoc.setThanhTien(tienThuoc);
        ctThuoc.setGhiChu("Dựa trên hồ sơ sử dụng thuốc");
        details.add(ctThuoc);

        // Dịch vụ xét nghiệm & cận lâm sàng tạm tính
        BigDecimal tienDichVu = new BigDecimal("500000");
        ChiTietHoaDon ctDichVu = new ChiTietHoaDon();
        ctDichVu.setHoaDon(hd);
        ctDichVu.setKhoanMuc("Dịch vụ xét nghiệm & cận lâm sàng (Tạm tính)");
        ctDichVu.setSoLuong(1);
        ctDichVu.setDonGia(tienDichVu);
        ctDichVu.setThanhTien(tienDichVu);
        ctDichVu.setGhiChu("Xét nghiệm mẫu & cận lâm sàng");
        details.add(ctDichVu);

        // 6. Tổng tiền
        BigDecimal tongTien = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : details) {
            tongTien = tongTien.add(ct.getThanhTien());
        }
        hd.setTongTien(tongTien);
        hd.setTongCanThanhToan(tongTien); // TongCanThanhToan = TongTien - GiamBHYT - GiamUuDai
        hd.setChiTietHoaDons(details);
        // 7. Lưu HoaDon (JPA sẽ tự động lưu ChiTietHoaDon nhờ CascadeType.ALL)
        HoaDon savedHd = hoaDonRepository.save(hd);

        return savedHd;
    }

    public HoaDon taoHoaDonTuPhieuKham(String maPhieuKham) {
        // 1. Tìm PhieuKham theo maPhieuKham
        Optional<PhieuKham> pkOpt = khamBenhService.getPhieuKham(maPhieuKham);
        if (!pkOpt.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy phiếu khám với mã: " + maPhieuKham);
        }
        PhieuKham pk = pkOpt.get();

        // 2. Lấy mã lịch hẹn để tránh tạo trùng hóa đơn
        String maLichHen = null;
        if (pk.getLichHen() != null) {
            maLichHen = pk.getLichHen().getMaLichHen();
            Optional<HoaDon> existingOpt = hoaDonRepository.findByMaLichHen(maLichHen);
            if (existingOpt.isPresent()) {
                return existingOpt.get(); // Nếu đã tạo hóa đơn cho lịch hẹn này rồi thì trả về
            }
        } else {
            // Nếu phiếu khám không có lịch hẹn, lấy theo mã phiếu khám (lưu tạm vào maLichHen)
            maLichHen = pk.getMaPhieuKham();
            Optional<HoaDon> existingOpt = hoaDonRepository.findByMaLichHen(maLichHen);
            if (existingOpt.isPresent()) {
                return existingOpt.get();
            }
        }

        // 3. Khởi tạo HoaDon
        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(generateMaHoaDon());
        hd.setBenhNhan(pk.getBenhNhan());
        hd.setMaLichHen(maLichHen);
        hd.setTrangThai("Chờ thanh toán");
        hd.setGiamBHYT(BigDecimal.ZERO);
        hd.setGiamUuDai(BigDecimal.ZERO);
        hd.setSoTienDaThanhToan(BigDecimal.ZERO);

        List<ChiTietHoaDon> details = new ArrayList<>();

        // 4. Tạo ChiTietHoaDon cho Tiền Công Khám
        BigDecimal phiKham = new BigDecimal("150000"); // Mặc định 150.000đ
        if (pk.getBacSi() != null && pk.getBacSi().getPhiKham() != null) {
            phiKham = pk.getBacSi().getPhiKham();
        } else if (pk.getLichHen() != null && pk.getLichHen().getPhiDuKien() != null) {
            phiKham = pk.getLichHen().getPhiDuKien();
        }
        ChiTietHoaDon ctKham = new ChiTietHoaDon();
        ctKham.setHoaDon(hd);
        ctKham.setKhoanMuc("Phí khám bệnh (" + (pk.getBacSi() != null ? pk.getBacSi().getHoTen() : "Bác sĩ") + ")");
        ctKham.setSoLuong(1);
        ctKham.setDonGia(phiKham);
        ctKham.setThanhTien(phiKham);
        ctKham.setGhiChu("Mã phiếu: " + pk.getMaPhieuKham());
        details.add(ctKham);

        // 5. Tính tiền Dịch vụ Cận lâm sàng (Từ PhieuChiDinh)
        if (pk.getPhieuChiDinhs() != null) {
            for (PhieuChiDinh pcd : pk.getPhieuChiDinhs()) {
                if (pcd.getChiTietChiDinhs() != null) {
                    for (ChiTietChiDinh ctcd : pcd.getChiTietChiDinhs()) {
                        ChiTietHoaDon ctDichVu = new ChiTietHoaDon();
                        ctDichVu.setHoaDon(hd);
                        ctDichVu.setKhoanMuc("Dịch vụ CLS: " + (ctcd.getDichVu() != null ? ctcd.getDichVu().getTenDichVu() : "N/A"));
                        ctDichVu.setSoLuong(1);
                        BigDecimal giaDichVu = ctcd.getDichVu() != null && ctcd.getDichVu().getGiaDichVu() != null ? ctcd.getDichVu().getGiaDichVu() : BigDecimal.ZERO;
                        ctDichVu.setDonGia(giaDichVu);
                        ctDichVu.setThanhTien(giaDichVu);
                        ctDichVu.setGhiChu("Phiếu CĐ: " + pcd.getMaPhieuChiDinh());
                        details.add(ctDichVu);
                    }
                }
            }
        }

        // 6. Tính tiền Thuốc (Từ DonThuoc)
        if (pk.getDonThuocs() != null) {
            for (DonThuoc dt : pk.getDonThuocs()) {
                if (dt.getChiTietDonThuocs() != null) {
                    for (ChiTietDonThuoc ctdt : dt.getChiTietDonThuocs()) {
                        ChiTietHoaDon ctThuoc = new ChiTietHoaDon();
                        ctThuoc.setHoaDon(hd);
                        ctThuoc.setKhoanMuc("Thuốc: " + (ctdt.getThuoc() != null ? ctdt.getThuoc().getTenThuoc() : "N/A"));
                        ctThuoc.setSoLuong(ctdt.getSoLuong() != null ? ctdt.getSoLuong() : 1);
                        BigDecimal giaThuoc = ctdt.getThuoc() != null && ctdt.getThuoc().getGiaBan() != null ? ctdt.getThuoc().getGiaBan() : BigDecimal.ZERO;
                        ctThuoc.setDonGia(giaThuoc);
                        ctThuoc.setThanhTien(giaThuoc.multiply(new BigDecimal(ctThuoc.getSoLuong())));
                        ctThuoc.setGhiChu("Liều dùng: " + ctdt.getLieuDung());
                        details.add(ctThuoc);
                    }
                }
            }
        }

        // 7. Tổng tiền
        BigDecimal tongTien = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : details) {
            tongTien = tongTien.add(ct.getThanhTien());
        }
        hd.setTongTien(tongTien);
        hd.setTongCanThanhToan(tongTien);
        hd.setChiTietHoaDons(details);

        // 8. Lưu HoaDon (JPA sẽ tự động lưu ChiTietHoaDon nhờ CascadeType.ALL)
        HoaDon savedHd = hoaDonRepository.save(hd);

        return savedHd;
    }

    public void deleteHoaDonVaChiTiet(String maHoaDon) {
        Optional<HoaDon> hdOpt = hoaDonRepository.findById(maHoaDon);
        if (hdOpt.isPresent()) {
            HoaDon hd = hdOpt.get();
            // Xóa chi tiết hóa đơn trước
            List<ChiTietHoaDon> details = chiTietHoaDonRepository.findByHoaDon_MaHoaDon(maHoaDon);
            if (details != null && !details.isEmpty()) {
                chiTietHoaDonRepository.deleteAll(details);
            }
            // Xóa hóa đơn sau
            hoaDonRepository.delete(hd);
        } else {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn có mã: " + maHoaDon);
        }
    }

    public void apDungKhuyenMai(String maHoaDon, String maCode) throws Exception {
        HoaDon hd = hoaDonRepository.findById(maHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn có mã: " + maHoaDon));

        if ("Đã thanh toán".equals(hd.getTrangThai())) {
            throw new IllegalStateException("Hóa đơn đã được thanh toán, không thể áp dụng khuyến mãi.");
        }

        if (maCode == null || maCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khuyến mãi không được để trống.");
        }

        String code = maCode.trim().toUpperCase();
        KhuyenMai km = khuyenMaiRepository.findByMaCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Mã khuyến mãi không tồn tại."));

        if (!"Hoạt động".equals(km.getTrangThai())) {
            throw new IllegalStateException("Mã khuyến mãi này hiện đang bị khóa hoặc ngưng hoạt động.");
        }

        // Validate dates
        Date now = new Date();
        if (km.getNgayBatDau() != null && now.before(km.getNgayBatDau())) {
            throw new IllegalStateException("Mã khuyến mãi chưa đến thời gian áp dụng.");
        }

        java.sql.Date sqlToday = new java.sql.Date(System.currentTimeMillis());
        if (km.getNgayKetThuc() != null && sqlToday.after(km.getNgayKetThuc())) {
            throw new IllegalStateException("Mã khuyến mãi đã hết hạn sử dụng.");
        }

        // Validate usage
        if (km.getSoLuotToiDa() != null && km.getSoLuotDaDung() != null && km.getSoLuotDaDung() >= km.getSoLuotToiDa()) {
            throw new IllegalStateException("Mã khuyến mãi đã đạt số lượt sử dụng tối đa.");
        }

        // Apply discount
        BigDecimal discount = km.getGiaTriGiam();
        if (discount == null) {
            discount = BigDecimal.ZERO;
        }

        if (discount.compareTo(hd.getTongTien()) > 0) {
            discount = hd.getTongTien();
        }

        hd.setGiamUuDai(discount);

        BigDecimal giamBHYT = hd.getGiamBHYT() != null ? hd.getGiamBHYT() : BigDecimal.ZERO;
        BigDecimal tempTotal = hd.getTongTien().subtract(giamBHYT).subtract(discount);
        if (tempTotal.compareTo(BigDecimal.ZERO) < 0) {
            tempTotal = BigDecimal.ZERO;
        }
        hd.setTongCanThanhToan(tempTotal);

        // Update usage count
        int currentUsage = km.getSoLuotDaDung() != null ? km.getSoLuotDaDung() : 0;
        km.setSoLuotDaDung(currentUsage + 1);

        // Save
        hoaDonRepository.save(hd);
        khuyenMaiRepository.save(km);
    }
}
