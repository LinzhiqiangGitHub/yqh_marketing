package com.yqh.marketing.service.NormalLogin;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.DateUtils;
import com.yqh.marketing.basedevss.base.web.QueryParams;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethod;
import com.yqh.marketing.basedevss.base.web.webmethod.WebMethodServlet;
import com.yqh.marketing.common.GlobalLogics;
import com.yqh.marketing.common.TimeUtils;
import com.yqh.marketing.config.RedisComponent;
import com.yqh.marketing.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class NormalLoginTest extends WebMethodServlet {

    private static final Logger logger = LoggerFactory.getLogger(NormalLoginTest.class);

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
    public Record normallogin_user(HttpServletRequest req, QueryParams qp) throws IOException {

        logger.debug("用户登录");

        //过期时间，设置为3天后
        TimeUtils tg = new TimeUtils();

        String EXPIRE_TIME = TimeUtils.getOtherDay(DateUtils.nowMillis(), 3);

        String user_name = "Daboss";

        String password = "12345678";

        if (user_name.isEmpty()){
            out_rec.put("status",0);
            out_rec.put("message","用户名不能为空");
            return out_rec;
        }
        if (user_name.length() > 32)
        {
            out_rec.put("status",0);
            out_rec.put("message","用户名格式不正确");
            return out_rec;
        }

        if (StringUtils.isEmpty(password)) {
            out_rec.put("status", 0);
            out_rec.put("message", "您未填写密码");
            return out_rec;
        }

        //从redis获取输入错误密码的记录
        //String redisComponents = redisComponent.get(LOGIN_TIMES_PREFIX + user_name);
        //Record u = GlobalLogics.getNormalLoginLogic().getUserByUserName(user_name);

        Record userByUserNameAndPassword = GlobalLogics.getNormalLoginLogic().getuserByUserNameAndPassword(user_name);

        String userpassword = userByUserNameAndPassword.getString("PASSWORD");
        if (!userByUserNameAndPassword.isEmpty()){
            if(StringUtils.isEmpty(userpassword)){
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

        out_rec.put("status",1);
        out_rec.put("message","登录成功");
        out_rec.put("user_id",userByUserNameAndPassword.getString("ID"));

        return out_rec;
    }
}
/* String username = userByUserNameAndPassword.getString("LOGIN_NAME");
        String id = userByUserNameAndPassword.getString("ID");
        String now_ticket = Constants.genTicket(user_name);
        Ticket ticket = new Ticket();
        // String USER_ID, String USER_TICKET, String EXPIRE_TIME, int USER_TYPE, String AUTH_FROM, String OPEN_ID
        boolean b = GlobalLogics.getVerificationCodeLogic().saveUser_ticket(ticket.setUSER_ID(id),now_ticket,EXPIRE_TIME,);*/
/*else {
            //错误密码记录存在,就获取错误次数(redisComponent.get(LOGIN_TIMES_PREFIX + user_name))
            Integer cLoginTime = Integer.valueOf(null);/////
            if (cLoginTime < 5) {
                //错误次数不到5次,可以继续验证用户密码是否正确
                if (!loginUser.getPassword().equals(MD5Utils.hash(password))) {
                    //密码错误,错误次数+1并存入redis,提示密码错误
                    //redisComponent.set(LOGIN_TIMES_PREFIX + user_name, String.valueOf(cLoginTime + 1), PASSWORD_EFFECTIVE_TIME);
                    out_rec.put("status", 0);
                    out_rec.put("message", "密码错误");
                    return out_rec;

                }
            } else {
                //错误次数达到5次,冻结用户,提示：输入密码错误超过5次请稍后重试
                //String loginId = this.loginUserService.getPrimaryKey();
                //this.loginUserStatusService.updateLoginUserStatus(loginId, LoginUserConstant.LoginUserStatusType.MORE_TIMES_STSTUS);
                out_rec.put("status", 0);
                out_rec.put("message", "输入密码错误超过5次，请稍后重试");
                return out_rec;
            }
        }*/
/*密码正确,就登录*/
//this.loginUserStatusService.updateLoginUserStatus(loginUser.getId(), LoginUserConstant.LoginUserStatusType.NORMAL_STATUS);

        /*LoginRef loginRef = loginUser.getLoginRef();
        if (loginRef == null) {
            out_rec.put("status", 0);
            out_rec.put("message", "loginRef为空");
            return out_rec;
        }

        Map<String,String> map = new HashMap<>();
        map.put("loginId",loginUser.getId());
        map.put("userId",loginRef.getUserId());

        LoginResult result = new LoginResult();
        result.setLoginId(loginUser.getId());
        result.setLoginUserType(loginUser.getLoginUserType());
        result.setUserId(loginRef.getUserId());

        redisComponent.set(TOKEN +result.getToken(),map,TOKEN_EFFECTIVE_TIME);*/
