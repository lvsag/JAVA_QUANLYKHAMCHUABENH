package com.java.BaoCaoDoAn.Service;

import com.java.BaoCaoDoAn.Model.KhungGioKham;
import com.java.BaoCaoDoAn.Model.LichLamViec;
import com.java.BaoCaoDoAn.Repository.KhungGioKhamRepository;
import com.java.BaoCaoDoAn.Repository.LichLamViecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// ===================================================================
// NEW FILE: Service tự động sinh KhungGioKham từ LichLamViec
//
// BUG 5 FIX: BacSiDetailService.getDoctorSchedule() query DB trực tiếp
// nhưng KhungGioKham KHÔNG BAO GIỜ tự sinh ra → lịch luôn rỗng.
// Service này giải quyết bằng cách tính toán và insert slots trước.
// ===================================================================
@Service
@RequiredArgsConstructor
public class KhungGioGeneratorService {

    private final LichLamViecRepository lichLamViecRepository;
    private final KhungGioKhamRepository khungGioKhamRepository;

    // Thời lượng mỗi slot (phút)
    private static final int SLOT_DURATION_MINUTES = 30;

    /**
     * Sinh KhungGioKham cho 1 bác sĩ trong khoảng ngày sắp tới.
     * Gọi khi admin phân lịch hoặc scheduler chạy hàng ngày.
     */
    @Transactional
    public void generateSlotsForDoctor(String maBacSi, LocalDate fromDate, LocalDate toDate) {
        List<LichLamViec> schedules = lichLamViecRepository.findByBacSi_MaBacSi(maBacSi);

        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            int dayOfWeek = toDayOfWeekNumber(date.getDayOfWeek()); // 2=Thứ 2 ... 8=CN

            for (LichLamViec llv : schedules) {
                if (llv.getNgayTrongTuan() != dayOfWeek) continue;
                if (!"Làm việc".equals(llv.getTrangThai())) continue;

                // Kiểm tra đã sinh slot cho ngày này chưa (tránh duplicate)
                boolean existed = khungGioKhamRepository
                        .findKhungGioByBacSiAndDateRange(maBacSi, currentDate, currentDate)
                        .stream()
                        .anyMatch(k -> k.getLichLamViec().getMaLichLamViec()
                                .equals(llv.getMaLichLamViec()));
                if (existed) continue;

                // Sinh các slot theo bội số SLOT_DURATION_MINUTES
                LocalTime cursor = llv.getGioBatDau().toLocalTime();
                LocalTime end    = llv.getGioKetThuc().toLocalTime();
                List<KhungGioKham> newSlots = new ArrayList<>();

                long totalMinutes = java.time.Duration.between(cursor, end).toMinutes();
                if (totalMinutes <= 0) {
                    totalMinutes += 24 * 60; // Nếu giờ kết thúc qua ngày hôm sau
                }

                int numSlots = (int) (totalMinutes / SLOT_DURATION_MINUTES);
                for (int i = 0; i < numSlots; i++) {
                    KhungGioKham slot = new KhungGioKham();
                    slot.setLichLamViec(llv);
                    slot.setNgayKham(currentDate);
                    slot.setGioBatDau(cursor);
                    
                    LocalTime next = cursor.plusMinutes(SLOT_DURATION_MINUTES);
                    slot.setGioKetThuc(next);
                    slot.setTrangThai("Còn chỗ");
                    newSlots.add(slot);
                    
                    cursor = next;
                }

                // Giới hạn số slot theo SoSlotToiDa
                int max = llv.getSoSlotToiDa() != null ? llv.getSoSlotToiDa() : 4;
                if (newSlots.size() > max) {
                    newSlots = newSlots.subList(0, max);
                }

                khungGioKhamRepository.saveAll(newSlots);
            }
        }
    }

    /**
     * Sinh slots cho tất cả bác sĩ, 7 ngày từ hôm nay.
     * Dùng cho @Scheduled task hoặc gọi thủ công khi khởi động.
     */
    @Transactional
    public void generateSlotsForAll7Days() {
        LocalDate today = LocalDate.now();
        List<LichLamViec> all = lichLamViecRepository.findAll();
        all.stream()
                .map(l -> l.getBacSi().getMaBacSi())
                .distinct()
                .forEach(maBacSi -> generateSlotsForDoctor(maBacSi, today, today.plusDays(6)));
    }

    // Chuyển DayOfWeek (Java) → số thứ phong cách Việt (2=Thứ2 ... 8=CN)
    private int toDayOfWeekNumber(DayOfWeek dow) {
        switch (dow) {
            case MONDAY:    return 2;
            case TUESDAY:   return 3;
            case WEDNESDAY: return 4;
            case THURSDAY:  return 5;
            case FRIDAY:    return 6;
            case SATURDAY:  return 7;
            case SUNDAY:    return 8;
            default:        return 0;
        }
    }
}
