package com.java.BaoCaoDoAn.DTO;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

// ===================================================================
// DTO nhận dữ liệu từ form đặt lịch (POST /dat-lich) qua 4 bước
// ===================================================================
@Data
public class DatLichRequestDTO {

    // Bước 1: Danh sách dịch vụ được chọn
    private List<String> danhSachMaDichVu = new ArrayList<>();

    // Bước 2: Thông tin hành chính & y tế
    private String maChuyenKhoa;
    private String maBacSi;
    private Integer maKhungGio; // Dùng Integer để khớp với value="1", "2"... ở thẻ select HTML
    private String ngayHen;     // Định dạng: "yyyy-MM-dd"
    private String gioHen;      // Định dạng: "HH:mm"

    // Thông tin người khám
    private String hoTenNguoiKham;
    private String sdtNguoiKham;
    private String ngaySinhNguoiKham;
    private String emailXacNhan;
    private String ghiChuTrieuChung = "";

    // BHYT & Khuyến mãi
    private Boolean coBHYT = false;
    private String soBHYT = "";
    private String maKhuyenMai = "";

    // Người thân (Tùy chọn)
    private String hoTenNguoiThanLH;
    private String sdtNguoiThanLH;

    // Bước 3: Phương thức thanh toán (Mặc định chọn Tiền mặt)
    private String phuongThucThanhToan = "TIEN_MAT";

}