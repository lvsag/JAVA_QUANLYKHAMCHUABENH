package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "ChuyenKhoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChuyenKhoa {
    @Id
    @Column(name = "MaChuyenKhoa", length = 10)
    private String maChuyenKhoa;

    @Column(name = "TenChuyenKhoa", nullable = false, length = 100)
    private String tenChuyenKhoa;

    @Column(name = "MoTa", length = 500)
    private String moTa;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @Column(name = "SoBacSi")
    private Integer soBacSi = 0;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Hoạt động";

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
