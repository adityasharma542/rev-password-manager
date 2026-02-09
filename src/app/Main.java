package app;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import service.UserService;
import service.PasswordService;
import util.OTPUtil;

public class Main {

    // 🔥 LOGGER DECLARATION (CLASS LEVEL)
    private static final Logger logger =
            LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        // 🔎 STARTUP CHECK (VERY IMPORTANT)
        System.out.println(">>> SYSTEM PRINT WORKING <<<");
        logger.info(">>> LOG4J PRINT WORKING <<<");

        logger.info("Password Manager Application Started");

        Scanner sc = new Scanner(System.in);
        int userId = -1; // -1 = not logged in

        while (true) {

            // ================= NOT LOGGED IN MENU =================
            if (userId == -1) {

                System.out.println("\n1. Register");
                System.out.println("2. Login");
                System.out.println("3. Forgot Password (OTP)");
                System.out.println("4. Exit");
                System.out.print("Choose: ");

                String choice = sc.nextLine().trim();

                if (choice.isEmpty()) {
                    System.out.println("Please enter a choice");
                    logger.warn("Empty menu choice entered (not logged in)");
                    continue;
                }

                // -------- REGISTER --------
                if (choice.equals("1")) {

                    logger.info("User selected REGISTER");

                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();

                    System.out.print("Password: ");
                    String p = sc.nextLine().trim();

                    UserService.register(u, p);
                    logger.info("User registration attempted for username: {}", u);

                    // CHILD NAME TAKEN ONLY ONCE (REGISTRATION)
                    System.out.print("Enter your child name (used for password recovery): ");
                    String childName = sc.nextLine().trim();

                    int uid = UserService.login(u, p);
                    if (uid != -1) {
                        UserService.addSecurityQuestion(
                                uid,
                                "What is your child name?",
                                childName
                        );
                        logger.info("Security question added for userId: {}", uid);
                    }
                }

                // -------- LOGIN --------
                else if (choice.equals("2")) {

                    logger.info("User selected LOGIN");

                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();

                    System.out.print("Password: ");
                    String p = sc.nextLine().trim();

                    userId = UserService.login(u, p);

                    if (userId != -1) {
                        logger.info("Login successful for username: {} (userId={})", u, userId);
                    } else {
                        logger.warn("Login failed for username: {}", u);
                    }
                }

                // -------- FORGOT PASSWORD WITH OTP --------
                else if (choice.equals("3")) {

                    logger.info("User selected FORGOT PASSWORD");

                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();

                    System.out.print("Security Answer (Child name): ");
                    String ans = sc.nextLine().trim();

                    String otp = OTPUtil.generateOTP();
                    System.out.println("Your OTP is: " + otp + " (One-time use)");
                    logger.info("OTP generated for user: {}", u);

                    System.out.print("Enter OTP: ");
                    String enteredOTP = sc.nextLine().trim();

                    System.out.print("New Password: ");
                    String newPass = sc.nextLine().trim();

                    UserService.forgotPasswordWithOTP(
                            u, ans, enteredOTP, newPass);

                    logger.info("Forgot-password flow executed for username: {}", u);
                }

                // -------- EXIT --------
                else if (choice.equals("4")) {
                    logger.info("Application exited by user");
                    System.out.println("Bye 👋");
                    break;
                }

                else {
                    System.out.println("Invalid choice");
                    logger.warn("Invalid menu choice entered (not logged in): {}", choice);
                }
            }

            // ================= LOGGED IN MENU =================
            else {

                System.out.println("\n1. Add Password");
                System.out.println("2. View Passwords");
                System.out.println("3. Delete Password");
                System.out.println("4. Logout");
                System.out.print("Choose: ");

                String choice = sc.nextLine().trim();

                if (choice.isEmpty()) {
                    System.out.println("Please enter a choice");
                    logger.warn("Empty menu choice entered (logged in userId={})", userId);
                    continue;
                }

                if (choice.equals("1")) {

                    logger.info("User selected ADD PASSWORD (userId={})", userId);

                    System.out.print("Account Name: ");
                    String acc = sc.nextLine().trim();

                    System.out.print("Account Password: ");
                    String pass = sc.nextLine().trim();

                    PasswordService.addPassword(userId, acc, pass);
                    logger.info("Password added for account '{}' (userId={})", acc, userId);
                }

                else if (choice.equals("2")) {

                    logger.info("User selected VIEW PASSWORDS (userId={})", userId);
                    PasswordService.viewPasswords(userId);
                }

                else if (choice.equals("3")) {

                    logger.info("User selected DELETE PASSWORD (userId={})", userId);

                    System.out.print("Enter Password ID to delete: ");
                    String idStr = sc.nextLine().trim();

                    if (!idStr.matches("\\d+")) {
                        System.out.println("Invalid ID");
                        logger.warn("Invalid password ID entered: {}", idStr);
                        continue;
                    }

                    int id = Integer.parseInt(idStr);
                    PasswordService.deletePassword(id);
                    logger.info("Password deleted with ID: {}", id);
                }

                else if (choice.equals("4")) {
                    userId = -1;
                    logger.info("User logged out");
                    System.out.println("Logged out");
                }

                else {
                    System.out.println("Invalid choice");
                    logger.warn("Invalid menu choice entered (logged in): {}", choice);
                }
            }
        }

        sc.close();
        logger.info("Scanner closed, application terminated");
    }
}
