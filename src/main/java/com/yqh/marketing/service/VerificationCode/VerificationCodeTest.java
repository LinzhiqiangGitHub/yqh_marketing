package com.yqh.marketing.service.VerificationCode;

import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.base.NumberConstant;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.sms.SmsConstant;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.utils.RegexUtils;
import com.yqh.marketing.utils.SmsUtils;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class VerificationCodeTest  extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("VerificationCodeTest.class");


    @Override
    public void init() throws ServletException {
        super.init();
    }

    //注册时验证码
    @WebMethod("verificationtest/createMobileVerificationCode1")
    public Record createMobileVerificationCodetest(HttpServletRequest req, QueryParams qp) throws IOException, ParseException {

        Record out_rec = new Record();

        Random rand = new Random();

        //生成ID
        String ID = getUUID();
        //获取手机号码
        String loginMobile = "19921500739";

        //检查手机号码格式是否正确
        if (loginMobile.isEmpty() || !RegexUtils.checkMobile(loginMobile)) {
            out_rec.put("status", 0);
            out_rec.put("message", "手机号码格式不正确");
            return out_rec;
        }

        //手机号是否重复
        if (this.checkLoginName(loginMobile)) {

            //当前时间
            Date date = new Date();
            //有效时间
            Record record = GlobalLogics.getVerificationCodeLogic().VerificationCodeByLoginMobile(loginMobile);
            String end_time = record.getString("END_DATE_TIME");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date end_date_time = sdf.parse(end_time);

            if (end_date_time.getTime() > date.getTime()){

                //有效时间END_DATE_TIME(十五分钟后)
                long curren = System.currentTimeMillis();
                curren += 15 * 60 * 1000;
                Date end_date_time_one = new Date(curren);
                boolean registerVerificationCode = GlobalLogics.getVerificationCodeLogic().updateRegisterVerificationCode(loginMobile,end_date_time_one,date);

                //重新发送
                CloseableHttpResponse sign = new SmsUtils().sign(loginMobile,record.getString("VERIFICATION_CODE"),SmsConstant.SMS_TYPE.VERIFICATION_CODE);

                out_rec.put("status", 1);
                out_rec.put("message", "已重新发送");
                out_rec.put("sign", sign.getStatusLine());
                out_rec.put("registerVerificationCode", registerVerificationCode);

                return out_rec;
            }

            int code = rand.nextInt(1000000) + 1;

            System.out.println(code);
            //随机生成一个验证码
            String VerificationCode = String.valueOf(code);
            //有效时间END_DATE_TIME(十五分钟后)
            long curren = System.currentTimeMillis();
            curren += 15 * 60 * 1000;
            Date end_date_time_one = new Date(curren);

            boolean registerVerificationCode = GlobalLogics.getVerificationCodeLogic().updateCodeByloginMobile(loginMobile, VerificationCode, end_date_time_one, date);

            //重新发送
            CloseableHttpResponse sign = new SmsUtils().sign(loginMobile,VerificationCode,SmsConstant.SMS_TYPE.VERIFICATION_CODE);

            out_rec.put("status", 1);
            out_rec.put("message", "新的验证码，已重新发送");
            out_rec.put("sign", sign.getStatusLine());
            out_rec.put("registerVerificationCode", registerVerificationCode);

            return out_rec;
        }

        int code = rand.nextInt(1000000) + 1;

        System.out.println(code);
        //随机生成一个验证码
        String VerificationCode = String.valueOf(code);
        //当前时间
        Date date = new Date();
        //当前状态（注册）
        String registerTypePrefix = LoginUserConstant.ShortMessagePrefix.REGISTER_TYPE_PREFIX;

        //有效时间END_DATE_TIME(三分钟后)
        long curren = System.currentTimeMillis();
        curren += 15 * 60 * 1000;
        Date end_date_time = new Date(curren);

        boolean registerVerificationCode = GlobalLogics.getVerificationCodeLogic().createRegisterVerificationCode(ID,VerificationCode,loginMobile,end_date_time,date,registerTypePrefix);



        CloseableHttpResponse sign = new SmsUtils().sign(loginMobile,VerificationCode,SmsConstant.SMS_TYPE.VERIFICATION_CODE);
        out_rec.put("status", 1);
        out_rec.put("message", "已发送");
        out_rec.put("sign", sign.getStatusLine());
        out_rec.put("registerVerificationCode", registerVerificationCode);
        return out_rec;
    }




    //登录时验证码
    @WebMethod("verificationtest/createloginVerificationCode")
    public Record createloginVerificationCode(HttpServletRequest req, QueryParams qp) throws IOException, ParseException {

        Record out_rec = new Record();

        Random rand = new Random();
        //生成ID
        String ID = getUUID();

        //获取手机号码
        String loginMobile = "15755405989";

        //检查手机号码格式是否正确
        if (loginMobile.isEmpty() || !RegexUtils.checkMobile(loginMobile)) {
            out_rec.put("status", 0);
            out_rec.put("message", "手机号码格式不正确");
            return out_rec;
        }
        //手机号是否重复
        if (this.checkLoginName(loginMobile)) {

            //当前时间
            Date date = new Date();
            //有效时间
            Record record = GlobalLogics.getVerificationCodeLogic().VerificationCodeByLoginMobile(loginMobile);
            String end_time = record.getString("END_DATE_TIME");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date end_date_time = sdf.parse(end_time);

            if (end_date_time.getTime() > date.getTime()){

                //有效时间END_DATE_TIME(十五分钟后)
                long curren = System.currentTimeMillis();
                curren += 15 * 60 * 1000;
                Date end_date_time_one = new Date(curren);
                boolean registerVerificationCode = GlobalLogics.getVerificationCodeLogic().updateRegisterVerificationCode(loginMobile,end_date_time_one,date);

                //重新发送
                CloseableHttpResponse sign = new SmsUtils().sign(loginMobile,record.getString("VERIFICATION_CODE"),SmsConstant.SMS_TYPE.VERIFICATION_CODE);

                out_rec.put("status", 1);
                out_rec.put("message", "已重新发送");
                out_rec.put("sign", sign.getStatusLine());
                out_rec.put("registerVerificationCode", registerVerificationCode);

                return out_rec;
            }

            int code = rand.nextInt(1000000) + 1;

            System.out.println(code);
            //随机生成一个验证码
            String VerificationCode = String.valueOf(code);
            //有效时间END_DATE_TIME(十五分钟后)
            long curren = System.currentTimeMillis();
            curren += 15 * 60 * 1000;
            Date end_date_time_one = new Date(curren);

            boolean registerVerificationCode = GlobalLogics.getVerificationCodeLogic().updateCodeByloginMobile(loginMobile, VerificationCode, end_date_time_one, date);

            //重新发送
            CloseableHttpResponse sign = new SmsUtils().sign(loginMobile,VerificationCode,SmsConstant.SMS_TYPE.LOGIN);

            out_rec.put("status", 1);
            out_rec.put("message", "新的验证码，已重新发送");
            out_rec.put("sign", sign.getStatusLine());
            out_rec.put("registerVerificationCode", registerVerificationCode);

            return out_rec;
        }
        int code = rand.nextInt(1000000) + 1;
        System.out.println(code);
        //随机生成一个验证码
        String VerificationCode = String.valueOf(code);

        //当前时间
        Date date = new Date();
        //当前状态（登录）
        String registerTypePrefix = LoginUserConstant.ShortMessagePrefix.LOGIN_TYPE_PREFIX;

        //有效时间END_DATE_TIME(十五分钟后)
        long curren = System.currentTimeMillis();
        curren += 15 * 60 * 1000;
        Date end_date_time = new Date(curren);

        boolean registerVerificationCode = GlobalLogics.getVerificationCodeLogic().createRegisterVerificationCode(ID, VerificationCode, loginMobile, end_date_time, date, registerTypePrefix);

        CloseableHttpResponse sign = new SmsUtils().sign(loginMobile,VerificationCode,SmsConstant.SMS_TYPE.LOGIN);

        out_rec.put("status", 1);
        out_rec.put("message", "已发送");
        out_rec.put("sign", sign.getStatusLine());
        out_rec.put("registerVerificationCode",registerVerificationCode);
        return out_rec;
    }

    /**
     * 通过手机号查找用户数量
     *
     * @param loginMobile
     * @return
     */
    public boolean checkLoginName(String loginMobile) {
        //Record count = GlobalLogics.getUserLogic().countByLoginMobile(loginMobile);
        Record count = GlobalLogics.getVerificationCodeLogic().countByLoginMobile(loginMobile);
        System.out.println(count);
        int count1 = (int) count.getInt("COUNT");
        return count1 > NumberConstant.Int.INT_ZERO;
    }

    //生成UUID
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }
}
