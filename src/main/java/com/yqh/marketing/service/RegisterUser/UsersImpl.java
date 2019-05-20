package com.yqh.marketing.service.RegisterUser;

import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.redis.RedisUtil;
import com.yqh.marketing.basedevss.base.sql.ConnectionFactory;
import com.yqh.marketing.basedevss.base.sql.SQLExecutor;
import com.yqh.marketing.basedevss.base.util.Initializable;
import com.yqh.marketing.service.accessory.AccessoryImpl;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

public class UsersImpl implements UserLogic,Initializable {

    private static final Logger L = Logger.getLogger(AccessoryImpl.class);

    private String userTable = "t_yqh_enterprise_user";
    private String sysLoginUser = "t_yqh_sys_login_user";
    private String sysThirdPartyAccount = "t_yqh_sys_third_party_account";

    RedisUtil jedis = new RedisUtil();

    private ConnectionFactory write_connectionFactory;
    private String write_db;
    private ConnectionFactory read_connectionFactory;
    private String read_db;


    @Override
    public void init() {
        Configuration conf = GlobalConfig.get();
        jedis = new RedisUtil();
        this.write_connectionFactory = ConnectionFactory.getConnectionFactory("dbcp");
        this.write_db = conf.getString("write.service.db", null);
        this.read_connectionFactory = ConnectionFactory.getConnectionFactory("dbcp");
        this.read_db = conf.getString("read.service.db", null);
    }

    @Override
    public void destroy() {
        this.write_connectionFactory = ConnectionFactory.close(write_connectionFactory);
        this.write_db = null;
        this.read_connectionFactory = ConnectionFactory.close(read_connectionFactory);
        this.read_db = null;
    }


    private SQLExecutor getWriteSqlExecutor() {
        return new SQLExecutor(write_connectionFactory, write_db);
    }

    private SQLExecutor read_getSqlExecutor() {
        return new SQLExecutor(read_connectionFactory, read_db);
    }

    /*
    *   LoginUser loginUser = new LoginUser();
        loginUser.setId(loginId);
        loginUser.setLoginName(user_name);
        loginUser.setPassword(user_password);
        loginUser.setLoginMobile(login_mobile);
        loginUser.setLoginStatus(LoginUserConstant.LoginUserStatusType.NORMAL_STATUS);
        loginUser.setLoginUserType(LoginUserConstant.LoginUserType.SHORT_MESSAGE);
    * */
    /*@Override
    public boolean short_save_login_user(LoginUser loginUser) {

        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO " +
                        sysLoginUser
                        + " (ID,LOGIN_NAME,PASSWORD,LOGIN_MOBILE,LOGIN_STATUS,LOGIN_USER_TYPE,REGISTER_TIME) VALUES ('"
                        + loginUser.getUserId() + "','"
                        + loginUser.getLoginName() + "','"
                        + loginUser.getPassword() + "','"
                        + loginUser.getLoginMobile() + "','"
                        + loginUser.getLoginStatus() + "','"
                        + loginUser.getLoginUserType() + "','"
                        + DateFormatUtils.format(loginUser.getRegisterTime(),"yyyy-MM-dd HH:mm:ss") + "')";
        long n = se.executeUpdate(sql);
        if (n > 0){
            return true;
        }
        return false;
    }*/

    //test
    @Override
    public boolean sava_user(String user_id, String user_name, String user_password, String login_mobile, Date create_time) {
        /* USER_ID,USER_NAME,USER_PASSWORD,PHONE,USER_TYPE,TYPE_DETAILS_ID,IS_FLAG,LONGIN_TIME,CREATE_TIME,UPDATE_TIME,*/
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO " +
                        userTable
                        + " (USER_ID,USER_NAME,USER_PASSWORD,PHONE,CREATE_TIME) VALUES ('"
                        + user_id + "','"
                        + user_name + "','"
                        + user_password + "','"
                        + login_mobile + "','"
                        + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss") + "')";
        long n = se.executeUpdate(sql);
        if (n > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Record getUserByUserName(String login_name) {
        String sql =
                "SELECT "
                        + "ID,LOGIN_NAME,LOGIN_MOBILE,LOGIN_MAIL,PASSWORD,LOGIN_STATUS,LAST_LOGIN_TIME,LAST_LOGIN_DEVICE,REGISTER_TIME,LOGIN_USER_TYPE,USER_TYPE FROM "
                        + sysLoginUser + "WHERE LOGIN_NAME='"
                        + login_name + "'";
        SQLExecutor se = read_getSqlExecutor();
        Record record = se.executeRecord(sql);
        return record;
    }

    @Override
    public Record countByLoginMobile(String loginMobile) {
        String sql =
                "SELECT COUNT(LOGIN_MOBILE) AS COUNT FROM "
                + sysLoginUser + " WHERE LOGIN_MOBILE='"
                + loginMobile + "' GROUP BY LOGIN_MOBILE";
        SQLExecutor se = read_getSqlExecutor();
        //long o = se.executeIntScalar(sql,0);
        Record record = se.executeRecord(sql);
        return record;
    }

    @Override
    public boolean short_save_login_user(String userId, String loginName, String password, String loginMobile, String loginStatus, String loginUserType, Date registerTime) {
        SQLExecutor se = getWriteSqlExecutor();
        //ID,LOGIN_NAME,LOGIN_MOBILE,LOGIN_MAIL,PASSWORD,LOGIN_STATUS,LAST_LOGIN_TIME,LAST_LOGIN_IP,LAST_LOGIN_DEVICE,REGISTER_TIME,LOGIN_USER_TYPE
        String sql =
                "INSERT INTO " +
                        sysLoginUser
                        + "(LOGIN_ID,LOGIN_NAME,PASSWORD,LOGIN_MOBILE,LOGIN_STATUS,LOGIN_USER_TYPE,REGISTER_TIME)" +
                        " VALUES (" + "'"
                        + userId + "','" + loginName + "','" + password + "','"
                        + loginMobile + "','" + loginStatus + "','" + loginUserType + "','"
                        + DateFormatUtils.format(registerTime,"yyyy-MM-dd HH:mm:ss") + "')";
        long n = se.executeUpdate(sql);
        if (n > 0) {
           return true;
        }
        return false;
    }

    @Override
    public Record getUserByUserNameCount(String loginName) {
        String sql =
                "SELECT COUNT(LOGIN_NAME) AS COUNT_NAME FROM "
                        + sysLoginUser + " WHERE LOGIN_NAME='"
                        + loginName + "' GROUP BY LOGIN_NAME";
        SQLExecutor se = read_getSqlExecutor();
        //long o = se.executeIntScalar(sql,0);
        Record record = se.executeRecord(sql);
        return record;
    }

    @Override
    public Record getUserBylodinName(String loginName) {
        String sql = "SELECT "
                + "ID,LOGIN_NAME,LOGIN_MOBILE,LOGIN_MAIL,PASSWORD,LOGIN_STATUS,LAST_LOGIN_TIME,LAST_LOGIN_IP,LAST_LOGIN_DEVICE,REGISTER_TIME,LOGIN_USER_TYPE "
                + sysLoginUser + "WHERE LOGIN_NAME='" + loginName + "' ";
        SQLExecutor se = read_getSqlExecutor();
        Record rec = se.executeRecord(sql, null);
        return rec;
    }
}
