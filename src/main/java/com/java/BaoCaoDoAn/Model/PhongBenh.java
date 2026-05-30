package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "PhongBenh")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhongBenh {
    @Id
    @Column(name = "MaPhong", length = 10)
    private String maPhong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChuyenKhoa")
    private ChuyenKhoa chuyenKhoa;

    @Column(name = "LoaiPhong", length = 30)
    private String loaiPhong;

    @Column(name = "SoGiuongToiDa")
    private Integer soGiuongToiDa = 6;

    @Column(name = "SoGiuongDangDung")
    private Integer soGiuongDangDung = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBacSiPhuTrach")
    private BacSi bacSiPhuTrach;

    @Column(name = "GhiChu", length = 200)
    private String ghiChu;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Còn nhận";

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
