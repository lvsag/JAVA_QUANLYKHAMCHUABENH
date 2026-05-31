package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.GiuongBenh;
import com.java.BaoCaoDoAn.Model.PhongBenh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GiuongBenhRepository extends JpaRepository<GiuongBenh, Integer> {
    List<GiuongBenh> findByPhongBenh(PhongBenh phongBenh);
    
    long countByPhongBenhAndTrangThai(PhongBenh phongBenh, String trangThai);
    
    long countByPhongBenh(PhongBenh phongBenh);
    
    List<GiuongBenh> findByTrangThai(String trangThai);
    
    void deleteByPhongBenh(PhongBenh phongBenh);
}
