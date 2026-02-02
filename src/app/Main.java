package app;

import java.util.Scanner;

import service.UserService;
import service.PasswordService;
import util.OTPUtil;

public class Main {

    public static void main(String[] args) {

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
                    continue;
                }

                // -------- REGISTER --------
                if (choice.equals("1")) {

                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();

                    System.out.print("Password: ");
                    String p = sc.nextLine().trim();

                    UserService.register(u, p);

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
                    }
                }

                // -------- LOGIN --------
                else if (choice.equals("2")) {

                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();

                    System.out.print("Password: ");
                    String p = sc.nextLine().trim();

                    userId = UserService.login(u, p);
                }

                // -------- FORGOT PASSWORD WITH OTP --------
                else if (choice.equals("3")) {

                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();

                    // VERIFY SECURITY ANSWER
                    System.out.print("Security Answer (Child name): ");
                    String ans = sc.nextLine().trim();

                    // Generate OTP
                    String otp = OTPUtil.generateOTP();
                    System.out.println("Your OTP is: " + otp + " (One-time use)");

                    System.out.print("Enter OTP: ");
                    String enteredOTP = sc.nextLine().trim();

                    System.out.print("New Password: ");
                    String newPass = sc.nextLine().trim();

                    UserService.forgotPasswordWithOTP(
                            u, ans, enteredOTP, newPass);
                }

                // -------- EXIT --------
                else if (choice.equals("4")) {
                    System.out.println("Bye 👋");
                    break;
                }

                else {
                    System.out.println("Invalid choice");
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
                    continue;
                }

                if (choice.equals("1")) {

                    System.out.print("Account Name: ");
                    String acc = sc.nextLine().trim();

                    System.out.print("Account Password: ");
                    String pass = sc.nextLine().trim();

                    PasswordService.addPassword(userId, acc, pass);
                }

                else if (choice.equals("2")) {
                    PasswordService.viewPasswords(userId);
                }

                else if (choice.equals("3")) {

                    System.out.print("Enter Password ID to delete: ");
                    String idStr = sc.nextLine().trim();

                    if (!idStr.matches("\\d+")) {
                        System.out.println("Invalid ID");
                        continue;
                    }

                    int id = Integer.parseInt(idStr);
                    PasswordService.deletePassword(id);
                }

                else if (choice.equals("4")) {
                    userId = -1;
                    System.out.println("Logged out");
                }

                else {
                    System.out.println("Invalid choice");
                }
            }
        }

        sc.close();
    }
}
