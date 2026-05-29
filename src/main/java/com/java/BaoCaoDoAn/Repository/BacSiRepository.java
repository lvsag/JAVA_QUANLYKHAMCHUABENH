package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.BacSi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BacSiRepository extends JpaRepository<BacSi, String> {
    Optional<BacSi> findByTaiKhoan_MaTaiKhoan(Integer maTaiKhoan);
    List<BacSi> findByChuyenKhoa_MaChuyenKhoa(String maChuyenKhoa);
    List<BacSi> findByTrangThai(String trangThai);
    List<BacSi> findByHoTenContainingIgnoreCase(String hoTen);

    @org.springframework.data.jpa.repository.Query("SELECT b FROM BacSi b WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(b.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND (:maChuyenKhoa IS NULL OR :maChuyenKhoa = '' OR b.chuyenKhoa.maChuyenKhoa = :maChuyenKhoa) AND (:hocViList IS NULL OR b.hocVi IN :hocViList) AND (:minRating IS NULL OR b.diemDanhGiaTB >= :minRating)")
    List<BacSi> searchBacSi(
        @org.springframework.data.repository.query.Param("keyword") String keyword, 
        @org.springframework.data.repository.query.Param("maChuyenKhoa") String maChuyenKhoa,
        @org.springframework.data.repository.query.Param("hocViList") java.util.List<String> hocViList,
        @org.springframework.data.repository.query.Param("minRating") java.math.BigDecimal minRating,
        org.springframework.data.domain.Sort sort
    );

    @org.springframework.data.jpa.repository.Query("SELECT AVG(b.diemDanhGiaTB) FROM BacSi b WHERE b.diemDanhGiaTB > 0")
    java.math.BigDecimal getAverageRating();
}
