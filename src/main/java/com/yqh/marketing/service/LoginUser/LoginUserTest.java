package com.yqh.marketing.service.LoginUser;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.sms.SmsConstant;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.utils.RegexUtils;
import com.yqh.marketing.utils.SmsUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Logger;

public class LoginUserTest extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("LoginUserTest.class");

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @WebMethod("login_user/short_login_user")
    public Record Short_login_user(HttpServletRequest req, QueryParams qp) throws IOException, ParseException {

        logger.info("手机短信验证码登录");

        Record out_rec = new Record();


        //手机号码
        String login_mobile = "15755405989";
        if (login_mobile.isEmpty() || !RegexUtils.checkMobile(login_mobile)){

            out_rec.put("status",0);
            out_rec.put("message","手机号不正确");
            return out_rec;
        }
        //当前时间
        Date date = new Date();

        //验证码
        String verification_code = "436133";
        if (verification_code.isEmpty() || !GlobalLogics.getVerificationCodeLogic().checkLoginVerificationCode(login_mobile, verification_code)){
            out_rec.put("status",0);
            out_rec.put("message","验证码不正确");
            return out_rec;
        }

        //获取验证码有效时间
        String end_time = GlobalLogics.getVerificationCodeLogic().checkLoginVerificationCodeOutTime(login_mobile);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date end_date_time = sdf.parse(end_time);
        if (date.getTime() > end_date_time.getTime()){
            out_rec.put("status",0);
            out_rec.put("message","验证码已过期,请重新获取");
            return out_rec;
        }

        out_rec.put("status",1);
        out_rec.put("message","登录成功");
        out_rec.put("record",true);
        out_rec.put("end_date_time",end_date_time);
        return out_rec;

    }
}
