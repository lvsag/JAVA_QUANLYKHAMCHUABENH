package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietDonThuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonThuoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTietDonThuoc")
    private Long maChiTietDonThuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDonThuoc", nullable = false)
    private DonThuoc donThuoc;

    @Column(name = "TenThuoc", nullable = false, length = 200)
    private String tenThuoc;

    @Column(name = "LieuDung", length = 100)
    private String lieuDung;

    @Column(name = "SoLanTrongNgay", length = 100)
    private String soLanTrongNgay;

    @Column(name = "ThoiGianDung", length = 100)
    private String thoiGianDung;

    @Column(name = "SoLuong", length = 100)
    private String soLuong;

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;
}
