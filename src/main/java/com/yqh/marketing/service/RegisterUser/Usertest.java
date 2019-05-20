package com.yqh.marketing.service.RegisterUser;

import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.RandomUtils;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.config.RedisComponent;
import com.yqh.marketing.domain.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.logging.Logger;


public class Usertest extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("Usertest.class");

    @Override
    public void init() throws ServletException {
        super.init();
    }
    @Autowired
    private RedisComponent redisComponent;

    @WebMethod("user/short_save_login_user1")
    public Record Short_save_login_user(HttpServletRequest req, QueryParams qp) {

        logger.info("手机注册");

        Record record = new Record();

        String verification_code = "123456";
        //登录ID
        String loginId = String.valueOf(RandomUtils.generateId()) + "login_id";
        //登录关联ID
        String loginRefId = String.valueOf(RandomUtils.generateId()) + "ref_id";

        /*//用户与登录关联表数据
        LoginRef loginRef = new LoginRef();
        loginRef.setId(loginRefId);
        loginRef.setLoginId(loginId);
        loginRef.setUserId(user_id);

        Date date = new Date();
        //将数据存储到LoginUser实体类中
        LoginUser loginUser = new LoginUser();

        loginUser.setLoginStatus(LoginUserConstant.LoginUserStatusType.NORMAL_STATUS);
        loginUser.setLoginUserType(LoginUserConstant.LoginUserType.SHORT_MESSAGE);
        loginUser.setLoginRef(loginRef);
        loginUser.setRegisterTime(date);
        //登录用户注册成功
        GlobalLogics.getUserLogic().Short_save_login_user1(loginUser.getUserId(), loginUser.getLoginName(), loginUser.getPassword(),
                loginUser.getLoginMobile(), loginUser.getLoginStatus(), loginUser.getLoginUserType(), loginUser.getRegisterTime());
*/
        //LoginUserRegisterResult result = new LoginUserRegisterResult();
        //result.setLoginId(loginId);
        //result.setUserId(user_id);
        //redisComponent.remove(REGISTER_TYPE_PREFIX + login_mobile);

        //用户ID
        String user_id = String.valueOf(RandomUtils.generateId());
        //用户名
        String user_name = "user";
        //密码
        String user_password = "password";
        //手机号码
        //String login_mobile = "12345678901";
        String login_mobile = "15755405989";
        if (login_mobile.isEmpty()){
            record.put("status",0);
            record.put("message","手机错误");

            return record;
        }
        //验证码

        LoginUser loginUser = new LoginUser();
        loginUser.setLoginStatus(LoginUserConstant.LoginUserStatusType.NORMAL_STATUS);
        loginUser.setLoginUserType(LoginUserConstant.LoginUserType.SHORT_MESSAGE);
        //登录用户注册成功
        boolean b = GlobalLogics.getUserLogic().short_save_login_user(
                user_id,user_name,user_password,login_mobile,loginUser.getLoginStatus(),
                loginUser.getLoginUserType(), new Date());

        record.put("status",1);
        record.put("message",b);
        record.put("record",user_id);
        return record;
    }

}
