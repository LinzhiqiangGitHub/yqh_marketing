package com.yqh.marketing.basedevss.base.util;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * Created by pingchen on 15/12/29.
 */
public final class StringUtil {

    private final static String MONEY_DIMES_FMT = "0.0";

    private final static String MONEY_CENTS_FMT = "0.00";

    private final static String MONEY_INTEGER_FMT = "0";

    private final static String MONEY_LIKE_FMT = "#.##";


    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    //金额验证
    public static boolean isNumber(String str){
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(str);
        if(match.matches()==false){
            return false;
        }else{
            return true;
        }
    }

    public static String fmtIntegerMoney(Object obj){
        return fmt(obj,MONEY_INTEGER_FMT);
    }

    public static String fmtCentsMoney(Object obj){
        return fmt(obj,MONEY_CENTS_FMT);
    }

    public static String fmtDimesMoney(Object obj){
        return fmt(obj,MONEY_DIMES_FMT);
    }

    public static String fmtLikeMoney(Object obj){
        return fmt(obj,MONEY_LIKE_FMT);
    }

    public static String fmt(Object obj, String fmt){
        return obj == null ? "" : new DecimalFormat(fmt).format(obj);
    }

    public static boolean find(String str, String regex){
        return Pattern.compile(regex).matcher(str).find();
    }

    public static boolean isEmpty(String str){
        if(str != null && !Objects.equals(str, "")){
            return false;
        }else {
            return true;
        }
    }
}
