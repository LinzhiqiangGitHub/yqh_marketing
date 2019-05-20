package com.yqh.marketing.basedevss.base.auth;


import com.yqh.marketing.basedevss.ServiceResult;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.util.Encoders;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.common.StringFilter;
import org.apache.commons.lang.Validate;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

import static com.yqh.marketing.basedevss.base.util.Encoders.md5Base64;

public class WebSignatures {
    private static final Logger L = Logger.getLogger(WebSignatures.class);
    public static String md5Sign(QueryParams qp) {
        TreeSet<String> set = new TreeSet<String>(qp.keySet());
        set.remove("app_id");
        set.remove("app_type");
        set.remove("user_type");
        set.remove("device_id");
        set.remove("location");
        set.remove("language");
        set.remove("version_code");
        set.remove("user_agent");
        set.remove("app_platform");
        set.remove("client_type");
        set.remove("token");
        set.remove("call_id");
        set.remove("ticket");
        set.remove("sign");
        set.remove("sign_method");
        set.remove("callback");
        set.remove("fresh");
        set.remove("_");
        set.remove("channel_id");
        set.remove("ip_addr");
        set.remove("url");
        set.remove("frompage");
        set.remove("USER_IMG");
        set.remove("USER_FILE");
        set.remove("callbackType");
        set.remove("testId");
        set.remove("success");
        set.remove("open_id");

        Iterator i = set.iterator();
        String p = "";
        while (i.hasNext()) {
            String i0 = i.next().toString();
            p += i0 + "=" + qp.getString(i0, "") + "|";
        }
        if (p.lastIndexOf("|") > 0)
            p = p.substring(0, p.length() - 1);

        return md5Sign(p);
    }

    public static String ClientMd5Sign(QueryParams qp,String app_id,String app_key) {

        TreeSet<String> set = new TreeSet<String>(qp.keySet());

        set.remove("app_id");
        set.remove("app_type");
        set.remove("app_key");
        set.remove("user_type");
        set.remove("device_id");
        set.remove("location");
        set.remove("language");
        set.remove("version_code");
        set.remove("user_agent");
        set.remove("app_platform");
        set.remove("client_type");
        set.remove("token");
        set.remove("call_id");
        set.remove("ticket");
        set.remove("sign");
        set.remove("sign_method");
        set.remove("callback");
        set.remove("fresh");
        set.remove("_");
        set.remove("channel_id");
        set.remove("ip_addr");
        set.remove("url");
        set.remove("frompage");
        set.remove("USER_IMG");
        set.remove("USER_FILE");
        set.remove("callbackType");
        set.remove("testId");
        set.remove("success");
        set.remove("open_id");

        Iterator i = set.iterator();
        String p = "";
        while (i.hasNext()) {
            String i0 = i.next().toString();
            p += i0 + "=" + qp.getString(i0, "") + "|";
        }
        if (p.lastIndexOf("|") > 0)
            p = p.substring(0, p.length() - 1);

        return md5Sign(app_id + p + app_key);
    }


    public static String md5Sign(String s) {
        Validate.notNull(s);
        return md5Base64(s);
    }
    private static String[]  systemParams=new String[]{"_","app_platform","app_type","call_id","callback","callbackType","channel_id",
            "device_id","fresh","frompage","ip_addr","language","location","sign","sign_method","ticket",
            "url","user_agent","USER_FILE",
            "USER_IMG","user_type","version_code"};
    public static ServiceResult checkUserParams(QueryParams qp){

        Iterator i = qp.keySet().iterator();
        ServiceResult result=new ServiceResult();
        while (i.hasNext()) {
            String i0 = i.next().toString();
            if(Arrays.binarySearch(systemParams,i0)<0){
                String q=qp.getString(i0, "").trim();
                q=q.replaceAll("'","’");
                q=q.replaceAll("\"","”");
                q=q.replaceAll("--","——");
                q=q.replaceAll("#","＃");
                q=q.replaceAll("&amp;","＆");
                q=q.replaceAll("&","＆");
                qp.setString(i0.trim(),q);
                if(!StringFilter.validSql(qp.getString(i0, "").trim())){
                    result.addErrorMessage(i0.trim()+":"+qp.getString(i0, "").trim()+"，含有非法字符");
                    return result;
                }
                qp.setString(i0.trim(),StringFilter.validXss(qp.getString(i0, "").trim()));
            }
        }
        return result;
    }
    public static String md5MallSign(QueryParams qp,String key) {
        TreeSet<String> set = new TreeSet<String>(qp.keySet());
        set.remove("app_type");
        set.remove("user_type");
        set.remove("device_id");
        set.remove("location");
        set.remove("language");
        set.remove("version_code");
        set.remove("user_agent");
        set.remove("app_platform");
        set.remove("client_type");
        set.remove("token");
        set.remove("call_id");
        set.remove("ticket");
        set.remove("sign");
        set.remove("sign_method");
        set.remove("callback");
        set.remove("fresh");
        set.remove("_");
        set.remove("channel_id");
        set.remove("ip_addr");
        set.remove("url");
        set.remove("frompage");
        set.remove("USER_IMG");
        set.remove("USER_FILE");
        set.remove("callbackType");
        set.remove("testId");
        set.remove("success");
        set.remove("open_id");
        Iterator i = set.iterator();
        String p = "";
        while (i.hasNext()) {
            String i0 = i.next().toString();
            p += i0.trim() + ":" + qp.getString(i0, "").trim().replace(" ","").replace("&","＆") + ",";
        }
        if (p.lastIndexOf(",") > 0)
            p = p.substring(0, p.length() - 1);
        return Encoders.md5Hex(p+"_"+key).toLowerCase();
    }

