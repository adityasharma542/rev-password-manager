package test;

import org.junit.Test;
import static org.junit.Assert.*;

import util.EncryptionUtil;

public class EncryptionUtilTest {

    @Test
    public void testEncryptNotNull() {
        String enc = EncryptionUtil.encrypt("password123");
        assertNotNull(enc);
    }

    @Test
    public void testSameInputSameOutput() {
        String e1 = EncryptionUtil.encrypt("abc");
        String e2 = EncryptionUtil.encrypt("abc");
        assertEquals(e1, e2);
    }
}
