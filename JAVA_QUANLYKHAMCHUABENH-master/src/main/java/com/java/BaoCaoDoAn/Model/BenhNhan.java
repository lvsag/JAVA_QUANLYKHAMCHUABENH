package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Table(name = "BenhNhan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BenhNhan {
    @Id
    @Column(name = "MaBenhNhan", length = 20)
    private String maBenhNhan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTaiKhoan", unique = true)
    private TaiKhoan taiKhoan;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "NgaySinh")
    @Temporal(TemporalType.DATE)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaySinh;

    @Column(name = "GioiTinh", length = 10)
    private String gioiTinh;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "SoDienThoai", length = 15)
    private String soDienThoai;

    @Column(name = "SoCCCD", length = 20)
    private String soCCCD;

    @Column(name = "QuocTich", length = 50)
    private String quocTich = "Việt Nam";

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "NhomMau", length = 5)
    private String nhomMau;

    @Column(name = "ChieuCao")
    private BigDecimal chieuCao;

    @Column(name = "CanNang")
    private BigDecimal canNang;

    @Column(name = "SoBHYT", length = 20)
    private String soBHYT;

    @Column(name = "NgayHetHanBHYT")
    @Temporal(TemporalType.DATE)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayHetHanBHYT;

    @Column(name = "DiUngThuoc", length = 500)
    private String diUngThuoc;

    @Column(name = "TienSuBenhManTinh", length = 500)
    private String tienSuBenhManTinh;

    @Column(name = "GhiChuYTe", length = 1000)
    private String ghiChuYTe;

    @Column(name = "HoTenNguoiThan", length = 100)
    private String hoTenNguoiThan;

    @Column(name = "QuanHeNguoiThan", length = 50)
    private String quanHeNguoiThan;

    @Column(name = "SDTNguoiThan", length = 15)
    private String sdtNguoiThan;

    @Column(name = "NhanEmailNhacLich")
    private Boolean nhanEmailNhacLich = true;

    @Column(name = "NhanSMSNhacLich")
    private Boolean nhanSMSNhacLich = true;

    @Column(name = "NhanEmailKetQua")
    private Boolean nhanEmailKetQua = true;

    @Column(name = "NhanTinTucSucKhoe")
    private Boolean nhanTinTucSucKhoe = false;

    @Column(name = "HinhDaiDien", length = 255)
    private String hinhDaiDien;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
