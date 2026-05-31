package com.java.BaoCaoDoAn.Controller;


import com.java.BaoCaoDoAn.DTO.BacSiDetailDTO;
import com.java.BaoCaoDoAn.DTO.LichKhamDayDTO;
import com.java.BaoCaoDoAn.DTO.ReviewDTO;
import com.java.BaoCaoDoAn.Service.BacSiDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/bac-si")
@CrossOrigin(origins = "*") // Hỗ trợ fetch từ Flutter/React
@RequiredArgsConstructor
public class UserBacSiController {

    private final BacSiDetailService bacSiDetailService;

    // 1. API lấy thông tin chi tiết + chuyên môn + bằng cấp
    @GetMapping("/{maBacSi}")
    public ResponseEntity<BacSiDetailDTO> getDoctorInfo(@PathVariable String maBacSi) {
        return ResponseEntity.ok(bacSiDetailService.getDoctorDetail(maBacSi));
    }

    // 2. API lấy danh sách Đánh giá (Hiển thị phần bên trái màn hình UI 04)
    @GetMapping("/{maBacSi}/danh-gia")
    public ResponseEntity<List<ReviewDTO>> getDoctorReviews(@PathVariable String maBacSi) {
        return ResponseEntity.ok(bacSiDetailService.getDoctorReviews(maBacSi));
    }

    // 3. API lấy Lịch khám & Các slot giờ trong 7 ngày (Bên phải màn hình UI 04)
    @GetMapping("/{maBacSi}/lich-kham")
    public ResponseEntity<List<LichKhamDayDTO>> getDoctorSchedule(@PathVariable String maBacSi) {
        return ResponseEntity.ok(bacSiDetailService.getDoctorSchedule(maBacSi));
    }
}