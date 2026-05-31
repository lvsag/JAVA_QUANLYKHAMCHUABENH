package com.java.BaoCaoDoAn.DTO;



import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LichKhamDayDTO {
    private LocalDate ngayKham;
    private String thuTrongTuan;
    private List<KhungGioDTO> danhSachKhungGio;
}