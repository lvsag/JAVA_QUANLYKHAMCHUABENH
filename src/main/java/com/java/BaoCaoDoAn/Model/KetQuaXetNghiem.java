package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "KetQuaDichVu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaXetNghiem {
    @Id
    @Column(name = "MaKetQua", length = 20)
    private String maKetQua;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhieuChiDinh")
    private PhieuChiDinh phieuChiDinh;

    @Transient
    private ChiTietChiDinh chiTietChiDinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBenhNhan", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBacSiChiDinh")
    private BacSi bacSi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDichVu")
    private DichVu dichVu;

    @Column(name = "NgayThucHien")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayKetQua = new Date();

    @Column(name = "ChiSoChinh", length = 1000)
    private String chiSoChinh;

    @Column(name = "KetQuaChiTiet", length = 2000)
    private String ketQuaChiTiet;

    @Column(name = "NhanDinhDichVu", length = 1000)
    private String nhanDinh;

    @Column(name = "KetLuanBacSi", length = 1000)
    private String ketLuanBacSi;

    @Column(name = "FileKetQua", length = 255)
    private String fileDinhKem;

    @Column(name = "TrangThai", length = 30)
    private String trangThai = "Moi";
}
