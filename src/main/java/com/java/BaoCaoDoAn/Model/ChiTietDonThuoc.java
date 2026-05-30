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
    @Column(name = "Id")
    private Long maChiTietDonThuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDonThuoc", nullable = false)
    private DonThuoc donThuoc;

    // Added: map prescription details to the real Thuoc table instead of a non-existent TenThuoc column.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaThuoc", nullable = false)
    private Thuoc thuoc;

    @Column(name = "LieuDung", length = 100)
    private String lieuDung;

    @Column(name = "SoLanNgay", length = 100)
    private String soLanTrongNgay;

    @Column(name = "ThoiGianUong", length = 100)
    private String thoiGianDung;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "GhiChu", length = 500)
    private String ghiChu;

    public String getTenThuoc() {
        return thuoc != null ? thuoc.getTenThuoc() : "";
    }
}
