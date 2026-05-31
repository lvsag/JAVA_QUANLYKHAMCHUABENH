package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.DTO.DatLichRequestDTO;
import com.java.BaoCaoDoAn.Model.*;
import com.java.BaoCaoDoAn.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LichHenService {

    private final LichHenRepository    lichHenRepository;
    private final BenhNhanRepository   benhNhanRepository;
    private final BacSiRepository      bacSiRepository;
    private final ChuyenKhoaRepository chuyenKhoaRepository;
    private final KhungGioKhamRepository khungGioKhamRepository;

    private final DichVuRepository     dichVuRepository;
    private final ChiTietLichHenRepository chiTietLichHenRepository;
    private final KhuyenMaiRepository  khuyenMaiRepository;
    private final HoaDonRepository     hoaDonRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final KhuyenMaiService     khuyenMaiService;

    // ------------------------------------------------------------------
    // HÀM TÍNH TỔNG TIỀN (Ánh xạ cho màn hình Thanh Toán và Bước 1)
    // ------------------------------------------------------------------
    // ------------------------------------------------------------------
    // HÀM TÍNH TỔNG TIỀN (Gộp chung Dịch vụ + Phí Bác sĩ)
    // ------------------------------------------------------------------
    public double tinhTongTien(List<String> maDichVus, String voucherCode, String maBacSi) {
        double subtotal = 0.0;

        // 1. Cộng tiền Dịch vụ
        if (maDichVus != null && !maDichVus.isEmpty()) {
            List<DichVu> dichVus = dichVuRepository.findAllById(maDichVus);
            for (DichVu dv : dichVus) {
                if (dv.getGiaDichVu() != null) {
                    subtotal += dv.getGiaDichVu().doubleValue();
                }
            }
        }

        // 2. Cộng phí khám của Bác sĩ
        if (maBacSi != null && !maBacSi.isEmpty()) {
            BacSi bs = bacSiRepository.findById(maBacSi).orElse(null);
            if (bs != null && bs.getPhiKham() != null) {
                subtotal += bs.getPhiKham().doubleValue();
            }
        }

        // 3. Áp dụng mã khuyến mãi
        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            double discount = khuyenMaiService.tinhGiamGiaHopLe(voucherCode, subtotal);
            subtotal = Math.max(0.0, subtotal - discount);
        }
        return subtotal;
    }

    // ------------------------------------------------------------------
    // 1. ĐẶT LỊCH HẸN (POST /dat-lich)
    // ------------------------------------------------------------------
    @Transactional
    public LichHen datLich(DatLichRequestDTO req, String maBenhNhan) throws Exception {

        // === Validate: Slot còn chỗ không? ===
        if (req.getMaKhungGio() != null) {
            long booked = lichHenRepository.countBookedSlot(req.getMaKhungGio());
            if (booked > 0) {
                throw new Exception("Khung giờ này đã được đặt. Vui lòng chọn khung giờ khác.");
            }
        }

        // === Lấy entities ===
        BenhNhan benhNhan = benhNhanRepository.findById(maBenhNhan)
                .orElseThrow(() -> new Exception("Không tìm thấy bệnh nhân: " + maBenhNhan));

        BacSi bacSi = null;
        if (req.getMaBacSi() != null && !req.getMaBacSi().isEmpty()) {
            bacSi = bacSiRepository.findById(req.getMaBacSi()).orElse(null);
        }

        ChuyenKhoa chuyenKhoa = null;
        if (req.getMaChuyenKhoa() != null && !req.getMaChuyenKhoa().isEmpty()) {
            chuyenKhoa = chuyenKhoaRepository.findById(req.getMaChuyenKhoa()).orElse(null);
        }

        // === Sinh mã lịch hẹn dạng LH-yyyyMMdd-XXXX ===
        String maLichHen = genMaLichHen();

        // === Parse ngày/giờ an toàn ===
        Date ngayHen;
        Time gioHen = Time.valueOf("08:00:00");
        try {
            ngayHen = new SimpleDateFormat("yyyy-MM-dd").parse(req.getNgayHen());
        } catch (Exception e) {
            throw new Exception("Định dạng ngày/giờ không hợp lệ. Hãy kiểm tra lại form.");
        }

        // === KIỂM TRA TRÙNG LỊCH BẰNG PESSIMISTIC LOCKING ===
        if (req.getMaKhungGio() != null) {
            KhungGioKham slot = khungGioKhamRepository.findByIdWithLock(req.getMaKhungGio())
                    .orElseThrow(() -> new Exception("Không tìm thấy Khung giờ."));
            if (!"Còn chỗ".equals(slot.getTrangThai())) {
                throw new Exception("Rất tiếc, khung giờ này vừa có người đặt. Vui lòng chọn khung giờ khác.");
            }
            
            // Gán giờ thực tế từ Database
            if (slot.getGioBatDau() != null) {
                gioHen = java.sql.Time.valueOf(slot.getGioBatDau());
            }

            // Đánh dấu là đã đặt
            slot.setTrangThai("Đã đặt");
            khungGioKhamRepository.save(slot);
        }

        Date ngaySinh = null;
        if (req.getNgaySinhNguoiKham() != null && !req.getNgaySinhNguoiKham().isEmpty()) {
            try {
                // Sửa thành yyyy-MM-dd vì HTML <input type="date"> gửi định dạng này
                ngaySinh = new SimpleDateFormat("yyyy-MM-dd").parse(req.getNgaySinhNguoiKham());
            } catch (Exception ignored) {
                // Thử lại định dạng cũ phòng trường hợp có form nào đó dùng dd/MM/yyyy
                try {
                    ngaySinh = new SimpleDateFormat("dd/MM/yyyy").parse(req.getNgaySinhNguoiKham());
                } catch (Exception ignored2) {}
            }
        }

        // === TÍNH TOÁN TIỀN LƯU VÀO DATABASE ===
        double tongTien = tinhTongTien(req.getDanhSachMaDichVu(), req.getMaKhuyenMai(), req.getMaBacSi());
        // Nếu Bác sĩ có phí khám riêng thì cộng thêm vào

        // === Tạo entity ===
        LichHen lichHen = new LichHen();
        lichHen.setMaLichHen(maLichHen);
        lichHen.setBenhNhan(benhNhan);
        lichHen.setBacSi(bacSi);
        lichHen.setChuyenKhoa(chuyenKhoa);
        lichHen.setNgayHen(ngayHen);
        lichHen.setGioHen(gioHen);
        lichHen.setMaKhungGio(req.getMaKhungGio());
        lichHen.setHoTenNguoiKham(req.getHoTenNguoiKham());
        lichHen.setSdtNguoiKham(req.getSdtNguoiKham());
        lichHen.setNgaySinhNguoiKham(ngaySinh);
        lichHen.setEmailXacNhan(req.getEmailXacNhan());
        lichHen.setGhiChuTrieuChung(req.getGhiChuTrieuChung());
        lichHen.setHoTenNguoiThanLH(req.getHoTenNguoiThanLH());
        lichHen.setSdtNguoiThanLH(req.getSdtNguoiThanLH());
        lichHen.setCoBHYT(req.getSoBHYT() != null && !req.getSoBHYT().isEmpty());
        lichHen.setSoTheBHYT(req.getSoBHYT());
        lichHen.setPhuongThucThanhToan(req.getPhuongThucThanhToan());
        lichHen.setTrangThai("Chờ xác nhận");

        // ĐÃ SỬA: Set trường PhiDuKien lưu thẳng vào CSDL
        lichHen.setPhiDuKien(BigDecimal.valueOf(tongTien));

        LichHen saved = lichHenRepository.save(lichHen);

        if (req.getDanhSachMaDichVu() != null && !req.getDanhSachMaDichVu().isEmpty()) {
            List<DichVu> dichVus = dichVuRepository.findAllById(req.getDanhSachMaDichVu());
            for (DichVu dv : dichVus) {
                ChiTietLichHen ctlh = new ChiTietLichHen();
                ctlh.setLichHen(saved);
                ctlh.setDichVu(dv);
                ctlh.setSoLuong(1);
                ctlh.setDonGia(dv.getGiaDichVu());
                ctlh.setThanhTien(dv.getGiaDichVu());
                chiTietLichHenRepository.save(ctlh);
            }
        }

        if (req.getMaKhungGio() != null) {
            khungGioKhamRepository.findById(req.getMaKhungGio()).ifPresent(slot -> {
                slot.setTrangThai("Hết chỗ");
                khungGioKhamRepository.save(slot);
            });
        }

        // TỰ ĐỘNG TẠO HÓA ĐƠN NGOẠI TRÚ KHI ĐẶT LỊCH THÀNH CÔNG VÀ TĂNG LƯỢT DÙNG MÃ KM
        java.util.Optional<HoaDon> existingHd = hoaDonRepository.findByMaLichHen(saved.getMaLichHen());
        if (!existingHd.isPresent()) {
            double subtotal = 0.0;
            List<ChiTietHoaDon> details = new java.util.ArrayList<>();

            // A. Dịch vụ cận lâm sàng/chuyên sâu
            if (req.getDanhSachMaDichVu() != null && !req.getDanhSachMaDichVu().isEmpty()) {
                List<DichVu> dichVus = dichVuRepository.findAllById(req.getDanhSachMaDichVu());
                for (DichVu dv : dichVus) {
                    if (dv.getGiaDichVu() != null) {
                        double price = dv.getGiaDichVu().doubleValue();
                        subtotal += price;

                        ChiTietHoaDon ct = new ChiTietHoaDon();
                        ct.setKhoanMuc(dv.getTenDichVu());
                        ct.setSoLuong(1);
                        ct.setDonGia(dv.getGiaDichVu());
                        ct.setThanhTien(dv.getGiaDichVu());
                        ct.setGhiChu("Dịch vụ cận lâm sàng");
                        details.add(ct);
                    }
                }
            }

            // B. Phí khám của Bác sĩ
            if (bacSi != null && bacSi.getPhiKham() != null) {
                double price = bacSi.getPhiKham().doubleValue();
                subtotal += price;

                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setKhoanMuc("Phí khám bác sĩ (" + bacSi.getHoTen() + ")");
                ct.setSoLuong(1);
                ct.setDonGia(bacSi.getPhiKham());
                ct.setThanhTien(bacSi.getPhiKham());
                ct.setGhiChu("Phí khám ban đầu");
                details.add(ct);
            }

            double finalTotal = saved.getPhiDuKien().doubleValue();
            double giamGia = Math.max(0.0, subtotal - finalTotal);

            HoaDon hd = new HoaDon();
            hd.setMaHoaDon("HD" + System.currentTimeMillis());
            hd.setBenhNhan(benhNhan);
            hd.setMaLichHen(saved.getMaLichHen());
            hd.setTongTien(BigDecimal.valueOf(subtotal));
            hd.setGiamBHYT(BigDecimal.ZERO);
            hd.setGiamUuDai(BigDecimal.valueOf(giamGia));
            hd.setTongCanThanhToan(BigDecimal.valueOf(finalTotal));
            hd.setSoTienDaThanhToan(BigDecimal.ZERO);
            hd.setTrangThai("Chờ thanh toán");
            hd.setPhuongThucThanhToan("Chưa thanh toán");
            if (req.getMaKhuyenMai() != null && !req.getMaKhuyenMai().trim().isEmpty()) {
                hd.setGhiChu("Mã khuyến mãi áp dụng: " + req.getMaKhuyenMai().trim().toUpperCase());
            } else {
                hd.setGhiChu("Đặt lịch khám ngoại trú");
            }

            HoaDon savedHd = hoaDonRepository.save(hd);

            for (ChiTietHoaDon ct : details) {
                ct.setHoaDon(savedHd);
            }
            chiTietHoaDonRepository.saveAll(details);

            // C. Tăng số lượt đã dùng của mã khuyến mãi (chỉ tăng một lần duy nhất khi tạo hóa đơn thành công)
            if (req.getMaKhuyenMai() != null && !req.getMaKhuyenMai().trim().isEmpty()) {
                try {
                    KhuyenMai km = khuyenMaiService.layKhuyenMaiHopLe(req.getMaKhuyenMai());
                    int currentUsage = km.getSoLuotDaDung() != null ? km.getSoLuotDaDung() : 0;
                    km.setSoLuotDaDung(currentUsage + 1);
                    khuyenMaiRepository.save(km);
                } catch (Exception ignored) {}
            }
        }

        return saved;
    }

    // ------------------------------------------------------------------
    // 2. HỦY LỊCH (PATCH /dat-lich/{maLichHen}/huy)
    // ------------------------------------------------------------------
    @Transactional
    public void huyLich(String maLichHen, String lyDoHuy, String maBenhNhan) throws Exception {
        LichHen lichHen = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new Exception("Không tìm thấy lịch hẹn: " + maLichHen));

        if (!lichHen.getBenhNhan().getMaBenhNhan().equals(maBenhNhan)) {
            throw new Exception("Bạn không có quyền hủy lịch này.");
        }

        if (!"Chờ xác nhận".equals(lichHen.getTrangThai())) {
            throw new Exception("Chỉ được hủy lịch khi trạng thái là 'Chờ xác nhận'. Trạng thái hiện tại: " + lichHen.getTrangThai());
        }

        lichHen.setTrangThai("Đã hủy");
        lichHen.setLyDoHuy(lyDoHuy);
        lichHenRepository.save(lichHen);

        if (lichHen.getMaKhungGio() != null) {
            khungGioKhamRepository.findById(lichHen.getMaKhungGio()).ifPresent(slot -> {
                slot.setTrangThai("Còn chỗ");
                khungGioKhamRepository.save(slot);
            });
        }
    }

    // ------------------------------------------------------------------
    // 3. XÁC NHẬN LỊCH (chỉ Admin)
    // ------------------------------------------------------------------
    @Transactional
    public void xacNhanLich(String maLichHen) throws Exception {
        LichHen lichHen = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new Exception("Không tìm thấy lịch hẹn: " + maLichHen));

        if (!"Chờ xác nhận".equals(lichHen.getTrangThai())) {
            throw new Exception("Lịch hẹn không ở trạng thái 'Chờ xác nhận'.");
        }
        lichHen.setTrangThai("Đã xác nhận");
        lichHenRepository.save(lichHen);
    }

    // ------------------------------------------------------------------
    // 4. CẬP NHẬT TRẠNG THÁI (Admin)
    // ------------------------------------------------------------------
    @Transactional
    public void capNhatTrangThai(String maLichHen, String trangThaiMoi) throws Exception {
        LichHen lichHen = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new Exception("Không tìm thấy lịch hẹn: " + maLichHen));
        lichHen.setTrangThai(trangThaiMoi);
        lichHenRepository.save(lichHen);
    }

    // ------------------------------------------------------------------
    // 5. QUERY HELPERS
    // ------------------------------------------------------------------
    public List<LichHen> getAllLichHen() {
        return lichHenRepository.findAll();
    }

    public List<LichHen> getLichHenByBenhNhan(String maBenhNhan) {
        return lichHenRepository.findByBenhNhan_MaBenhNhanOrderByNgayHenDescGioHenDesc(maBenhNhan);
    }

    public List<LichHen> getLichHenByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.isEmpty()) {
            return lichHenRepository.findAll();
        }
        return lichHenRepository.findByTrangThaiOrderByNgayHenDesc(trangThai);
    }

    // ------------------------------------------------------------------
    // PRIVATE: Sinh mã lịch hẹn
    // ------------------------------------------------------------------
    private String genMaLichHen() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomCode = 1000 + new java.util.Random().nextInt(9000);
        return String.format("LH-%s-%d", dateStr, randomCode);
    }

    public java.util.Optional<LichHen> getLichHenById(String id) {
        return lichHenRepository.findById(id);
    }

    public void saveLichHen(LichHen lichHen) {
        lichHenRepository.save(lichHen);
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        if (date instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) date).toLocalDateTime().toLocalDate();
        }
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
}