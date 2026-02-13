package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.OTPUtil;
import util.DBUtil;
import util.EncryptionUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserService {

    // ================= LOG4J LOGGER =================
    private static final Logger logger =
            LogManager.getLogger(UserService.class);

    // ================= REGISTER USER =================
    public static void register(String username, String password) {
        try {
            Connection con = DBUtil.getConnection();

            String sql =
                    "INSERT INTO users(username, master_password) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, EncryptionUtil.encrypt(password));

            ps.executeUpdate();

            logger.info("User registered successfully: {}", username);
            System.out.println("Registered Successfully");

        } catch (Exception e) {
            logger.error("Registration Failed for user: {}", username, e);
            System.out.println("Registration Failed");
        }
    }

    // ================= LOGIN USER =================
    public static int login(String username, String password) {
        try {
            Connection con = DBUtil.getConnection();

            String sql =
                    "SELECT user_id FROM users WHERE username=? AND master_password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, EncryptionUtil.encrypt(password));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Login Successful for user: {}", username);
                System.out.println("Login Successful");
                return rs.getInt("user_id");
            } else {
                logger.warn("Invalid login attempt for user: {}", username);
                System.out.println("Invalid Username or Password");
                return -1;
            }

        } catch (Exception e) {
            logger.error("Login error for user: {}", username, e);
            return -1;
        }
    }

    // ================= ADD SECURITY QUESTION =================
    public static void addSecurityQuestion(int userId, String question, String answer) {
        try {
            Connection con = DBUtil.getConnection();

            String sql =
                    "INSERT INTO security_questions(user_id, question, answer) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, question);
            ps.setString(3, EncryptionUtil.encrypt(answer));

            ps.executeUpdate();

            logger.info("Security question saved for userId: {}", userId);
            System.out.println("Security question saved");

        } catch (Exception e) {
            logger.error("Error saving security question for userId: {}", userId, e);
        }
    }

    // ================= FORGOT PASSWORD WITH OTP =================
    public static void forgotPasswordWithOTP(
            String username,
            String answer,
            String enteredOTP,
            String newPassword) {

        try {
            Connection con = DBUtil.getConnection();

            String sql =
                    "SELECT u.user_id FROM users u " +
                            "JOIN security_questions s ON u.user_id = s.user_id " +
                            "WHERE u.username=? AND s.answer=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, EncryptionUtil.encrypt(answer));

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                logger.warn("Security answer incorrect for user: {}", username);
                System.out.println("Security answer incorrect");
                return;
            }

            // OTP VALIDATION
            if (!OTPUtil.validateOTP(enteredOTP)) {
                logger.warn("Invalid or expired OTP for user: {}", username);
                System.out.println("Invalid or Expired OTP");
                return;
            }

            int userId = rs.getInt("user_id");

            String updateSql =
                    "UPDATE users SET master_password=? WHERE user_id=?";
            PreparedStatement ups = con.prepareStatement(updateSql);
            ups.setString(1, EncryptionUtil.encrypt(newPassword));
            ups.setInt(2, userId);
            ups.executeUpdate();

            logger.info("Password reset successful using OTP for user: {}", username);
            System.out.println("Password reset successful using OTP");

        } catch (Exception e) {
            logger.error("Forgot password with OTP error for user: {}", username, e);
        }
    }

    // ================= UPDATE PROFILE (NAME & EMAIL) =================
    public static void updateProfile(int userId, String name, String email) {
        try {
            Connection con = DBUtil.getConnection();

            String sql =
                    "UPDATE users SET name=?, email=? WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, userId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Profile updated for userId: {}", userId);
                System.out.println("Profile updated successfully ✅");
            } else {
                logger.warn("Profile update failed for userId: {}", userId);
                System.out.println("Profile update failed ❌");
            }

        } catch (Exception e) {
            logger.error("Error updating profile for userId: {}", userId, e);
        }
    }
}
