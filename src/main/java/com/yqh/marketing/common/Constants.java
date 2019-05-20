package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.BaseErrors;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.sfs.SFSUtils;
import com.yqh.marketing.basedevss.base.sfs.StaticFileStorage;
import com.yqh.marketing.basedevss.base.sfs.local.LocalSFS;
import com.yqh.marketing.basedevss.base.util.DateUtils;
import com.yqh.marketing.basedevss.base.util.Encoders;
import com.yqh.marketing.basedevss.base.util.RandomUtils;
import com.yqh.marketing.basedevss.base.util.StringUtils2;
import com.yqh.marketing.basedevss.base.web.Cookies;
import com.yqh.marketing.common.aliyun.AliyunOSS;
import com.yqh.marketing.common.aliyun.AliyunOSSDir;
import net.coobird.thumbnailator.Thumbnails;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Constants extends SQLExecutorBase {
    private static final Logger L = Logger.getLogger(Constants.class);

    public static final String redis_artist_activetype_list_key = "redis_artist_activetype_list_key";
    public static final String redis_artist_producttype_list_key = "redis_artist_producttype_list_key";
    public static final String redis_artist_artistcountry_list_key = "redis_artist_artistcountry_list_key";
    public static final String redis_artist_goods_info_list_key = "redis_artist_goods_info_list_key";
    public static final String redis_artist_list_key = "redis_artist_list_key";
    public static final String redis_artist_active_list_key = "redis_artist_active_list_key";
    public static final String redis_artist_goods_list_key_pre = "redis_artist_goods_list_key_";

    public static final int USER_REGIST_TYPE_SYSTEM = 0;
    public static final int USER_REGIST_TYPE_QQYQH = 1;
    public static final int USER_REGIST_TYPE_SINA = 2;
    public static final int USER_REGIST_TYPE_WECHAT = 3;
    public static final int USER_REGIST_TYPE_MINIPROGRAM_MALL = 4;
    public static final int USER_REGIST_TYPE_MINIPROGRAM_VIDEO = 5;

    public static final String REQUEST_METHOD_GET= "GET";
    public static final String REQUEST_METHOD_POST= "POST";
    public static final String REQUEST_METHOD_PUT= "PUT";

    public static final String AREA_HUADONG = "山东省,江苏省,安徽省,浙江省,福建省,上海,上海市";
    public static final String AREA_HUANAN = "广东省,广西省,海南省";
    public static final String AREA_HUAZHONG = "湖北省,湖南省,河南省,江西省";
    public static final String AREA_HUABEI = "北京,北京市,天津,天津市,河北省,山西省,内蒙古自治区";
    public static final String AREA_XIBEI = "宁夏回族自治区,新疆维吾尔自治区,青海省,陕西省,甘肃省";
    public static final String AREA_XINAN = "四川省,云南省,贵州省,西藏自治区,重庆,重庆市";
    public static final String AREA_DONGBEI = "辽宁省,吉林省,黑龙江省";
    public static final String AREA_GANGAOTAI = "台湾,香港,澳门";

    //  1网站         2微信H5页面   3微信小程序  4手机 5 平板
    public static final int CLIENT_TYPE_WEBSITE= 1;
    public static final int CLIENT_TYPE_WEIXIN= 2;
    public static final int CLIENT_TYPE_WEIXINPROGRAM= 3;
    public static final int CLIENT_TYPE_MOBILE= 1;
    public static final int CLIENT_TYPE_PAD= 5;

    public static final String CLIENT_PLATFORM_IOS= "IOS";
    public static final String CLIENT_PLATFORM_ANDROID = "ANDROID";
    public static final String CLIENT_PLATFORM_WP= "WP";
    public static final String CLIENT_PLATFORM_WEBPAGE= "WEB";
    public static final String CLIENT_PLATFORM_WEIXPROGRAM= "WEIXINPROGRAM";

    //===============以上是对象的分类====================
    //用户类型    1为个人用户 2为各种管理员  9为超级管理员  0为未登录
    //11为投资者
    public static final int USER_TYPE_PERSONAL = 1;
    public static final int USER_TYPE_ADMIN = 2;
    public static final int USER_TYPE_NOT_LOGIN = 0;
    public static final int USER_TYPE_SUPER_ADMIN = 9;

    //<option value="1">满减</option>
    //<option value="2">满折</option>
    //<option value="3">秒杀</option>
    //<option value="4">满赠</option>
    //<option value="5">折上折</option>
    //==================================<option value="6">团购</option>
    //<option value="7">渠道扣减</option>
    public static final int MAKETING_TYPE_FULL_REDUCTION = 1;
    public static final int MAKETING_TYPE_FULL_DISCOUNT = 2;
    public static final int MAKETING_TYPE_SECOND_KILL = 3;
    public static final int MAKETING_TYPE_FULL_GIFT = 4;
    public static final int MAKETING_TYPE_DISCOUNT_DISCOUNT = 5;
    public static final int MAKETING_TYPE_GROUPON = 6;
    public static final int MAKETING_TYPE_CHANNEL_REDUCTION = 7;

    public static final int GOODS_INFO_SORT_TYPE_DEFAULT = 9;
    public static final int GOODS_INFO_SORT_TYPE_SOLD_COUNT = 1;
    public static final int GOODS_INFO_SORT_TYPE_PRICE = 2;
    public static final int GOODS_INFO_SORT_TYPE_NEW_GOODS = 3;

    public static final int GOODS_INFO_SORT_ASC = 1;
    public static final int GOODS_INFO_SORT_DESC = 0;


    //验证手机号码
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isEmailAddress(String address) {
        Pattern p = Pattern.compile("\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}");
        Matcher m = p.matcher(address);
        return m.matches();
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
    public boolean isAddrEmail(String addr) {
        boolean isEmail = false;
        String emailAddressPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";     //手机"1[0-9]{10}"
        Pattern pattern = Pattern.compile(emailAddressPattern, Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(addr);
        while (match.find()) {
            isEmail = true;
        }
        return isEmail;
    }
    public static String dateString2longYearMonth(long d) {
        String date = new SimpleDateFormat("yyyy-MM").format(new Date(d));
        return date;
    }

    public static String dateString2longYearMonthSimple(long d) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date(d));
        return date;
    }

    public static String dateLongToString(long d) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(d));
        return date;
    }

    public static String dateLongToStringHour(long d) {
        String date = new SimpleDateFormat("HH").format(new Date(d));
        return date;
    }

    public static String dateLongToStringShort(long d) {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(d));
        return date;
    }

    public static String dateLongToStringShortShort(long d) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date(d));
        return date;
    }

    public static String dateLongToStringShortShort2(long d) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(d));
        return date;
    }

    public static long dateString2long(String in_time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        long f = 0;
        try {
            date = format.parse(in_time);
            f = Long.parseLong(String.valueOf(date.getTime()));
        } catch (ParseException e) {
        }

        return f;
    }

    public static String getNowYear() {
        String date = new SimpleDateFormat("yyyy").format(new Date(DateUtils.nowMillis()));
        return date;
    }
    public static String getNowMonth() {
        String date = new SimpleDateFormat("MM").format(new Date(DateUtils.nowMillis()));
        return date;
    }
    public static String getNowDay() {
        String date = new SimpleDateFormat("dd").format(new Date(DateUtils.nowMillis()));
        return date;
    }

    public static boolean uploadFile(FileItem file_item, String real_fileName, StaticFileStorage fileStorage) {
        String file_name = file_item.getName();
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_name == null || file_name.trim().length() == 0) {
            legal_file = false;
        }
        if (file_size <= 0) {
            legal_file = false;
        }
        if (legal_file) {
            SFSUtils.saveUpload(file_item, fileStorage, real_fileName);
            return true;
        } else {
            return false;
        }
    }
    public static boolean uploadImgOSS(FileItem file_item, String real_fileName) {
        String file_name = file_item.getName();
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_name == null || file_name.trim().length() == 0) {
            legal_file = false;
        }
        if (file_size <= 0) {
            legal_file = false;
        }
        if (legal_file) {
            uploadFileOSS(file_item, AliyunOSSDir.IMG_STORAGE, real_fileName);
            return true;
        } else {
            return false;
        }
    }
    public static boolean uploadFileOSS(FileItem file_item, String real_fileName) {
        String file_name = file_item.getName();
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_name == null || file_name.trim().length() == 0) {
            legal_file = false;
        }
        if (file_size <= 0) {
            legal_file = false;
        }
        if (legal_file) {
            uploadFileOSS(file_item, AliyunOSSDir.VIDEO_STORAGE, real_fileName);
            return true;
        } else {
            return false;
        }
    }

    public static Boolean uploadFileOSS(FileItem file_item, AliyunOSSDir dir, String fileName){
        if(StringUtils.isEmpty(fileName) || file_item == null){
            return false;
        }
        String path  = dir.getPath();
        if(path.contains("$date")){
            path = path.replace("$date",DateUtils.formatDate(new Date(),"yyyyMMdd"));
        }
        try {
            AliyunOSS.uploadFile(path + fileName, file_item.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean uploadFileSavedLocal(FileItem file_item, String real_fileName, StaticFileStorage fileStorage) {
        String file_name = file_item.getName();
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_name == null || file_name.trim().length() == 0) {
            legal_file = false;
        }
        if (file_size <= 0) {
            legal_file = false;
        }
        if (legal_file) {
            SFSUtils.saveUpload(file_item, fileStorage, real_fileName);
            return true;
        } else {
            return false;
        }
    }

    //图片两个压缩方法一
    public static boolean uploadImageFileAndScare(FileItem file_item, String real_fileName, StaticFileStorage photoStorage, int width_0) {
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_size <= 0) {
            legal_file = false;
        }
        if (file_size > 10 * 1024 * 1024) {
            System.out.println("too big file upload. max size is 10M, current file size is [" + file_size + "]");
            legal_file = false;
        }
        if (legal_file) {
            int width, height, bWidth = 0, bHeight = 0;

            ImageIO.setUseCache(false);
            BufferedImage image = null;
            try {
                image = ImageIO.read(file_item.getInputStream());
                width = image.getWidth();
                height = image.getHeight();

                bWidth = width_0;
                bHeight = width_0 * height / width;

                String format = real_fileName.substring(real_fileName.lastIndexOf(".") + 1);
                if (!format.toUpperCase().equals("JPG") || !format.toUpperCase().equals("JPEG") || !format.toUpperCase().equals("PNG")
                        || !format.toUpperCase().equals("GIF") || !format.toUpperCase().equals("BMP") || !format.toUpperCase().equals("TIFF")
                        || !format.toUpperCase().equals("PSD") || !format.toUpperCase().equals("SVG"))
                    format = "jpg";
                SFSUtils.saveScaledUploadImage(file_item, photoStorage, real_fileName, Integer.toString(bWidth), Integer.toString(bHeight), format);
            } catch (IOException e) {
            }
            return true;
        } else {
            return false;
        }
    }
    public static boolean isImage(String CT) {
        List<String> allowType = Arrays.asList("image/bmp", "image/png", "image/gif", "image/jpg", "image/jpeg", "image/pjpeg");
        return allowType.contains(CT);

    }
    //图片两个压缩方法二
    public static boolean uploadImageFileThumbnail(String oldFileName, FileItem file_item, String real_fileName, StaticFileStorage photoStorage, int width_0) {
        long file_size = file_item.getSize();
        boolean legal_file = true;
        if (file_size <= 0) {
            legal_file = false;
        }
        if (file_size > 10 * 1024 * 1024) {   //大于10MB失败
            legal_file = false;
        }
        if (legal_file) {
            int width, height, bWidth = 0, bHeight = 0;

            ImageIO.setUseCache(false);
            BufferedImage image = null;
            try {
                image = ImageIO.read(file_item.getInputStream());
                width = image.getWidth();
                height = image.getHeight();

                bWidth = width_0;
                bHeight = width_0 * height / width;

                String path = ((LocalSFS) photoStorage).directory;
                // bugsImgStorage wanghanxiao OSS
//                uploadFileOSS(file_item, AliyunOSSDir.USER_PHOTO_IMG_STORAGE, real_fileName);

                Thumbnails.of(new File(path + oldFileName))
                        .size(bWidth, bHeight)
                        .toFile(new File(path + real_fileName));

            } catch (IOException e) {
            } finally {
                image = null;
            }
            return true;
        } else {
            return false;
        }
    }


    public static String genTicket(String loginName) {
        return Encoders.toBase64(loginName + "_" + DateUtils.nowMillis() + "_" + new Random().nextInt(10000));
    }


    public static String generateHash(String sid, String username, String password) {
        String sourceHash = sid + username + password;
        String hash = "";
        byte[] baseBytes = Base64.encodeBase64(DigestUtils.md5(sourceHash.getBytes(StandardCharsets.UTF_8)));
        hash = new String(baseBytes, StandardCharsets.UTF_8);
        return hash;
    }

    public static String strLen1To2(String inStr) {
        if (inStr.length() == 1)
            inStr = "0" + inStr;
        return inStr;
    }

    public static boolean checkHash(String expectHash, String sid, String username, String password) {
        String actualHash = generateHash(sid, username, password);
        return (StringUtils.isNotEmpty(expectHash)) && (expectHash.equalsIgnoreCase(actualHash));
    }

    public static String matchBadStr(String instr, String matchStr) {
        String[] myStr = StringUtils2.splitArray(matchStr, " ", true);
        for (int i = 0; i < myStr.length; i++) {
            instr = instr.replace(myStr[i], "**");
        }
        return instr;
    }

    public static String one2TwoStr(String instr) {
        if (instr.length() == 1)
            instr = "0" + instr;
        return instr;
    }

    public static String getPercent(int x, int total) {
        String result = "";//接受百分比的值
        if (total == 0 || x == 0)
            return "0.00%";
        result = String.valueOf((double) (Math.round(x * 100 / total))) + "%";
        return result;
    }

    public  static String getTwoXs(float in){
        if (in==0)
            return "0.00";
        DecimalFormat   df   =new   DecimalFormat("#.00");
        String dd = df.format(in);
        return dd;
    }

    public static String getOrignalImaUrl(String old_url, String width0) {
        if (old_url.indexOf("Storage") < 0)
            return old_url;

        String[] arrayName = StringUtils2.splitArray(old_url, "/", true);


        String lastName = arrayName[arrayName.length - 1];

        String preName = old_url.replace(lastName, "");
        String[] arrayNameLast = StringUtils2.splitArray(lastName, ".", true);

        String newName = preName + arrayNameLast[0] + "_" + width0 + "." + arrayNameLast[1];
        return newName;
    }

    //判断字符串是否含中文
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    //判断一个字符串出现的次数
    public static int countInString(String text, String sub) {
        int count = 0, start = 0;
        while ((start = text.indexOf(sub, start)) >= 0) {
            start += sub.length();
            count++;
        }
        return count;
    }

    public static final String allChar = "0123456789abcdefghjklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";

    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }


    //日期格式化
    public static String formatStandardDate(String dd) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String dd = "2015-9-3   13:8:9";
        Date ddd = format.parse(dd);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ddd);
        return date;
    }

    public static String replaceErrStr(String cv){
        if (cv.equals(""))
            return cv;
        cv = cv.replaceAll("'","");
        cv = cv.replaceAll("\\+","");
        cv = cv.replaceAll("&","");
        cv = cv.replaceAll("=","");
        return cv;
    }

    //标准化EXCEL导入时的各种时间格式
    //2015-02-27 12:33
    //2015-03-17 14:02:30
    //4/10/15 9:42
    public static String formatExcelStandardDateNum(String dd) {
        //20150511214234
        String date1="";
        String date2="";
        String date3="";
        String date4="00";
        String date5="00";
        String date6="00";
        if (dd.length() != 14)
            return DateUtils.now();

        date1 = dd.substring(0, 4);
        date2 = dd.substring(4, 6);
        date3 = dd.substring(6, 8);

        date4 = dd.substring(8, 10);
        date5 = dd.substring(10, 12);
        date6 = dd.substring(12, 14);
        return date1+"-"+date2+"-"+date3 + " " + date4+":"+date5+":"+date6;
    }

    public static String formatExcelStandardDate(String dd) {
        dd = dd.replace("AM","").replace("PM","").replace("   "," ");
        dd = dd.replace("  "," ").replace("  "," ");

        String date1="";
        String date2="";
        String date3="";
        String date4="00";
        String date5="00";
        String date6="00";
        if (dd.contains("/")) {
            String D0[] = dd.split(" ");
            String d01[] = D0[0].split("/");
            if (d01.length==3){
                date1 =YearLen2To4(d01[2]);
                date2 =DayLen1To2(d01[0]);
                date3 =DayLen1To2(d01[1]);
            }
            String d02[] = D0[1].split(":");
            if (d02.length==2){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
            }
            if (d02.length==3){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
                date6 =DayLen1To2(d02[2]);
            }
        } else if (dd.contains("-")){
            String D0[] = dd.split(" ");
            String d01[] = D0[0].split("-");
            if (d01.length==3){
                date1 =YearLen2To4(d01[0]);
                date2 =DayLen1To2(d01[1]);
                date3 =DayLen1To2(d01[2]);
            }
            String d02[] = D0[1].split(":");
            if (d02.length==2){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
            }
            if (d02.length==3){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
                date6 =DayLen1To2(d02[2]);
            }
        }
        return date1+"-"+date2+"-"+date3 + " " + date4+":"+date5+":"+date6;
    }

    public static String formatExcelStandardDateForKd(String dd) {
        String date1="";
        String date2="";
        String date3="";
        if (dd.contains("/")) {
            String d01[] = dd.split("/");
            if (d01.length==3){
                date1 =YearLen2To4(d01[0]);
                date2 =DayLen1To2(d01[1]);
                date3 =DayLen1To2(d01[2]);
            }
        } else if (dd.contains("-")){
            String d01[] = dd.split("-");
            if (d01.length==3){
                date1 =YearLen2To4(d01[0]);
                date2 =DayLen1To2(d01[1]);
                date3 =DayLen1To2(d01[2]);
            }
        }
        return date1+"-"+date2+"-"+date3;
    }

    public static String formatExcelStandardDateForTemp(String dd) {
        //      10/1/15 12:26
        dd = dd.replace("AM","").replace("PM","").replace("   "," ");
        dd = dd.replace("  "," ").replace("  "," ");

        String date1="";
        String date2="";
        String date3="";
        String date4="00";
        String date5="00";
        String date6="00";
        if (dd.contains("/")) {
            String D0[] = dd.split(" ");
            String d01[] = D0[0].split("/");
            if (d01.length==3){
                date1 =YearLen2To4(d01[2]);
                date2 =DayLen1To2(d01[0]);
                date3 =DayLen1To2(d01[1]);
            }
            String d02[] = D0[1].split(":");
            if (d02.length==2){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
            }
            if (d02.length==3){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
                date6 =DayLen1To2(d02[2]);
            }
        } else if (dd.contains("-")){
            String D0[] = dd.split(" ");
            String d01[] = D0[0].split("-");
            if (d01.length==3){
                date1 =YearLen2To4(d01[0]);
                date2 =DayLen1To2(d01[1]);
                date3 =DayLen1To2(d01[2]);
            }
            String d02[] = D0[1].split(":");
            if (d02.length==2){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
            }
            if (d02.length==3){
                date4 =DayLen1To2(d02[0]);
                date5 =DayLen1To2(d02[1]);
                date6 =DayLen1To2(d02[2]);
            }
        }
        return date1+"-"+date2+"-"+date3 + " " + date4+":"+date5+":"+date6;
    }

    public static String YearLen2To4(String instr) {
        if (instr.length() == 2) {
            instr = "20" + instr;
        }
        return instr;
    }

    public static String DayLen1To2(String instr) {
        if (instr.length() == 1) {
            instr = "0" + instr;
        }
        return instr;
    }

    public static void addZip(String oldFileName, String newFileName,String static_file_addr){
        zipFile( static_file_addr + oldFileName,  static_file_addr + newFileName);
    }
    public static void zipFile(String srcFilePath, String zipFilePath) {
        if(srcFilePath == null){
            throw new RuntimeException("需要压缩的文件的完整路径 不能为空!");
        }
        if(zipFilePath == null){
            throw new RuntimeException("压缩生成的文件的路径 不能为空!");
        }

        ZipOutputStream zout = null;
        FileInputStream fin = null;

        try{
            File txtFile = new File(srcFilePath);
            fin = new FileInputStream(txtFile);
        }catch (FileNotFoundException e) {
            throw new RuntimeException("压缩失败!找不到文件" + srcFilePath);
        }finally {
            try {
                fin.close();
            } catch (Exception e) {

            }
        }
        try {
            zout = new ZipOutputStream(new FileOutputStream(new File(zipFilePath)));

            File srcFile = new File(srcFilePath);
            if (srcFile.exists())
                srcFile.delete();
            fin = new FileInputStream(srcFile);

            byte[] bb = new byte[4096];
            int i = 0;
            zout.putNextEntry(new ZipEntry(srcFile.getName()));
            while ((i = fin.read(bb)) != -1) {
                zout.setLevel(9);
                zout.write(bb, 0, i);
            }
        }  catch (IOException e) {
            throw new RuntimeException("压缩失败!", e);
        } finally {
            try {
                zout.close();
                fin.close();
            } catch (Exception e) {
            }
        }
    }


    public static String bigProName2DetailName(String bigName){
        String DETAIL_NAME="";
        return DETAIL_NAME;
    }


    public static String len1To4(int inNum){
        String outStr = "";
        if (String.valueOf(inNum).length()==1){
            outStr = "000"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==2){
            outStr = "00"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==3){
            outStr = "0"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==4){
            outStr = String.valueOf(inNum);
        }
        return outStr;
    }
    public static String len1To3(int inNum){
        String outStr = "";
        if (String.valueOf(inNum).length()==1){
            outStr = "00"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==2){
            outStr = "0"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==3){
            outStr = String.valueOf(inNum);
        }
//        if (String.valueOf(inNum).length()==4){
//            outStr = String.valueOf(inNum);
//        }
        return outStr;
    }

    public static String[] getBigArea(String bigArea){
        if (bigArea.contains("华东"))
            return AREA_HUADONG.split(",");

        if (bigArea.contains("华南"))
            return AREA_HUANAN.split(",");

        if (bigArea.contains("华中"))
            return AREA_HUAZHONG.split(",");

        if (bigArea.contains("华北"))
            return AREA_HUABEI.split(",");

        if (bigArea.contains("西北"))
            return AREA_XIBEI.split(",");

        if (bigArea.contains("西南"))
            return AREA_XINAN.split(",");

        if (bigArea.contains("东北"))
            return AREA_DONGBEI.split(",");

        if (bigArea.contains("港澳台"))
            return AREA_GANGAOTAI.split(",");

        return new String[]{};
    }


    public static String formatString(String inStr){
        if (inStr.length()<=0)
            return inStr;
        inStr = inStr.replace("'","");
        inStr = "'"+inStr.replace(",","','")+"'";
        return inStr;
    }
    public static String getNowDateNUm(String ORDER_NO){
        //再加6个随机数
        Random random = new Random();
        int num = -1 ;
        num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
        return ORDER_NO+String.valueOf(num);
    }

    public static Map<String,String> ipMap=null;
    public static String generateOrderId() {
        String DATE = DateUtils.now().replace("-", "").replace(" ", "").replace(":", "");
        //再加5个随机数
        Random random = new Random();
        int num = -1 ;
        num = (int)(random.nextDouble()*(100000 - 10000) + 10000);
        return DATE+String.valueOf(num);
    }

    public static String filterEmoji(String source) {
        if(source != null)
        {
            Pattern emoji = Pattern.compile(
                    "[\\\\ud83c\\\\udc00-\\\\ud83c\\\\udfff]|[\\\\ud83d\\\\udc00-\\\\ud83d\\\\udfff]|[\\\\u2600-\\\\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find())
            {
                source = emojiMatcher.replaceAll("");
                return source ;
            }
            return source;
        }
        return source;
    }

    //unicode替换
    public static String encodingtoStr(String str) {
        if (str.length()<=0)
            return str;
        if(str != null)
        {
            Pattern emoji =  Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

            Matcher emojiMatcher = emoji.matcher(str);
            if ( emojiMatcher.find())
            {
                str = emojiMatcher.replaceAll("");
                return str ;
            }
            return str;
        }
        return str;
    }


    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     * @param  s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }
    public static String getServerIpName(String ip){
        String server="";
        if(ipMap==null){
            ipMap=new HashMap<String, String>();
            ipMap.put("10.163.13.187","U8Server");
            ipMap.put("10.171.98.191","finance");
            ipMap.put("10.51.63.156","156");
            ipMap.put("10.27.35.225","web2");
            ipMap.put("10.24.193.20","userweb2");
            ipMap.put("10.26.47.27","userweb1");
            ipMap.put("10.46.183.120","data");
            ipMap.put("10.24.195.219","web3");
            ipMap.put("10.24.190.243","web1");
            ipMap.put("10.47.212.48","test");
            ipMap.put("10.46.165.71","erp");
        }
        if(!ipMap.containsKey(ip)){
            String[] adArr = ip.split("\\.");
            if(adArr.length>0) {
                server = adArr[adArr.length - 1];
            }
        }else {
            server=ipMap.get(ip);
        }
        return server;
    }

    public static float getPackageWeight(int package_size){
        int max = 996;
        int min = 900;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;
        double b = (double)s * package_size / 1000;
        return (float)b;
    }


    public static String generateRandomId_three() {
        //3个随机数
        Random random = new Random();
        int num = -1;
        num = (int) (random.nextDouble() * (1000 - 100) + 100);
        return String.valueOf(num);
    }

    public static String guessClientFormByUA(String UA){
        String c = "";
        if (UA.contains("ANDROID"))
            c = Constants.CLIENT_PLATFORM_ANDROID;
        if (UA.contains("IOS"))
            c = Constants.CLIENT_PLATFORM_IOS;
        return c;
    }

    public static String getTicket(HttpServletRequest req){
        String ticket = "ticket";

        String oldTicket = req.getParameter(ticket),
                newTicket = oldTicket;

        try {
            if(null == newTicket || newTicket.isEmpty() || newTicket.equals("null") || newTicket.equals("undefined")) {
                Cookie cookie = Cookies.getCookie(req, ticket);
                if(null != cookie)
                    newTicket = cookie.getValue();

                if(null == newTicket || newTicket.isEmpty() || newTicket.equals("null") || newTicket.equals("undefined"))
                    newTicket = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(newTicket == null)
            newTicket = "";
        return newTicket;
    }

    public static Sheet getSheetByNum(Workbook book, int number){
        Sheet sheet = null;
        try {
            sheet = book.getSheetAt(number);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return sheet;
    }
    public static String getCellValueByCell(Cell cell) {
        //判断是否为null或空串
        if (cell==null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        int cellType=cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_STRING: //字符串类型
                cellValue= cell.getStringCellValue().trim();
                cellValue= StringUtils.isEmpty(cellValue) ? "" : cellValue;
                break;
            case Cell.CELL_TYPE_BOOLEAN:  //布尔类型
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC: //数值类型
                DecimalFormat df = new DecimalFormat("0");
                cellValue = String.valueOf(df.format(cell.getNumericCellValue()));
                break;
            default: //其它类型，取空串吧
                cellValue = "";
                break;
        }
        return cellValue;
    }


    public static String chnToPinYin(String chn_str){

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = chn_str.trim().toCharArray();
        String output = "";
        for (int i = 0; i < input.length; i++) {
            if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {  //判断字符是否是中文
                //toHanyuPinyinStringArray 如果传入的不是汉字，就不能转换成拼音，那么直接返回null
                //由于中文有很多是多音字，所以这些字会有多个String，在这里我们默认的选择第一个作为pinyin
                String[] temp = new String[0];
                try {
                    temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
                output += temp[0];
            } else {
                output += Character.toString(input[i]);
            }
        }
        return output;
    }

    public static String paserOrderStatus(long order_status){
        String str = "";
        if (order_status==0)
            str = "未支付";
        else if (order_status==1)
            str = "已支付未发货";
        else if (order_status==2)
            str = "已发货";
        else if (order_status==3)
            str = "已收货";
        else if (order_status==4)
            str = "作废";
        else if (order_status==10)
            str = "确认收货";
        return str;
    }
    public static String paserOrderTradeType(long trader_type){
        String str = "";
        if (trader_type==1)
            str = "一般贸易";
        else if (trader_type==2)
            str = "跨境直邮";
        else if (trader_type==3)
            str = "保税集货";
        else if (trader_type==4)
            str = "保税备货";
        else if (trader_type==5)
            str = "跨境快件";
        return str;
    }
    public static String paserOrderPakingType(long packaging){
        String str = "";
        if (packaging==1)
            str = "木箱";
        else if (packaging==2)
            str = "纸箱";
        else if (packaging==3)
            str = "桶装";
        else if (packaging==4)
            str = "散装";
        else if (packaging==5)
            str = "托盘";
        else if (packaging==6)
            str = "包";
        else if (packaging==7)
            str = "其他";
        return str;
    }
    public static String paserOrderPayType(long pay_id){
        String str = "";
        if (pay_id==1)
            str = "支付宝支付";
        else if (pay_id==3)
            str = "微信支付";
        return str;
    }

    public static String paserMarketingType(long MAKETING_TYPE){
        String str = "";
        if (MAKETING_TYPE==1)
            str = "满减";
        else if (MAKETING_TYPE==2)
            str = "满折";
        else if (MAKETING_TYPE==3)
            str = "抢购";
        else if (MAKETING_TYPE==4)
            str = "满赠";
        else if (MAKETING_TYPE==5)
            str = "折上折";
        else if (MAKETING_TYPE==6)
            str = "团购";
        return str;
    }

    public static String goods_info_col = " goods_info_id,goods_id,goods_info_added,goods_info_item_no,goods_info_name,goods_info_purchase_price,goods_info_subtitle,goods_info_maketing_term,goods_info_added_time,goods_info_unadded_time,goods_info_stock,goods_info_prefer_price,goods_info_market_price,goods_info_cost_price,goods_info_weight,goods_info_img_id,third_id,third_name,is_third,goods_info_isbn,is_customer_discount,isMailBay,goods_info_barcode,fictitious_sales_count, unit,country,hs_code,trade_type,tariff_rate,consumption_tax_rate,value_added_tax_rate,main_ingredients,enterprise_name,enterprise_countries,is_gifts,volume,weight,tax_rate,goods_info_sort,isMailBay_price,default_sold_count ";
    public static String goods_info_col_ref = "info.goods_info_id,info.goods_info_added,info.goods_info_maketing_term,info.goods_info_purchase_price,info.goods_id,info.goods_info_item_no,info.goods_info_name,info.goods_info_subtitle,info.goods_info_added_time,info.goods_info_unadded_time,info.goods_info_stock,info.goods_info_prefer_price,info.goods_info_market_price,info.goods_info_cost_price,info.goods_info_weight,info.goods_info_img_id,info.third_id,info.third_name,info.is_third,info.goods_info_isbn,info.is_customer_discount,info.isMailBay,info.goods_info_barcode,info.fictitious_sales_count,info.unit,info.country,info.isMailBay_price,info.hs_code,info.trade_type,info.tariff_rate,info.consumption_tax_rate,info.value_added_tax_rate,info.main_ingredients,info.enterprise_name,info.enterprise_countries,info.is_gifts,info.volume,info.weight,info.tax_rate,info.goods_info_sort,info.packaging,info.default_sold_count ";
    public static String goods_info_col_ref_list = "info.goods_info_id,info.goods_info_added,info.goods_info_maketing_term,info.goods_info_purchase_price,info.goods_id,info.goods_info_name,info.goods_info_subtitle,info.goods_info_stock,info.goods_info_prefer_price,info.goods_info_market_price,info.goods_info_weight,info.goods_info_img_id,info.third_id,info.third_name,info.is_third,info.isMailBay,info.unit,info.country,info.trade_type,info.volume,info.weight,info.goods_info_sort,info.tariff_rate,info.consumption_tax_rate,info.value_added_tax_rate,info.tax_rate,info.isMailBay_price,info.default_sold_count ";
    public static String goods_col = " goods_id,goods_name,goods_subtitle,goods_no,goods_keywords,goods_brief,goods_img,goods_deno,goods_detail_desc,goods_score,brand_id,mobile_desc,goods_belo,is_third,goods_belo,goods_belo_name ";
    public static String goods_info_col_base = "info.goods_info_id,info.goods_id,info.goods_info_added,info.goods_info_item_no,info.goods_info_name,info.goods_info_subtitle,info.goods_info_added_time,info.third_id,info.third_name,info.isMailBay_price,info.default_sold_count ";
    public static String goods_info_rank = "info.goods_info_id,info.goods_info_img_id,info.goods_info_name,info.goods_info_subtitle,info.trade_type,info.third_id,info.third_name ";

    public static String replaceGoodsInfoImg(String goods_info_id,String goods_info_img_id){
        return goods_info_img_id.replace("!160","!352");
    }

    public static int OBJECT_TYPE_INFORMATION=1;
    public static int OBJECT_TYPE_PRODUCT=2;
    public static int OBJECT_TYPE_STORE=3;
    public static int OBJECT_TYPE_BRAND=4;
    public static int OBJECT_TYPE_CAT_LEVEL1=5;
    public static int OBJECT_TYPE_CAT_LEVEL2=6;
    public static int OBJECT_TYPE_CAT_LEVEL3=7;
    public static int OBJECT_TYPE_PAGE=8;
    public static int OBJECT_TYPE_VIDEO=9;

    //切割字符串======用于搜索=============
    public static List<String> splitStr(String instr){
        List<String> out_ls = new ArrayList<>();
        //1,先将字符串以空格 中文逗号，英文逗号，竖线 分隔
        List<String> ls_space = StringUtils2.splitList(instr," ",true);
        for (String l : ls_space){
            List<String> ls_dh_eng = StringUtils2.splitList(l,",",true);
            for (String l1 : ls_dh_eng){
                List<String> ls_dh_chn = StringUtils2.splitList(l1,"，",true);
                for (String l2 : ls_dh_chn){
                    List<String> ls_dh_sx = StringUtils2.splitList(l2,"|",true);
                    for (String l3 : ls_dh_sx){
                        Pattern p = Pattern.compile("[a-zA-Z]+");
                        Matcher m = p.matcher(l3);//英文分段
                        while (m.find()) {
                            String f_eng = m.group();
                            if (!out_ls.contains(f_eng))
                                out_ls.add(f_eng);
                        }

                        Pattern p_num = Pattern.compile("[0-9]+");
                        Pattern p_num_not = Pattern.compile("[^0-9]+");
                        // 非英文组合
                        p = Pattern.compile("[^a-zA-Z]+");
                        m = p.matcher(l3);//中文分段
                        while (m.find()) {
                            String f_chn = m.group();

                            Matcher m_num = p_num.matcher(f_chn);//数字分段
                            while (m_num.find()) {
                                String s1 = m_num.group();
                                if (!out_ls.contains(s1))
                                    out_ls.add(s1);
                            }

                            Matcher m_num_not = p_num_not.matcher(f_chn);//非数字分段
                            while (m_num_not.find()) {
                                String s2 = m_num_not.group();
                                if (!out_ls.contains(s2))
                                    out_ls.add(s2);
                            }
                        }
                    }
                }
            }
        }
        return out_ls;
    }
    //计算分值======用于搜索=============

    public static int getStrScore(List<String> ls_str, String value_str,int defaultScore){
        int v = 0;
        for (String l : ls_str){
            if (value_str.toUpperCase().indexOf(l.toUpperCase())>=0)
                v += defaultScore;
        }
        return v;
    }


    public static String formatProvinceName(String in_provincename){
        String out_provincename = in_provincename;
        if (in_provincename.equals("北京市"))
            out_provincename = "北京";
        if (in_provincename.equals("天津市"))
            out_provincename = "天津";
        if (in_provincename.equals("重庆市"))
            out_provincename = "重庆";
        if (in_provincename.equals("上海市"))
            out_provincename = "上海";
        return out_provincename;
    }

    public static int getGoodsIdCardVerifyType(RecordSet all_goods_info){
        int verify_type = 1;

        for (Record g : all_goods_info){
            if (g.getInt("trade_type")==5 || g.getInt("trade_type")==3 || g.getInt("trade_type")==4 || g.getInt("trade_type")==2){
                verify_type = 2;
                break;
            }
        }
        return verify_type;
    }

    /**
     * 验证身份证号码
     */
    private static final int IN_MEMORY_FILE_SIZE_THRESHOLD = 1024 * 20; // 20KB
    private static final int MAX_UPLOAD_FILE_SIZE = 1024 * 1024 * 500; // 500MB
    private static ServletFileUpload createFileUpload() {
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory(IN_MEMORY_FILE_SIZE_THRESHOLD, new File(getTempDir())));
        upload.setSizeMax(MAX_UPLOAD_FILE_SIZE);
        return upload;
    }
    private static String getTempDir() {
        try {
            String tempDir = FileUtils.getTempDirectoryPath() + "/upload_" + DateUtils.nowNano();
            FileUtils.forceMkdir(new File(tempDir));
            return tempDir;
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_IO_ERROR, e);
        }
    }
    public static Record formatFileItemImg(FileItem file_item,String pre_name){
        Record out_rec = new Record();
        if (file_item.getSize()<0){
            out_rec.put("code", "0");
            out_rec.put("message", "图片上传不正确");
            out_rec.put("pic_str", "");
            out_rec.put("pic_url", "");
            return out_rec;
        }
        String fileName = file_item.getName();
        String suffix  = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!suffix .toUpperCase().equals("JPG") && !suffix .toUpperCase().equals("JPEG") && !suffix .toUpperCase().equals("PNG") && !suffix .toUpperCase().equals("MPEG") && !suffix .toUpperCase().equals("GIF")){
            out_rec.put("code", "0");
            out_rec.put("message", "图片格式不正确");
            out_rec.put("pic_str", "");
            out_rec.put("pic_url", "");
            return out_rec;
        }

        String pic_url= "";
        String full_path = "";
        if (file_item != null && file_item.getSize() > 0) {
            String file_name = file_item.getName().substring(file_item.getName().lastIndexOf("\\") + 1);
            String expName = "";
            if (file_name.contains(".")) {
                expName = file_name.substring(file_name.lastIndexOf(".") + 1);
            }
            pic_url = pre_name + "_" + RandomUtils.generateId() + "." + expName;
//            success = Constants.uploadIdCardFileOSS(file_item, pic_url);
            String videoOSSPattern = GlobalConfig.get().getString("service.video.oss.UrlPattern", "https://qqyqh-video.oss-cn-beijing.aliyuncs.com/idcardImgStorage/%s");
            full_path = String.format(videoOSSPattern, pic_url);
        }else{
            out_rec.put("code", "0");
            out_rec.put("message", "图片保存失败");
            out_rec.put("pic_str", "");
            out_rec.put("pic_url", "");
            out_rec.put("pic_url_s", "");
            return out_rec;
        }

        L.debug(null,"file_item fileName = " + fileName);
        byte[] buffer = null;
        try {
            InputStream inputStream = file_item.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            inputStream.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            L.debug(null,"file_item write stream error = " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            L.debug(null,"file_item write stream error1 = " + e.toString());
            e.printStackTrace();
        }
        String pic_str = Encoders.toBase64(buffer);
        out_rec.put("code", "1");
        out_rec.put("message", "图片上传正确");
        out_rec.put("pic_str", pic_str);
        out_rec.put("pic_url_s", pic_url);
        out_rec.put("pic_url", full_path);

        return out_rec;
    }

    public static Record getMiniProgramAppIdByYqhAppId(String yqh_app_id){
        String app_id = "";
        String app_secret = "";
        String login_type = "";
        if (yqh_app_id.equals("20181008001")){
            app_id = GlobalConfig.get().getString("miniprogram.zsyqh.appId","wx11fa544dfafc1dee");
            app_secret = GlobalConfig.get().getString("miniprogram.zsyqh.AppSecret","f1d7197fc2ffcf1931a581f6340b624b");
            login_type = String.valueOf(Constants.USER_REGIST_TYPE_MINIPROGRAM_MALL);
        }
        if (yqh_app_id.equals("20181008003")){
            app_id = GlobalConfig.get().getString("miniprogram.video.appId","wxc93d07687ba011f9");
            app_secret = GlobalConfig.get().getString("miniprogram.video.AppSecret","b71000599eaaf95a6b430bf2d5ac542d");
            login_type = String.valueOf(Constants.USER_REGIST_TYPE_MINIPROGRAM_VIDEO);
        }
        Record rec = new Record();
        rec.put("app_id",app_id);
        rec.put("app_secret",app_secret);
        rec.put("login_type",login_type);
        return rec;
    }

    public static String formatClearanceStatus(int customs_clearance_status){
        String returnOrderClearanceMessage = "";
        if (customs_clearance_status==1){
            returnOrderClearanceMessage = "未清关";
        }
        if (customs_clearance_status==1){
            returnOrderClearanceMessage = "清关报文已生成";
        }
        if (customs_clearance_status==2){
            returnOrderClearanceMessage = "海关接收报文成功";
        }
        if (customs_clearance_status==3){
            returnOrderClearanceMessage = "清关成功";
        }
        if (customs_clearance_status==4){
            returnOrderClearanceMessage = "清关失败";
        }
        if (customs_clearance_status==5){
            returnOrderClearanceMessage = "收货人状态已修改";
        }
        if (customs_clearance_status==6){
            returnOrderClearanceMessage = "重新报送海关";
        }
        return returnOrderClearanceMessage;
    }
    public static String formatPayPushStatus(int pay_push_status){
        String payPushMessage = "";
        if (pay_push_status==1){
            payPushMessage = "报文生成，推送中";
        }
        if (pay_push_status==2){
            payPushMessage = "推送成功";
        }
        if (pay_push_status==3){
            payPushMessage = "推送失败";
        }
        if (pay_push_status==0){
            payPushMessage = "未推送";
        }
        return payPushMessage;
    }
}
