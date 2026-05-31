package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "khuyenmai")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKhuyenMai")
    private Integer maKhuyenMai;

    @Column(name = "MaCode", nullable = false, unique = true, length = 50)
    private String maCode;

    @Column(name = "TenKhuyenMai", nullable = false, length = 200)
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
