package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;

@Entity
@Table(name = "LichHen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LichHen {
    @Id
    @Column(name = "MaLichHen", length = 20)
    private String maLichHen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBenhNhan", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBacSi")
    private BacSi bacSi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChuyenKhoa")
    private ChuyenKhoa chuyenKhoa;

    @Column(name = "NgayHen", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayHen;

    @Column(name = "GioHen", nullable = false)
    private Time gioHen;

    @Column(name = "MaKhungGio")
    private Integer maKhungGio;

    @Column(name = "HoTenNguoiKham", length = 100)
    private String hoTenNguoiKham;

    @Column(name = "SDTNguoiKham", length = 15)
    private String sdtNguoiKham;

    @Column(name = "NgaySinhNguoiKham")
    @Temporal(TemporalType.DATE)
    private Date ngaySinhNguoiKham;

    @Column(name = "EmailXacNhan", length = 100)
    private String emailXacNhan;

    @Column(name = "GhiChuTrieuChung", columnDefinition = "TEXT")
    private String ghiChuTrieuChung;

    @Column(name = "HoTenNguoiThanLH", length = 100)
    private String hoTenNguoiThanLH;

    @Column(name = "SDTNguoiThanLH", length = 15)
    private String sdtNguoiThanLH;

    @Column(name = "CoBHYT")
    private Boolean coBHYT = false;

    @Column(name = "SoTheBHYT", length = 20)
    private String soTheBHYT;

    @Column(name = "TrangThai", length = 30)
    private String trangThai = "Chờ xác nhận";

    @Column(name = "LyDoHuy", length = 500)
    private String lyDoHuy;

    @Column(name = "PhuongThucThanhToan", length = 50)
    private String phuongThucThanhToan;

    @Column(name = "PhiDuKien")
    private BigDecimal phiDuKien;

    @Column(name = "PhongKham", length = 50)
    private String phongKham;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
