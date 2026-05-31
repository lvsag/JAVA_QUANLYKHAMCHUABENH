package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "TinTuc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TinTuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTinTuc")
    private Integer maTinTuc;

    @Column(name = "TieuDe", length = 200, nullable = false)
    private String tieuDe;

    @Column(name = "NoiDung", columnDefinition = "LONGTEXT")
    private String noiDung;

    @Column(name = "TomTat", length = 500)
    private String tomTat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChuyenKhoa")
    private ChuyenKhoa chuyenKhoa;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @Column(name = "TacGia", length = 100)
    private String tacGia;

    @Column(name = "NgayDang")
    @Temporal(TemporalType.DATE)
    private Date ngayDang;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Đã đăng";

    @Column(name = "LuotXem")
    private Integer luotXem = 0;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;
}
