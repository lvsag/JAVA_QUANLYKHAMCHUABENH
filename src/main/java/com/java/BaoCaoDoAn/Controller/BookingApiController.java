package com.java.BaoCaoDoAn.Controller;

import com.java.BaoCaoDoAn.Model.BacSi;
import com.java.BaoCaoDoAn.Model.KhungGioKham;
import com.java.BaoCaoDoAn.Repository.BacSiRepository;
import com.java.BaoCaoDoAn.Repository.KhungGioKhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingApiController {

    private final BacSiRepository bacSiRepository;
    private final KhungGioKhamRepository khungGioKhamRepository;

    @GetMapping("/bac-si")
    public ResponseEntity<List<Map<String, Object>>> getBacSiByChuyenKhoa(@RequestParam String maChuyenKhoa) {
        List<BacSi> bacSiList;
        if (maChuyenKhoa == null || maChuyenKhoa.isEmpty()) {
            bacSiList = bacSiRepository.findAll();
        } else {
            bacSiList = bacSiRepository.findByChuyenKhoa_MaChuyenKhoa(maChuyenKhoa);
        }

        List<Map<String, Object>> response = bacSiList.stream().map(bs -> {
            Map<String, Object> map = new HashMap<>();
            map.put("maBacSi", bs.getMaBacSi());
            map.put("hoTen", bs.getHoTen());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/khung-gio")
    public ResponseEntity<List<Map<String, Object>>> getAvailableSlots(
            @RequestParam String maBacSi,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayKham) {
        
        List<KhungGioKham> slots = khungGioKhamRepository.findAvailableSlots(maBacSi, ngayKham, "Còn chỗ");

        List<Map<String, Object>> response = slots.stream().map(slot -> {
            Map<String, Object> map = new HashMap<>();
            map.put("maKhungGio", slot.getMaKhungGio());
            map.put("thoiGian", slot.getGioBatDau() + " - " + slot.getGioKetThuc());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
