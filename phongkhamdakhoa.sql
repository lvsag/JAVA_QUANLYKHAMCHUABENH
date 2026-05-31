-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.45 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for phongkhamdakhoa
CREATE DATABASE IF NOT EXISTS `phongkhamdakhoa` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `phongkhamdakhoa`;

-- Dumping structure for table phongkhamdakhoa.bacsi
CREATE TABLE IF NOT EXISTS `bacsi` (
  `MaBacSi` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaTaiKhoan` int DEFAULT NULL,
  `HoTen` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaChuyenKhoa` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `HocVi` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoNamKinhNghiem` int DEFAULT NULL,
  `Email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoDienThoai` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PhiKham` decimal(12,0) DEFAULT NULL,
  `GioiThieu` text COLLATE utf8mb4_unicode_ci,
  `BangCap` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ChungChiHanhNghe` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoBenhNhanDaKham` int DEFAULT '0',
  `DiemDanhGiaTB` decimal(2,1) DEFAULT '0.0',
  `SoLuongDanhGia` int DEFAULT '0',
  `PhongKham` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HinhDaiDien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Đang làm việc',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaBacSi`),
  UNIQUE KEY `UQ_BacSi_MaTaiKhoan` (`MaTaiKhoan`),
  KEY `IX_BacSi_ChuyenKhoa` (`MaChuyenKhoa`),
  KEY `IX_BacSi_HoTen` (`HoTen`),
  KEY `IX_BacSi_DanhGia` (`DiemDanhGiaTB`),
  CONSTRAINT `FK_BacSi_ChuyenKhoa` FOREIGN KEY (`MaChuyenKhoa`) REFERENCES `chuyenkhoa` (`MaChuyenKhoa`),
  CONSTRAINT `FK_BacSi_TaiKhoan` FOREIGN KEY (`MaTaiKhoan`) REFERENCES `taikhoan` (`MaTaiKhoan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.bacsi: ~5 rows (approximately)
DELETE FROM `bacsi`;
INSERT INTO `bacsi` (`MaBacSi`, `MaTaiKhoan`, `HoTen`, `MaChuyenKhoa`, `HocVi`, `SoNamKinhNghiem`, `Email`, `SoDienThoai`, `PhiKham`, `GioiThieu`, `BangCap`, `ChungChiHanhNghe`, `SoBenhNhanDaKham`, `DiemDanhGiaTB`, `SoLuongDanhGia`, `PhongKham`, `HinhDaiDien`, `TrangThai`, `NgayTao`, `NgayCapNhat`) VALUES
	('BS001', 2, 'Nguyễn Văn Minh', 'CK01', 'TS.BS', 15, 'minh@phongkham.vn', '0901 111 222', 200000, 'TS.BS. Nguyễn Văn Minh là chuyên gia hàng đầu trong lĩnh vực Tim Mạch với hơn 15 năm kinh nghiệm.', 'Tiến sĩ Y khoa — ĐH Y Hà Nội — 2005', 'CC-BS-0125/BYT (HSD: 2030)', 1248, 4.9, 120, 'Phòng khám số 3, Tầng 2', NULL, 'Đang làm việc', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('BS002', 3, 'Trần Thị Lan', 'CK02', 'PGS.TS', 20, 'lan@phongkham.vn', '0902 222 333', 250000, 'PGS.TS. Trần Thị Lan là chuyên gia Nhi Khoa hàng đầu.', NULL, NULL, 980, 4.8, 98, 'Phòng khám số 5', NULL, 'Đang làm việc', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('BS003', 4, 'Lê Hoàng Nam', 'CK03', 'BS.CKII', 12, 'nam@phongkham.vn', '0903 333 444', 180000, 'BS.CK2. Lê Hoàng Nam chuyên điều trị các bệnh lý Nha Khoa.', NULL, NULL, 750, 4.7, 75, 'Phòng khám số 7', NULL, 'Đang làm việc', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('BS004', NULL, 'Phạm Quốc Huy', 'CK04', 'BS.CKI', 8, NULL, NULL, 200000, NULL, NULL, NULL, 320, 4.6, 52, NULL, NULL, 'Đang làm việc', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('BS005', NULL, 'Võ Mai Anh', 'CK05', 'BS.CKI', 10, NULL, NULL, 220000, NULL, NULL, NULL, 500, 4.5, 44, NULL, NULL, 'Đang làm việc', '2026-05-28 18:57:39', '2026-05-28 18:57:39');

-- Dumping structure for table phongkhamdakhoa.benhnhan
CREATE TABLE IF NOT EXISTS `benhnhan` (
  `MaBenhNhan` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaTaiKhoan` int DEFAULT NULL,
  `HoTen` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `NgaySinh` date DEFAULT NULL,
  `GioiTinh` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoDienThoai` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoCCCD` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `QuocTich` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'Việt Nam',
  `DiaChi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NhomMau` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ChieuCao` decimal(5,1) DEFAULT NULL,
  `CanNang` decimal(5,1) DEFAULT NULL,
  `SoBHYT` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayHetHanBHYT` date DEFAULT NULL,
  `DiUngThuoc` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TienSuBenhManTinh` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GhiChuYTe` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HoTenNguoiThan` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `QuanHeNguoiThan` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SDTNguoiThan` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NhanEmailNhacLich` tinyint(1) DEFAULT '1',
  `NhanSMSNhacLich` tinyint(1) DEFAULT '1',
  `NhanEmailKetQua` tinyint(1) DEFAULT '1',
  `NhanTinTucSucKhoe` tinyint(1) DEFAULT '0',
  `HinhDaiDien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaBenhNhan`),
  UNIQUE KEY `MaTaiKhoan` (`MaTaiKhoan`),
  KEY `IX_BenhNhan_HoTen` (`HoTen`),
  KEY `IX_BenhNhan_SDT` (`SoDienThoai`),
  CONSTRAINT `FK_BenhNhan_TaiKhoan` FOREIGN KEY (`MaTaiKhoan`) REFERENCES `taikhoan` (`MaTaiKhoan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.benhnhan: ~2 rows (approximately)
DELETE FROM `benhnhan`;
INSERT INTO `benhnhan` (`MaBenhNhan`, `MaTaiKhoan`, `HoTen`, `NgaySinh`, `GioiTinh`, `Email`, `SoDienThoai`, `SoCCCD`, `QuocTich`, `DiaChi`, `NhomMau`, `ChieuCao`, `CanNang`, `SoBHYT`, `NgayHetHanBHYT`, `DiUngThuoc`, `TienSuBenhManTinh`, `GhiChuYTe`, `HoTenNguoiThan`, `QuanHeNguoiThan`, `SDTNguoiThan`, `NhanEmailNhacLich`, `NhanSMSNhacLich`, `NhanEmailKetQua`, `NhanTinTucSucKhoe`, `HinhDaiDien`, `NgayTao`, `NgayCapNhat`) VALUES
	('BN-1779977287490', 8, 'Đặng Hoài Nam', '2005-06-07', 'Nam', 'danghoainam@gmail.com', '0947826889', '086205000492', 'Việt Nam', '110 Lê Trọng Tấn, Quận Tân Phú, Thành Phố Hồ Chí Minh', 'A+', 171.0, 50.0, 'DN4981938412', NULL, 'Peniciliin', 'Huyết áp cao', 'Mong được giúp đỡ', 'Nguyễn Nhật Trường', 'Con cái', '0908774211', 1, 1, 1, 0, NULL, '2026-05-28 21:08:07', '2026-05-31 13:23:40'),
	('BN-2025-0001', 5, 'Nguyễn Văn An', '1992-03-25', 'Nam', 'an@gmail.com', '0901 234 567', '079xxxxxxxxx', 'Việt Nam', '123 Lê Văn Sỹ, Phường 1, Quận 3, TP.HCM', 'O+', 170.0, 68.0, 'DN4xxxxxxxxxxx', NULL, 'Penicillin', 'Huyết áp cao, Tiểu đường type 2', 'Đang sử dụng: Amlodipine 5mg (sáng), Metformin 500mg (sáng, chiều). Thăm khám định kỳ mỗi 3 tháng.', 'Nguyễn Thị Hoa', 'Vợ / Chồng', '0912 345 678', 1, 1, 1, 0, NULL, '2026-05-28 18:57:39', '2026-05-28 18:57:39');

-- Dumping structure for table phongkhamdakhoa.caidathethong
CREATE TABLE IF NOT EXISTS `caidathethong` (
  `MaCaiDat` int NOT NULL AUTO_INCREMENT,
  `TenCaiDat` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `GiaTri` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MoTa` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaCaiDat`),
  UNIQUE KEY `TenCaiDat` (`TenCaiDat`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.caidathethong: ~6 rows (approximately)
DELETE FROM `caidathethong`;
INSERT INTO `caidathethong` (`MaCaiDat`, `TenCaiDat`, `GiaTri`, `MoTa`, `NgayCapNhat`) VALUES
	(1, 'TenPhongKham', 'Phòng Khám Đa Khoa', 'Tên hiển thị của phòng khám', '2026-05-28 18:57:39'),
	(2, 'DiaChi', '123 Đường ABC, Q.1, TP.HCM', 'Địa chỉ phòng khám', '2026-05-28 18:57:39'),
	(3, 'SoDienThoai', '028 1234 5678', 'Số điện thoại liên hệ', '2026-05-28 18:57:39'),
	(4, 'Email', 'info@phongkham.vn', 'Email liên hệ', '2026-05-28 18:57:39'),
	(5, 'GioLamViec', 'Thứ 2-7: 07:00-17:00 | CN: 07:00-12:00', 'Giờ làm việc', '2026-05-28 18:57:39'),
	(6, 'PhienBan', 'v1.0.0', 'Phiên bản hệ thống', '2026-05-28 18:57:39');

-- Dumping structure for table phongkhamdakhoa.chitietchidinh
CREATE TABLE IF NOT EXISTS `chitietchidinh` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `MaPhieuChiDinh` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaDichVu` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DonGia` decimal(12,0) NOT NULL,
  `PhongThucHien` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Chờ',
  `GhiChu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_CTCD_PhieuChiDinh` (`MaPhieuChiDinh`),
  KEY `FK_CTCD_DichVu` (`MaDichVu`),
  CONSTRAINT `FK_CTCD_DichVu` FOREIGN KEY (`MaDichVu`) REFERENCES `dichvu` (`MaDichVu`),
  CONSTRAINT `FK_CTCD_PhieuChiDinh` FOREIGN KEY (`MaPhieuChiDinh`) REFERENCES `phieuchidinh` (`MaPhieuChiDinh`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.chitietchidinh: ~1 rows (approximately)
DELETE FROM `chitietchidinh`;
INSERT INTO `chitietchidinh` (`Id`, `MaPhieuChiDinh`, `MaDichVu`, `DonGia`, `PhongThucHien`, `TrangThai`, `GhiChu`) VALUES
	(1, 'CD1780216113833', 'DV002', 150000, 'XN01', 'Da xong', NULL),
	(2, 'CD1780229073084', 'DV001', 200000, NULL, 'Da xong', NULL),
	(3, 'CD1780229073084', 'DV002', 150000, 'XN01', 'Cho', NULL),
	(4, 'CD1780229073084', 'DV003', 250000, 'SA01', 'Cho', NULL),
	(5, 'CD1780229073084', 'DV004', 180000, 'XQ01', 'Cho', NULL),
	(6, 'CD1780229073084', 'DV005', 250000, NULL, 'Cho', NULL),
	(7, 'CD1780229073084', 'DV006', 450000, 'SA01', 'Cho', NULL),
	(8, 'CD1780229073084', 'DV007', 150000, 'P.202', 'Cho', NULL),
	(9, 'CD1780229073084', 'DV008', 200000, 'XN01', 'Cho', NULL),
	(10, 'CD1780229073084', 'DV009', 120000, 'XN02', 'Cho', NULL);

-- Dumping structure for table phongkhamdakhoa.chitietdonthuoc
CREATE TABLE IF NOT EXISTS `chitietdonthuoc` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `MaDonThuoc` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaThuoc` int NOT NULL,
  `LieuDung` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoLanNgay` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ThoiGianUong` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoLuong` int NOT NULL,
  `GhiChu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_CTDT_DonThuoc` (`MaDonThuoc`),
  KEY `FK_CTDT_Thuoc` (`MaThuoc`),
  CONSTRAINT `FK_CTDT_DonThuoc` FOREIGN KEY (`MaDonThuoc`) REFERENCES `donthuoc` (`MaDonThuoc`),
  CONSTRAINT `FK_CTDT_Thuoc` FOREIGN KEY (`MaThuoc`) REFERENCES `thuoc` (`MaThuoc`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.chitietdonthuoc: ~1 rows (approximately)
DELETE FROM `chitietdonthuoc`;
INSERT INTO `chitietdonthuoc` (`Id`, `MaDonThuoc`, `MaThuoc`, `LieuDung`, `SoLanNgay`, `ThoiGianUong`, `SoLuong`, `GhiChu`) VALUES
	(2, 'DT1780217549314', 5, '1', '3', NULL, 6, 'Trước mỗi buổi ăn');

-- Dumping structure for table phongkhamdakhoa.chitiethoadon
CREATE TABLE IF NOT EXISTS `chitiethoadon` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `MaHoaDon` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `KhoanMuc` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SoLuong` int DEFAULT '1',
  `DonGia` decimal(12,0) NOT NULL,
  `ThanhTien` decimal(12,0) NOT NULL,
  `GhiChu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_CTHD_HoaDon` (`MaHoaDon`),
  CONSTRAINT `FK_CTHD_HoaDon` FOREIGN KEY (`MaHoaDon`) REFERENCES `hoadon` (`MaHoaDon`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.chitiethoadon: ~3 rows (approximately)
DELETE FROM `chitiethoadon`;
INSERT INTO `chitiethoadon` (`Id`, `MaHoaDon`, `KhoanMuc`, `SoLuong`, `DonGia`, `ThanhTien`, `GhiChu`) VALUES
	(7, 'HD1780218955067', 'Phí khám bệnh (Nguyễn Văn Minh)', 1, 200000, 200000, 'Mã phiếu: PK1780212856839'),
	(8, 'HD1780218955067', 'Dịch vụ CLS: Xét nghiệm máu', 1, 150000, 150000, 'Phiếu CĐ: CD1780216113833'),
	(9, 'HD1780218955067', 'Thuốc: Paracetamol 500mg', 6, 3000, 18000, 'Liều dùng: 1'),
	(10, 'HD1780229219581', 'Phí khám bệnh (Trần Thị Lan)', 1, 250000, 250000, 'Mã phiếu: PK1780228770919'),
	(11, 'HD1780229219581', 'Dịch vụ CLS: Khám tổng quát', 1, 200000, 200000, 'Phiếu CĐ: CD1780229073084'),
	(12, 'HD1780229219581', 'Dịch vụ CLS: Xét nghiệm máu', 1, 150000, 150000, 'Phiếu CĐ: CD1780229073084'),
	(13, 'HD1780229219581', 'Dịch vụ CLS: Siêu âm bụng', 1, 250000, 250000, 'Phiếu CĐ: CD1780229073084'),
	(14, 'HD1780229219581', 'Dịch vụ CLS: X-quang ngực', 1, 180000, 180000, 'Phiếu CĐ: CD1780229073084'),
	(15, 'HD1780229219581', 'Dịch vụ CLS: Khám tim mạch tổng quát', 1, 250000, 250000, 'Phiếu CĐ: CD1780229073084'),
	(16, 'HD1780229219581', 'Dịch vụ CLS: Siêu âm tim', 1, 450000, 450000, 'Phiếu CĐ: CD1780229073084'),
	(17, 'HD1780229219581', 'Dịch vụ CLS: Điện tâm đồ ECG', 1, 150000, 150000, 'Phiếu CĐ: CD1780229073084'),
	(18, 'HD1780229219581', 'Dịch vụ CLS: Xét nghiệm máu tổng quát', 1, 200000, 200000, 'Phiếu CĐ: CD1780229073084'),
	(19, 'HD1780229219581', 'Dịch vụ CLS: Xét nghiệm nước tiểu', 1, 120000, 120000, 'Phiếu CĐ: CD1780229073084');

-- Dumping structure for table phongkhamdakhoa.chitietlichhen
CREATE TABLE IF NOT EXISTS `chitietlichhen` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `MaLichHen` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaDichVu` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SoLuong` int DEFAULT '1',
  `DonGia` decimal(12,0) NOT NULL,
  `GiaGiam` decimal(12,0) DEFAULT '0',
  `ThanhTien` decimal(12,0) NOT NULL,
  `GhiChu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ChiTietLH_LichHen` (`MaLichHen`),
  KEY `FK_ChiTietLH_DichVu` (`MaDichVu`),
  CONSTRAINT `FK_ChiTietLH_DichVu` FOREIGN KEY (`MaDichVu`) REFERENCES `dichvu` (`MaDichVu`),
  CONSTRAINT `FK_ChiTietLH_LichHen` FOREIGN KEY (`MaLichHen`) REFERENCES `lichhen` (`MaLichHen`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.chitietlichhen: ~1 rows (approximately)
DELETE FROM `chitietlichhen`;
INSERT INTO `chitietlichhen` (`Id`, `MaLichHen`, `MaDichVu`, `SoLuong`, `DonGia`, `GiaGiam`, `ThanhTien`, `GhiChu`) VALUES
	(13, 'LH-20260531-7846', 'DV001', 1, 200000, 0, 200000, NULL),
	(14, 'LH-20260531-2036', 'DV001', 1, 200000, 0, 200000, NULL);

-- Dumping structure for table phongkhamdakhoa.chuyenkhoa
CREATE TABLE IF NOT EXISTS `chuyenkhoa` (
  `MaChuyenKhoa` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `TenChuyenKhoa` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MoTa` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HinhAnh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoBacSi` int DEFAULT '0',
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Hoạt động',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaChuyenKhoa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.chuyenkhoa: ~12 rows (approximately)
DELETE FROM `chuyenkhoa`;
INSERT INTO `chuyenkhoa` (`MaChuyenKhoa`, `TenChuyenKhoa`, `MoTa`, `HinhAnh`, `SoBacSi`, `TrangThai`, `NgayTao`, `NgayCapNhat`) VALUES
	('CK01', 'Tim Mạch', NULL, NULL, 8, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK02', 'Nhi Khoa', NULL, NULL, 6, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK03', 'Nha Khoa', NULL, NULL, 10, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK04', 'Da Liễu', NULL, NULL, 2, 'Tạm khóa', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK05', 'Sản Phụ Khoa', NULL, NULL, 4, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK06', 'Thần Kinh', NULL, NULL, 6, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK07', 'Mắt', NULL, NULL, 5, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK08', 'Hô Hấp', NULL, NULL, 7, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK09', 'Tai Mũi Họng', NULL, NULL, 4, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK10', 'Xương Khớp', NULL, NULL, 3, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK11', 'Nội Tổng Hợp', NULL, NULL, 5, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('CK12', 'Chẩn Đoán Hình Ảnh', NULL, NULL, 3, 'Hoạt động', '2026-05-28 18:57:39', '2026-05-28 18:57:39');

-- Dumping structure for table phongkhamdakhoa.chuyenmonbacsi
CREATE TABLE IF NOT EXISTS `chuyenmonbacsi` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `MaBacSi` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `TenChuyenMon` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ChuyenMon_BacSi` (`MaBacSi`),
  CONSTRAINT `FK_ChuyenMon_BacSi` FOREIGN KEY (`MaBacSi`) REFERENCES `bacsi` (`MaBacSi`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.chuyenmonbacsi: ~5 rows (approximately)
DELETE FROM `chuyenmonbacsi`;
INSERT INTO `chuyenmonbacsi` (`Id`, `MaBacSi`, `TenChuyenMon`) VALUES
	(1, 'BS001', 'Huyết áp cao'),
	(2, 'BS001', 'Rối loạn nhịp tim'),
	(3, 'BS001', 'Suy tim'),
	(4, 'BS001', 'Bệnh mạch vành'),
	(5, 'BS001', 'Tiên đường mạch');

-- Dumping structure for table phongkhamdakhoa.danhgia
CREATE TABLE IF NOT EXISTS `danhgia` (
  `MaDanhGia` int NOT NULL AUTO_INCREMENT,
  `MaBenhNhan` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBacSi` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaLichHen` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DiemDanhGia` int NOT NULL,
  `NoiDung` text COLLATE utf8mb4_unicode_ci,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Hiện',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaDanhGia`),
  KEY `FK_DanhGia_BenhNhan` (`MaBenhNhan`),
  KEY `FK_DanhGia_BacSi` (`MaBacSi`),
  KEY `FK_DanhGia_LichHen` (`MaLichHen`),
  CONSTRAINT `FK_DanhGia_BacSi` FOREIGN KEY (`MaBacSi`) REFERENCES `bacsi` (`MaBacSi`),
  CONSTRAINT `FK_DanhGia_BenhNhan` FOREIGN KEY (`MaBenhNhan`) REFERENCES `benhnhan` (`MaBenhNhan`),
  CONSTRAINT `FK_DanhGia_LichHen` FOREIGN KEY (`MaLichHen`) REFERENCES `lichhen` (`MaLichHen`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.danhgia: ~0 rows (approximately)
DELETE FROM `danhgia`;

-- Dumping structure for table phongkhamdakhoa.dichvu
CREATE TABLE IF NOT EXISTS `dichvu` (
  `MaDichVu` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `TenDichVu` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaChuyenKhoa` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LoaiDichVu` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MoTa` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GiaDichVu` decimal(12,0) NOT NULL,
  `GiaGiam` decimal(12,0) DEFAULT NULL,
  `ThoiGianThucHien` int DEFAULT NULL,
  `HinhAnh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PhongThucHien` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Đang dùng',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaDichVu`),
  KEY `IX_DichVu_ChuyenKhoa` (`MaChuyenKhoa`),
  KEY `IX_DichVu_LoaiDichVu` (`LoaiDichVu`),
  CONSTRAINT `FK_DichVu_ChuyenKhoa` FOREIGN KEY (`MaChuyenKhoa`) REFERENCES `chuyenkhoa` (`MaChuyenKhoa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.dichvu: ~9 rows (approximately)
DELETE FROM `dichvu`;
INSERT INTO `dichvu` (`MaDichVu`, `TenDichVu`, `MaChuyenKhoa`, `LoaiDichVu`, `MoTa`, `GiaDichVu`, `GiaGiam`, `ThoiGianThucHien`, `HinhAnh`, `PhongThucHien`, `TrangThai`, `NgayTao`, `NgayCapNhat`) VALUES
	('DV001', 'Khám tổng quát', NULL, 'Khám', 'Khám, tư vấn và đánh giá sức khỏe tổng quát', 200000, NULL, 20, NULL, NULL, 'Đang dùng', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('DV002', 'Xét nghiệm máu', NULL, 'Xét nghiệm', 'Kiểm tra các chỉ số máu cơ bản, tổng quát', 150000, NULL, 30, NULL, 'XN01', 'Đang dùng', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('DV003', 'Siêu âm bụng', NULL, 'Chẩn đoán hình ảnh', 'Siêu âm đánh giá cấu trúc và chức năng ổ bụng', 250000, NULL, 25, NULL, 'SA01', 'Đang dùng', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('DV004', 'X-quang ngực', 'CK12', 'Chẩn đoán hình ảnh', 'Chụp X-quang kiểm tra phổi và lồng ngực', 180000, NULL, 15, NULL, 'XQ01', 'Đang dùng', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('DV005', 'Khám tim mạch tổng quát', 'CK01', 'Khám', 'Khám, tư vấn và đánh giá sức khỏe tim mạch', 250000, NULL, 30, NULL, NULL, 'Đang dùng', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('DV006', 'Siêu âm tim', 'CK01', 'Chẩn đoán hình ảnh', 'Siêu âm đánh giá cấu trúc và chức năng tim', 450000, NULL, 45, NULL, 'SA01', 'Đang dùng', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('DV007', 'Điện tâm đồ ECG', 'CK01', 'Xét nghiệm', 'Ghi và phân tích hoạt động điện của tim', 150000, NULL, 15, '', 'P.202', 'Đang dùng', '2026-05-28 18:57:39', '2026-05-31 17:29:07'),
	('DV008', 'Xét nghiệm máu tổng quát', NULL, 'Xét nghiệm', 'Kiểm tra các chỉ số máu cơ bản, tổng quát', 200000, NULL, 20, NULL, 'XN01', 'Đang dùng', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('DV009', 'Xét nghiệm nước tiểu', NULL, 'Xét nghiệm', 'Xét nghiệm nước tiểu tổng quát', 120000, NULL, 15, NULL, 'XN02', 'Đang dùng', '2026-05-28 18:57:39', '2026-05-28 18:57:39');

-- Dumping structure for table phongkhamdakhoa.donthuoc
CREATE TABLE IF NOT EXISTS `donthuoc` (
  `MaDonThuoc` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaPhieuKham` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBenhNhan` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBacSi` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ChanDoan` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayKeDon` datetime DEFAULT CURRENT_TIMESTAMP,
  `LoiDan` text COLLATE utf8mb4_unicode_ci,
  `TrangThai` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT 'Đang dùng',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaDonThuoc`),
  KEY `FK_DonThuoc_PhieuKham` (`MaPhieuKham`),
  KEY `FK_DonThuoc_BacSi` (`MaBacSi`),
  KEY `IX_DonThuoc_BenhNhan` (`MaBenhNhan`),
  CONSTRAINT `FK_DonThuoc_BacSi` FOREIGN KEY (`MaBacSi`) REFERENCES `bacsi` (`MaBacSi`),
  CONSTRAINT `FK_DonThuoc_BenhNhan` FOREIGN KEY (`MaBenhNhan`) REFERENCES `benhnhan` (`MaBenhNhan`),
  CONSTRAINT `FK_DonThuoc_PhieuKham` FOREIGN KEY (`MaPhieuKham`) REFERENCES `phieukham` (`MaPhieuKham`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.donthuoc: ~1 rows (approximately)
DELETE FROM `donthuoc`;
INSERT INTO `donthuoc` (`MaDonThuoc`, `MaPhieuKham`, `MaBenhNhan`, `MaBacSi`, `ChanDoan`, `NgayKeDon`, `LoiDan`, `TrangThai`, `NgayTao`, `NgayCapNhat`) VALUES
	('DT1780217549314', 'PK1780212856839', 'BN-1779977287490', 'BS001', 'Sốt nhẹ', '2026-05-31 08:52:29', 'Tránh ăn đồ cay nóng', 'Dang dung', '2026-05-31 15:52:29', '2026-05-31 15:52:29');

-- Dumping structure for table phongkhamdakhoa.giuongbenh
CREATE TABLE IF NOT EXISTS `giuongbenh` (
  `MaGiuong` int NOT NULL AUTO_INCREMENT,
  `MaPhong` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SoGiuong` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Trống',
  `GhiChu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`MaGiuong`),
  KEY `FK_Giuong_Phong` (`MaPhong`),
  CONSTRAINT `FK_Giuong_Phong` FOREIGN KEY (`MaPhong`) REFERENCES `phongbenh` (`MaPhong`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.giuongbenh: ~0 rows (approximately)
DELETE FROM `giuongbenh`;

-- Dumping structure for table phongkhamdakhoa.hoadon
CREATE TABLE IF NOT EXISTS `hoadon` (
  `MaHoaDon` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBenhNhan` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaLichHen` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MaNoiTru` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TongTien` decimal(12,0) NOT NULL,
  `GiamBHYT` decimal(12,0) DEFAULT '0',
  `GiamUuDai` decimal(12,0) DEFAULT '0',
  `TongCanThanhToan` decimal(12,0) NOT NULL,
  `SoTienDaThanhToan` decimal(12,0) DEFAULT '0',
  `PhuongThucThanhToan` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT 'Chờ thanh toán',
  `NgayThanhToan` datetime DEFAULT NULL,
  `NguoiThu` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GhiChu` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaHoaDon`),
  KEY `FK_HoaDon_BenhNhan` (`MaBenhNhan`),
  KEY `FK_HoaDon_LichHen` (`MaLichHen`),
  KEY `FK_HoaDon_NoiTru` (`MaNoiTru`),
  KEY `IX_HoaDon_TrangThai` (`TrangThai`),
  KEY `IX_HoaDon_NgayTao` (`NgayTao`),
  CONSTRAINT `FK_HoaDon_BenhNhan` FOREIGN KEY (`MaBenhNhan`) REFERENCES `benhnhan` (`MaBenhNhan`),
  CONSTRAINT `FK_HoaDon_LichHen` FOREIGN KEY (`MaLichHen`) REFERENCES `lichhen` (`MaLichHen`),
  CONSTRAINT `FK_HoaDon_NoiTru` FOREIGN KEY (`MaNoiTru`) REFERENCES `nhapviennoitru` (`MaNoiTru`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.hoadon: ~1 rows (approximately)
DELETE FROM `hoadon`;
INSERT INTO `hoadon` (`MaHoaDon`, `MaBenhNhan`, `MaLichHen`, `MaNoiTru`, `TongTien`, `GiamBHYT`, `GiamUuDai`, `TongCanThanhToan`, `SoTienDaThanhToan`, `PhuongThucThanhToan`, `TrangThai`, `NgayThanhToan`, `NguoiThu`, `GhiChu`, `NgayTao`, `NgayCapNhat`) VALUES
	('HD1780218955067', 'BN-1779977287490', 'LH-20260531-7846', NULL, 368000, 0, 0, 368000, 368000, 'Tiền mặt', 'Đã thanh toán', '2026-05-31 09:18:41', 'Quầy Thu Ngân', NULL, '2026-05-31 16:15:55', '2026-05-31 16:18:41'),
	('HD1780229219581', 'BN-2025-0001', 'LH-20260531-2036', NULL, 2200000, 0, 0, 2200000, 2200000, 'Tiền mặt', 'Đã thanh toán', '2026-05-31 12:08:34', 'Quầy Thu Ngân', NULL, '2026-05-31 19:06:59', '2026-05-31 19:08:34');

-- Dumping structure for table phongkhamdakhoa.ketquadichvu
CREATE TABLE IF NOT EXISTS `ketquadichvu` (
  `MaKetQua` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaPhieuChiDinh` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaDichVu` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBenhNhan` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBacSiChiDinh` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayThucHien` datetime DEFAULT NULL,
  `LoaiMau` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrieuChungBanDau` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ChiSoChinh` text COLLATE utf8mb4_unicode_ci,
  `KetQuaChiTiet` text COLLATE utf8mb4_unicode_ci,
  `NhanDinhDichVu` text COLLATE utf8mb4_unicode_ci,
  `KetLuanBacSi` text COLLATE utf8mb4_unicode_ci,
  `FileKetQua` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `FileHinhAnh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DanhGia` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GhiChu` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Chờ',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaKetQua`),
  KEY `FK_KetQua_PhieuCD` (`MaPhieuChiDinh`),
  KEY `FK_KetQua_DichVu` (`MaDichVu`),
  KEY `FK_KetQua_BenhNhan` (`MaBenhNhan`),
  KEY `FK_KetQua_BacSi` (`MaBacSiChiDinh`),
  CONSTRAINT `FK_KetQua_BacSi` FOREIGN KEY (`MaBacSiChiDinh`) REFERENCES `bacsi` (`MaBacSi`),
  CONSTRAINT `FK_KetQua_BenhNhan` FOREIGN KEY (`MaBenhNhan`) REFERENCES `benhnhan` (`MaBenhNhan`),
  CONSTRAINT `FK_KetQua_DichVu` FOREIGN KEY (`MaDichVu`) REFERENCES `dichvu` (`MaDichVu`),
  CONSTRAINT `FK_KetQua_PhieuCD` FOREIGN KEY (`MaPhieuChiDinh`) REFERENCES `phieuchidinh` (`MaPhieuChiDinh`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.ketquadichvu: ~1 rows (approximately)
DELETE FROM `ketquadichvu`;
INSERT INTO `ketquadichvu` (`MaKetQua`, `MaPhieuChiDinh`, `MaDichVu`, `MaBenhNhan`, `MaBacSiChiDinh`, `NgayThucHien`, `LoaiMau`, `TrieuChungBanDau`, `ChiSoChinh`, `KetQuaChiTiet`, `NhanDinhDichVu`, `KetLuanBacSi`, `FileKetQua`, `FileHinhAnh`, `DanhGia`, `GhiChu`, `TrangThai`, `NgayTao`, `NgayCapNhat`) VALUES
	('KQ1780220699289', 'CD1780216113833', 'DV002', 'BN-1779977287490', 'BS001', '2026-05-31 09:44:59', NULL, NULL, 'Glucose: 5.4 mmol/L\r\nHồng cầu (RBC): 4.8 T/L\r\nBạch cầu (WBC): 7.2 G/L\r\nTiểu cầu (PLT): 250 G/L\r\nHemoglobin (Hb): 145 g/L', '- Glucose máu: 5.4 mmol/L (Bình thường: 3.9 - 6.1 mmol/L)\r\n- Hồng cầu (RBC): 4.8 T/L (Bình thường: 4.2 - 5.9 T/L)\r\n- Bạch cầu (WBC): 7.2 G/L (Bình thường: 4.0 - 10.0 G/L)\r\n- Tiểu cầu (PLT): 250 G/L (Bình thường: 150 - 450 G/L)\r\n- Hemoglobin (Hb): 145 g/L (Bình thường: 130 - 170 g/L)\r\n\r\nCác chỉ số nằm trong giới hạn tham chiếu bình thường.', 'Kết quả xét nghiệm máu trong giới hạn bình thường. Chưa ghi nhận dấu hiệu thiếu máu, nhiễm trùng hoặc rối loạn đường huyết tại thời điểm xét nghiệm.', 'Bình thường', NULL, NULL, NULL, NULL, 'Da xem', '2026-05-31 16:44:59', '2026-05-31 16:45:58'),
	('KQ1780229129987', 'CD1780229073084', 'DV001', 'BN-2025-0001', 'BS002', '2026-05-31 12:05:30', NULL, NULL, 'Bị thần kinh', '', '', 'Ko chữa được', NULL, NULL, NULL, NULL, 'Da xem', '2026-05-31 19:05:29', '2026-05-31 19:06:10');

-- Dumping structure for table phongkhamdakhoa.khunggiokham
CREATE TABLE IF NOT EXISTS `khunggiokham` (
  `MaKhungGio` int NOT NULL AUTO_INCREMENT,
  `MaLichLamViec` int NOT NULL,
  `NgayKham` date NOT NULL,
  `GioBatDau` time NOT NULL,
  `GioKetThuc` time NOT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Còn chỗ',
  PRIMARY KEY (`MaKhungGio`),
  KEY `FK_KhungGio_LichLamViec` (`MaLichLamViec`),
  KEY `IX_KhungGio_NgayKham` (`NgayKham`),
  CONSTRAINT `FK_KhungGio_LichLamViec` FOREIGN KEY (`MaLichLamViec`) REFERENCES `lichlamviec` (`MaLichLamViec`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.khunggiokham: ~4 rows (approximately)
DELETE FROM `khunggiokham`;
INSERT INTO `khunggiokham` (`MaKhungGio`, `MaLichLamViec`, `NgayKham`, `GioBatDau`, `GioKetThuc`, `TrangThai`) VALUES
	(1, 3, '2026-05-31', '15:30:00', '16:00:00', 'Hết chỗ'),
	(2, 3, '2026-05-31', '16:00:00', '16:30:00', 'Hết chỗ'),
	(3, 3, '2026-05-31', '16:30:00', '17:00:00', 'Hết chỗ'),
	(4, 3, '2026-05-31', '17:00:00', '17:30:00', 'Hết chỗ'),
	(5, 4, '2026-05-31', '18:30:00', '19:00:00', 'Hết chỗ'),
	(6, 4, '2026-05-31', '19:00:00', '19:30:00', 'Còn chỗ');

-- Dumping structure for table phongkhamdakhoa.khuyenmai
CREATE TABLE IF NOT EXISTS `khuyenmai` (
  `MaKhuyenMai` int NOT NULL AUTO_INCREMENT,
  `MaCode` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `TenKhuyenMai` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `GiaTriGiam` decimal(12,0) NOT NULL,
  `SoLuotToiDa` int DEFAULT NULL,
  `SoLuotDaDung` int DEFAULT '0',
  `NgayBatDau` date NOT NULL,
  `NgayKetThuc` date NOT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Hoạt động',
  PRIMARY KEY (`MaKhuyenMai`),
  UNIQUE KEY `MaCode` (`MaCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.khuyenmai: ~0 rows (approximately)
DELETE FROM `khuyenmai`;

-- Dumping structure for table phongkhamdakhoa.lichhen
CREATE TABLE IF NOT EXISTS `lichhen` (
  `MaLichHen` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBenhNhan` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBacSi` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MaChuyenKhoa` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayHen` date NOT NULL,
  `GioHen` time NOT NULL,
  `MaKhungGio` int DEFAULT NULL,
  `HoTenNguoiKham` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SDTNguoiKham` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgaySinhNguoiKham` date DEFAULT NULL,
  `EmailXacNhan` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GhiChuTrieuChung` text COLLATE utf8mb4_unicode_ci,
  `HoTenNguoiThanLH` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SDTNguoiThanLH` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CoBHYT` tinyint(1) DEFAULT '0',
  `SoTheBHYT` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT 'Chờ xác nhận',
  `LyDoHuy` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PhuongThucThanhToan` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PhiDuKien` decimal(12,0) DEFAULT NULL,
  `PhongKham` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaLichHen`),
  KEY `FK_LichHen_ChuyenKhoa` (`MaChuyenKhoa`),
  KEY `FK_LichHen_KhungGio` (`MaKhungGio`),
  KEY `IX_LichHen_NgayHen` (`NgayHen`),
  KEY `IX_LichHen_TrangThai` (`TrangThai`),
  KEY `IX_LichHen_BenhNhan` (`MaBenhNhan`),
  KEY `IX_LichHen_BacSi` (`MaBacSi`),
  CONSTRAINT `FK_LichHen_BacSi` FOREIGN KEY (`MaBacSi`) REFERENCES `bacsi` (`MaBacSi`),
  CONSTRAINT `FK_LichHen_BenhNhan` FOREIGN KEY (`MaBenhNhan`) REFERENCES `benhnhan` (`MaBenhNhan`),
  CONSTRAINT `FK_LichHen_ChuyenKhoa` FOREIGN KEY (`MaChuyenKhoa`) REFERENCES `chuyenkhoa` (`MaChuyenKhoa`),
  CONSTRAINT `FK_LichHen_KhungGio` FOREIGN KEY (`MaKhungGio`) REFERENCES `khunggiokham` (`MaKhungGio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.lichhen: ~1 rows (approximately)
DELETE FROM `lichhen`;
INSERT INTO `lichhen` (`MaLichHen`, `MaBenhNhan`, `MaBacSi`, `MaChuyenKhoa`, `NgayHen`, `GioHen`, `MaKhungGio`, `HoTenNguoiKham`, `SDTNguoiKham`, `NgaySinhNguoiKham`, `EmailXacNhan`, `GhiChuTrieuChung`, `HoTenNguoiThanLH`, `SDTNguoiThanLH`, `CoBHYT`, `SoTheBHYT`, `TrangThai`, `LyDoHuy`, `PhuongThucThanhToan`, `PhiDuKien`, `PhongKham`, `NgayTao`, `NgayCapNhat`) VALUES
	('LH-20260531-2036', 'BN-2025-0001', 'BS002', 'CK02', '2026-05-31', '18:30:00', 5, 'Nguyễn Văn An', '0901 234 567', '1992-03-25', 'an@gmail.com', '', 'Nguyễn Thị Hoa', '0912 345 678', 1, 'DN4xxxxxxxxxxx', 'Đã xác nhận', NULL, 'TIEN_MAT', 450000, NULL, '2026-05-31 18:57:37', '2026-05-31 18:59:30'),
	('LH-20260531-7846', 'BN-1779977287490', 'BS001', 'CK01', '2026-05-31', '16:30:00', 3, 'Đặng Hoài Nam', '0947826889', '2005-06-07', 'danghoainam@gmail.com', '', 'Nguyễn Nhật Trường', '0908774211', 1, 'DN4981938412', 'Đã xác nhận', NULL, 'TIEN_MAT', 400000, NULL, '2026-05-31 13:58:00', '2026-05-31 14:34:16');

-- Dumping structure for table phongkhamdakhoa.lichlamviec
CREATE TABLE IF NOT EXISTS `lichlamviec` (
  `MaLichLamViec` int NOT NULL AUTO_INCREMENT,
  `MaBacSi` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `NgayTrongTuan` int NOT NULL,
  `CaLamViec` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `GioBatDau` time NOT NULL,
  `GioKetThuc` time NOT NULL,
  `SoSlotToiDa` int DEFAULT '4',
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Làm việc',
  PRIMARY KEY (`MaLichLamViec`),
  KEY `IX_LichLamViec_BacSi` (`MaBacSi`),
  CONSTRAINT `FK_LichLamViec_BacSi` FOREIGN KEY (`MaBacSi`) REFERENCES `bacsi` (`MaBacSi`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.lichlamviec: ~1 rows (approximately)
DELETE FROM `lichlamviec`;
INSERT INTO `lichlamviec` (`MaLichLamViec`, `MaBacSi`, `NgayTrongTuan`, `CaLamViec`, `GioBatDau`, `GioKetThuc`, `SoSlotToiDa`, `TrangThai`) VALUES
	(3, 'BS001', 8, 'Tối', '15:30:00', '19:30:00', 4, 'Làm việc'),
	(4, 'BS002', 8, 'Tối', '18:30:00', '23:03:00', 2, 'Làm việc');

-- Dumping structure for table phongkhamdakhoa.nhapviennoitru
CREATE TABLE IF NOT EXISTS `nhapviennoitru` (
  `MaNoiTru` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBenhNhan` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBacSiDieuTri` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaPhong` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MaGiuong` int DEFAULT NULL,
  `MaChuyenKhoa` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ChanDoan` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayNhapVien` date NOT NULL,
  `DuKienDieuTri` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayXuatVien` date DEFAULT NULL,
  `TinhTrangRaVien` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Đang điều trị',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaNoiTru`),
  KEY `FK_NoiTru_BenhNhan` (`MaBenhNhan`),
  KEY `FK_NoiTru_BacSi` (`MaBacSiDieuTri`),
  KEY `FK_NoiTru_Phong` (`MaPhong`),
  KEY `FK_NoiTru_Giuong` (`MaGiuong`),
  KEY `FK_NoiTru_ChuyenKhoa` (`MaChuyenKhoa`),
  CONSTRAINT `FK_NoiTru_BacSi` FOREIGN KEY (`MaBacSiDieuTri`) REFERENCES `bacsi` (`MaBacSi`),
  CONSTRAINT `FK_NoiTru_BenhNhan` FOREIGN KEY (`MaBenhNhan`) REFERENCES `benhnhan` (`MaBenhNhan`),
  CONSTRAINT `FK_NoiTru_ChuyenKhoa` FOREIGN KEY (`MaChuyenKhoa`) REFERENCES `chuyenkhoa` (`MaChuyenKhoa`),
  CONSTRAINT `FK_NoiTru_Giuong` FOREIGN KEY (`MaGiuong`) REFERENCES `giuongbenh` (`MaGiuong`),
  CONSTRAINT `FK_NoiTru_Phong` FOREIGN KEY (`MaPhong`) REFERENCES `phongbenh` (`MaPhong`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.nhapviennoitru: ~0 rows (approximately)
DELETE FROM `nhapviennoitru`;

-- Dumping structure for table phongkhamdakhoa.phieuchidinh
CREATE TABLE IF NOT EXISTS `phieuchidinh` (
  `MaPhieuChiDinh` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaPhieuKham` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBenhNhan` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBacSiChiDinh` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `NgayChiDinh` datetime DEFAULT CURRENT_TIMESTAMP,
  `GhiChuChiDinh` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TongTamTinh` decimal(12,0) DEFAULT '0',
  `TrangThai` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT 'Chờ thực hiện',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaPhieuChiDinh`),
  KEY `FK_PhieuCD_PhieuKham` (`MaPhieuKham`),
  KEY `FK_PhieuCD_BenhNhan` (`MaBenhNhan`),
  KEY `FK_PhieuCD_BacSi` (`MaBacSiChiDinh`),
  CONSTRAINT `FK_PhieuCD_BacSi` FOREIGN KEY (`MaBacSiChiDinh`) REFERENCES `bacsi` (`MaBacSi`),
  CONSTRAINT `FK_PhieuCD_BenhNhan` FOREIGN KEY (`MaBenhNhan`) REFERENCES `benhnhan` (`MaBenhNhan`),
  CONSTRAINT `FK_PhieuCD_PhieuKham` FOREIGN KEY (`MaPhieuKham`) REFERENCES `phieukham` (`MaPhieuKham`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.phieuchidinh: ~1 rows (approximately)
DELETE FROM `phieuchidinh`;
INSERT INTO `phieuchidinh` (`MaPhieuChiDinh`, `MaPhieuKham`, `MaBenhNhan`, `MaBacSiChiDinh`, `NgayChiDinh`, `GhiChuChiDinh`, `TongTamTinh`, `TrangThai`, `NgayTao`, `NgayCapNhat`) VALUES
	('CD1780216113833', 'PK1780212856839', 'BN-1779977287490', 'BS001', '2026-05-31 08:28:33', 'Xét nghiệm máu ', 150000, 'Cho thuc hien', '2026-05-31 15:28:33', '2026-05-31 15:28:33'),
	('CD1780229073084', 'PK1780228770919', 'BN-2025-0001', 'BS002', '2026-05-31 12:04:33', '', 1950000, 'Cho thuc hien', '2026-05-31 19:04:33', '2026-05-31 19:04:33');

-- Dumping structure for table phongkhamdakhoa.phieukham
CREATE TABLE IF NOT EXISTS `phieukham` (
  `MaPhieuKham` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaLichHen` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `MaBenhNhan` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaBacSi` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `PhongKham` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NgayKham` datetime DEFAULT CURRENT_TIMESTAMP,
  `TrieuChung` text COLLATE utf8mb4_unicode_ci,
  `TienSuBenh` text COLLATE utf8mb4_unicode_ci,
  `DauHieuSinhTon` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ChanDoanBanDau` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ChanDoanCuoiCung` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `KetLuanBacSi` text COLLATE utf8mb4_unicode_ci,
  `TrangThai` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT 'Chờ',
  `SoThuTu` int DEFAULT NULL,
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaPhieuKham`),
  KEY `FK_PhieuKham_LichHen` (`MaLichHen`),
  KEY `FK_PhieuKham_BenhNhan` (`MaBenhNhan`),
  KEY `FK_PhieuKham_BacSi` (`MaBacSi`),
  CONSTRAINT `FK_PhieuKham_BacSi` FOREIGN KEY (`MaBacSi`) REFERENCES `bacsi` (`MaBacSi`),
  CONSTRAINT `FK_PhieuKham_BenhNhan` FOREIGN KEY (`MaBenhNhan`) REFERENCES `benhnhan` (`MaBenhNhan`),
  CONSTRAINT `FK_PhieuKham_LichHen` FOREIGN KEY (`MaLichHen`) REFERENCES `lichhen` (`MaLichHen`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.phieukham: ~1 rows (approximately)
DELETE FROM `phieukham`;
INSERT INTO `phieukham` (`MaPhieuKham`, `MaLichHen`, `MaBenhNhan`, `MaBacSi`, `PhongKham`, `NgayKham`, `TrieuChung`, `TienSuBenh`, `DauHieuSinhTon`, `ChanDoanBanDau`, `ChanDoanCuoiCung`, `KetLuanBacSi`, `TrangThai`, `SoThuTu`, `NgayTao`, `NgayCapNhat`) VALUES
	('PK1780212856839', 'LH-20260531-7846', 'BN-1779977287490', 'BS001', 'Phòng khám số 3, Tầng 2', '2026-05-31 02:30:00', '', NULL, NULL, NULL, NULL, NULL, 'Cho kham', NULL, '2026-05-31 14:34:16', '2026-05-31 14:34:16'),
	('PK1780228770919', 'LH-20260531-2036', 'BN-2025-0001', 'BS002', 'Phòng khám số 5', '2026-05-31 04:30:00', '', NULL, NULL, NULL, NULL, NULL, 'Cho kham', NULL, '2026-05-31 18:59:30', '2026-05-31 18:59:30');

-- Dumping structure for table phongkhamdakhoa.phongbenh
CREATE TABLE IF NOT EXISTS `phongbenh` (
  `MaPhong` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaChuyenKhoa` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LoaiPhong` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `SoGiuongToiDa` int DEFAULT '6',
  `SoGiuongDangDung` int DEFAULT '0',
  `MaBacSiPhuTrach` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GhiChu` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Còn nhận',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaPhong`),
  KEY `FK_PhongBenh_ChuyenKhoa` (`MaChuyenKhoa`),
  KEY `FK_PhongBenh_BacSi` (`MaBacSiPhuTrach`),
  CONSTRAINT `FK_PhongBenh_BacSi` FOREIGN KEY (`MaBacSiPhuTrach`) REFERENCES `bacsi` (`MaBacSi`),
  CONSTRAINT `FK_PhongBenh_ChuyenKhoa` FOREIGN KEY (`MaChuyenKhoa`) REFERENCES `chuyenkhoa` (`MaChuyenKhoa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.phongbenh: ~7 rows (approximately)
DELETE FROM `phongbenh`;
INSERT INTO `phongbenh` (`MaPhong`, `MaChuyenKhoa`, `LoaiPhong`, `SoGiuongToiDa`, `SoGiuongDangDung`, `MaBacSiPhuTrach`, `GhiChu`, `TrangThai`, `NgayTao`, `NgayCapNhat`) VALUES
	('P.201', 'CK01', 'Thường', 6, 0, NULL, NULL, 'Còn nhận', '2026-05-28 18:57:39', '2026-05-31 15:53:21'),
	('P.202', 'CK02', 'Thường', 6, 0, NULL, NULL, 'Còn nhận', '2026-05-28 18:57:39', '2026-05-31 15:53:21'),
	('P.203', 'CK02', 'Dịch vụ', 4, 0, NULL, NULL, 'Còn nhận', '2026-05-28 18:57:39', '2026-05-31 15:53:21'),
	('P.301', 'CK09', 'Xét nghiệm', 0, 0, NULL, NULL, 'Đang xử lý', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	('P.305', 'CK12', 'Thường', 6, 0, NULL, NULL, 'Còn nhận', '2026-05-28 18:57:39', '2026-05-31 15:53:21'),
	('P.401', 'CK01', 'Thường', 6, 0, NULL, NULL, 'Còn nhận', '2026-05-28 18:57:39', '2026-05-31 15:53:21'),
	('P.402', 'CK01', 'Dịch vụ', 4, 0, NULL, NULL, 'Còn nhận', '2026-05-28 18:57:39', '2026-05-31 15:53:21');

-- Dumping structure for table phongkhamdakhoa.taikhoan
CREATE TABLE IF NOT EXISTS `taikhoan` (
  `MaTaiKhoan` int NOT NULL AUTO_INCREMENT,
  `TenDangNhap` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MatKhau` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `HoTen` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MaVaiTro` int NOT NULL,
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Hoạt động',
  `LanDangNhap` datetime DEFAULT NULL,
  `GhiNhoDangNhap` tinyint(1) DEFAULT '0',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaTaiKhoan`),
  UNIQUE KEY `TenDangNhap` (`TenDangNhap`),
  UNIQUE KEY `Email` (`Email`),
  KEY `FK_TaiKhoan_VaiTro` (`MaVaiTro`),
  CONSTRAINT `FK_TaiKhoan_VaiTro` FOREIGN KEY (`MaVaiTro`) REFERENCES `vaitro` (`MaVaiTro`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.taikhoan: ~8 rows (approximately)
DELETE FROM `taikhoan`;
INSERT INTO `taikhoan` (`MaTaiKhoan`, `TenDangNhap`, `MatKhau`, `Email`, `HoTen`, `MaVaiTro`, `TrangThai`, `LanDangNhap`, `GhiNhoDangNhap`, `NgayTao`, `NgayCapNhat`) VALUES
	(1, 'admin', '$2a$10$cMcg2gU2cxMXahYe8Se72uTT4wi3Np3cXNKSRDM6K5gKaQVEdceZa', 'admin@phongkham.vn', 'Quản trị viên', 1, 'Hoạt động', NULL, 0, '2026-05-28 18:57:39', '2026-05-28 19:45:15'),
	(2, 'bsminh', '$2a$10$.buK6K6.yJvBRpznShyMXe70.UAPl0UevvIVnr/kH/Xg1opnNTuBy', 'minh@phongkham.vn', 'Nguyễn Văn Minh', 2, 'Hoạt động', NULL, 0, '2026-05-28 18:57:39', '2026-05-28 19:45:15'),
	(3, 'bslan', '$2a$10$FIkFQmsYtLKQQ33OVExxG.9CDwzAiJsQIzG0FwZ7zjrnyBOCuA2Ge', 'lan@phongkham.vn', 'Trần Thị Lan', 2, 'Hoạt động', NULL, 0, '2026-05-28 18:57:39', '2026-05-28 19:45:15'),
	(4, 'bsnam', '$2a$10$6kmGU6V5GXfwsyx6MfP.nOQ/FpxqC9gVo4.iUyenFJro5lN/Q6Spy', 'nam@phongkham.vn', 'Lê Hoàng Nam', 2, 'Hoạt động', NULL, 0, '2026-05-28 18:57:39', '2026-05-28 19:45:16'),
	(5, 'useran', '$2a$10$YfSHo0M2XbXBVN/PCDVQeeYpnaEzoiAjPupIgyQtzgIyk09XxPywu', 'an@gmail.com', 'Nguyễn Văn An', 3, 'Tạm khóa', NULL, 0, '2026-05-28 18:57:39', '2026-05-31 19:09:06'),
	(6, 'ktv01', '$2a$10$L78qj4jzxLZ0uRuPKQkWz.1bdmngrWSc0TdPU89s4mvw9WIQwG1ga', 'cls@phongkham.vn', 'Nhân viên CLS', 1, 'Tạm khóa', NULL, 0, '2026-05-28 18:57:39', '2026-05-28 19:45:16'),
	(7, 'thungan', '$2a$10$Z.w9EKOOjARuEWV8.oNLSePdztIG7BbA5zDGjGOkpWKZesjvzSLCa', 'payment@phongkham.vn', 'Thu ngân', 1, 'Hoạt động', NULL, 0, '2026-05-28 18:57:39', '2026-05-28 19:45:16'),
	(8, 'danghoainam@gmail.com', '$2a$10$Xryf3P2GGjSRvTxKlslDNuNuQu5dJbGEhcLmEYs3BOGgiNTKDWsTS', 'danghoainam@gmail.com', 'Đặng Hoài Nam', 3, 'Hoạt động', NULL, 0, '2026-05-28 21:08:07', '2026-05-28 21:08:07');

-- Dumping structure for table phongkhamdakhoa.thuoc
CREATE TABLE IF NOT EXISTS `thuoc` (
  `MaThuoc` int NOT NULL AUTO_INCREMENT,
  `TenThuoc` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `HoatChat` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DangBaoChe` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DonViTinh` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `HamLuong` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `NhaSanXuat` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GiaNhap` decimal(12,0) DEFAULT NULL,
  `GiaBan` decimal(12,0) DEFAULT NULL,
  `TonKho` int DEFAULT '0',
  `NguongCanhBao` int DEFAULT '5',
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Còn hàng',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaThuoc`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.thuoc: ~4 rows (approximately)
DELETE FROM `thuoc`;
INSERT INTO `thuoc` (`MaThuoc`, `TenThuoc`, `HoatChat`, `DangBaoChe`, `DonViTinh`, `HamLuong`, `NhaSanXuat`, `GiaNhap`, `GiaBan`, `TonKho`, `NguongCanhBao`, `TrangThai`, `NgayTao`, `NgayCapNhat`) VALUES
	(5, 'Paracetamol 500mg', '', 'Viên nén', 'Vỉ', '500mg', 'DHG Pharma', 2000, 3000, 294, 50, 'Còn hàng', '2026-05-28 18:57:39', '2026-05-31 15:52:29'),
	(7, 'Amoxicillin 500mg', 'Amoxicillin', 'Viên nang', 'Viên', '500mg', 'Imexpharm', 1000, 2000, 80, 15, 'Còn hàng', '2026-05-31 15:42:33', '2026-05-31 15:42:33'),
	(8, 'Vitamin C 1000mg', 'Acid Ascorbic', 'Viên nang', 'Viên', '1000mg', 'Hasan-Dermapharm', 1000, 2000, 100, 5, 'Còn hàng', '2026-05-31 15:44:21', '2026-05-31 15:44:21'),
	(9, 'Oresol 245', 'Oresol', 'Bột pha hỗn dịch', 'Viên', '27.9g', 'OPC Pharma', 1000, 2000, 200, 5, 'Còn hàng', '2026-05-31 15:45:50', '2026-05-31 15:45:50');

-- Dumping structure for table phongkhamdakhoa.vaitro
CREATE TABLE IF NOT EXISTS `vaitro` (
  `MaVaiTro` int NOT NULL AUTO_INCREMENT,
  `TenVaiTro` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `MoTa` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `TrangThai` tinyint(1) DEFAULT '1',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaVaiTro`),
  UNIQUE KEY `TenVaiTro` (`TenVaiTro`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.vaitro: ~3 rows (approximately)
DELETE FROM `vaitro`;
INSERT INTO `vaitro` (`MaVaiTro`, `TenVaiTro`, `MoTa`, `TrangThai`, `NgayTao`) VALUES
	(1, 'ADMIN', 'Quản trị viên hệ thống', 1, '2026-05-28 18:57:39'),
	(2, 'BAC_SI', 'Bác sĩ khám chữa bệnh', 1, '2026-05-28 18:57:39'),
	(3, 'USER', 'Bệnh nhân / Người dùng', 1, '2026-05-28 18:57:39');

-- Dumping structure for table phongkhamdakhoa.vattuyte
CREATE TABLE IF NOT EXISTS `vattuyte` (
  `MaVatTu` int NOT NULL AUTO_INCREMENT,
  `TenVatTu` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `DonViTinh` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GiaNhap` decimal(12,0) DEFAULT NULL,
  `GiaBan` decimal(12,0) DEFAULT NULL,
  `TonKho` int DEFAULT '0',
  `NguongCanhBao` int DEFAULT '5',
  `TrangThai` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'Còn hàng',
  `NgayTao` datetime DEFAULT CURRENT_TIMESTAMP,
  `NgayCapNhat` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaVatTu`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table phongkhamdakhoa.vattuyte: ~3 rows (approximately)
DELETE FROM `vattuyte`;
INSERT INTO `vattuyte` (`MaVatTu`, `TenVatTu`, `DonViTinh`, `GiaNhap`, `GiaBan`, `TonKho`, `NguongCanhBao`, `TrangThai`, `NgayTao`, `NgayCapNhat`) VALUES
	(1, 'Bơm tiêm 5ml', 'Cái', NULL, NULL, 2, 10, 'Còn hàng', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	(2, 'Găng tay y tế (hộp)', 'Hộp', NULL, NULL, 4, 5, 'Còn hàng', '2026-05-28 18:57:39', '2026-05-28 18:57:39'),
	(3, 'Que lấy mẫu', 'Cái', NULL, NULL, 8, 20, 'Còn hàng', '2026-05-28 18:57:39', '2026-05-28 18:57:39');

-- Dumping structure for view phongkhamdakhoa.v_bacsichuyenkhoa
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_bacsichuyenkhoa` (
	`MaBacSi` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`HoTen` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`HocVi` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`SoNamKinhNghiem` INT NULL,
	`DiemDanhGiaTB` DECIMAL(2,1) NULL,
	`SoLuongDanhGia` INT NULL,
	`PhiKham` DECIMAL(12,0) NULL,
	`PhongKham` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`TrangThai` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`HinhDaiDien` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`MaChuyenKhoa` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`TenChuyenKhoa` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci'
) ENGINE=MyISAM;

-- Dumping structure for view phongkhamdakhoa.v_canhbaotonkho
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_canhbaotonkho` (
	`TenSanPham` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`LoaiSanPham` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`TonKho` INT NULL,
	`NguongCanhBao` INT NULL
) ENGINE=MyISAM;

-- Dumping structure for view phongkhamdakhoa.v_dashboardtongquan
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_dashboardtongquan` (
	`LichHenHomNay` BIGINT NULL,
	`TongBenhNhan` BIGINT NULL,
	`DoanhThuHomNay` DECIMAL(34,0) NULL,
	`SapHetHang` BIGINT NULL
) ENGINE=MyISAM;

-- Dumping structure for view phongkhamdakhoa.v_lichhenhomnay
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_lichhenhomnay` (
	`MaLichHen` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`BenhNhan` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`BacSi` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`GioHen` TIME NOT NULL,
	`TrangThai` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`NgayHen` DATE NOT NULL,
	`PhongKham` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci'
) ENGINE=MyISAM;

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_bacsichuyenkhoa`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_bacsichuyenkhoa` AS select `bs`.`MaBacSi` AS `MaBacSi`,`bs`.`HoTen` AS `HoTen`,`bs`.`HocVi` AS `HocVi`,`bs`.`SoNamKinhNghiem` AS `SoNamKinhNghiem`,`bs`.`DiemDanhGiaTB` AS `DiemDanhGiaTB`,`bs`.`SoLuongDanhGia` AS `SoLuongDanhGia`,`bs`.`PhiKham` AS `PhiKham`,`bs`.`PhongKham` AS `PhongKham`,`bs`.`TrangThai` AS `TrangThai`,`bs`.`HinhDaiDien` AS `HinhDaiDien`,`ck`.`MaChuyenKhoa` AS `MaChuyenKhoa`,`ck`.`TenChuyenKhoa` AS `TenChuyenKhoa` from (`bacsi` `bs` join `chuyenkhoa` `ck` on((`bs`.`MaChuyenKhoa` = `ck`.`MaChuyenKhoa`)));

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_canhbaotonkho`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_canhbaotonkho` AS select `thuoc`.`TenThuoc` AS `TenSanPham`,'Thuốc' AS `LoaiSanPham`,`thuoc`.`TonKho` AS `TonKho`,`thuoc`.`NguongCanhBao` AS `NguongCanhBao` from `thuoc` where (`thuoc`.`TonKho` <= `thuoc`.`NguongCanhBao`) union all select `vattuyte`.`TenVatTu` AS `TenVatTu`,'Vật tư' AS `LoaiSanPham`,`vattuyte`.`TonKho` AS `TonKho`,`vattuyte`.`NguongCanhBao` AS `NguongCanhBao` from `vattuyte` where (`vattuyte`.`TonKho` <= `vattuyte`.`NguongCanhBao`);

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_dashboardtongquan`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_dashboardtongquan` AS select (select count(0) from `lichhen` where (`lichhen`.`NgayHen` = curdate())) AS `LichHenHomNay`,(select count(0) from `benhnhan`) AS `TongBenhNhan`,(select ifnull(sum(`hoadon`.`TongCanThanhToan`),0) from `hoadon` where ((cast(`hoadon`.`NgayTao` as date) = curdate()) and (`hoadon`.`TrangThai` = 'Đã thanh toán'))) AS `DoanhThuHomNay`,((select count(0) from `thuoc` where (`thuoc`.`TonKho` <= `thuoc`.`NguongCanhBao`)) + (select count(0) from `vattuyte` where (`vattuyte`.`TonKho` <= `vattuyte`.`NguongCanhBao`))) AS `SapHetHang`;

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_lichhenhomnay`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_lichhenhomnay` AS select `lh`.`MaLichHen` AS `MaLichHen`,`bn`.`HoTen` AS `BenhNhan`,`bs`.`HoTen` AS `BacSi`,`lh`.`GioHen` AS `GioHen`,`lh`.`TrangThai` AS `TrangThai`,`lh`.`NgayHen` AS `NgayHen`,`lh`.`PhongKham` AS `PhongKham` from ((`lichhen` `lh` join `benhnhan` `bn` on((`lh`.`MaBenhNhan` = `bn`.`MaBenhNhan`))) left join `bacsi` `bs` on((`lh`.`MaBacSi` = `bs`.`MaBacSi`))) where (`lh`.`NgayHen` = curdate());

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
