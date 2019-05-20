package com.yqh.marketing.basedevss.base.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Method;
import java.security.*;

public class AESUtil {
    public static final AESUtil instance = new AESUtil();

    public static boolean initialized = false;

    /**
     * AES解密
     * @param content 密文
     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchProviderException
     */
    public byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws InvalidAlgorithmParameterException {
        initialize();
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");

            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void initialize(){
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }
    //生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception{
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }
    /***
     * encode by Base64
     */
    public static String encodeBase64(byte[]input) throws Exception{
        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod= clazz.getMethod("encode", byte[].class);
        mainMethod.setAccessible(true);
        Object retObj=mainMethod.invoke(null, new Object[]{input});
        return (String)retObj;
    }
    /***
     * decode by Base64
     */
    public static byte[] decodeBase64(String input) throws Exception{
        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod= clazz.getMethod("decode", String.class);
        mainMethod.setAccessible(true);
        Object retObj=mainMethod.invoke(null, input);
        return (byte[])retObj;
    }

}