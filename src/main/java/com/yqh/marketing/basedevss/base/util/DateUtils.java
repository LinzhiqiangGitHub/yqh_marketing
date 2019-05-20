package com.yqh.marketing.basedevss.base.util;



import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.common.ErrorCodes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
    public static final String defaultDate="1900-01-01 00:00:00",
    yyyy_MM_dd = "yyyy-MM-dd",
    MM_dd_HH = "MM-dd HH",
    yyyy_MM = "yyyy-MM",
    yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static String now(String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(new Date());
        //return new Date(System.currentTimeMillis());
    }

    public static String now() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(new Date());
        //return new Date(System.currentTimeMillis());
    }
    public static int intervalSecondToNow(Date startDate){
        long nowTime = new Date().getTime();
        long startTime = startDate.getTime();
        return (int)(nowTime - startTime)/1000;
    }

    public static String date() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(new Date());
        //return new Date(System.currentTimeMillis());
    }

    /**
     * 得到指定日期加上指定天数的日期
     * */
    public static String getAddDateByDay(Date SourceDate, int day, String format){
        if(format.isEmpty())
            format="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();//创建一个实例
        calendar.setTime(SourceDate);//实例化一个Calendar。 年、月、日、时、分、秒
        calendar.add(Calendar.DATE,day);//给当前日期加上指定天数，这里加的是1天
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 得到指定日期加上指定小时数的日期
     * */
    public static String getAddDateByHour(String date, int day, String format){
        Date d= new Date(dateToTimestamp(date) * 1000);
        return getAddDateByHour(d,day,format);
    }
    /**
     * 得到指定日期加上指定天数的日期
     * */
    public static String getAddDateByHour(Date SourceDate, int hour, String format){
        if(format.isEmpty())
            format="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();//创建一个实例
        calendar.setTime(SourceDate);//实例化一个Calendar。 年、月、日、时、分、秒
        calendar.add(Calendar.HOUR,hour);//给当前日期加上指定小时数，这里加的是hour小时
        return dateFormat.format(calendar.getTime());
    }
    public static String getAddDateByDay(String SourceDate, int day, String format){

        Date d= new Date(dateToTimestamp(SourceDate) * 1000);
        return getAddDateByDay(d,day,format);
    }

    public static List<String> getDateList(String beginDate,String endDate){
        long diff = getDateDiff(beginDate,endDate);
        List<String> dateList = new ArrayList<String>();
        for (int i = 0; i <= diff; i++) {
            dateList.add(DateUtils.getAddDateByDay(beginDate, i, DateUtils.yyyy_MM_dd));
        }
        return dateList;
    }

    /**
     * 格式化日期
     * */
    public static String formatDate(Date SourceDate, String format){
        if(format.isEmpty())
            format="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();//创建一个实例
        calendar.setTime(SourceDate);//实例化一个Calendar。 年、月、日、时、分、秒
        return dateFormat.format(calendar.getTime());
    }

    public static int getMonth(String dateStr){
        Date date = getDateFromString(dateStr,"yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH)+1;
    }

    public static int getYear(String dateStr){
        Date date = getDateFromString(dateStr,"yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 格式化日期
     * */
    public static String formatDate(String SourceDate, String format){
         Date d= new Date(dateToTimestamp(SourceDate) * 1000);
        if(format.isEmpty())
            format="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();//创建一个实例
        calendar.setTime(d);//实例化一个Calendar。 年、月、日、时、分、秒
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 格式化日期为中文格式
     * @param SourceDate
     * @param format
     * @return
     */
    public static String formatDateChina(String SourceDate, String format){
        Date d= new Date(dateToTimestamp(SourceDate) * 1000);
        if(format.isEmpty())
            format="MM月dd日";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();//创建一个实例
        calendar.setTime(d);//实例化一个Calendar。 年、月、日、时、分、秒
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 得到指定日期加上指定秒数的日期
     * */
    public static String getAddDateBySecond(Date SourceDate,int second,String format){
        if(format.isEmpty())
            format="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();//创建一个实例
        calendar.setTime(SourceDate);//实例化一个Calendar。 年、月、日、时、分、秒
        calendar.add(Calendar.SECOND,second);//给当前日期加上指定天数，这里加的是1天
        return dateFormat.format(calendar.getTime());
    }
    public static String getAddDateBySecond(String SourceDate, int second, String format){
        Date d= new Date(dateToTimestamp(SourceDate) * 1000);
        return getAddDateBySecond(d,second,format);
    }

    public static String unitTimestampToDate(long unit_timestamp, String formats){
        Long timestamp = unit_timestamp*1000;
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }
    public static long dateToTimestamp(String dateString,String format)  {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            Date date;
            try {
                date = df.parse(dateString);
            }catch (ParseException e){
                df = new SimpleDateFormat("yyyy-MM-dd");
                date = df.parse(dateString);
            }
            long s = date.getTime();
            return (s / 1000);
        }
        catch (ParseException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static long dateToTimestamp(String dateString)  {
        return dateToTimestamp(dateString,"yyyy-MM-dd HH:mm:ss");
    }

    public static long getLongDateFromDateString(String dateString)  {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(dateString);
            long s = date.getTime();
            return (s);
        }
        catch (ParseException ex) {
            df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = df.parse(dateString);
                long s = date.getTime();
                return (s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    public static long nowMillis() {
        return System.currentTimeMillis();
    }

    public static long nowNano() {
        return System.nanoTime();
    }

    public static String formatDateAndTime(long millis) {
        return formatDateAndTime(new Date(millis));
    }

    public static String formatDateMinute(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f.format(date);
    }

    public static String formatDateSecond(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(date);
    }

    public static String formatDateMinute(long millis) {
        return formatDateMinute(new Date(millis));
    }
    public static String formatDateSecond(long millis) {
        return formatDateSecond(new Date(millis));
    }
    public static String formatDateAndTime(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(date);
    }

    public static String formatDate(long millis) {
        return formatDate(new Date(millis));
    }
    public static String formatDate(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(date);
    }

    public static String formatDateCh(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
        return f.format(date);
    }

    public static String formatDateCh(long millis) {
        return formatDateCh(new Date(millis));
    }

    //get tody 0 timestamp
    public static long getTimesmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    /** * 获取指定日期是星期几

     * 参数为null时表示获取当前日期是星期几

     * @param date

     * @return

     */
    public static String getWeekOfDate(Date date) {

        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        Calendar calendar = Calendar.getInstance();

        if(date != null){

            calendar.setTime(date);

        }

        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        if (w < 0){

            w = 0;

        }
        return weekOfDays[w];
    }
    public static int getWeekIndexOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if(date != null){
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (w < 0){
            w = 0;
        }
        return w==0?7:w;
    }
    public static String getWeekOfDate(int weekIndex) {

        String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        if(weekIndex>=0&&weekIndex<=6) {
            return weekOfDays[weekIndex];
        }
        return "";
    }
    public static String getShortWeekOfDate(int weekIndex){
        String s=getWeekOfDate(weekIndex);
        return s.replace("星期","周");
    }
    public static String getShortWeekOfDate(Date date){
        String s=getWeekOfDate(date);
        return s.replace("星期","周");
    }

    public static String getLastDay(String date){
        return getLastDay(DateUtils.getYear(date),DateUtils.getMonth(date));
    }

    public static String getLastDay(int year, int month){
        return getAddDateByDay(getFirstDay(year,month+1),-1,DateUtils.yyyy_MM_dd);
    }

    public static String getFirstDay(String date){
        return getFirstDay(DateUtils.getYear(date),DateUtils.getMonth(date));
    }

    public static String getFirstDay(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.yyyy_MM_dd);

        String firstDayOfMonth = format.format(cal.getTime());
        return firstDayOfMonth ;
    }

    public static Date getDateFromString(String k, String dateformat) {

        if(dateformat.isEmpty())
            dateformat="yyyy-MM-dd HH:mm:ss";
        try {
            SimpleDateFormat format=new SimpleDateFormat(dateformat);
            format.setLenient(false);
            return format.parse(k);
        } catch (ParseException e) {
            throw new ServerException(ErrorCodes.SYSTEM_PARAMETER_TYPE_ERROR, "Invalid parameter '%s'", k);
        }
    }

    public static Date getDateFromString(String k) {
        return getDateFromString(k,"yyyy-MM-dd HH:mm:ss");
    }

    public static boolean checkDate(String k,String dateformat) {

        if(dateformat.isEmpty())
            dateformat="yyyy-MM-dd HH:mm:ss";
        try {
            SimpleDateFormat format=new SimpleDateFormat(dateformat);
            format.setLenient(false);
            format.parse(k);
            return true;
        } catch (ParseException e) {
        }
        return false;
    }
    public static long getDateDiff(String sdate,String edate) {
        long day = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date begin_date;
            Date end_date;
            begin_date = format.parse(sdate);
            end_date = format.parse(edate);
            day = (end_date.getTime() - begin_date.getTime())
                    / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return -1;
        }
        return day;
    }
    public static long getDateDiffSecond(String sdate,String edate) {
        long day = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date begin_date;
            Date end_date;
            begin_date = format.parse(sdate);
            end_date = format.parse(edate);
            day = (end_date.getTime() - begin_date.getTime())
                    /1000;
        } catch (Exception e) {
            return -1;
        }
        return day;
    }
    public static String getFirstDayofMonth(int sYear,int sMonth){
         Calendar   c   =   Calendar.getInstance();
        c.set(Calendar.YEAR,sYear);
        c.set(Calendar.MONTH,sMonth-1);
        return String.valueOf(sYear)+"-"+String.valueOf(sMonth)+"-"+c.getActualMinimum(Calendar.DAY_OF_MONTH);
    }
    public static String getEndDayofMonth(int sYear,int sMonth){
        Calendar   c   =   Calendar.getInstance();
        c.set(Calendar.YEAR,sYear);
        c.set(Calendar.MONTH,sMonth-1);
        return String.valueOf(sYear)+"-"+String.valueOf(sMonth)+"-"+c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    public static String getMinDate(String date,int weekIndex){
        int i=getWeekIndexOfDate(DateUtils.getDateFromString(date,"yyyy-MM-dd"));
        if(weekIndex<=i){
            weekIndex=weekIndex+7;
        }
        return DateUtils.getAddDateByDay(date,weekIndex-i,"yyyy-MM-dd");
    }
}
