package app;

import java.util.Scanner;

import service.UserService;
import service.PasswordService;
import util.OTPUtil;
import util.PasswordGenerator;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int userId = -1; // -1 = not logged in

        while (true) {

            // ================= NOT LOGGED IN =================
            if (userId == -1) {

                System.out.println("\n1. Register");
                System.out.println("2. Login");
                System.out.println("3. Forgot Password (OTP)");
                System.out.println("4. Exit");
                System.out.print("Choose: ");

                String choice = sc.nextLine().trim();

                // -------- REGISTER --------
                if (choice.equals("1")) {

                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();

                    System.out.print("Password: ");
                    String p = sc.nextLine().trim();

                    UserService.register(u, p);

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

                // -------- FORGOT PASSWORD --------
                else if (choice.equals("3")) {

                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();

                    System.out.print("Security Answer (Child name): ");
                    String ans = sc.nextLine().trim();

                    String otp = OTPUtil.generateOTP();
                    System.out.println("Your OTP is: " + otp);

                    System.out.print("Enter OTP: ");
                    String enteredOTP = sc.nextLine().trim();

                    System.out.print("New Password: ");
                    String newPass = sc.nextLine().trim();

                    UserService.forgotPasswordWithOTP(
                            u, ans, enteredOTP, newPass
                    );
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

            // ================= LOGGED IN =================
            else {

                System.out.println("\n1. Add Password");
                System.out.println("2. View Passwords");
                System.out.println("3. Search Password");
                System.out.println("4. Update Password");
                System.out.println("5. Delete Password");
                System.out.println("6. Generate Strong Password");
                System.out.println("7. Update Profile");
                System.out.println("8. Logout");
                System.out.print("Choose: ");

                String choice = sc.nextLine().trim();

                // -------- ADD PASSWORD --------
                if (choice.equals("1")) {

                    System.out.print("Account Name: ");
                    String acc = sc.nextLine().trim();

                    System.out.print("Account Password: ");
                    String pass = sc.nextLine().trim();

                    PasswordService.addPassword(userId, acc, pass);
                }

                // -------- VIEW PASSWORDS --------
                else if (choice.equals("2")) {
                    PasswordService.viewPasswords(userId);
                }

                // -------- SEARCH PASSWORD --------
                else if (choice.equals("3")) {

                    System.out.print("Enter Account Name to search: ");
                    String acc = sc.nextLine().trim();

                    PasswordService.searchPassword(userId, acc);
                }

                // -------- UPDATE PASSWORD --------
                else if (choice.equals("4")) {

                    System.out.print("Enter Password ID to update: ");
                    String idStr = sc.nextLine().trim();

                    if (!idStr.matches("\\d+")) {
                        System.out.println("Invalid ID");
                        continue;
                    }

                    int id = Integer.parseInt(idStr);

                    System.out.print("Enter new password: ");
                    String newPass = sc.nextLine().trim();

                    PasswordService.updatePassword(id, newPass);
                }

                // -------- DELETE PASSWORD --------
                else if (choice.equals("5")) {

                    System.out.print("Enter Password ID to delete: ");
                    String idStr = sc.nextLine().trim();

                    if (!idStr.matches("\\d+")) {
                        System.out.println("Invalid ID");
                        continue;
                    }

                    int id = Integer.parseInt(idStr);
                    PasswordService.deletePassword(id);
                }

                // -------- GENERATE STRONG PASSWORD --------
                else if (choice.equals("6")) {

                    System.out.print("Enter password length: ");
                    int length = Integer.parseInt(sc.nextLine());

                    System.out.print("Include letters? (yes/no): ");
                    boolean letters = sc.nextLine().equalsIgnoreCase("yes");

                    System.out.print("Include numbers? (yes/no): ");
                    boolean numbers = sc.nextLine().equalsIgnoreCase("yes");

                    System.out.print("Include symbols? (yes/no): ");
                    boolean symbols = sc.nextLine().equalsIgnoreCase("yes");

                    String generated =
                            PasswordGenerator.generate(length, letters, numbers, symbols);

                    System.out.println("Generated Password: " + generated);
                }

                // -------- UPDATE PROFILE --------
                else if (choice.equals("7")) {

                    System.out.print("Enter your name: ");
                    String name = sc.nextLine().trim();

                    System.out.print("Enter your email: ");
                    String email = sc.nextLine().trim();

                    UserService.updateProfile(userId, name, email);
                }

                // -------- LOGOUT --------
                else if (choice.equals("8")) {
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
