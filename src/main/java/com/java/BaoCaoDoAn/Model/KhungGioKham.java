package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "KhungGioKham")
@Data
public class KhungGioKham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKhungGio")
    private Integer maKhungGio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLichLamViec")
    private LichLamViec lichLamViec;

    @Column(name = "NgayKham")
    private LocalDate ngayKham;

    @Column(name = "GioBatDau")
    private LocalTime gioBatDau;

    @Column(name = "GioKetThuc")
    private LocalTime gioKetThuc;

    @Column(name = "TrangThai", length = 20)
    private String trangThai;
}