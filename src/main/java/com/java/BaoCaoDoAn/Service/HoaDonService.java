package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.*;
import com.java.BaoCaoDoAn.Repository.HoaDonRepository;
import com.java.BaoCaoDoAn.Repository.ChiTietHoaDonRepository;
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
    private NhapVienNoiTruService nhapVienNoiTruService;

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
        ctGiuong.setKhoanMuc("Tiền giường bệnh (" + (phong != null ? phong.getMaPhong() : "N/A") + " - " + days + " ngày)");
        ctGiuong.setSoLuong((int) days);
        ctGiuong.setDonGia(giaGiuong);
        ctGiuong.setThanhTien(tienGiuong);
        ctGiuong.setGhiChu("Phòng " + (phong != null ? phong.getLoaiPhong() : "thường") + ", giường " + (giuong != null ? giuong.getSoGiuong() : ""));
        details.add(ctGiuong);

        // Phí khám & bác sĩ điều trị
        BacSi bacSi = nt.getBacSiDieuTri();
        BigDecimal phiKham = new BigDecimal("150000"); // Mặc định 150.000 đ
        if (bacSi != null && bacSi.getPhiKham() != null) {
            phiKham = bacSi.getPhiKham();
        }
        ChiTietHoaDon ctKham = new ChiTietHoaDon();
        ctKham.setHoaDon(hd);
        ctKham.setKhoanMuc("Phí khám & bác sĩ điều trị (" + (bacSi != null ? bacSi.getHoTen() : "Bác sĩ điều trị") + ")");
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

        // 7. Lưu HoaDon và các ChiTietHoaDon
        HoaDon savedHd = hoaDonRepository.save(hd);
        chiTietHoaDonService.saveAll(details);

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
}
