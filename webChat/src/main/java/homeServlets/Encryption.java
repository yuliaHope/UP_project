package homeServlets;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Encryption {
    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for (byte b : crypt.digest()) {
                formatter.format("%02x", b);
            }
            sha1 = formatter.toString();
            formatter.close();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algorithm was not found");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Encoding is not supported");
        }
        return sha1;
    }
}
