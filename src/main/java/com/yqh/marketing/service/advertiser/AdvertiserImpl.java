package com.yqh.marketing.service.advertiser;

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

public class AdvertiserImpl implements AdvertiserLogic,Initializable {

    private static final Logger L = Logger.getLogger(AdvertiserImpl.class);

    private String advertiserTable = "t_yqh_enterprise_advertiser";
    private String imgTable = "t_yqh_enterprise_img";

    RedisUtil jedis=new RedisUtil();

    private ConnectionFactory write_connectionFactory;
    private String write_db;
    private ConnectionFactory read_connectionFactory;
    private String read_db;


    @Override
    public void init() {
        Configuration conf = GlobalConfig.get();
        jedis=new RedisUtil();
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

    private SQLExecutor getReadSqlExecutor() {
        return new SQLExecutor(read_connectionFactory,read_db);
    }


    @Override
    public boolean create_advertise_information(String advertiser_id, String user_id, String nick_name, String company_name, String advertiser_logo, String advertiser_contacts,String advertiser_phone, String advertiser_email, String advertiser_type, String microsignal, String advertiser_site, Date create_time) {
         SQLExecutor se = getWriteSqlExecutor();
         String sql =
                "INSERT INTO "
                        + advertiserTable
                        + "( ADVERTISER_ID, USER_ID, NICK_NAME,COMPANY_NAME, ADVERTISER_LOGO, ADVERTISER_CONTACTS, ADVERTISER_PHONE, ADVERTISER_EMAIL,ADVERTISER_TYPE, MICROSIGNAL,ADVERTISER_SITE,CREATE_TIME) "
                        + " VALUES('"+ advertiser_id + "','" + user_id + "','" + nick_name + "','" + company_name + "','" + advertiser_logo + "','" + advertiser_contacts + "','" + advertiser_phone + "','" + advertiser_email + "','" + advertiser_type + "','" + microsignal+ "','" + advertiser_site + "','" + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss")+"')";
         long n = se.executeUpdate(sql);
         if (n > 0){
             return true;
         }
         return false;
    }


    @Override
    public Record getAdvertiserByAdvertiserNameCount(String company_name) {
        return null;
    }



    @Override
    public boolean gat_all_advertise_information(String user_id, String nick_name, String company_name, String img_url, String advertiser_contacts, String advertiser_phone, String advertiser_email, String advertiser_type, String microsignal, String advertiser_site, Date update_time) {
        return false;
    }


    @Override
    public boolean create_Image(String img_id, String user_id, String img_url, String img_type, Date create_time) {
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO "
                        + imgTable
                        + "(IMG_ID,USER_ID,IMG_URL,IMG_TYPE,CREATED_TIME) "
                        + " VALUES('" + img_id
                        + "','"+ user_id + "','"+ img_url + "','"+ img_type + "','" + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss") + "')";
        long n = se.executeUpdate(sql);
        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean update_advertise_information(String user_id, String nick_name, String company_name, String advertiser_logo, String advertiser_contacts, String advertiser_phone, String advertiser_email, String advertiser_type, String microsignal, String advertiser_site, Date update_time) {
        SQLExecutor se = getWriteSqlExecutor();

        String sql =
                "UPDATE " + advertiserTable
                        +" SET NICK_NAME = '" + nick_name +"'"
                        + ",COMPANY_NAME = '" + company_name +"'"
                        + ",ADVERTISER_LOGO = '" + advertiser_logo +"'"
                        + ",ADVERTISER_CONTACTS = '" + advertiser_contacts +"'"
                        + ",ADVERTISER_PHONE = '" + advertiser_phone +"'"
                        + ",ADVERTISER_EMAIL = '" + advertiser_email +"'"
                        + ",ADVERTISER_TYPE = '" + advertiser_type +"'"
                        + ",MICROSIGNAL = '" + microsignal +"'"
                        + ",ADVERTISER_SITE = '" + advertiser_site +"'"
                        + ",UPDATE_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss") +"'"
                        + " WHERE USER_ID = '" + user_id +"';";
        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean update_Image(String user_id, String img_url, Date update_time) {

        SQLExecutor se = getWriteSqlExecutor();

        String sql =

                "UPDATE " + imgTable
                        + " SET IMG_URL = '" + img_url
                        + "',UPDATED_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")
                        + "' WHERE USER_ID ='" + user_id + "';";

        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;

    }

    @Override
    public Record advertiseByuserId(String user_id) {
        SQLExecutor se = getReadSqlExecutor();
        String sql =
                "SELECT"
                        + " ADVERTISER_ID,NICK_NAME,COMPANY_NAME,ADVERTISER_LOGO,ADVERTISER_CONTACTS,ADVERTISER_PHONE,ADVERTISER_EMAIL,ADVERTISER_TYPE,MICROSIGNAL,ADVERTISER_SITE,UPDATE_TIME FROM "
                        + advertiserTable + " WHERE USER_ID = '" + user_id +"'";
        Record record = se.executeRecord(sql);
        return record;
    }


    @Override
    public Record ImageByuserId(String user_id) {
        SQLExecutor se = getReadSqlExecutor();
        String sql =
                "SELECT"
                        + " IMG_ID,USER_ID,IMG_URL,IMG_TYPE,CREATED_TIME,UPDATED_TIME FROM "
                        + imgTable + " WHERE USER_ID = '" + user_id +"'";
        Record record = se.executeRecord(sql);
        return record;
    }

    @Override
    public boolean update_advertiserByStatus(String user_id, int user_status, Date update_time) {
        SQLExecutor se = getReadSqlExecutor();

        String sql =

                "UPDATE " + advertiserTable
                        + " SET ADVERTISER_STATUS = " + user_status
                        + ",ADVERTISER_UPDATE_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")
                        + "' WHERE USER_ID = '" + user_id + "';";

        long n = se.executeUpdate(sql);

        if (n > 0) {
            return true;
        }
        return false;
    }
}
