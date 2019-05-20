package com.yqh.marketing.service.RegisterUser;

import com.yqh.marketing.base.LoginUserConstant;
import com.yqh.marketing.base.NumberConstant;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.RandomUtils;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.config.RedisComponent;
import com.yqh.marketing.domain.entity.LoginRef;
import com.yqh.marketing.domain.entity.LoginUser;
import com.yqh.marketing.utils.MD5Utils;
import com.yqh.marketing.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;


public class Usertest2 extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("Usertest2.class");

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Autowired
    private RedisComponent redisComponent;

    @WebMethod("users/short_save_login_user2")
    public Record Short_save_login_user2(HttpServletRequest req, QueryParams qp) throws ParseException {

        logger.info("手机注册");

        Record out_rec = new Record();
        String loginId = String.valueOf(RandomUtils.generateId());
        //用户ID
        String user_id = String.valueOf(RandomUtils.generateId());

        //用户名loginName
        String loginName = "ddddddd";
        if (loginName.isEmpty()) {
            out_rec.put("status", 0);
            out_rec.put("message", "用户名不能为空！");
            return out_rec;
        }
        //先检查用户名是否存在
        if (this.getUserByUserNameCount(loginName)) {
            out_rec.put("status", 0);
            out_rec.put("message", "用户名已经存在了，需要更换");
            return out_rec;
        }

        //密码
        String user_password = "12345678";
        //检查密码是否正确
        if (user_password.length() < 8 || user_password.length() > 32) {
            out_rec.put("status", 0);
            out_rec.put("message", "密码格式错误，需要更换，范围：（6位至32位）");
            return out_rec;
        }

        //手机号码
        String login_mobile ="15755402111";
        //检查手机号码格式是否正确
        if (login_mobile.isEmpty() || !RegexUtils.checkMobile(login_mobile)) {

            out_rec.put("status", 0);
            out_rec.put("message", "手机号码格式不正确");
            return out_rec;

        }
        //检查手机是否重复
        if (this.checkLoginMobile(login_mobile)) {
            out_rec.put("status", 0);
            out_rec.put("message", "手机号码已注册，请换个手机号试试");
            return out_rec;
        }


        //生成验证码
        String verification_code = "732120";
        //检查验证码
        if (verification_code.isEmpty()) {
            out_rec.put("status", 0);
            out_rec.put("message", "验证码不能为空");
            return out_rec;
        }
        //验证码是否正确(带着手机号和密码去数据库查看是否匹配)
        if (!GlobalLogics.getVerificationCodeLogic().checkRegisterVerificationCode(login_mobile, verification_code)) {
            out_rec.put("status", 0);
            out_rec.put("message", "验证码不匹配");
            return out_rec;
        }



        //登录关联ID
        String loginRefId = String.valueOf(RandomUtils.generateId()) + "ref_id";

        //用户与登录关联表数据
        LoginRef loginRef = new LoginRef();
        loginRef.setId(loginRefId);
        //loginRef.setLoginId(loginId);
        loginRef.setUserId(user_id);
        //将数据存储到LoginUser实体类中
        LoginUser loginUser = new LoginUser();

        //当前时间
        Date date = new Date();
        loginUser.setUserId(user_id);
        loginUser.setLoginName(loginName);
        loginUser.setPassword(MD5Utils.hash(user_password));
        loginUser.setLoginMobile(login_mobile);
        loginUser.setLoginStatus(LoginUserConstant.LoginUserStatusType.NORMAL_STATUS);
        loginUser.setLoginUserType(LoginUserConstant.LoginUserType.SHORT_MESSAGE);

        loginUser.setLoginRef(loginRef);
        loginUser.setRegisterTime(date);

        //获取验证码有效时间
        String end_time = GlobalLogics.getVerificationCodeLogic().checkLoginVerificationCodeOutTime(login_mobile);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date end_date_time = sdf.parse(end_time);
        if (date.getTime() > end_date_time.getTime()){
            out_rec.put("status",0);
            out_rec.put("message","验证码已过期,请重新获取");
            return out_rec;
        }

        //登录用户注册成功
        boolean record =
                GlobalLogics.getUserLogic().short_save_login_user(
                        loginUser.getUserId(),
                        loginUser.getLoginName(),
                        loginUser.getPassword(),
                        loginUser.getLoginMobile(),
                        loginUser.getLoginStatus(),
                        loginUser.getLoginUserType(),
                        date);


        out_rec.put("status", 1);
        out_rec.put("message","注册成功");
        out_rec.put("record",user_id);

        return out_rec;

    }
    /**
     * 通过手机号查找用户数量
     * @param loginMobile
     * @return
     */
    public boolean checkLoginMobile(String loginMobile) {
        Record count = GlobalLogics.getUserLogic().countByLoginMobile(loginMobile);
        System.out.println(count);
        int count1 = (int) count.getInt("COUNT");
        System.out.println(count1);
        return count1 > NumberConstant.Int.INT_ZERO;
    }
    /**
     * 通过用户名查找用户数量
     * @param loginName
     * @return
     */
    public boolean getUserByUserNameCount(String loginName) {
        Record count = GlobalLogics.getUserLogic().getUserByUserNameCount(loginName);
        System.out.println(count);
        int count1 = (int) count.getInt("COUNT_NAME");
        System.out.println(count1);
        return count1 > NumberConstant.Int.INT_ZERO;
    }
}
