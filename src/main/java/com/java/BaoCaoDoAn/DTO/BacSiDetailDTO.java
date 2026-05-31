package com.java.BaoCaoDoAn.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BacSiDetailDTO {
    private String maBacSi;
    private String hoTen;
    private String hocVi;
    private Integer soNamKinhNghiem;
    private String trangThai;
    private BigDecimal diemDanhGiaTB;
    private Integer soLuongDanhGia;
    private Integer soBenhNhanDaKham;
    private String gioiThieu;
    private String bangCap;
    private String chungChiHanhNghe;
    private String phongKham;

    private List<String> danhSachChuyenMon;
}