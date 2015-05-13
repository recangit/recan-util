package se.recan.utils;

import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;

/**
 * @date 2014-jul-26
 * @author Anders Recksén (recan)
 */
public class SecurityUtilTest {

    private static final Logger LOGGER = Logger.getLogger("SecurityTest");

    @Test
    public void testEncrypt() throws UnsupportedEncodingException {
        LOGGER.debug("");

        String original = "Ungarna";
        String encrypted = SecurityUtil.encrypt(original);
        
        Assert.assertNotEquals(original, encrypted);
    }

}