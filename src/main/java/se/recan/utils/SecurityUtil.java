package se.recan.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Anders Recksén (anders[at]recksen[dot]se)
 */
public class SecurityUtil {

    /**
     *
     * @param password
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String encrypt(String password) throws UnsupportedEncodingException {
        byte[] defaultBytes = password.getBytes("UTF-8");

        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            password = hexString.toString();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return password;
    }
}
