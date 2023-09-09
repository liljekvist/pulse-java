package se.bth.pulse.Utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class PasswordUtility {
    private static final SecureRandom random = new SecureRandom();
    private static final MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] generateSalt(){
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] hashPassword(byte[] salt, String password){
        md.reset();
        md.update(salt);
        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    public static PasswordPair securePassword(String password){
        byte[] salt = generateSalt();
        byte[] hashed_password = hashPassword(salt, password);
        return new PasswordPair(hashed_password, salt);
    }

    public static Boolean checkPassword(byte[] userSalt, byte[] userHash, String password){
        byte[] hashed_password = hashPassword(userSalt, password);
        return Arrays.equals(hashed_password, userHash);
    }

}
