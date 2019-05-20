package com.yqh.marketing.service.LoginUser;

import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.RandomUtils;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.config.RedisComponent;
import com.yqh.marketing.domain.entity.LoginRef;
import com.yqh.marketing.domain.entity.LoginUser;
import com.yqh.marketing.domain.result.LoginResult;
import com.yqh.marketing.utils.MD5Utils;
import com.yqh.marketing.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;


public class LoginUserServlet extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("LoginUserServlet.class");

    private static long TOKEN_EFFECTIVE_TIME = 100000000 * 60 * 60;


    @Override
    public void init() throws ServletException {
        super.init();
    }
    @Autowired
    private RedisComponent redisComponent;

    @WebMethod("login_user/short_login_user")
    public Record Short_login_user(HttpServletRequest req, QueryParams qp) throws IOException {

            logger.info("手机短信验证码登录");

            Record out_rec = new Record();

            //手机号码
            String login_mobile = qp.checkGetString("LOGIN_MOBILE");
            if (login_mobile.isEmpty() || !RegexUtils.checkMobile(login_mobile)){

                out_rec.put("status",0);
                out_rec.put("message","手机号不正确");
                return out_rec;
            }

            //验证码
            String verification_code = qp.checkGetString("Verification_Code");
            if (verification_code.isEmpty() || !GlobalLogics.getVerificationCodeLogic().checkLoginVerificationCode(login_mobile, verification_code)){

                out_rec.put("status",0);
                out_rec.put("message","验证码不正确");
                return out_rec;
            }

            LoginResult loginResult = new LoginResult();
            //通过手机号拿到用户信息
            Record loginUser = GlobalLogics.getLoginUserLogic().getLoginUserByLoginMobile(login_mobile);

            LoginRef loginRef;
            String loginId;
            String userId;

            if (loginUser.isEmpty()) {
                //登录ID
                loginId = String.valueOf(RandomUtils.generateId()) + "login_id";
                //用户ID
                userId = String.valueOf(RandomUtils.generateId());
                //登录关联ID;
                String loginRefId = String.valueOf(RandomUtils.generateId()) + "loginReid";

                //Login User依赖User
                loginRef = new LoginRef();
                loginRef.setId(loginRefId);
                loginRef.setUserId(userId);
                loginRef.setLoginId(loginId);

                //用户注册随机生成一串密码（36位）
                String password = MD5Utils.hash("123456");

                //获取当前时间
                Date date = new Date();
                LoginUser loginUser1 = new LoginUser();
                loginUser1.setId(loginId);
                loginUser1.setPassword(MD5Utils.hash(password));
                loginUser1.setLoginMobile(login_mobile);
                loginUser1.setLoginStatus(LoginUserConstant.LoginUserStatusType.NORMAL_STATUS);
                loginUser1.setLoginUserType(LoginUserConstant.LoginUserType.SHORT_MESSAGE);
                loginUser1.setLoginRef(loginRef);
                loginUser1.setRegisterTime(date);

                //登录用户注册成功
                boolean record =
                        GlobalLogics.getUserLogic().short_save_login_user(
                                loginUser1.getUserId(),loginUser1.getLoginName(),loginUser1.getPassword(),
                                loginUser1.getLoginMobile(),loginUser1.getLoginUserType(),loginUser1.getUserType(),date);
            }

            out_rec.put("status",1);
            out_rec.put("message","登录成功");
            out_rec.put("user_id",out_rec);
            return out_rec;
    }

}
