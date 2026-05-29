package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Table(name = "BacSi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BacSi {
    @Id
    @Column(name = "MaBacSi", length = 10)
    private String maBacSi;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiKhoan")
    private TaiKhoan taiKhoan;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChuyenKhoa", nullable = false)
    private ChuyenKhoa chuyenKhoa;

    @Column(name = "HocVi", length = 50)
    private String hocVi;

    @Column(name = "SoNamKinhNghiem")
    private Integer soNamKinhNghiem;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "SoDienThoai", length = 15)
    private String soDienThoai;

    @Column(name = "PhiKham")
    private BigDecimal phiKham;

    @Column(name = "GioiThieu", length = 2000)
    private String gioiThieu;

    @Column(name = "BangCap", length = 500)
    private String bangCap;

    @Column(name = "ChungChiHanhNghe", length = 100)
    private String chungChiHanhNghe;

    @Column(name = "SoBenhNhanDaKham")
    private Integer soBenhNhanDaKham = 0;

    @Column(name = "DiemDanhGiaTB")
    private BigDecimal diemDanhGiaTB = BigDecimal.ZERO;

    @Column(name = "SoLuongDanhGia")
    private Integer soLuongDanhGia = 0;

    @Column(name = "PhongKham", length = 50)
    private String phongKham;

    @Column(name = "HinhDaiDien", length = 255)
    private String hinhDaiDien;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Đang làm việc";

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
