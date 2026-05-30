package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.VatTuYTe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VatTuYTeRepository extends JpaRepository<VatTuYTe, Integer> {
    List<VatTuYTe> findByTenVatTuContainingIgnoreCase(String tenVatTu);
    List<VatTuYTe> findByTrangThai(String trangThai);
}
