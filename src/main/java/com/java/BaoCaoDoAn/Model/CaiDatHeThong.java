package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "CaiDatHeThong")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaiDatHeThong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCaiDat")
    private Integer maCaiDat;

    @Column(name = "TenCaiDat", nullable = false, unique = true, length = 100)
    private String tenCaiDat;

    @Column(name = "GiaTri", length = 500)
    private String giaTri;

    @Column(name = "MoTa", length = 200)
    private String moTa;

    @Column(name = "NgayCapNhat", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;
}
