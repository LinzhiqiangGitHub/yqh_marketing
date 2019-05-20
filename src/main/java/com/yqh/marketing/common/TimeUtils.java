package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.util.StringUtils2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {
    private int weeks = 0;
    private int MaxDate;//一月最大天数
    private int MaxYear;//一年最大天数

    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    public static int getNowWeek() {
        Calendar cal0 = Calendar.getInstance();
        cal0.setTime(new Date());
        return cal0.get(Calendar.DAY_OF_WEEK);
    }



    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = TimeUtils.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static String formatEng(String strDate) {

        String o_date[] = StringUtils2.splitArray(strDate, "-", true);
        if (o_date.length > 1) {
            //2012-11-12
            return strDate;
        } else {
            //  Nov 18, 2012
            String regex = "\\d+.\\d+|\\w+";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(strDate);
            boolean b = m.find();
            String month = "01";
            if (b) {
                month = returnMonth(m.group());
            }
            String o_date_2[] = StringUtils2.splitArray(strDate, ",", true);
            String year = o_date_2[1].trim();
            String day= o_date_2[0].trim().replaceAll("[^\\d]", " ").trim();
            strDate = year + "-" + month + "-" + day;
        }
        return strDate;
    }

     private static String returnMonth(String engMonth){
         String returnStrdate="01";
         if (engMonth.contains("Jan"))
             returnStrdate = "01";
         if (engMonth.contains("Feb"))
             returnStrdate = "02";
         if (engMonth.contains("Mar"))
             returnStrdate = "03";
         if (engMonth.contains("Apr"))
             returnStrdate = "04";
         if (engMonth.contains("May"))
             returnStrdate = "05";
         if (engMonth.contains("Jun"))
             returnStrdate = "06";
         if (engMonth.contains("Jul"))
             returnStrdate = "07";
         if (engMonth.contains("Aug"))
             returnStrdate = "08";
         if (engMonth.contains("Sept"))
             returnStrdate = "09";
         if (engMonth.contains("Oct"))
             returnStrdate = "10";
         if (engMonth.contains("Nov"))
             returnStrdate = "11";
         if (engMonth.contains("Dec"))
             returnStrdate = "12";
         return returnStrdate;
     }

    /**
     * 两个时间之间的毫秒数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getSecondDiff(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long s = (date.getTime() - mydate.getTime());
        return s;
    }

    /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDaysDiff(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }
    /**
     * 两个时间之间的月份
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getMonthsDiff(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long month = (date.getTime() - mydate.getTime()) / (30 * 24 * 60 * 60 * 1000);
        return month;
    }
    // 计算当月最后一天,返回字符串
    public String getDefaultDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);//加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);//减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 上月第一天
    public String getPreviousMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);//减一个月，变为下月的1号
        //lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获取当月第一天
    public String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获得本周星期日的日期
    public String getCurrentWeekday() {
        weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    //获取当天时间
    public String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);//可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    // 获得当前日期与本周日相差的天数
    private int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;         //因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    //获得本周一的日期
    public String getMondayOFWeek() {
        weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return standDate(preMonday);
    }


    //获得本周日的日期
    public String getSundayOFWeekNow() {
        weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE,  mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return standDate(preMonday);
    }


    //获得本周四的日期
    public String getSaturdayOFWeek() {
        weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE,  mondayPlus + 7 * weeks + 3);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return standDate(preMonday);
    }


    public String standDate(String d) {           //2013-8-9转成 2013-08-09
        List<String> ls = StringUtils2.splitList(d, "-", true);
        String o = ls.get(0) + "-";
        if (ls.get(1).length() == 1) {
            o = o + "0" + ls.get(1);
        } else {
            o = o + ls.get(1);
        }
        o = o+ "-";
        if (ls.get(2).length() == 1) {
            o = o + "0" + ls.get(2);
        } else {
            o = o + ls.get(2);
        }
        return o;
    }
    ////////////////////////////////////////////

    // 获得上周星期四的日期
    public String getPreSaturdayWeekday() {
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 3);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return standDate(preMonday);
    }

     // 获得上周星期一的日期
    public String getPreMondayWeekday() {
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 0);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return standDate(preMonday);
    }

    //////////////////////////////////////////////////
    //获得相应周的周六的日期
    public String getSaturday() {
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得上周星期日的日期
    public String getPreviousWeekSunday() {
        weeks = 0;
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得上周星期一的日期
    public String getPreviousWeekday() {
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得下周星期一的日期
    public String getNextMonday() {
        weeks++;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得下周星期日的日期
    public String getNextSunday() {

        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }


    private int getMonthPlus() {
        Calendar cd = Calendar.getInstance();
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        cd.set(Calendar.DATE, 1);//把日期设置为当月第一天
        cd.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        MaxDate = cd.get(Calendar.DATE);
        if (monthOfNumber == 1) {
            return -MaxDate;
        } else {
            return 1 - monthOfNumber;
        }
    }

    //获得上月最后一天的日期
    public String getPreviousMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得某月第一天
    public String getMonthFirstDay(int YEAR,int MONTH) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.YEAR,YEAR);
        lastDate.set(Calendar.MONTH, MONTH);
        lastDate.add(Calendar.MONTH, -1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得某月最后一天
    public String getMonthLastDay(int YEAR,int MONTH) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.YEAR,YEAR);
        lastDate.set(Calendar.MONTH, MONTH-1);
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得某月最后一天
    public String getMonthLastDayLast(int YEAR,int MONTH) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.YEAR,YEAR);
        lastDate.set(Calendar.MONTH, MONTH-1);
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获取N个月后的最后一天
    public String getPreviousMonthEndN(int N) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, N);//正数是加，复数是减
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }


    //获得下个月第一天的日期
    public String getNextMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得N个月第一天的日期
    public String getNextMonthFirstN(int N) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, N);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得下个月最后一天的日期
    public String getNextMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//加一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得明年最后一天的日期
    public String getNextYearEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    //获得明年第一天的日期
    public String getNextYearFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime());
        return str;

    }

    //获得本年有多少天
    private int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);//把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);//把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    private int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);//获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);//把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);//把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    //获得本年第一天的日期
    public String getCurrentYearFirst() {
        int yearPlus = this.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }


    //获得本年最后一天的日期 *
    public String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }


    //获得上年第一天的日期 *
    public String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }

    //获得上年最后一天的日期
    public String getPreviousYearEnd() {
        weeks--;
        int yearPlus = this.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        getThisSeasonTime(11);
        return preYearDay;
    }

    //获得本季度
    public String getThisSeasonTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;//years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month + "-" + end_days;
        return seasonDate;

    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  年
     * @param month 月
     * @return 最后一天
     */
    private int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return
     */
    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }


    /**
     * 获取系统时间
     *
     * @return
     */
    public String getDate() {

        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH);//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        int minute = ca.get(Calendar.MINUTE);//分
        int hour = ca.get(Calendar.HOUR);//小时
        int second = ca.get(Calendar.SECOND);//秒

        String date = year + "年" + (month + 1) + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒";


        return date;

    }

    public static String getDateString(String in) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(in);
        return dateString;
    }

    public static String getTomorrowDay(int in) {
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,in);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    //获取增加的天数之后的日期
    public String getOtherDaySimple(String inDay ,int in) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        long f = 0;
        try {
            date = format.parse(inDay);
            f = Long.parseLong(String.valueOf(date.getTime()));
            f = f + in * 24 * 60 * 60 * 1000L;
            SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
            return f1.format(f);
        } catch (ParseException e) {
        }
        return getSundayOFWeekNow();
    }


    //获取增加的天数之后的时间
    public static String getOtherDay(long inDate ,int in) {
        long getDate = inDate + in * 24 * 60 * 60 * 1000L;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(getDate);
    }

    //获取某个日期 N 个月的第一天
    public static String getOtherFirstDayInOnDay1(String inDate,int ADDMONTH,int ADDDAY) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try{
            date = sdf.parse(inDate);//初始日期
        }catch(Exception e){

        }
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        lastDate.add(Calendar.MONTH, 1+ADDMONTH);//加减月份
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DAY_OF_YEAR, -ADDDAY);
        str = sdf.format(lastDate.getTime());
        return str;
    }
    //获取某个日期 N 个月的最后一天
    public static String getOtherLastDayInOnDay1(String inDate,int ADDMONTH,int ADDDAY) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try{
            date = sdf.parse(inDate);//初始日期
        }catch(Exception e){

        }
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        lastDate.add(Calendar.MONTH, 1+ADDMONTH);//加减月份
        lastDate.set(Calendar.DATE, ADDDAY);//把日期设置为下个月第一天
        lastDate.roll(Calendar.DATE, -ADDDAY);//回滚一个就是第一天
        str = sdf.format(lastDate.getTime());
        return str;

    }

    //根据进来的年月，获取+1或者减一的年月
    public static String getOtherMonth(String inDate,int ADDMONTH) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try{
            date = sdf.parse(inDate);//初始日期
        }catch(Exception e){

        }
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        lastDate.add(Calendar.MONTH, ADDMONTH);//加减月份
        str = sdf.format(lastDate.getTime());
        return str;

    }


    //获得某天所在的周的周一的日期
    public static String getMondayOFWeekOneDay(String YEAR_MONTH_DAY) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");//定义日期格式
        Date date=new Date();
        try {
            date=df.parse(YEAR_MONTH_DAY);//将String格式化
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        String imptimeBegin = df.format(cal.getTime());
        return imptimeBegin;
    }


    //获得某天所在的周的周日的日期
    public static String getSundayOFWeekNowOneDay(String YEAR_MONTH_DAY) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");//定义日期格式
        Date date=new Date();
        try {
            date=df.parse(YEAR_MONTH_DAY);//将String格式化
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, 6);
        String imptimeMi = df.format(cal.getTime());
        return imptimeMi;
    }
}
