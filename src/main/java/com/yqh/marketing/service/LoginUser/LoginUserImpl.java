package com.yqh.marketing.service.LoginUser;

import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.redis.RedisUtil;
import com.yqh.marketing.basedevss.base.sql.ConnectionFactory;
import com.yqh.marketing.basedevss.base.sql.SQLExecutor;
import com.yqh.marketing.basedevss.base.util.Initializable;
import com.yqh.marketing.domain.entity.LoginUser;
import com.yqh.marketing.service.VerificationCode.VerificationCodeImpl;
import com.yqh.marketing.service.VerificationCode.VerificationCodeLogic;
import com.yqh.marketing.service.accessory.AccessoryImpl;

public class LoginUserImpl implements LoginUserLogic,Initializable {

    private static final Logger L = Logger.getLogger(LoginUserImpl.class);
    public static int time = 3 * 60;

    private String sysloginuserTable = "t_yqh_sys_login_user";

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
        return new SQLExecutor(write_connectionFactory,write_db);
    }

    private SQLExecutor read_getSqlExecutor() {
        return new SQLExecutor(read_connectionFactory, read_db);
    }

    @Override
    public String Short_login_user() {
        return null;
    }

    @Override
    public LoginUser getLoginUserByLoginMail(String loginMail) {
        return null;
    }

    @Override
    public Record getLoginUserByLoginMobile(String loginMobile) {
        String sql = "SELECT" + " ID,LOGIN_MOBILE,PASSWORD FROM "+sysloginuserTable
                +" WHERE LOGIN_MOBILE='"+loginMobile+"'";
        return null;
    }

    @Override
    public LoginUser getLoginUserByOpenId(String openId, String thirdPartAccountType) {

        return null;
    }

    @Override
    public int countByLoginMail(String loginMail) {
        return 0;
    }

    @Override
    public int countByLoginMobile(String loginMobile) {
        return 0;
    }

    @Override
    public void modifyPassword(String loginId, String password) {

    }

    @Override
    public LoginUser getLoginUserByLoginUsername(String user_name) {
        return null;
    }

    @Override
    public int countByLoginUsername(String user_name) {
        return 0;
    }

}
