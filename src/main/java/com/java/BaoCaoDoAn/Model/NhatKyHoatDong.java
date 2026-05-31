package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "NhatKyHoatDong")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhatKyHoatDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNhatKy")
    private Integer maNhatKy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiKhoan")
    private TaiKhoan taiKhoan;

    @Column(name = "HanhDong", length = 200, nullable = false)
    private String hanhDong;

    @Column(name = "DoiTuong", length = 100)
    private String doiTuong;

    @Column(name = "MaDoiTuong", length = 50)
    private String maDoiTuong;

    @Column(name = "ChiTiet", length = 500)
    private String chiTiet;

    @Column(name = "DiaChiIP", length = 45)
    private String diaChiIP;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;
}
