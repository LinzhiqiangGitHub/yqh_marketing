package com.yqh.marketing.basedevss.base.util;


import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.BaseErrors;
import com.yqh.marketing.basedevss.base.io.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.Validate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encoders {

    private static final String PASSWORD_CRYPT_KEY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    private final static String DES = "DES";

    public static byte[] md5(byte[] bytes) {
        Validate.notNull(bytes);
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(bytes);
            return alga.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new ServerException(BaseErrors.PLATFORM_ENCODE_OR_DECODE_ERROR, e);
        }
    }

    public static byte[] md5(String s) {
        Validate.notNull(s);
        return md5(Charsets.toBytes(s));
    }

    public static String md5Hex(byte[] bytes) {
        return toHex(md5(bytes));
    }

    public static String md5Hex(String s) {
        return toHex(md5(s));
    }
    public static String toMD5(String s) {
        return Hex.encodeHexString(md5(s));
    }
    public static String md5Base64(byte[] bytes) {
        return toBase64(md5(bytes));
    }

    public static String md5Base64(String s) {
        return toBase64(md5(s));
    }

    public static String toHex(byte[] bytes) {
        return Hex.encodeHexString(bytes).toUpperCase();
    }

    public static byte[] fromHex(String s) {
        try {
            return Hex.decodeHex(s.toLowerCase().toCharArray());
        } catch (DecoderException e) {
            throw new ServerException(BaseErrors.PLATFORM_ENCODE_OR_DECODE_ERROR, e);
        }
    }

    public static String toBase64(String s) {
        return toBase64(Charsets.toBytes(s));
    }

    public static String toBase64(byte[] bytes) {
        return com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(bytes);
    }

    public static byte[] fromBase64(String s) {
        return com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(s);
    }

    public static String stringFromBase64(String s) {
        return Charsets.fromBytes(fromBase64(s));
    }

    private static byte[] desEncrypt(byte[] src, byte[] key) {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new ServerException(BaseErrors.PLATFORM_ENCODE_OR_DECODE_ERROR, "Encrypt error", e);
        }
    }

    private static byte[] desDecrypt(byte[] src, byte[] key) {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new ServerException(BaseErrors.PLATFORM_ENCODE_OR_DECODE_ERROR, "Decrypt error", e);
        }
    }

    public static String desEncryptBase64(String src) {
        return toBase64(desEncrypt(Charsets.toBytes(src), Charsets.toBytes(PASSWORD_CRYPT_KEY)));
    }

    public static String desDecryptFromBase64(String src) {
        byte[] data = fromBase64(src);
        return new String(desDecrypt(data, Charsets.toBytes(PASSWORD_CRYPT_KEY)));
    }
}
