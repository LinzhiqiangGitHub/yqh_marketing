package com.yqh.marketing.common;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.regex.Pattern;

/**
 * Created by chenguo on 2016/3/8.
 */
public class StringFilter {
    static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
    public static boolean validSql(String str) {

        if (sqlPattern.matcher(str).find()) {
            return false;
        }
        return true;
    }
    public static String validXss(String str){
        str=htmlEncode(str);
        str=StringEscapeUtils.escapeSql(str);
        return str;
    }
    public static String htmlEncode(String source) {
        if (source == null) {
            return "";
        }
        String html = "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '"':
                    buffer.append("&quot;");
                    break;
                case 10:
                case 13:
                    break;
                default:
                    buffer.append(c);
            }
        }
        html = buffer.toString();
        return html;
    }


}
