package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ChiTietHoaDon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaHoaDon", nullable = false)
    private HoaDon hoaDon;

    @Column(name = "KhoanMuc", nullable = false, length = 200)
    private String khoanMuc;

    @Column(name = "SoLuong")
    private Integer soLuong = 1;

    @Column(name = "DonGia", nullable = false)
    private BigDecimal donGia;

    @Column(name = "ThanhTien", nullable = false)
    private BigDecimal thanhTien;

    @Column(name = "GhiChu", length = 200)
    private String ghiChu;
}
