package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GiuongBenh")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiuongBenh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGiuong")
    private Integer maGiuong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhong", nullable = false)
    private PhongBenh phongBenh;

    @Column(name = "SoGiuong", length = 10)
    private String soGiuong;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Trống";

    @Column(name = "GhiChu", length = 200)
    private String ghiChu;
}
