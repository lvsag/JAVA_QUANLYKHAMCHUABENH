package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Table(name = "DichVu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DichVu {
    @Id
    @Column(name = "MaDichVu", length = 10)
    private String maDichVu;

    @Column(name = "TenDichVu", nullable = false, length = 200)
    private String tenDichVu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChuyenKhoa")
    private ChuyenKhoa chuyenKhoa;

    @Column(name = "LoaiDichVu", length = 50)
    private String loaiDichVu;

    @Column(name = "MoTa", length = 500)
    private String moTa;

    @Column(name = "GiaDichVu", nullable = false)
    private BigDecimal giaDichVu;

    @Column(name = "GiaGiam")
    private BigDecimal giaGiam;

    @Column(name = "ThoiGianThucHien")
    private Integer thoiGianThucHien;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @Column(name = "PhongThucHien", length = 50)
    private String phongThucHien;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Đang dùng";

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
