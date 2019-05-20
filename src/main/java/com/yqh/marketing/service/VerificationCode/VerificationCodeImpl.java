package com.yqh.marketing.service.VerificationCode;

import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.redis.RedisUtil;
import com.yqh.marketing.basedevss.base.sql.ConnectionFactory;
import com.yqh.marketing.basedevss.base.sql.SQLExecutor;
import com.yqh.marketing.basedevss.base.util.Initializable;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

public class VerificationCodeImpl implements VerificationCodeLogic,Initializable {

    private static final Logger L = Logger.getLogger(VerificationCodeImpl.class);
    public static int time = 3 * 60;

    private String sys_login_short_message = "t_yqh_sys_login_short_message";
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
    public boolean checkLoginVerificationCode(String loginMobile, String verificationCode) {
        String sql =
                "SELECT COUNT(*) AS COUNT FROM " + sys_login_short_message
                        + " WHERE LOGIN_MOBILE = '" + loginMobile + "' AND Verification_Code = '" + verificationCode + "'";
        SQLExecutor se = read_getSqlExecutor();
        long n =(Long)se.executeScalar(sql);
        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public String createLoginVerificationCode(String loginMobile) {
        return null;
    }

    @Override
    public boolean checkBindMobileVerificationCode(String loginMobile, String verificationCode) {
        return false;
    }

    @Override
    public boolean checkCancelBindMobileVerificationCode(String loginMobile, String verificationCode) {
        return false;
    }

    @Override
    public String createBindMobileVerificationCode(String loginMobile) {
        return null;
    }

    @Override
    public String createCancelBindMobileVerificationCode(String loginMobile) {
        return null;
    }

    @Override
    public boolean checkForgetPasswordVerificationCode(String loginMobile, String verificationCode) {
        return false;
    }

    @Override
    public String createForgetPasswordVerificationCode(String loginMobile) {
        return null;
    }

    @Override
    public boolean checkRegisterVerificationCode(String loginMobile, String verificationCode) {
        String sql =
            "SELECT COUNT(*) FROM " + sys_login_short_message
                + " WHERE LOGIN_MOBILE = '" + loginMobile + "' AND Verification_Code = '" + verificationCode + "'";
        SQLExecutor se = read_getSqlExecutor();
        long n =(Long)se.executeScalar(sql);
        //SQLExecutor.executeScalar();
        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean createRegisterVerificationCode(String ID, String VerificationCode, String loginMobile, Date end_date_time, Date create_time, String registerTypePrefix) {

        SQLExecutor se = getWriteSqlExecutor();

        String sql =
                "INSERT INTO " + sys_login_short_message
                + "(ID,VERIFICATION_CODE,LOGIN_MOBILE,END_DATE_TIME,CREATE_TIME,SHORT_MESSAGE_TYPE) "
                + " VALUES('" + ID + "','"
                + VerificationCode + "','"
                + loginMobile + "','"
                + DateFormatUtils.format(end_date_time,"yyyy-MM-dd HH:mm:ss") + "','"
                + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss") + "','"
                + registerTypePrefix + "')";

        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;
    }


    @Override
    public String createBindMailVerificationCode(String loginMail) {
        return null;
    }

    @Override
    public String createCancelBindMailVerificationCode(String loginMail) {
        return null;
    }

    @Override
    public boolean checkMailForgetPasswordVerificationCode(String loginMobile, String loginId, String verificationCode) {
        return false;
    }

    @Override
    public String createMailForgetPasswordVerificationCode(String loginMail) {
        return null;
    }

    @Override
    public boolean deleteVerificationCodeByLoginMobile(String loginMobile) {
        String sql = "DELETE FROM " + sys_login_short_message +
                " WHERE LOGIN_MOBILE = '" + loginMobile + "'";
        long n = getWriteSqlExecutor().executeUpdate(sql);
        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateVerificationCodeByID(String id) {
        return false;
    }


    @Override
    public String checkLoginVerificationCodeOutTime(String login_mobile) {

        SQLExecutor se = getWriteSqlExecutor();
        String sql = "SELECT END_DATE_TIME FROM " + sys_login_short_message
                + " WHERE LOGIN_MOBILE = '" + login_mobile + "'";

        Record record = se.executeRecord(sql);
        String end_date_time = record .getString("END_DATE_TIME");

        return end_date_time;
    }

    @Override
    public Record countByLoginMobile(String loginMobile) {
        String sql =
                "SELECT COUNT(LOGIN_MOBILE) AS COUNT FROM "
                        + sys_login_short_message + " WHERE LOGIN_MOBILE='"
                        + loginMobile + "' GROUP BY LOGIN_MOBILE";
        SQLExecutor se = read_getSqlExecutor();
        //long o = se.executeIntScalar(sql,0);
        Record record = se.executeRecord(sql);
        return record;
    }

    @Override
    public Record VerificationCodeByLoginMobile(String loginMobile) {

        String sql =
                "SELECT LOGIN_MOBILE,VERIFICATION_CODE,END_DATE_TIME,CREATE_TIME,SHORT_MESSAGE_TYPE FROM "
                        + sys_login_short_message + " WHERE LOGIN_MOBILE='"
                        + loginMobile + "'";
        SQLExecutor se = read_getSqlExecutor();
        //long o = se.executeIntScalar(sql,0);
        Record record = se.executeRecord(sql);
        return record;

    }

    @Override
    public boolean updateRegisterVerificationCode(String loginMobile, Date end_date_time, Date date) {

        SQLExecutor se = getWriteSqlExecutor();

        String sql =

                "UPDATE " + sys_login_short_message
                        + " SET END_DATE_TIME = '" + DateFormatUtils.format(end_date_time,"yyyy-MM-dd HH:mm:ss")
                        + "',CREATE_TIME = '" +DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss")
                        + "' WHERE LOGIN_MOBILE ='" + loginMobile + "';";

        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCodeByloginMobile(String loginMobile, String verificationCode, Date end_date_time_one, Date date) {

        SQLExecutor se = getWriteSqlExecutor();

        String sql =

                "UPDATE " + sys_login_short_message
                        + " SET VERIFICATION_CODE = '" + verificationCode
                        + "',END_DATE_TIME = '" + DateFormatUtils.format(end_date_time_one,"yyyy-MM-dd HH:mm:ss")
                        + "',CREATE_TIME = '" +DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss")
                        + "' WHERE LOGIN_MOBILE ='" + loginMobile + "';";

        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;
    }

    //过期时间
    public static void out_time() {
        while (time > 0) {
            time--;
            try {
                Thread.sleep(1000);
                int mm = time / 60 % 60;
                int ss = time % 60;
                System.out.println("还剩" + mm + "分钟" + ss + "秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
