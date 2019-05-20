package com.yqh.marketing.basedevss.base.util;

/**
 * XSS工具类
 * Created by jinwei on 15/1/2016.
 */
public class XssUtils {

    /**
     * 转义 < >
     * @param value
     * @return
     */
    public static String replaceXss(String value){
        if(value != null && value.length() > 0){
            return value.replaceAll("<", "&lt;").replace(">", "&gt;");
        }else {
            return value;
        }
    }
}
