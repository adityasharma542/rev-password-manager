package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521/ORCLPDB";
    private static final String USER = "PASS_USER";
    private static final String PASSWORD = "pass123";

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Oracle JDBC Driver Loaded");
        } catch (Exception e) {
            throw new RuntimeException("Oracle JDBC Driver Load Failed");
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Oracle DB Connection Failed", e);
        }
    }
}
