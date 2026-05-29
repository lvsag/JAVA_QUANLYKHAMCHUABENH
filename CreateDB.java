import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDB {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:54237;databaseName=master;integratedSecurity=true;trustServerCertificate=true;encrypt=false";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            
            // Check if database exists
            String sqlCheck = "IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'PhongKhamDaKhoa') BEGIN CREATE DATABASE PhongKhamDaKhoa END";
            stmt.execute(sqlCheck);
            System.out.println("Database PhongKhamDaKhoa created successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
