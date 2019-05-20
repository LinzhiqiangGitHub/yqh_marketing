package com.yqh.marketing.basedevss.base.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by chenguo on 2017/7/5.
 */
public class NumberUtils {
    /**
     *
     * @param num 数字
     * @param decimalDigits 小数位数
     * @return
     */
    public static String getPercent(double num,int decimalDigits){
        if(Double.isNaN(num)){
            return "";
        }
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(decimalDigits);
        //最后格式化并输出
        return nt.format(num);
    }
    public static String getPercent(double num,int minDecimalDigits,int maxDecimalDigits){
        if(Double.isNaN(num)){
            return "";
        }
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(minDecimalDigits);
        nt.setMaximumFractionDigits(maxDecimalDigits);
        //最后格式化并输出
        return nt.format(num);
    }
    /**
     *
     * @param num 数字
     * @param decimalDigits 小数位数
     * @return
     */
    public static String getPercent(float num,int decimalDigits){
        if(Float.isNaN(num)){
            return "";
        }
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(decimalDigits);
        //最后格式化并输出
        return nt.format(num);
    }
    public static String getPercent(float num,int minDecimalDigits,int maxDecimalDigits){
        if(Float.isNaN(num)){
            return "";
        }
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(minDecimalDigits);
        nt.setMaximumFractionDigits(maxDecimalDigits);
        //最后格式化并输出
        return nt.format(num);
    }


    public static double format(double num,int decimalDigits){
        BigDecimal b   =   new   BigDecimal(num);
        double   f1   =   b.setScale(decimalDigits,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
    public static float format(float num,int decimalDigits){
        BigDecimal b   =   new   BigDecimal(num);
        float   f1   =   b.setScale(decimalDigits,   BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }
    /**
     *
     * @param num 要格式化的数字，1.12933
     * @param format 格式：如：#.00 表示两位小数
     * @return
     */
    public static double format(double num,String format){
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat(format);
        return Double.parseDouble(df.format(num));
    }
    /**
     *
     * @param num 要格式化的数字，1.12933
     * @param format 格式：如：#.00 表示两位小数
     * @return
     */
    public static float format(float num,String format){
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat(format);
        return Float.parseFloat(df.format(num));
    }
    public static <T> String formatString(T num,String format){
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat(format);
        return df.format(num);
    }
    public static <T> String formatString(T num,int decimalDigits){
        String result = String .format("%."+decimalDigits+"f",num);
        return result;
    }
}
