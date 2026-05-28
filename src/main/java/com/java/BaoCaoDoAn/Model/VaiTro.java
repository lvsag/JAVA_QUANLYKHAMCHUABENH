package com.java.BaoCaoDoAn.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "VaiTro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaiTro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaVaiTro")
    private Integer maVaiTro;

    @Column(name = "TenVaiTro", nullable = false, unique = true, length = 50)
    private String tenVaiTro;

    @Column(name = "MoTa", length = 200)
    private String moTa;

    @Column(name = "TrangThai")
    private Boolean trangThai = true;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;
}
