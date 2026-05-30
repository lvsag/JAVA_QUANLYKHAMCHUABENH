package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ChuyenMonBacSi")
@Data
public class ChuyenMonBacSi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "MaBacSi")
    private String maBacSi;

    @Column(name = "TenChuyenMon")
    private String tenChuyenMon;
}