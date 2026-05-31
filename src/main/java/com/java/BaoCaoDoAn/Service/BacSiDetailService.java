package com.java.BaoCaoDoAn.Service;


import com.java.BaoCaoDoAn.DTO.KhungGioDTO;
import com.java.BaoCaoDoAn.DTO.BacSiDetailDTO;
import com.java.BaoCaoDoAn.DTO.LichKhamDayDTO;
import com.java.BaoCaoDoAn.DTO.ReviewDTO;
import com.java.BaoCaoDoAn.Model.BacSi;
import com.java.BaoCaoDoAn.Model.ChuyenMonBacSi;
import com.java.BaoCaoDoAn.Model.KhungGioKham;
import com.java.BaoCaoDoAn.Repository.BacSiRepository;
import com.java.BaoCaoDoAn.Repository.ChuyenMonBacSiRepository;
import com.java.BaoCaoDoAn.Repository.DanhGiaRepository;
import com.java.BaoCaoDoAn.Repository.KhungGioKhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BacSiDetailService {

    private final BacSiRepository bacSiRepository;
    private final ChuyenMonBacSiRepository chuyenMonRepository;
    private final DanhGiaRepository danhGiaRepository;
    private final KhungGioKhamRepository khungGioKhamRepository;

    // 1. Lấy thông tin chung & chuyên môn
    public BacSiDetailDTO getDoctorDetail(String maBacSi) {
        BacSi bacSi = bacSiRepository.findById(maBacSi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        BacSiDetailDTO dto = new BacSiDetailDTO();
        dto.setMaBacSi(bacSi.getMaBacSi());
        dto.setHoTen(bacSi.getHoTen());
        dto.setHocVi(bacSi.getHocVi());
        dto.setSoNamKinhNghiem(bacSi.getSoNamKinhNghiem());
        dto.setTrangThai(bacSi.getTrangThai());
        dto.setDiemDanhGiaTB(bacSi.getDiemDanhGiaTB());
        dto.setSoLuongDanhGia(bacSi.getSoLuongDanhGia());
        dto.setSoBenhNhanDaKham(bacSi.getSoBenhNhanDaKham());
        dto.setGioiThieu(bacSi.getGioiThieu());
        dto.setBangCap(bacSi.getBangCap());
        dto.setChungChiHanhNghe(bacSi.getChungChiHanhNghe());
        dto.setPhongKham(bacSi.getPhongKham());

        // Lấy tags chuyên môn
        List<String> chuyenMonList = chuyenMonRepository.findByMaBacSi(maBacSi)
                .stream().map(ChuyenMonBacSi::getTenChuyenMon).collect(Collectors.toList());
        dto.setDanhSachChuyenMon(chuyenMonList);

        return dto;
    }

    // 2. Lấy đánh giá
    public List<ReviewDTO> getDoctorReviews(String maBacSi) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return danhGiaRepository.findByMaBacSiOrderByNgayTaoDesc(maBacSi).stream().map(dg -> {
            ReviewDTO dto = new ReviewDTO();
            dto.setDiemDanhGia(dg.getDiemDanhGia());
            dto.setNoiDung(dg.getNoiDung());
            dto.setTenBenhNhan(dg.getBenhNhan().getHoTen());
            dto.setNgayDanhGia(dg.getNgayTao().format(formatter));
            return dto;
        }).collect(Collectors.toList());
    }

    // 3. Lấy lịch làm việc 7 ngày tới (Mini calendar & Time slots)
    public List<LichKhamDayDTO> getDoctorSchedule(String maBacSi) {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        // Query database lấy mọi khung giờ trong 7 ngày
        List<KhungGioKham> slots = khungGioKhamRepository.findKhungGioByBacSiAndDateRange(maBacSi, today, nextWeek);

        // Group dữ liệu theo Ngày khám (NgayKham)
        Map<LocalDate, List<KhungGioKham>> groupedByDate = slots.stream()
                .collect(Collectors.groupingBy(KhungGioKham::getNgayKham));

        List<LichKhamDayDTO> result = new ArrayList<>();

        // Sắp xếp theo ngày tăng dần
        groupedByDate.keySet().stream().sorted().forEach(date -> {
            LichKhamDayDTO dayDTO = new LichKhamDayDTO();
            dayDTO.setNgayKham(date);
            // Xác định thứ (VD: MONDAY -> Thứ 2)
            dayDTO.setThuTrongTuan(getVietnameseDayOfWeek(date));

            List<KhungGioDTO> slotDTOs = groupedByDate.get(date).stream().map(k -> {
                KhungGioDTO kDto = new KhungGioDTO();
                kDto.setMaKhungGio(k.getMaKhungGio());
                kDto.setGioBatDau(k.getGioBatDau());
                kDto.setCaLamViec(k.getLichLamViec().getCaLamViec()); // "Ca sáng" hoặc "Ca chiều"
                kDto.setTrangThai(k.getTrangThai()); // "Còn chỗ"
                return kDto;
            }).collect(Collectors.toList());

            dayDTO.setDanhSachKhungGio(slotDTOs);
            result.add(dayDTO);
        });

        return result;
    }

    private String getVietnameseDayOfWeek(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY: return "Thứ 2";
            case TUESDAY: return "Thứ 3";
            case WEDNESDAY: return "Thứ 4";
            case THURSDAY: return "Thứ 5";
            case FRIDAY: return "Thứ 6";
            case SATURDAY: return "Thứ 7";
            case SUNDAY: return "Chủ Nhật";
            default: return "";
        }
    }
}