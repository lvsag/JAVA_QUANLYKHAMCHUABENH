package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Table(name = "VatTuYTe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VatTuYTe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaVatTu")
    private Integer maVatTu;

    @Column(name = "TenVatTu", nullable = false, length = 200)
    private String tenVatTu;

    @Column(name = "DonViTinh", length = 30)
    private String donViTinh;

    @Column(name = "GiaNhap")
    private BigDecimal giaNhap;

    @Column(name = "GiaBan")
    private BigDecimal giaBan;

    @Column(name = "TonKho")
    private Integer tonKho = 0;

    @Column(name = "NguongCanhBao")
    private Integer nguongCanhBao = 5;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Còn hàng";

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
