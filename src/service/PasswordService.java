package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBUtil;
import util.EncryptionUtil;

public class PasswordService {

    // ADD PASSWORD
    public static void addPassword(int userId, String accountName, String password) {
        try {
            Connection con = DBUtil.getConnection();

            String sql = "INSERT INTO passwords(user_id, account_name, account_password) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, accountName);
            ps.setString(3, EncryptionUtil.encrypt(password));

            ps.executeUpdate();
            System.out.println("Password added successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW PASSWORDS
    public static void viewPasswords(int userId) {
        try {
            Connection con = DBUtil.getConnection();

            String sql = "SELECT id, account_name, account_password FROM passwords WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            System.out.println("\nSaved Passwords:");
            while (rs.next()) {
                System.out.println(
                    "ID: " + rs.getInt("id") +
                    " | Account: " + rs.getString("account_name") +
                    " | Password: " + rs.getString("account_password")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE PASSWORD
    public static void deletePassword(int passwordId) {
        try {
            Connection con = DBUtil.getConnection();

            String sql = "DELETE FROM passwords WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, passwordId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Password deleted successfully");
            } else {
                System.out.println("Invalid password ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
