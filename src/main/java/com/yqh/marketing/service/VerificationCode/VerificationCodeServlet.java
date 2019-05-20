package com.yqh.marketing.service.VerificationCode;

import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.base.NumberConstant;
import com.yqh.marketing.basedevss.base.context.AdminContext;
import com.yqh.marketing.basedevss.base.context.ClientContext;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.sms.SmsConstant;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.common.PortalContext;
import com.yqh.marketing.utils.RegexUtils;
import com.yqh.marketing.utils.SmsUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class VerificationCodeServlet extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("VerificationCodeServlet.class");

    @Override
    public void init() throws ServletException {
        super.init();
    }


    @WebMethod("verification/createMobileVerificationCode")
    public Record createMobileVerificationCode(HttpServletRequest req, QueryParams qp) throws IOException {
        ClientContext ctx = PortalContext.getClientContext(req, qp, true, true);
        Record out_rec = new Record();
        //生成ID
        String ID = getUUID();
        //获取手机号码
        String loginMobile = qp.checkGetString("loginMobile");

        //检查手机号码格式是否正确
        if (loginMobile.isEmpty() || !RegexUtils.checkMobile(loginMobile)){
            out_rec.put("status",0);
            out_rec.put("message","手机号码格式不正确");
            return out_rec;
        }
        //手机号是否重复
        if (this.checkLoginName(loginMobile)) {
            out_rec.put("status",0);
            out_rec.put("message","用户已存在");
            return out_rec;
        }

        Random rand = new Random();
        int code = rand.nextInt(1000000) + 1;
        System.out.println(code);
        //随机生成一个验证码
        String VerificationCode = String.valueOf(code) ;
        //当前时间
        Date date = new Date();
        //当前状态
        String registerTypePrefix = LoginUserConstant.ShortMessagePrefix.REGISTER_TYPE_PREFIX;
        //有效时间END_DATE_TIME(三分钟后)
        long curren = System.currentTimeMillis();
        curren += 15 * 60 * 1000;
        Date end_date_time = new Date(curren);

        boolean registerVerificationCode = GlobalLogics.getVerificationCodeLogic().createRegisterVerificationCode(ID,VerificationCode,loginMobile,end_date_time,date,registerTypePrefix);

        if (!registerVerificationCode) {
            //获取验证码
            out_rec.put("status", 0);
            out_rec.put("message", "已过期");
            out_rec.put("user_id", false);
            return out_rec;
        }

        CloseableHttpResponse sign = new SmsUtils().sign(loginMobile,VerificationCode,SmsConstant.SMS_TYPE.VERIFICATION_CODE);
        out_rec.put("status", 1);
        out_rec.put("message", "已发送");
        out_rec.put("sign", sign.getStatusLine());
        return out_rec;
    }


    //登录时验证码
    @WebMethod("verificationtest/createloginVerificationCode")
    public Record createloginVerificationCode(HttpServletRequest req, QueryParams qp) throws IOException {
        /*
        * MSISDNS 手机号码，如果有多个，以英文逗号分隔
          CONTENT 短信内容
          SMS_TYPE  1为验证码2为登录3物流4营销
        *
        * */
        Record out_rec = new Record();

        Random rand = new Random();
        //生成ID
        String ID = getUUID();
        //获取手机号码
        String loginMobile = qp.checkGetString("loginMobile");

        //检查手机号码格式是否正确
        if (loginMobile.isEmpty() || !RegexUtils.checkMobile(loginMobile)){
            out_rec.put("status",0);
            out_rec.put("message","手机号码格式不正确");
            return out_rec;
        }
        //手机号是否重复
        if (this.checkLoginName(loginMobile)) {
            out_rec.put("status",0);
            out_rec.put("message","用户已存在");
            return out_rec;
        }

        int code = rand.nextInt(1000000) + 1;
        System.out.println(code);
        //随机生成一个验证码
        String VerificationCode = String.valueOf(code) ;

        //当前时间
        Date date = new Date();
        //当前状态（登录）
        String registerTypePrefix = LoginUserConstant.ShortMessagePrefix.LOGIN_TYPE_PREFIX;
        //有效时间END_DATE_TIME(三分钟后)
        long curren = System.currentTimeMillis();
        curren += 15 * 60 * 1000;
        Date end_date_time = new Date(curren);

        boolean registerVerificationCode = GlobalLogics.getVerificationCodeLogic().createRegisterVerificationCode(ID,VerificationCode,loginMobile,end_date_time,date,registerTypePrefix);

        if (!registerVerificationCode) {
            //获取验证码
            out_rec.put("status", 0);
            out_rec.put("message", "已过期");
            out_rec.put("user_id", false);
            return out_rec;
        }

        CloseableHttpResponse sign = new SmsUtils().sign(loginMobile,VerificationCode,SmsConstant.SMS_TYPE.LOGIN);
        out_rec.put("status", 1);
        out_rec.put("message", "已发送");
        out_rec.put("sign", sign.getStatusLine());
        return out_rec;
    }

    /**
     * 通过手机号查找用户数量
     * @param loginMobile
     * @return
     */
    public boolean checkLoginName(String loginMobile) {
        Record count = GlobalLogics.getUserLogic().countByLoginMobile(loginMobile);
        System.out.println(count);
        int count1 = (int) count.getInt("COUNT");
        return count1 > NumberConstant.Int.INT_ZERO;
    }
    //生成UUID
    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }
}
