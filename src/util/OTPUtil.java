package util;

import java.util.Random;

public class OTPUtil {

    private static String currentOTP;

    // Generate 6-digit OTP
    public static String generateOTP() {
        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000);
        currentOTP = String.valueOf(otp);
        return currentOTP;
    }

    // Validate OTP (one-time use)
    public static boolean validateOTP(String inputOTP) {
        if (currentOTP != null && currentOTP.equals(inputOTP)) {
            currentOTP = null; // invalidate after use
            return true;
        }
        return false;
    }
}
