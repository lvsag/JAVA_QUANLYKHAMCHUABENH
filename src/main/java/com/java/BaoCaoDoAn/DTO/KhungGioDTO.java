package com.java.BaoCaoDoAn.DTO;


import lombok.Data;

import java.time.LocalTime;

@Data
public class KhungGioDTO {
    private Integer maKhungGio;
    private LocalTime gioBatDau;
    private String caLamViec;
    private String trangThai;
}