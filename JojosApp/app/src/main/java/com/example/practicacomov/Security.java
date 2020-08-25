package com.example.practicacomov;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class Security {

    public static String hashPassword(String password, byte[] salt) {
        try {
            if (salt == null) {
                salt = new byte[128];
                SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
            }
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, 10000, 512);
            return toHex(salt) + ":" + toHex(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(pbeKeySpec).getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int padding = (array.length * 2) - hex.length();
        if (padding > 0)
            return String.format("%0" + padding + "d", 0) + hex;
        return hex;
    }

    public static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }


}
