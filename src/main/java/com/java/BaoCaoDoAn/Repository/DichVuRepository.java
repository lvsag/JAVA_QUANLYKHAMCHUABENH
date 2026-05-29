package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.DichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DichVuRepository extends JpaRepository<DichVu, String> {
}
