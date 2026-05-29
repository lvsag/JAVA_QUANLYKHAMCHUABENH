package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DonThuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonThuoc {
    @Id
    @Column(name = "MaDonThuoc", length = 20)
    private String maDonThuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhieuKham")
    private PhieuKham phieuKham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBenhNhan", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBacSi")
    private BacSi bacSi;

    @Column(name = "NgayKe")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayKe = new Date();

    @Column(name = "ChanDoan", length = 1000)
    private String chanDoan;

    @Column(name = "LoiDan", length = 1000)
    private String loiDan;

    @Column(name = "TrangThai", length = 30)
    private String trangThai = "Dang dung";

    @OneToMany(mappedBy = "donThuoc", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ChiTietDonThuoc> chiTietDonThuocs;
}
