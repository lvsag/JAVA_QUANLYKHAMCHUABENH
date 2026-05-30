package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietChiDinh")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietChiDinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long maChiTietChiDinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhieuChiDinh", nullable = false)
    private PhieuChiDinh phieuChiDinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDichVu", nullable = false)
    private DichVu dichVu;

    // Added: schema requires unit price and execution room for each ordered service.
    @Column(name = "DonGia", nullable = false)
    private java.math.BigDecimal donGia = java.math.BigDecimal.ZERO;

    @Column(name = "PhongThucHien", length = 50)
    private String phongThucHien;

    @Column(name = "TrangThai", length = 30)
    private String trangThai = "Cho";

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;
}
