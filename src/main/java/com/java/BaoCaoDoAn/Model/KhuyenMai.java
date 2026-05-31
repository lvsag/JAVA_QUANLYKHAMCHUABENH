package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "KhuyenMai")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKhuyenMai")
    private Integer maKhuyenMai;

    @Column(name = "MaCode", length = 50, nullable = false)
    private String maCode;

    @Column(name = "TenKhuyenMai", length = 200, nullable = false)
    private String tenKhuyenMai;

    @Column(name = "GiaTriGiam", nullable = false)
    private BigDecimal giaTriGiam;

    @Column(name = "SoLuotToiDa")
    private Integer soLuotToiDa;

    @Column(name = "SoLuotDaDung")
    private Integer soLuotDaDung = 0;

    @Column(name = "NgayBatDau", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayBatDau;

    @Column(name = "NgayKetThuc", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayKetThuc;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Hoạt động";

}
