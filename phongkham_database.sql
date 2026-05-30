-- ============================================================
-- DATABASE: HỆ THỐNG QUẢN LÝ PHÒNG KHÁM ĐA KHOA
-- Phong Kham Da Khoa - Multi-Specialty Clinic Management System
-- ============================================================
-- Database Engine: MySQL 8.0+
-- Chuyển đổi từ Microsoft SQL Server
-- ============================================================

CREATE DATABASE IF NOT EXISTS PhongKhamDaKhoa
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE PhongKhamDaKhoa;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 1. BẢNG VAI TRÒ (ROLES)
-- ============================================================
CREATE TABLE VaiTro (
    MaVaiTro    INT AUTO_INCREMENT PRIMARY KEY,
    TenVaiTro   VARCHAR(50) NOT NULL UNIQUE,
    MoTa        VARCHAR(200),
    TrangThai   TINYINT(1) DEFAULT 1,
    NgayTao     DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 2. BẢNG TÀI KHOẢN (ACCOUNTS)
-- ============================================================
CREATE TABLE TaiKhoan (
    MaTaiKhoan      INT AUTO_INCREMENT PRIMARY KEY,
    TenDangNhap     VARCHAR(50) NOT NULL UNIQUE,
    MatKhau         VARCHAR(255) NOT NULL,
    Email           VARCHAR(100) NOT NULL UNIQUE,
    HoTen           VARCHAR(100) NOT NULL,
    MaVaiTro        INT NOT NULL,
    TrangThai       VARCHAR(20) DEFAULT 'Hoạt động',
    LanDangNhap     DATETIME,
    GhiNhoDangNhap  TINYINT(1) DEFAULT 0,
    NgayTao         DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_TaiKhoan_VaiTro FOREIGN KEY (MaVaiTro) REFERENCES VaiTro(MaVaiTro)
);

-- ============================================================
-- 3. BẢNG CHUYÊN KHOA (SPECIALTIES)
-- ============================================================
CREATE TABLE ChuyenKhoa (
    MaChuyenKhoa    VARCHAR(10) PRIMARY KEY,
    TenChuyenKhoa   VARCHAR(100) NOT NULL,
    MoTa            VARCHAR(500),
    HinhAnh         VARCHAR(255),
    SoBacSi         INT DEFAULT 0,
    TrangThai       VARCHAR(20) DEFAULT 'Hoạt động',
    NgayTao         DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 4. BẢNG BỆNH NHÂN (PATIENTS)
-- ============================================================
CREATE TABLE BenhNhan (
    MaBenhNhan          VARCHAR(20) PRIMARY KEY,
    MaTaiKhoan          INT UNIQUE,
    HoTen               VARCHAR(100) NOT NULL,
    NgaySinh            DATE,
    GioiTinh            VARCHAR(10),
    Email               VARCHAR(100),
    SoDienThoai         VARCHAR(15),
    SoCCCD              VARCHAR(20),
    QuocTich            VARCHAR(50) DEFAULT 'Việt Nam',
    DiaChi              VARCHAR(255),

    NhomMau             VARCHAR(5),
    ChieuCao            DECIMAL(5,1),
    CanNang             DECIMAL(5,1),
    SoBHYT              VARCHAR(20),
    NgayHetHanBHYT      DATE,
    DiUngThuoc          VARCHAR(500),
    TienSuBenhManTinh   VARCHAR(500),
    GhiChuYTe           VARCHAR(1000),

    HoTenNguoiThan      VARCHAR(100),
    QuanHeNguoiThan     VARCHAR(50),
    SDTNguoiThan        VARCHAR(15),

    NhanEmailNhacLich   TINYINT(1) DEFAULT 1,
    NhanSMSNhacLich     TINYINT(1) DEFAULT 1,
    NhanEmailKetQua     TINYINT(1) DEFAULT 1,
    NhanTinTucSucKhoe   TINYINT(1) DEFAULT 0,

    HinhDaiDien         VARCHAR(255),
    NgayTao             DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_BenhNhan_TaiKhoan FOREIGN KEY (MaTaiKhoan) REFERENCES TaiKhoan(MaTaiKhoan)
);

-- ============================================================
-- 5. BẢNG BÁC SĨ (DOCTORS)
-- ============================================================
CREATE TABLE BacSi (
    MaBacSi             VARCHAR(10) PRIMARY KEY,
    MaTaiKhoan          INT,
    HoTen               VARCHAR(100) NOT NULL,
    MaChuyenKhoa        VARCHAR(10) NOT NULL,
    HocVi               VARCHAR(50),
    SoNamKinhNghiem     INT,
    Email               VARCHAR(100),
    SoDienThoai         VARCHAR(15),
    PhiKham             DECIMAL(12,0),
    GioiThieu           TEXT,
    BangCap             VARCHAR(500),
    ChungChiHanhNghe    VARCHAR(100),
    SoBenhNhanDaKham    INT DEFAULT 0,
    DiemDanhGiaTB       DECIMAL(2,1) DEFAULT 0,
    SoLuongDanhGia      INT DEFAULT 0,
    PhongKham           VARCHAR(50),
    HinhDaiDien         VARCHAR(255),
    TrangThai           VARCHAR(20) DEFAULT 'Đang làm việc',
    NgayTao             DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_BacSi_ChuyenKhoa FOREIGN KEY (MaChuyenKhoa) REFERENCES ChuyenKhoa(MaChuyenKhoa),
    CONSTRAINT FK_BacSi_TaiKhoan FOREIGN KEY (MaTaiKhoan) REFERENCES TaiKhoan(MaTaiKhoan)
);

-- Partial UNIQUE index: chỉ áp dụng khi MaTaiKhoan IS NOT NULL
-- MySQL không hỗ trợ filtered index như SQL Server, dùng trigger hoặc để NULL tự xử lý UNIQUE
-- UNIQUE constraint trên cột nullable cho phép nhiều NULL (chuẩn SQL) trong MySQL 8+
CREATE UNIQUE INDEX UQ_BacSi_MaTaiKhoan ON BacSi(MaTaiKhoan);

-- ============================================================
-- 6. BẢNG CHUYÊN MÔN BÁC SĨ
-- ============================================================
CREATE TABLE ChuyenMonBacSi (
    Id              INT AUTO_INCREMENT PRIMARY KEY,
    MaBacSi         VARCHAR(10) NOT NULL,
    TenChuyenMon    VARCHAR(100) NOT NULL,

    CONSTRAINT FK_ChuyenMon_BacSi FOREIGN KEY (MaBacSi) REFERENCES BacSi(MaBacSi)
);

-- ============================================================
-- 7. BẢNG LỊCH LÀM VIỆC BÁC SĨ
-- ============================================================
CREATE TABLE LichLamViec (
    MaLichLamViec   INT AUTO_INCREMENT PRIMARY KEY,
    MaBacSi         VARCHAR(10) NOT NULL,
    NgayTrongTuan   INT NOT NULL,
    CaLamViec       VARCHAR(20) NOT NULL,
    GioBatDau       TIME NOT NULL,
    GioKetThuc      TIME NOT NULL,
    SoSlotToiDa     INT DEFAULT 4,
    TrangThai       VARCHAR(20) DEFAULT 'Làm việc',

    CONSTRAINT FK_LichLamViec_BacSi FOREIGN KEY (MaBacSi) REFERENCES BacSi(MaBacSi)
);

-- ============================================================
-- 8. BẢNG KHUNG GIỜ KHÁM
-- ============================================================
CREATE TABLE KhungGioKham (
    MaKhungGio      INT AUTO_INCREMENT PRIMARY KEY,
    MaLichLamViec   INT NOT NULL,
    NgayKham        DATE NOT NULL,
    GioBatDau       TIME NOT NULL,
    GioKetThuc      TIME NOT NULL,
    TrangThai       VARCHAR(20) DEFAULT 'Còn chỗ',

    CONSTRAINT FK_KhungGio_LichLamViec FOREIGN KEY (MaLichLamViec) REFERENCES LichLamViec(MaLichLamViec)
);

-- ============================================================
-- 9. BẢNG DỊCH VỤ KHÁM
-- ============================================================
CREATE TABLE DichVu (
    MaDichVu            VARCHAR(10) PRIMARY KEY,
    TenDichVu           VARCHAR(200) NOT NULL,
    MaChuyenKhoa        VARCHAR(10),
    LoaiDichVu          VARCHAR(50),
    MoTa                VARCHAR(500),
    GiaDichVu           DECIMAL(12,0) NOT NULL,
    GiaGiam             DECIMAL(12,0),
    ThoiGianThucHien    INT,
    HinhAnh             VARCHAR(255),
    PhongThucHien       VARCHAR(50),
    TrangThai           VARCHAR(20) DEFAULT 'Đang dùng',
    NgayTao             DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_DichVu_ChuyenKhoa FOREIGN KEY (MaChuyenKhoa) REFERENCES ChuyenKhoa(MaChuyenKhoa)
);

-- ============================================================
-- 10. BẢNG LỊCH HẸN / ĐẶT LỊCH KHÁM
-- ============================================================
CREATE TABLE LichHen (
    MaLichHen               VARCHAR(20) PRIMARY KEY,
    MaBenhNhan              VARCHAR(20) NOT NULL,
    MaBacSi                 VARCHAR(10),
    MaChuyenKhoa            VARCHAR(10),
    NgayHen                 DATE NOT NULL,
    GioHen                  TIME NOT NULL,
    MaKhungGio              INT,

    HoTenNguoiKham          VARCHAR(100),
    SDTNguoiKham            VARCHAR(15),
    NgaySinhNguoiKham       DATE,
    EmailXacNhan            VARCHAR(100),
    GhiChuTrieuChung        TEXT,

    HoTenNguoiThanLH        VARCHAR(100),
    SDTNguoiThanLH          VARCHAR(15),

    CoBHYT                  TINYINT(1) DEFAULT 0,
    SoTheBHYT               VARCHAR(20),

    TrangThai               VARCHAR(30) DEFAULT 'Chờ xác nhận',
    LyDoHuy                 VARCHAR(500),

    PhuongThucThanhToan     VARCHAR(50),
    PhiDuKien               DECIMAL(12,0),
    PhongKham               VARCHAR(50),
    NgayTao                 DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat             DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_LichHen_BenhNhan  FOREIGN KEY (MaBenhNhan)   REFERENCES BenhNhan(MaBenhNhan),
    CONSTRAINT FK_LichHen_BacSi     FOREIGN KEY (MaBacSi)      REFERENCES BacSi(MaBacSi),
    CONSTRAINT FK_LichHen_ChuyenKhoa FOREIGN KEY (MaChuyenKhoa) REFERENCES ChuyenKhoa(MaChuyenKhoa),
    CONSTRAINT FK_LichHen_KhungGio  FOREIGN KEY (MaKhungGio)   REFERENCES KhungGioKham(MaKhungGio)
);

-- ============================================================
-- 11. BẢNG CHI TIẾT DỊCH VỤ ĐÃ CHỌN TRONG LỊCH HẸN
-- ============================================================
CREATE TABLE ChiTietLichHen (
    Id          INT AUTO_INCREMENT PRIMARY KEY,
    MaLichHen   VARCHAR(20) NOT NULL,
    MaDichVu    VARCHAR(10) NOT NULL,
    SoLuong     INT DEFAULT 1,
    DonGia      DECIMAL(12,0) NOT NULL,
    GiaGiam     DECIMAL(12,0) DEFAULT 0,
    ThanhTien   DECIMAL(12,0) NOT NULL,
    GhiChu      VARCHAR(200),

    CONSTRAINT FK_ChiTietLH_LichHen FOREIGN KEY (MaLichHen) REFERENCES LichHen(MaLichHen),
    CONSTRAINT FK_ChiTietLH_DichVu  FOREIGN KEY (MaDichVu)  REFERENCES DichVu(MaDichVu)
);

-- ============================================================
-- 12. BẢNG PHIẾU KHÁM BỆNH
-- ============================================================
CREATE TABLE PhieuKham (
    MaPhieuKham         VARCHAR(20) PRIMARY KEY,
    MaLichHen           VARCHAR(20),
    MaBenhNhan          VARCHAR(20) NOT NULL,
    MaBacSi             VARCHAR(10) NOT NULL,
    PhongKham           VARCHAR(50),
    NgayKham            DATETIME DEFAULT CURRENT_TIMESTAMP,

    TrieuChung          TEXT,
    TienSuBenh          TEXT,
    DauHieuSinhTon      VARCHAR(500),
    ChanDoanBanDau      VARCHAR(500),
    ChanDoanCuoiCung    VARCHAR(500),
    KetLuanBacSi        TEXT,

    TrangThai           VARCHAR(30) DEFAULT 'Chờ',
    SoThuTu             INT,
    NgayTao             DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_PhieuKham_LichHen  FOREIGN KEY (MaLichHen)  REFERENCES LichHen(MaLichHen),
    CONSTRAINT FK_PhieuKham_BenhNhan FOREIGN KEY (MaBenhNhan) REFERENCES BenhNhan(MaBenhNhan),
    CONSTRAINT FK_PhieuKham_BacSi    FOREIGN KEY (MaBacSi)    REFERENCES BacSi(MaBacSi)
);

-- ============================================================
-- 13. BẢNG PHIẾU CHỈ ĐỊNH DỊCH VỤ CẬN LÂM SÀNG
-- ============================================================
CREATE TABLE PhieuChiDinh (
    MaPhieuChiDinh  VARCHAR(20) PRIMARY KEY,
    MaPhieuKham     VARCHAR(20) NOT NULL,
    MaBenhNhan      VARCHAR(20) NOT NULL,
    MaBacSiChiDinh  VARCHAR(10) NOT NULL,
    NgayChiDinh     DATETIME DEFAULT CURRENT_TIMESTAMP,
    GhiChuChiDinh   VARCHAR(500),
    TongTamTinh     DECIMAL(12,0) DEFAULT 0,
    TrangThai       VARCHAR(30) DEFAULT 'Chờ thực hiện',
    NgayTao         DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_PhieuCD_PhieuKham FOREIGN KEY (MaPhieuKham)    REFERENCES PhieuKham(MaPhieuKham),
    CONSTRAINT FK_PhieuCD_BenhNhan  FOREIGN KEY (MaBenhNhan)     REFERENCES BenhNhan(MaBenhNhan),
    CONSTRAINT FK_PhieuCD_BacSi     FOREIGN KEY (MaBacSiChiDinh) REFERENCES BacSi(MaBacSi)
);

-- ============================================================
-- 14. BẢNG CHI TIẾT CHỈ ĐỊNH DỊCH VỤ
-- ============================================================
CREATE TABLE ChiTietChiDinh (
    Id              INT AUTO_INCREMENT PRIMARY KEY,
    MaPhieuChiDinh  VARCHAR(20) NOT NULL,
    MaDichVu        VARCHAR(10) NOT NULL,
    DonGia          DECIMAL(12,0) NOT NULL,
    PhongThucHien   VARCHAR(50),
    TrangThai       VARCHAR(20) DEFAULT 'Chờ',
    GhiChu          VARCHAR(200),

    CONSTRAINT FK_CTCD_PhieuChiDinh FOREIGN KEY (MaPhieuChiDinh) REFERENCES PhieuChiDinh(MaPhieuChiDinh),
    CONSTRAINT FK_CTCD_DichVu       FOREIGN KEY (MaDichVu)       REFERENCES DichVu(MaDichVu)
);

-- ============================================================
-- 15. BẢNG KẾT QUẢ XÉT NGHIỆM / DỊCH VỤ CẬN LÂM SÀNG
-- ============================================================
CREATE TABLE KetQuaDichVu (
    MaKetQua            VARCHAR(20) PRIMARY KEY,
    MaPhieuChiDinh      VARCHAR(20) NOT NULL,
    MaDichVu            VARCHAR(10) NOT NULL,
    MaBenhNhan          VARCHAR(20) NOT NULL,
    MaBacSiChiDinh      VARCHAR(10),
    NgayThucHien        DATETIME,

    LoaiMau             VARCHAR(50),
    TrieuChungBanDau    VARCHAR(500),
    ChiSoChinh          TEXT,
    KetQuaChiTiet       TEXT,
    NhanDinhDichVu      TEXT,
    KetLuanBacSi        TEXT,

    FileKetQua          VARCHAR(255),
    FileHinhAnh         VARCHAR(255),

    DanhGia             VARCHAR(30),
    GhiChu              VARCHAR(500),
    TrangThai           VARCHAR(20) DEFAULT 'Chờ',
    NgayTao             DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_KetQua_PhieuCD  FOREIGN KEY (MaPhieuChiDinh) REFERENCES PhieuChiDinh(MaPhieuChiDinh),
    CONSTRAINT FK_KetQua_DichVu   FOREIGN KEY (MaDichVu)       REFERENCES DichVu(MaDichVu),
    CONSTRAINT FK_KetQua_BenhNhan FOREIGN KEY (MaBenhNhan)     REFERENCES BenhNhan(MaBenhNhan),
    CONSTRAINT FK_KetQua_BacSi    FOREIGN KEY (MaBacSiChiDinh) REFERENCES BacSi(MaBacSi)
);

-- ============================================================
-- 16. BẢNG CHI TIẾT CHỈ SỐ XÉT NGHIỆM
-- ============================================================
CREATE TABLE ChiSoXetNghiem (
    Id          INT AUTO_INCREMENT PRIMARY KEY,
    MaKetQua    VARCHAR(20) NOT NULL,
    TenChiSo    VARCHAR(100) NOT NULL,
    KetQua      VARCHAR(100) NOT NULL,
    DonVi       VARCHAR(50),
    ThamChieu   VARCHAR(100),
    DanhGia     VARCHAR(30),

    CONSTRAINT FK_ChiSo_KetQua FOREIGN KEY (MaKetQua) REFERENCES KetQuaDichVu(MaKetQua)
);

-- ============================================================
-- 17. BẢNG THUỐC
-- ============================================================
CREATE TABLE Thuoc (
    MaThuoc         INT AUTO_INCREMENT PRIMARY KEY,
    TenThuoc        VARCHAR(200) NOT NULL,
    HoatChat        VARCHAR(200),
    DangBaoChe      VARCHAR(50),
    DonViTinh       VARCHAR(30),
    HamLuong        VARCHAR(50),
    NhaSanXuat      VARCHAR(200),
    GiaNhap         DECIMAL(12,0),
    GiaBan          DECIMAL(12,0),
    TonKho          INT DEFAULT 0,
    NguongCanhBao   INT DEFAULT 5,
    TrangThai       VARCHAR(20) DEFAULT 'Còn hàng',
    NgayTao         DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 18. BẢNG VẬT TƯ Y TẾ
-- ============================================================
CREATE TABLE VatTuYTe (
    MaVatTu         INT AUTO_INCREMENT PRIMARY KEY,
    TenVatTu        VARCHAR(200) NOT NULL,
    DonViTinh       VARCHAR(30),
    GiaNhap         DECIMAL(12,0),
    GiaBan          DECIMAL(12,0),
    TonKho          INT DEFAULT 0,
    NguongCanhBao   INT DEFAULT 5,
    TrangThai       VARCHAR(20) DEFAULT 'Còn hàng',
    NgayTao         DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- 19. BẢNG ĐƠN THUỐC
-- ============================================================
CREATE TABLE DonThuoc (
    MaDonThuoc  VARCHAR(20) PRIMARY KEY,
    MaPhieuKham VARCHAR(20) NOT NULL,
    MaBenhNhan  VARCHAR(20) NOT NULL,
    MaBacSi     VARCHAR(10) NOT NULL,
    ChanDoan    VARCHAR(500),
    NgayKeDon   DATETIME DEFAULT CURRENT_TIMESTAMP,
    LoiDan      TEXT,
    TrangThai   VARCHAR(30) DEFAULT 'Đang dùng',
    NgayTao     DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_DonThuoc_PhieuKham FOREIGN KEY (MaPhieuKham) REFERENCES PhieuKham(MaPhieuKham),
    CONSTRAINT FK_DonThuoc_BenhNhan  FOREIGN KEY (MaBenhNhan)  REFERENCES BenhNhan(MaBenhNhan),
    CONSTRAINT FK_DonThuoc_BacSi     FOREIGN KEY (MaBacSi)     REFERENCES BacSi(MaBacSi)
);

-- ============================================================
-- 20. BẢNG CHI TIẾT ĐƠN THUỐC
-- ============================================================
CREATE TABLE ChiTietDonThuoc (
    Id          INT AUTO_INCREMENT PRIMARY KEY,
    MaDonThuoc  VARCHAR(20) NOT NULL,
    MaThuoc     INT NOT NULL,
    LieuDung    VARCHAR(100),
    SoLanNgay   VARCHAR(50),
    ThoiGianUong VARCHAR(100),
    SoLuong     INT NOT NULL,
    GhiChu      VARCHAR(200),

    CONSTRAINT FK_CTDT_DonThuoc FOREIGN KEY (MaDonThuoc) REFERENCES DonThuoc(MaDonThuoc),
    CONSTRAINT FK_CTDT_Thuoc    FOREIGN KEY (MaThuoc)    REFERENCES Thuoc(MaThuoc)
);

-- ============================================================
-- 21. BẢNG LỊCH TÁI KHÁM
-- ============================================================
CREATE TABLE LichTaiKham (
    MaLichTaiKham   INT AUTO_INCREMENT PRIMARY KEY,
    MaPhieuKham     VARCHAR(20) NOT NULL,
    MaBenhNhan      VARCHAR(20) NOT NULL,
    MaBacSi         VARCHAR(10) NOT NULL,
    NgayTaiKham     DATE NOT NULL,
    KhungGio        VARCHAR(30),
    PhongKham       VARCHAR(50),
    HinhThuc        VARCHAR(50),
    LyDoTaiKham     VARCHAR(500),

    GuiSMSTruoc24h  TINYINT(1) DEFAULT 1,
    GuiEmailXacNhan TINYINT(1) DEFAULT 1,
    HienThiLichSu   TINYINT(1) DEFAULT 1,

    TrangThai       VARCHAR(20) DEFAULT 'Đã đặt',
    NgayTao         DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_LichTK_PhieuKham FOREIGN KEY (MaPhieuKham) REFERENCES PhieuKham(MaPhieuKham),
    CONSTRAINT FK_LichTK_BenhNhan  FOREIGN KEY (MaBenhNhan)  REFERENCES BenhNhan(MaBenhNhan),
    CONSTRAINT FK_LichTK_BacSi     FOREIGN KEY (MaBacSi)     REFERENCES BacSi(MaBacSi)
);

-- ============================================================
-- 22. BẢNG PHÒNG BỆNH
-- ============================================================
CREATE TABLE PhongBenh (
    MaPhong             VARCHAR(10) PRIMARY KEY,
    MaChuyenKhoa        VARCHAR(10),
    LoaiPhong           VARCHAR(30),
    SoGiuongToiDa       INT DEFAULT 6,
    SoGiuongDangDung    INT DEFAULT 0,
    MaBacSiPhuTrach     VARCHAR(10),
    GhiChu              VARCHAR(200),
    TrangThai           VARCHAR(20) DEFAULT 'Còn nhận',
    NgayTao             DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_PhongBenh_ChuyenKhoa FOREIGN KEY (MaChuyenKhoa)    REFERENCES ChuyenKhoa(MaChuyenKhoa),
    CONSTRAINT FK_PhongBenh_BacSi      FOREIGN KEY (MaBacSiPhuTrach) REFERENCES BacSi(MaBacSi)
);

-- ============================================================
-- 23. BẢNG GIƯỜNG BỆNH
-- ============================================================
CREATE TABLE GiuongBenh (
    MaGiuong    INT AUTO_INCREMENT PRIMARY KEY,
    MaPhong     VARCHAR(10) NOT NULL,
    SoGiuong    VARCHAR(10),
    TrangThai   VARCHAR(20) DEFAULT 'Trống',
    GhiChu      VARCHAR(200),

    CONSTRAINT FK_Giuong_Phong FOREIGN KEY (MaPhong) REFERENCES PhongBenh(MaPhong)
);

-- ============================================================
-- 24. BẢNG NHẬP VIỆN NỘI TRÚ
-- ============================================================
CREATE TABLE NhapVienNoiTru (
    MaNoiTru            VARCHAR(20) PRIMARY KEY,
    MaBenhNhan          VARCHAR(20) NOT NULL,
    MaBacSiDieuTri      VARCHAR(10) NOT NULL,
    MaPhong             VARCHAR(10),
    MaGiuong            INT,
    MaChuyenKhoa        VARCHAR(10),
    ChanDoan            VARCHAR(500),
    NgayNhapVien        DATE NOT NULL,
    DuKienDieuTri       VARCHAR(50),

    NgayXuatVien        DATE,
    TinhTrangRaVien     VARCHAR(200),

    TrangThai           VARCHAR(20) DEFAULT 'Đang điều trị',
    NgayTao             DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_NoiTru_BenhNhan  FOREIGN KEY (MaBenhNhan)     REFERENCES BenhNhan(MaBenhNhan),
    CONSTRAINT FK_NoiTru_BacSi     FOREIGN KEY (MaBacSiDieuTri) REFERENCES BacSi(MaBacSi),
    CONSTRAINT FK_NoiTru_Phong     FOREIGN KEY (MaPhong)        REFERENCES PhongBenh(MaPhong),
    CONSTRAINT FK_NoiTru_Giuong    FOREIGN KEY (MaGiuong)       REFERENCES GiuongBenh(MaGiuong),
    CONSTRAINT FK_NoiTru_ChuyenKhoa FOREIGN KEY (MaChuyenKhoa) REFERENCES ChuyenKhoa(MaChuyenKhoa)
);

-- ============================================================
-- 25. BẢNG HÓA ĐƠN / THANH TOÁN
-- ============================================================
CREATE TABLE HoaDon (
    MaHoaDon                VARCHAR(20) PRIMARY KEY,
    MaBenhNhan              VARCHAR(20) NOT NULL,
    MaLichHen               VARCHAR(20),
    MaNoiTru                VARCHAR(20),

    TongTien                DECIMAL(12,0) NOT NULL,
    GiamBHYT               DECIMAL(12,0) DEFAULT 0,
    GiamUuDai               DECIMAL(12,0) DEFAULT 0,
    TongCanThanhToan        DECIMAL(12,0) NOT NULL,
    SoTienDaThanhToan       DECIMAL(12,0) DEFAULT 0,

    PhuongThucThanhToan     VARCHAR(50),
    TrangThai               VARCHAR(30) DEFAULT 'Chờ thanh toán',

    NgayThanhToan           DATETIME,
    NguoiThu                VARCHAR(100),
    GhiChu                  VARCHAR(500),
    NgayTao                 DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat             DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_HoaDon_BenhNhan FOREIGN KEY (MaBenhNhan) REFERENCES BenhNhan(MaBenhNhan),
    CONSTRAINT FK_HoaDon_LichHen  FOREIGN KEY (MaLichHen)  REFERENCES LichHen(MaLichHen),
    CONSTRAINT FK_HoaDon_NoiTru   FOREIGN KEY (MaNoiTru)   REFERENCES NhapVienNoiTru(MaNoiTru)
);

-- ============================================================
-- 26. BẢNG CHI TIẾT HÓA ĐƠN
-- ============================================================
CREATE TABLE ChiTietHoaDon (
    Id          INT AUTO_INCREMENT PRIMARY KEY,
    MaHoaDon    VARCHAR(20) NOT NULL,
    KhoanMuc    VARCHAR(200) NOT NULL,
    SoLuong     INT DEFAULT 1,
    DonGia      DECIMAL(12,0) NOT NULL,
    ThanhTien   DECIMAL(12,0) NOT NULL,
    GhiChu      VARCHAR(200),

    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon)
);

-- ============================================================
-- 27. BẢNG ĐÁNH GIÁ
-- ============================================================
CREATE TABLE DanhGia (
    MaDanhGia   INT AUTO_INCREMENT PRIMARY KEY,
    MaBenhNhan  VARCHAR(20) NOT NULL,
    MaBacSi     VARCHAR(10) NOT NULL,
    MaLichHen   VARCHAR(20),
    DiemDanhGia INT NOT NULL,
    NoiDung     TEXT,
    TrangThai   VARCHAR(20) DEFAULT 'Hiện',
    NgayTao     DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_DanhGia_BenhNhan FOREIGN KEY (MaBenhNhan) REFERENCES BenhNhan(MaBenhNhan),
    CONSTRAINT FK_DanhGia_BacSi    FOREIGN KEY (MaBacSi)    REFERENCES BacSi(MaBacSi),
    CONSTRAINT FK_DanhGia_LichHen  FOREIGN KEY (MaLichHen)  REFERENCES LichHen(MaLichHen),
    CONSTRAINT CK_DiemDanhGia CHECK (DiemDanhGia BETWEEN 1 AND 5)
);

-- ============================================================
-- 28. BẢNG TIN TỨC SỨC KHỎE
-- ============================================================
CREATE TABLE TinTuc (
    MaTinTuc        INT AUTO_INCREMENT PRIMARY KEY,
    TieuDe          VARCHAR(200) NOT NULL,
    NoiDung         LONGTEXT,
    TomTat          VARCHAR(500),
    MaChuyenKhoa    VARCHAR(10),
    HinhAnh         VARCHAR(255),
    TacGia          VARCHAR(100),
    NgayDang        DATE DEFAULT (CURRENT_DATE),
    TrangThai       VARCHAR(20) DEFAULT 'Đã đăng',
    LuotXem         INT DEFAULT 0,
    NgayTao         DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_TinTuc_ChuyenKhoa FOREIGN KEY (MaChuyenKhoa) REFERENCES ChuyenKhoa(MaChuyenKhoa)
);

-- ============================================================
-- 29. BẢNG THÔNG BÁO
-- ============================================================
CREATE TABLE ThongBao (
    MaThongBao      INT AUTO_INCREMENT PRIMARY KEY,
    MaTaiKhoan      INT NOT NULL,
    TieuDe          VARCHAR(200) NOT NULL,
    NoiDung         VARCHAR(500),
    LoaiThongBao    VARCHAR(50),
    DuongDanLienKet VARCHAR(255),
    DaDoc           TINYINT(1) DEFAULT 0,
    NgayTao         DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_ThongBao_TaiKhoan FOREIGN KEY (MaTaiKhoan) REFERENCES TaiKhoan(MaTaiKhoan)
);

-- ============================================================
-- 30. BẢNG NHẬT KÝ HOẠT ĐỘNG
-- ============================================================
CREATE TABLE NhatKyHoatDong (
    MaNhatKy    INT AUTO_INCREMENT PRIMARY KEY,
    MaTaiKhoan  INT,
    HanhDong    VARCHAR(200) NOT NULL,
    DoiTuong    VARCHAR(100),
    MaDoiTuong  VARCHAR(50),
    ChiTiet     VARCHAR(500),
    DiaChiIP    VARCHAR(45),
    NgayTao     DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_NhatKy_TaiKhoan FOREIGN KEY (MaTaiKhoan) REFERENCES TaiKhoan(MaTaiKhoan)
);

-- ============================================================
-- 31. BẢNG MÃ KHUYẾN MÃI
-- ============================================================
CREATE TABLE KhuyenMai (
    MaKhuyenMai     INT AUTO_INCREMENT PRIMARY KEY,
    MaCode          VARCHAR(50) NOT NULL UNIQUE,
    TenKhuyenMai    VARCHAR(200) NOT NULL,
    LoaiGiam        VARCHAR(20) NOT NULL,
    GiaTriGiam      DECIMAL(12,0) NOT NULL,
    DonHangToiThieu DECIMAL(12,0) DEFAULT 0,
    SoLuotToiDa     INT,
    SoLuotDaDung    INT DEFAULT 0,
    NgayBatDau      DATE NOT NULL,
    NgayKetThuc     DATE NOT NULL,
    TrangThai       VARCHAR(20) DEFAULT 'Hoạt động',
    NgayTao         DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- 32. BẢNG CÀI ĐẶT HỆ THỐNG
-- ============================================================
CREATE TABLE CaiDatHeThong (
    MaCaiDat    INT AUTO_INCREMENT PRIMARY KEY,
    TenCaiDat   VARCHAR(100) NOT NULL UNIQUE,
    GiaTri      VARCHAR(500),
    MoTa        VARCHAR(200),
    NgayCapNhat DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ============================================================
-- INDEXES
-- ============================================================

CREATE INDEX IX_BenhNhan_HoTen    ON BenhNhan(HoTen);
CREATE INDEX IX_BenhNhan_SDT      ON BenhNhan(SoDienThoai);

CREATE INDEX IX_BacSi_ChuyenKhoa ON BacSi(MaChuyenKhoa);
CREATE INDEX IX_BacSi_HoTen      ON BacSi(HoTen);
CREATE INDEX IX_BacSi_DanhGia    ON BacSi(DiemDanhGiaTB DESC);

CREATE INDEX IX_LichHen_NgayHen  ON LichHen(NgayHen);
CREATE INDEX IX_LichHen_TrangThai ON LichHen(TrangThai);
CREATE INDEX IX_LichHen_BenhNhan ON LichHen(MaBenhNhan);
CREATE INDEX IX_LichHen_BacSi    ON LichHen(MaBacSi);

CREATE INDEX IX_DichVu_ChuyenKhoa ON DichVu(MaChuyenKhoa);
CREATE INDEX IX_DichVu_LoaiDichVu ON DichVu(LoaiDichVu);

CREATE INDEX IX_HoaDon_TrangThai ON HoaDon(TrangThai);
CREATE INDEX IX_HoaDon_NgayTao   ON HoaDon(NgayTao);

CREATE INDEX IX_DonThuoc_BenhNhan ON DonThuoc(MaBenhNhan);

CREATE INDEX IX_KhungGio_NgayKham ON KhungGioKham(NgayKham);

CREATE INDEX IX_LichLamViec_BacSi ON LichLamViec(MaBacSi);

CREATE INDEX IX_ThongBao_ChuaDoc  ON ThongBao(MaTaiKhoan, DaDoc);

-- ============================================================
-- DỮ LIỆU MẪU (SAMPLE DATA)
-- ============================================================

-- 1. Vai trò
INSERT INTO VaiTro (TenVaiTro, MoTa) VALUES
('ADMIN',   'Quản trị viên hệ thống'),
('BAC_SI',  'Bác sĩ khám chữa bệnh'),
('USER',    'Bệnh nhân / Người dùng');

-- 2. Tài khoản
INSERT INTO TaiKhoan (TenDangNhap, MatKhau, Email, HoTen, MaVaiTro, TrangThai) VALUES
('admin',    '$2a$10$hashed_admin_password',   'admin@phongkham.vn',   'Quản trị viên',   1, 'Hoạt động'),
('bsminh',   '$2a$10$hashed_doctor_password',  'minh@phongkham.vn',    'Nguyễn Văn Minh', 2, 'Hoạt động'),
('bslan',    '$2a$10$hashed_doctor_password2', 'lan@phongkham.vn',     'Trần Thị Lan',    2, 'Hoạt động'),
('bsnam',    '$2a$10$hashed_doctor_password3', 'nam@phongkham.vn',     'Lê Hoàng Nam',    2, 'Hoạt động'),
('useran',   '$2a$10$hashed_user_password',    'an@gmail.com',         'Nguyễn Văn An',   3, 'Hoạt động'),
('ktv01',    '$2a$10$hashed_cls_password',     'cls@phongkham.vn',     'Nhân viên CLS',   1, 'Tạm khóa'),
('thungan',  '$2a$10$hashed_payment_password', 'payment@phongkham.vn', 'Thu ngân',        1, 'Hoạt động');

-- 3. Chuyên khoa
INSERT INTO ChuyenKhoa (MaChuyenKhoa, TenChuyenKhoa, SoBacSi, TrangThai) VALUES
('CK01', 'Tim Mạch',             8,  'Hoạt động'),
('CK02', 'Nhi Khoa',             6,  'Hoạt động'),
('CK03', 'Nha Khoa',             10, 'Hoạt động'),
('CK04', 'Da Liễu',              2,  'Tạm khóa'),
('CK05', 'Sản Phụ Khoa',         4,  'Hoạt động'),
('CK06', 'Thần Kinh',            6,  'Hoạt động'),
('CK07', 'Mắt',                  5,  'Hoạt động'),
('CK08', 'Hô Hấp',               7,  'Hoạt động'),
('CK09', 'Tai Mũi Họng',         4,  'Hoạt động'),
('CK10', 'Xương Khớp',           3,  'Hoạt động'),
('CK11', 'Nội Tổng Hợp',         5,  'Hoạt động'),
('CK12', 'Chẩn Đoán Hình Ảnh',   3,  'Hoạt động');

-- 4. Bác sĩ
INSERT INTO BacSi (MaBacSi, MaTaiKhoan, HoTen, MaChuyenKhoa, HocVi, SoNamKinhNghiem, Email, SoDienThoai, PhiKham, GioiThieu, BangCap, ChungChiHanhNghe, SoBenhNhanDaKham, DiemDanhGiaTB, SoLuongDanhGia, PhongKham, TrangThai) VALUES
('BS001', 2, 'Nguyễn Văn Minh', 'CK01', 'TS.BS',  15, 'minh@phongkham.vn', '0901 111 222', 200000,
 'TS.BS. Nguyễn Văn Minh là chuyên gia hàng đầu trong lĩnh vực Tim Mạch với hơn 15 năm kinh nghiệm.',
 'Tiến sĩ Y khoa — ĐH Y Hà Nội — 2005', 'CC-BS-0125/BYT (HSD: 2030)', 1248, 4.9, 120, 'Phòng khám số 3, Tầng 2', 'Đang làm việc'),
('BS002', 3, 'Trần Thị Lan',    'CK02', 'PGS.TS', 20, 'lan@phongkham.vn',  '0902 222 333', 250000,
 'PGS.TS. Trần Thị Lan là chuyên gia Nhi Khoa hàng đầu.', NULL, NULL, 980, 4.8, 98, 'Phòng khám số 5', 'Đang làm việc'),
('BS003', 4, 'Lê Hoàng Nam',    'CK03', 'BS.CKII', 12, 'nam@phongkham.vn', '0903 333 444', 180000,
 'BS.CK2. Lê Hoàng Nam chuyên điều trị các bệnh lý Nha Khoa.', NULL, NULL, 750, 4.7, 75, 'Phòng khám số 7', 'Đang làm việc'),
('BS004', NULL, 'Phạm Quốc Huy', 'CK04', 'BS.CKI', 8,  NULL, NULL, 200000, NULL, NULL, NULL, 320, 4.6, 52, NULL, 'Đang làm việc'),
('BS005', NULL, 'Võ Mai Anh',    'CK05', 'BS.CKI', 10, NULL, NULL, 220000, NULL, NULL, NULL, 500, 4.5, 44, NULL, 'Đang làm việc');

-- 5. Chuyên môn bác sĩ
INSERT INTO ChuyenMonBacSi (MaBacSi, TenChuyenMon) VALUES
('BS001', 'Huyết áp cao'),
('BS001', 'Rối loạn nhịp tim'),
('BS001', 'Suy tim'),
('BS001', 'Bệnh mạch vành'),
('BS001', 'Tiên đường mạch');

-- 6. Bệnh nhân
INSERT INTO BenhNhan (MaBenhNhan, MaTaiKhoan, HoTen, NgaySinh, GioiTinh, Email, SoDienThoai, SoCCCD, DiaChi, NhomMau, ChieuCao, CanNang, SoBHYT, DiUngThuoc, TienSuBenhManTinh, GhiChuYTe, HoTenNguoiThan, QuanHeNguoiThan, SDTNguoiThan) VALUES
('BN-2025-0001', 5, 'Nguyễn Văn An', '1992-03-25', 'Nam', 'an@gmail.com', '0901 234 567', '079xxxxxxxxx',
 '123 Lê Văn Sỹ, Phường 1, Quận 3, TP.HCM', 'O+', 170, 68, 'DN4xxxxxxxxxxx', 'Penicillin',
 'Huyết áp cao, Tiểu đường type 2',
 'Đang sử dụng: Amlodipine 5mg (sáng), Metformin 500mg (sáng, chiều). Thăm khám định kỳ mỗi 3 tháng.',
 'Nguyễn Thị Hoa', 'Vợ / Chồng', '0912 345 678');

-- 7. Dịch vụ
INSERT INTO DichVu (MaDichVu, TenDichVu, MaChuyenKhoa, LoaiDichVu, MoTa, GiaDichVu, ThoiGianThucHien, PhongThucHien, TrangThai) VALUES
('DV001', 'Khám tổng quát',           NULL,   'Khám',                   'Khám, tư vấn và đánh giá sức khỏe tổng quát',            200000, 20, NULL,   'Đang dùng'),
('DV002', 'Xét nghiệm máu',           NULL,   'Xét nghiệm',             'Kiểm tra các chỉ số máu cơ bản, tổng quát',               150000, 30, 'XN01', 'Đang dùng'),
('DV003', 'Siêu âm bụng',             NULL,   'Chẩn đoán hình ảnh',     'Siêu âm đánh giá cấu trúc và chức năng ổ bụng',           250000, 25, 'SA01', 'Đang dùng'),
('DV004', 'X-quang ngực',             'CK12', 'Chẩn đoán hình ảnh',     'Chụp X-quang kiểm tra phổi và lồng ngực',                 180000, 15, 'XQ01', 'Đang dùng'),
('DV005', 'Khám tim mạch tổng quát',  'CK01', 'Khám',                   'Khám, tư vấn và đánh giá sức khỏe tim mạch',              250000, 30, NULL,   'Đang dùng'),
('DV006', 'Siêu âm tim',              'CK01', 'Chẩn đoán hình ảnh',     'Siêu âm đánh giá cấu trúc và chức năng tim',              450000, 45, 'SA01', 'Đang dùng'),
('DV007', 'Điện tâm đồ ECG',          'CK01', 'Xét nghiệm',             'Ghi và phân tích hoạt động điện của tim',                 150000, 15, NULL,   'Đang dùng'),
('DV008', 'Xét nghiệm máu tổng quát', NULL,   'Xét nghiệm',             'Kiểm tra các chỉ số máu cơ bản, tổng quát',               200000, 20, 'XN01', 'Đang dùng'),
('DV009', 'Xét nghiệm nước tiểu',     NULL,   'Xét nghiệm',             'Xét nghiệm nước tiểu tổng quát',                          120000, 15, 'XN02', 'Đang dùng');

-- 8. Thuốc
INSERT INTO Thuoc (TenThuoc, DangBaoChe, DonViTinh, HamLuong, TonKho, NguongCanhBao) VALUES
('Amlodipine 5mg',    'Viên', 'Vỉ', '5mg',   3,   5),
('Metformin 500mg',   'Viên', 'Vỉ', '500mg', 50,  10),
('Omeprazole 20mg',   'Viên', 'Vỉ', '20mg',  120, 20),
('Domperidone 10mg',  'Viên', 'Vỉ', '10mg',  80,  15),
('Paracetamol 500mg', 'Viên', 'Vỉ', '500mg', 300, 50),
('Amoxicillin 500mg', 'Viên', 'Vỉ', '500mg', 200, 30);

-- 9. Vật tư y tế
INSERT INTO VatTuYTe (TenVatTu, DonViTinh, TonKho, NguongCanhBao) VALUES
('Bơm tiêm 5ml',         'Cái',  2, 10),
('Găng tay y tế (hộp)', 'Hộp',  4, 5),
('Que lấy mẫu',          'Cái',  8, 20);

-- 10. Phòng bệnh
INSERT INTO PhongBenh (MaPhong, MaChuyenKhoa, LoaiPhong, SoGiuongToiDa, SoGiuongDangDung, TrangThai) VALUES
('P.201', 'CK01', 'Thường',      6, 3, 'Còn nhận'),
('P.202', 'CK02', 'Thường',      6, 5, 'Còn nhận'),
('P.203', 'CK02', 'Dịch vụ',     4, 1, 'Còn nhận'),
('P.301', 'CK09', 'Xét nghiệm',  0, 0, 'Đang xử lý'),
('P.305', 'CK12', 'Thường',      6, 3, 'Còn nhận'),
('P.401', 'CK01', 'Thường',      6, 2, 'Còn nhận'),
('P.402', 'CK01', 'Dịch vụ',     4, 1, 'Còn nhận');

-- 11. Cài đặt hệ thống
INSERT INTO CaiDatHeThong (TenCaiDat, GiaTri, MoTa) VALUES
('TenPhongKham', 'Phòng Khám Đa Khoa',                           'Tên hiển thị của phòng khám'),
('DiaChi',       '123 Đường ABC, Q.1, TP.HCM',                   'Địa chỉ phòng khám'),
('SoDienThoai',  '028 1234 5678',                                 'Số điện thoại liên hệ'),
('Email',        'info@phongkham.vn',                             'Email liên hệ'),
('GioLamViec',   'Thứ 2-7: 07:00-17:00 | CN: 07:00-12:00',       'Giờ làm việc'),
('PhienBan',     'v1.0.0',                                        'Phiên bản hệ thống');

-- ============================================================
-- VIEWS TIỆN ÍCH
-- ============================================================

-- View: Dashboard thống kê tổng quan
CREATE OR REPLACE VIEW V_DashboardTongQuan AS
SELECT
    (SELECT COUNT(*) FROM LichHen WHERE NgayHen = CURDATE()) AS LichHenHomNay,
    (SELECT COUNT(*) FROM BenhNhan) AS TongBenhNhan,
    (SELECT IFNULL(SUM(TongCanThanhToan), 0)
        FROM HoaDon
        WHERE DATE(NgayTao) = CURDATE() AND TrangThai = 'Đã thanh toán') AS DoanhThuHomNay,
    (SELECT COUNT(*) FROM Thuoc WHERE TonKho <= NguongCanhBao)
    + (SELECT COUNT(*) FROM VatTuYTe WHERE TonKho <= NguongCanhBao) AS SapHetHang;

-- View: Danh sách lịch hẹn hôm nay
CREATE OR REPLACE VIEW V_LichHenHomNay AS
SELECT
    LH.MaLichHen,
    BN.HoTen AS BenhNhan,
    BS.HoTen AS BacSi,
    LH.GioHen,
    LH.TrangThai,
    LH.NgayHen,
    LH.PhongKham
FROM LichHen LH
INNER JOIN BenhNhan BN ON LH.MaBenhNhan = BN.MaBenhNhan
LEFT  JOIN BacSi   BS ON LH.MaBacSi    = BS.MaBacSi
WHERE LH.NgayHen = CURDATE();

-- View: Cảnh báo tồn kho thấp
CREATE OR REPLACE VIEW V_CanhBaoTonKho AS
SELECT TenThuoc  AS TenSanPham, 'Thuốc'   AS LoaiSanPham, TonKho, NguongCanhBao FROM Thuoc   WHERE TonKho <= NguongCanhBao
UNION ALL
SELECT TenVatTu,                'Vật tư'  AS LoaiSanPham, TonKho, NguongCanhBao FROM VatTuYTe WHERE TonKho <= NguongCanhBao;

-- View: Bác sĩ và chuyên khoa
CREATE OR REPLACE VIEW V_BacSiChuyenKhoa AS
SELECT
    BS.MaBacSi, BS.HoTen, BS.HocVi, BS.SoNamKinhNghiem,
    BS.DiemDanhGiaTB, BS.SoLuongDanhGia, BS.PhiKham,
    BS.PhongKham, BS.TrangThai, BS.HinhDaiDien,
    CK.MaChuyenKhoa, CK.TenChuyenKhoa
FROM BacSi BS
INNER JOIN ChuyenKhoa CK ON BS.MaChuyenKhoa = CK.MaChuyenKhoa;

SET FOREIGN_KEY_CHECKS = 1;

SELECT '✅ Database PhongKhamDaKhoa (MySQL) đã được tạo thành công!' AS ThongBao;
SELECT '📊 32 bảng + 4 views + indexes + dữ liệu mẫu' AS ThongTin;