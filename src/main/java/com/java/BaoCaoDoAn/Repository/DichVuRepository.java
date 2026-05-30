package com.java.BaoCaoDoAn.Repository;

import com.java.BaoCaoDoAn.Model.DichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DichVuRepository extends JpaRepository<DichVu, String> {
    // Added for service-list pages: filter public/admin service views by keyword, specialty, and service type.
    List<DichVu> findByTenDichVuContainingIgnoreCase(String keyword);

    // Added for service-list pages: load services by specialty when the user filters the public catalog.
    List<DichVu> findByChuyenKhoa_MaChuyenKhoa(String maChuyenKhoa);

    List<DichVu> findByTenDichVuContainingIgnoreCaseOrMaDichVuContainingIgnoreCase(String ten, String ma);
}
