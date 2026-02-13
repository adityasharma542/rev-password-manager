package util;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String LETTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*";

    public static String generate(
            int length,
            boolean useLetters,
            boolean useNumbers,
            boolean useSymbols) {

        StringBuilder allowedChars = new StringBuilder();

        if (useLetters) allowedChars.append(LETTERS);
        if (useNumbers) allowedChars.append(NUMBERS);
        if (useSymbols) allowedChars.append(SYMBOLS);

        if (allowedChars.length() == 0) {
            return "❌ Select at least one character type";
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            password.append(allowedChars.charAt(index));
        }

        return password.toString();
    }
}
