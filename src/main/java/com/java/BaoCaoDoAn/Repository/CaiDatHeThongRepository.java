package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.CaiDatHeThong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaiDatHeThongRepository extends JpaRepository<CaiDatHeThong, Integer> {
    Optional<CaiDatHeThong> findByTenCaiDat(String tenCaiDat);
}
