package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "ThongBao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThongBao")
    private Integer maThongBao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiKhoan", nullable = false)
    private TaiKhoan taiKhoan;

    @Column(name = "TieuDe", length = 200, nullable = false)
    private String tieuDe;

    @Column(name = "NoiDung", length = 500)
    private String noiDung;

    @Column(name = "LoaiThongBao", length = 50)
    private String loaiThongBao;

    @Column(name = "DuongDanLienKet", length = 255)
    private String duongDanLienKet;

    @Column(name = "DaDoc")
    private Boolean daDoc = false;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;
}
