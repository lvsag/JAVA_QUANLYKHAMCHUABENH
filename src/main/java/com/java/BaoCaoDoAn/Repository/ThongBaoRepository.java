package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
}
