package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {
    Optional<KhuyenMai> findByMaCode(String maCode);
    boolean existsByMaCode(String maCode);
}
