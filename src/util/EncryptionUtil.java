package util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

    // ⚠️ 16-byte secret key (AES-128)
    private static final String SECRET_KEY = "MySecretKey12345";

    // 🔐 ENCRYPT
    public static String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key =
                    new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    // 🔓 DECRYPT
    public static String decrypt(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key =
                    new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedBytes =
                    Base64.getDecoder().decode(encryptedValue);

            return new String(cipher.doFinal(decodedBytes));

        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
