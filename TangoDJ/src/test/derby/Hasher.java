package test.derby;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Hasher {
    /**
     * Gets the md5 hash of the passed in bytes
     *
     * @param bytes The bytes to determine the md5 hash of
     * @return a String containing the md5 hash string
     */
    public String getMd5Hash(byte[] bytes) {
        Formatter fm = new Formatter();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(bytes);
            bytes = digest.digest();
            for (byte b : bytes) {
                fm.format("%02x", b);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception: " + e);
        }

        return fm.out().toString();
    }

    public static void main(String[] args) {
        Hasher hasher = new Hasher();
        String hash = hasher.getMd5Hash("what is the md5 hash of this?".getBytes());
        System.out.println("hash = " + hash);
    }
}