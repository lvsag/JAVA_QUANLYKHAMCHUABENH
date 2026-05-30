package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PhieuChiDinh")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuChiDinh {
    @Id
    @Column(name = "MaPhieuChiDinh", length = 20)
    private String maPhieuChiDinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhieuKham")
    private PhieuKham phieuKham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBenhNhan", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBacSiChiDinh")
    private BacSi bacSi;

    @Column(name = "NgayChiDinh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayChiDinh = new Date();

    @Column(name = "GhiChuChiDinh", length = 1000)
    private String ghiChu;

    @Column(name = "TongTamTinh")
    private BigDecimal tongTien = BigDecimal.ZERO;

    @Column(name = "TrangThai", length = 30)
    private String trangThai = "Cho thuc hien";

    @OneToMany(mappedBy = "phieuChiDinh", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ChiTietChiDinh> chiTietChiDinhs;
}
