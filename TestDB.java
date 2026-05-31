import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/PhongKhamDaKhoa?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database! Fixing data...");

            String sql = "UPDATE PhieuKham SET MaLichHen = 'LH-20260531-7846' WHERE MaPhieuKham = 'PK1780212856839'";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int rows = pstmt.executeUpdate();
                System.out.println("Updated " + rows + " rows to fix MaLichHen in PhieuKham.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
