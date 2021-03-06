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
import com.yqh.marketing.domain.entity.LoginUser;
import com.yqh.marketing.utils.MD5Utils;
import com.yqh.marketing.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.logging.Logger;

public class UserServlet extends WebMethodServlet {

    private static final Logger logger = Logger.getLogger("UserServlet.class");

    @Override
    public void init() throws ServletException {
        super.init();
    }
    @Autowired
    private RedisComponent redisComponent;

    @WebMethod("user/short_save_login_user")
    public Record Short_save_login_user(HttpServletRequest req, QueryParams qp) {

        logger.info("手机注册");

        Record out_rec = new Record();

        //用户ID
        String user_id = String.valueOf(RandomUtils.generateId());

        //用户名
        String user_name = qp.checkGetString("LOGIN_NAME");
        if (user_name.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","用户名不能为空！");
            return out_rec;
        }
        //先检查用户名是否存在
        if (this.getUserByUserNameCount(user_name)) {
            out_rec.put("status", 0);
            out_rec.put("message", "用户名已经存在了，需要更换");
            return out_rec;
        }

        //密码
        String user_password = qp.checkGetString("PASSWORD");
        //检查密码是否正确
        if (user_password.length() < 8 || user_password.length() > 32){
            out_rec.put("status",0);
            out_rec.put("message","密码格式错误，需要更换，范围：（6位至32位）");
            return out_rec;
        }

        //手机号码
        String login_mobile = qp.checkGetString("LOGIN_MOBILE");
        //检查手机号码格式是否正确
        if (login_mobile.isEmpty() || !RegexUtils.checkMobile(login_mobile)){
            out_rec.put("status",0);
            out_rec.put("message","手机号码格式不正确");
            return out_rec;
        }
        if (this.checkLoginMobile(login_mobile)){
            out_rec.put("status",0);
            out_rec.put("message","手机号码已存在");
            return out_rec;
        }

        //验证码
        String verification_code = qp.checkGetString("Verification_Code");
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

        //登录ID
        String loginId = String.valueOf(RandomUtils.generateId());


        //登录关联ID
        //String loginRefId = String.valueOf(RandomUtils.generateId())+"ref_id";

        //用户与登录关联表数据
        /*LoginRef loginRef = new LoginRef();
        loginRef.setId(loginRefId);
        loginRef.setLoginId(loginId);
        loginRef.setUserId(user_id);*/

        Date date = new Date();
        //将数据存储到LoginUser实体类中
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(loginId);
        loginUser.setLoginName(user_name);
        //密码通过MD5加密
        loginUser.setPassword(MD5Utils.hash(user_password));
        loginUser.setLoginMobile(login_mobile);
        loginUser.setLoginStatus(LoginUserConstant.LoginUserStatusType.NORMAL_STATUS);
        loginUser.setLoginUserType(LoginUserConstant.LoginUserType.SHORT_MESSAGE);
        //loginUser.setLoginRef(loginRef);
        loginUser.setRegisterTime(date);
        //登录用户注册成功
        boolean record =
                GlobalLogics.getUserLogic().short_save_login_user(
                        loginUser.getUserId(), loginUser.getLoginName(), loginUser.getPassword(),
                        loginUser.getLoginMobile(),loginUser.getLoginStatus(),loginUser.getLoginUserType(), date);

        //LoginUserRegisterResult result = new LoginUserRegisterResult();
        //result.setLoginId(loginId);
        //result.setUserId(user_id);
        //redisComponent.remove(REGISTER_TYPE_PREFIX + login_mobile);

        out_rec.put("status",1);
        out_rec.put("message",record == false?"注册失败":"注册成功");
        out_rec.put("user_id",user_id);
        return out_rec;
    }

    //用户登出
    @WebMethod("user/logout")
    public boolean User_logout(HttpServletRequest req, QueryParams qp) {
        //AdminContext ctx = PortalContext.getAdminContext(req, qp, true, false);
        //return GlobalLogics.getUser().deleteUser_ticket(ctx, ctx.getTicket());
        return false;
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
     * @param username
     * @return
     */
    public boolean getUserByUserNameCount(String username) {
        Record count = GlobalLogics.getUserLogic().getUserByUserNameCount(username);
        System.out.println(count);
        int count1 = (int) count.getInt("LOGIN_NAME");
        System.out.println(count1);
        return count1 > NumberConstant.Int.INT_ZERO;
    }

}
