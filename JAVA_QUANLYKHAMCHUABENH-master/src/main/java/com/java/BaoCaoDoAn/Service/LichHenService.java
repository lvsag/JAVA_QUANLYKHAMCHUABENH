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

    // ĐÃ THÊM: Repository để lấy giá Dịch vụ từ Database
    private final DichVuRepository     dichVuRepository;

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
            String code = voucherCode.trim().toUpperCase();
            switch (code) {
                case "GIAM30K"    -> subtotal = Math.max(0, subtotal - 30000);
                case "CENTRAL10"  -> subtotal = subtotal * 0.9;
                case "BHYT20"     -> subtotal = subtotal * 0.8;
            }
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
        Time gioHen;
        try {
            ngayHen = new SimpleDateFormat("yyyy-MM-dd").parse(req.getNgayHen());

            // XỬ LÝ LỖI: Dịch maKhungGio (Integer) thành chuỗi giờ chuẩn (HH:mm)
            String gioStr = "08:00"; // Giờ mặc định
            if (req.getMaKhungGio() != null) {
                switch(req.getMaKhungGio()) {
                    case 1 -> gioStr = "08:00";
                    case 2 -> gioStr = "08:30";
                    case 3 -> gioStr = "09:00";
                    case 4 -> gioStr = "09:30";
                    case 5 -> gioStr = "13:30";
                    case 6 -> gioStr = "14:00";
                }
            }
            gioHen  = Time.valueOf(gioStr + ":00");

        } catch (Exception e) {
            throw new Exception("Định dạng ngày/giờ không hợp lệ. Hãy kiểm tra lại form.");
        }

        Date ngaySinh = null;
        if (req.getNgaySinhNguoiKham() != null && !req.getNgaySinhNguoiKham().isEmpty()) {
            try {
                ngaySinh = new SimpleDateFormat("dd/MM/yyyy").parse(req.getNgaySinhNguoiKham());
            } catch (Exception ignored) {}
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
        lichHen.setMaKhungGio(null); // Bypass Foreign Key constraint tạm thời
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

        if (req.getMaKhungGio() != null) {
            khungGioKhamRepository.findById(req.getMaKhungGio()).ifPresent(slot -> {
                slot.setTrangThai("Hết chỗ");
                khungGioKhamRepository.save(slot);
            });
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
        return lichHenRepository.findByBenhNhan_MaBenhNhanOrderByNgayHenDesc(maBenhNhan);
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
}