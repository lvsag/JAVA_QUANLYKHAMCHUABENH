import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/PhongKhamDaKhoa?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT MaPhong, GhiChu, MaBacSiPhuTrach FROM PhongBenh";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("MaPhong: " + rs.getString("MaPhong") + ", GhiChu: '" + rs.getString("GhiChu") + "', MaBacSi: '" + rs.getString("MaBacSiPhuTrach") + "'");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