    public static String md5MiniProgramSign(String app_id,QueryParams qp,String key) {
        TreeSet<String> set = new TreeSet<String>(qp.keySet());
        set.remove("app_id");
        set.remove("sold_channel");
        set.remove("app_key");
        set.remove("client_type");
        set.remove("app_type");
        set.remove("user_type");
        set.remove("device_id");
        set.remove("location");
        set.remove("language");
        set.remove("version_code");
        set.remove("user_agent");
        set.remove("app_platform");
        set.remove("token");
        set.remove("call_id");
        set.remove("ticket");
        set.remove("sign");
        set.remove("sign_method");
        set.remove("callback");
        set.remove("fresh");
        set.remove("_");
        set.remove("channel_id");
        set.remove("ip_addr");
        set.remove("url");
        set.remove("frompage");
        set.remove("USER_IMG");
        set.remove("USER_FILE");
        set.remove("callbackType");
        set.remove("testId");
        set.remove("success");
        set.remove("open_id");

        Iterator i = set.iterator();
        String p = "";
        while (i.hasNext()) {
            String i0 = i.next().toString();
            if (qp.getString(i0, "").length()>0){
                p += i0 + "=" + qp.getString(i0, "") + "|";
            }
        }
        if (p.lastIndexOf("|") > 0)
            p = p.substring(0, p.length() - 1);
//        L.debug(null,"sign p = "+p);
        String sign = md5Base64(app_id+p+key).toLowerCase();
//        L.debug(null,"sign = "+sign);
        return sign;
    }


    public static void main(String[] args) {
        //[DEBUG] 2018-56-26 11:56:25.166 -  qp={app_type=, device_id=, language=, ticket=oAxzy5LSu3iAZFm3ppuBwzcLAANs, token=, open_id=, location=, client_type=3, app_platform=, user_agent=Mozilla/5.0 (iPhone; CPU iPhone OS 11_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15G77 MicroMessenger/6.7.3(0x16070321) NetType/WIFI Language/zh_CN, call_id=Fri Oct 26 2018 11:56:24 GMT+0800 (CST), channel_id=, url=, frompage=, sign=p4lcns7xekjcaisks+nmcq==, info_cardid=1112221, info_realname=Lalle, app_id=20181008001}
        String app_id = "20181008001";
        String key = "12190b21cac111e8b81400163e0e242f";
        //https%3A%2F%2Fwx.qlogo.cn%2Fmmopen%2Fvi_32%2FiaxgvyIjNFolLL0o2I8icJ3RbLEMPCgvKqeXILyc0n20pCzwuCJbv207FADChGpDDWg2JjegZCmcqXeTYH6gOicqg%2F132
        String q = "20181008001info_cardid=1112221";
        String p = "20181008001info_cardid=111222112190b21cac111e8b81400163e0e242f";
        String p2 = md5Base64(app_id+p+key);
        String bbb="p4lcns7xekjcaisks+nmcq==";
        String old_url="https://wx.qlogo.cn/mmopen/vi_32/iaxgvyIjNFolLL0o2I8icJ3RbLEMPCgvKqeXILyc0n20pCzwuCJbv207FADChGpDDWg2JjegZCmcqXeTYH6gOicqg/132";
        BASE64Encoder encoder = new BASE64Encoder();

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] textByte = old_url.getBytes(StandardCharsets.UTF_8);
            String encodedText = encoder.encode(textByte);
            String ccc = new String(decoder.decodeBuffer(bbb), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (p.equals(p2)){

        }
    }

    public static String md5OtherSystemSign(String app_id,QueryParams qp,String key) {
        TreeSet<String> set = new TreeSet<String>(qp.keySet());
        set.remove("app_type");
        set.remove("user_type");
        set.remove("device_id");
        set.remove("location");
        set.remove("language");
        set.remove("version_code");
        set.remove("user_agent");
        set.remove("app_platform");
        set.remove("client_type");
        set.remove("token");
        set.remove("call_id");
        set.remove("ticket");
        set.remove("sign");
        set.remove("sign_method");
        set.remove("callback");
        set.remove("fresh");
        set.remove("_");
        set.remove("channel_id");
        set.remove("ip_addr");
        set.remove("url");
        set.remove("frompage");
        set.remove("USER_IMG");
        set.remove("USER_FILE");
        set.remove("callbackType");
        set.remove("testId");
        set.remove("success");
        set.remove("open_id");

        Iterator i = set.iterator();
        String p = "";
        while (i.hasNext()) {
            String i0 = i.next().toString();
            if (qp.getString(i0, "").length()>0){
                p += i0 + "=" + qp.getString(i0, "") + "|";
            }
        }
        if (p.lastIndexOf("|") > 0)
            p = p.substring(0, p.length() - 1);

        return md5Sign(app_id+p+key);
    }
}
