package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Time;

@Entity
@Table(name = "LichLamViec")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichLamViec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLichLamViec")
    private Integer maLichLamViec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaBacSi", nullable = false)
    private BacSi bacSi;

    @Column(name = "NgayTrongTuan", nullable = false)
    private Integer ngayTrongTuan; // 2=Thứ 2, 3=Thứ 3... 8=CN

    @Column(name = "CaLamViec", nullable = false, length = 20)
    private String caLamViec;

    @Column(name = "GioBatDau", nullable = false)
    private Time gioBatDau;

    @Column(name = "GioKetThuc", nullable = false)
    private Time gioKetThuc;

    @Column(name = "SoSlotToiDa")
    private Integer soSlotToiDa = 4;

    @Column(name = "TrangThai", length = 20)
    private String trangThai = "Làm việc";
}
