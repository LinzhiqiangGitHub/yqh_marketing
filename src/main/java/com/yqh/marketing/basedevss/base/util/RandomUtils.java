package com.yqh.marketing.basedevss.base.util;


import java.util.Random;

public class RandomUtils {
    private static final Random RANDOM = new Random();
    public static final String ALLCHAR = "123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
    public static final String LETTERCHAR = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
    public static final String NUMBERCHAR = "0123456789";
    public static String generateRandomNumberString(int len) {
        Random random = new Random();
        StringBuilder buff = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            buff.append(random.nextInt(10));
        }
        return buff.toString();
    }

    public static synchronized long generateId() {
        return generateId(DateUtils.nowMillis());
    }
    public static synchronized String generateStrId() {
        return String.valueOf(generateId(DateUtils.nowMillis()));
    }

    public static synchronized long generateId(long timestamp) {
        return (timestamp << 21) | ((long)RANDOM.nextInt(2097152));
    }

    public static synchronized String generateStrId1() {
        return String.valueOf(generateId1(DateUtils.nowMillis()));
    }
    public static synchronized long generateId1(long timestamp) {
        return (timestamp << 14) | ((long)RANDOM.nextInt(2097152));
    }
    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }
    /**
     * 返回一个定长的随机字符串(只包含数字)
     *
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateNumString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
        }
        return sb.toString();
    }
    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     *
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateMixString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(LETTERCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
     *
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
     *
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    /**
     * 生成一个定长的纯0字符串
     *
     * @param length
     *            字符串长度
     * @return 纯0字符串
     */
    public static String generateZeroString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param num
     *            数字
     * @param fixdlenth
     *            字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(long num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            strNum=strNum.substring(strNum.length()-fixdlenth );
        }
        sb.append(strNum);
        return sb.toString();
    }
}
