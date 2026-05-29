package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTaiKhoan")
    private Integer maTaiKhoan;

    @Column(name = "TenDangNhap", nullable = false, unique = true, length = 50)
    private String tenDangNhap;

    @Column(name = "MatKhau", nullable = false, length = 255)
    private String matKhau;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaVaiTro", nullable = false)
    private VaiTro vaiTro;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Hoạt động";

    @Column(name = "LanDangNhap")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lanDangNhap;

    @Column(name = "GhiNhoDangNhap")
    private Boolean ghiNhoDangNhap = false;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
