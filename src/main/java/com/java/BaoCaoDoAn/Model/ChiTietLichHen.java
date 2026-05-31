package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ChiTietLichHen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietLichHen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLichHen", nullable = false)
    private LichHen lichHen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDichVu", nullable = false)
    private DichVu dichVu;

    @Column(name = "SoLuong")
    private Integer soLuong = 1;

    @Column(name = "DonGia")
    private BigDecimal donGia;

    @Column(name = "GiaGiam")
    private BigDecimal giaGiam = BigDecimal.ZERO;

    @Column(name = "ThanhTien")
    private BigDecimal thanhTien;

    @Column(name = "GhiChu", length = 200)
    private String ghiChu;
}
