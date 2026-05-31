package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiSoXetNghiem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiSoXetNghiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKetQua", nullable = false)
    private KetQuaXetNghiem ketQuaXetNghiem; // This maps to KetQuaDichVu in SQL (we'll fix KetQuaXetNghiem table name next)

    @Column(name = "TenChiSo", length = 100, nullable = false)
    private String tenChiSo;

    @Column(name = "KetQua", length = 100, nullable = false)
    private String ketQua;

    @Column(name = "DonVi", length = 50)
    private String donVi;

    @Column(name = "ThamChieu", length = 100)
    private String thamChieu;

    @Column(name = "DanhGia", length = 30)
    private String danhGia;
}
