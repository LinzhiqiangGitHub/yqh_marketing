package com.yqh.marketing.utils;//package com.yqh.platform.service.customs.hangzhou;

import org.apache.commons.codec.binary.Hex;

import org.apache.commons.lang.Validate;

import java.security.MessageDigest;


public class MD5UtilDemo {

    public static byte[] md5(byte[] bytes) {
        Validate.notNull(bytes);
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(bytes);
            return alga.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] md5(String s) {
        Validate.notNull(s);
        return md5(toBytes(s));
    }

    public static byte[] toBytes(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String toMD5(String s) {
        return String.valueOf(Hex.encodeHex(md5(s)));

    }

    public static void main(String[] args) {
        String str = "6bb96d9630c911e9b81400163e0e242fappKey=2018082044793014&count=40&end_time=2019-02-22 11:50:11&method=queryOrder&page=0&start_time=2019-02-22 11:50:10&timestamp=1550820894";
//        String md5Str = MD5UtilDemo.toMD5(str).toLowerCase();
        System.out.println("");
    }

}