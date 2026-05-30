package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Table(name = "Thuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Thuoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThuoc")
    private Integer maThuoc;

    @Column(name = "TenThuoc", nullable = false, length = 200)
    private String tenThuoc;

    @Column(name = "HoatChat", length = 200)
    private String hoatChat;

    @Column(name = "DangBaoChe", length = 50)
    private String dangBaoChe;

    @Column(name = "DonViTinh", length = 30)
    private String donViTinh;

    @Column(name = "HamLuong", length = 50)
    private String hamLuong;

    @Column(name = "NhaSanXuat", length = 200)
    private String nhaSanXuat;

    @Column(name = "GiaNhap")
    private BigDecimal giaNhap;

    @Column(name = "GiaBan")
    private BigDecimal giaBan;

    @Column(name = "TonKho")
    private Integer tonKho = 0;

    @Column(name = "NguongCanhBao")
    private Integer nguongCanhBao = 5;

    @Column(name = "TrangThai", length = 20)
    private String trangThai;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;
}
