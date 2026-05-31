package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "NhapVienNoiTru")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhapVienNoiTru {
    @Id
    @Column(name = "MaNoiTru", length = 20)
    private String maNoiTru;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBenhNhan", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBacSiDieuTri", nullable = false)
    private BacSi bacSiDieuTri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhong")
    private PhongBenh phongBenh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaGiuong")
    private GiuongBenh giuongBenh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChuyenKhoa")
    private ChuyenKhoa chuyenKhoa;

    @Column(name = "ChanDoan", length = 500)
    private String chanDoan;

    @Column(name = "NgayNhapVien", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayNhapVien;

    @Column(name = "DuKienDieuTri", length = 50)
    private String duKienDieuTri;

    @Column(name = "NgayXuatVien")
    @Temporal(TemporalType.DATE)
    private Date ngayXuatVien;

    @Column(name = "TinhTrangRaVien", length = 200)
    private String tinhTrangRaVien;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Đang điều trị";

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
