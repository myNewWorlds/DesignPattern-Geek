package com.example.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHAUtil {
    public static String SHA(String msg) {
        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
            byte[] sha256Bin = sha256.digest(msg.getBytes());
            return new String(Base64.getEncoder().encode(sha256Bin));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
