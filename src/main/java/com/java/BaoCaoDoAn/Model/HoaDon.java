package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HoaDon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {

    @Id
    @Column(name = "MaHoaDon", length = 20)
    private String maHoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBenhNhan", nullable = false)
    private BenhNhan benhNhan;

    @Column(name = "MaLichHen", length = 20)
    private String maLichHen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNoiTru")
    private NhapVienNoiTru nhapVienNoiTru;

    @Column(name = "TongTien", nullable = false)
    private BigDecimal tongTien;

    @Column(name = "GiamBHYT")
    private BigDecimal giamBHYT = BigDecimal.ZERO;

    @Column(name = "GiamUuDai")
    private BigDecimal giamUuDai = BigDecimal.ZERO;

    @Column(name = "TongCanThanhToan", nullable = false)
    private BigDecimal tongCanThanhToan;

    @Column(name = "SoTienDaThanhToan")
    private BigDecimal soTienDaThanhToan = BigDecimal.ZERO;

    @Column(name = "PhuongThucThanhToan", length = 50)
    private String phuongThucThanhToan;

    @Column(name = "TrangThai", length = 30)
    private String trangThai = "Chờ thanh toán";

    @Column(name = "NgayThanhToan")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayThanhToan;

    @Column(name = "NguoiThu", length = 100)
    private String nguoiThu;

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ChiTietHoaDon> chiTietHoaDons;
}
