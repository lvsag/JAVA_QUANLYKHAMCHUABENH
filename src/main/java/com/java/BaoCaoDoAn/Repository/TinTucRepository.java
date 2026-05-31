package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.TinTuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinTucRepository extends JpaRepository<TinTuc, Integer> {
}
