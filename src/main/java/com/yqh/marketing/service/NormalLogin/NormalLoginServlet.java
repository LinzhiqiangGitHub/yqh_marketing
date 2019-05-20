package com.yqh.marketing.service.NormalLogin;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.DateUtils;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.common.TimeUtils;
import com.yqh.marketing.config.RedisComponent;
import com.yqh.marketing.domain.entity.LoginUser;
import com.yqh.marketing.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用户正常登录
 */
public class NormalLoginServlet extends WebMethodServlet {

    private static final Logger logger = LoggerFactory.getLogger(NormalLoginServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
    }

    private static long PASSWORD_EFFECTIVE_TIME = 15 * 60;
    private static long TOKEN_EFFECTIVE_TIME = 100000000 * 60 * 60;
    public static String LOGIN_TIMES_PREFIX = "LOGIN_TIMES_";
    public static String TOKEN = "LG_";


    Record out_rec = new Record();

    @Autowired
    private RedisComponent redisComponent;

    @WebMethod("normalLogin/normalLogin_user")
    public Record normalLogin_user(HttpServletRequest req, QueryParams qp) throws IOException {

        logger.debug("用户登录");

        //过期时间，设置为3天后
        TimeUtils tg = new TimeUtils();
        String EXPIRE_TIME = TimeUtils.getOtherDay(DateUtils.nowMillis(), 3);
        LoginUser loginUser = new LoginUser();
        String loginTimes = "0";

        String user_name = qp.checkGetString("USER_NAME");
        String password = qp.checkGetString("USER_PASSWORD");

        if (user_name.isEmpty()) {
            out_rec.put("status", 0);
            out_rec.put("message", "用户名不能为空");
            return out_rec;
        }

        if (StringUtils.isEmpty(password)) {
            out_rec.put("status", 0);
            out_rec.put("message", "您未设置密码");
            return out_rec;
        }

        Record userByUserNameAndPassword = GlobalLogics.getNormalLoginLogic().getuserByUserNameAndPassword(user_name);

        String userpassword = userByUserNameAndPassword.getString("PASSWORD");
        if (!userByUserNameAndPassword.isEmpty()) {
            if (StringUtils.isEmpty(userpassword)) {
                out_rec.put("status", 0);
                out_rec.put("message", "您未填写密码");
                return out_rec;
            }
            //错误密码记录不存在,验证用户密码是否正确
            if (!userpassword.equals(MD5Utils.hash(password))) {
                //密码错误,向redis插入错误密码的记录,提示密码错误
                out_rec.put("status", 0);
                out_rec.put("message", "密码错误");
                return out_rec;
            }
        }

        out_rec.put("status", 1);
        out_rec.put("message", "登录成功");
        out_rec.put("user_id", userByUserNameAndPassword.getString("ID"));

        return out_rec;
    }
}
