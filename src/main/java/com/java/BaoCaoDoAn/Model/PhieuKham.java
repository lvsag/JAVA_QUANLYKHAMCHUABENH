package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PhieuKham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuKham {
    @Id
    @Column(name = "MaPhieuKham", length = 20)
    private String maPhieuKham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLichHen")
    private LichHen lichHen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBenhNhan", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBacSi")
    private BacSi bacSi;

    @Column(name = "NgayKham")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayKham = new Date();

    @Column(name = "TrieuChung", length = 1000)
    private String trieuChung;

    @Column(name = "TienSuBenh", length = 1000)
    private String tienSuBenh;

    @Column(name = "DauHieuSinhTon", length = 500)
    private String dauHieuSinhTon;

    @Column(name = "ChanDoanBanDau", length = 1000)
    private String chanDoanBanDau;

    @Column(name = "ChanDoanCuoi", length = 1000)
    private String chanDoanCuoi;

    @Column(name = "TrangThai", length = 30)
    private String trangThai = "Dang kham";

    @OneToMany(mappedBy = "phieuKham")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<PhieuChiDinh> phieuChiDinhs;

    @OneToMany(mappedBy = "phieuKham")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DonThuoc> donThuocs;
}
