package com.java.BaoCaoDoAn.Repository;



import com.java.BaoCaoDoAn.Model.ChuyenMonBacSi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuyenMonBacSiRepository extends JpaRepository<ChuyenMonBacSi, Integer> {
    List<ChuyenMonBacSi> findByMaBacSi(String maBacSi);
}
