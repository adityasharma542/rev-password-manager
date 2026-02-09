package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBUtil;
import util.EncryptionUtil;

public class PasswordService {

    // ================= ADD PASSWORD =================
    public static void addPassword(int userId, String accountName, String password) {
        try {
            Connection con = DBUtil.getConnection();

            String sql = "INSERT INTO passwords (user_id, account_name, account_password) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, accountName);
            ps.setString(3, EncryptionUtil.encrypt(password));

            ps.executeUpdate();
            System.out.println("Password added successfully ✅");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= VIEW PASSWORDS =================
    public static void viewPasswords(int userId) {
        try {
            Connection con = DBUtil.getConnection();

            String sql = "SELECT id, account_name, account_password FROM passwords WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            System.out.println("\nSaved Passwords:");
            System.out.println("-------------------------------------------------");
            System.out.printf("%-5s %-20s %-20s%n", "ID", "ACCOUNT", "PASSWORD");
            System.out.println("-------------------------------------------------");

            while (rs.next()) {
                found = true;

                int id = rs.getInt("id");
                String acc = rs.getString("account_name");

                // 🔐 decrypt password before display
                String decryptedPassword =
                        EncryptionUtil.decrypt(rs.getString("account_password"));

                System.out.printf("%-5d %-20s %-20s%n",
                        id, acc, decryptedPassword);
            }

            if (!found) {
                System.out.println("No saved passwords found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE PASSWORD =================
    public static void deletePassword(int passwordId) {
        try {
            Connection con = DBUtil.getConnection();

            String sql = "DELETE FROM passwords WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, passwordId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Password deleted successfully ✅");
            } else {
                System.out.println("Invalid password ID ❌");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
