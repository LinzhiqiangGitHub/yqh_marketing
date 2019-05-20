package com.yqh.marketing.service.accessory;

import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.redis.RedisUtil;
import com.yqh.marketing.basedevss.base.sql.ConnectionFactory;
import com.yqh.marketing.basedevss.base.sql.SQLExecutor;
import com.yqh.marketing.basedevss.base.util.DateUtils;
import com.yqh.marketing.basedevss.base.util.Initializable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccessoryImpl implements AccessoryLogic,Initializable {

    private static final Logger L = Logger.getLogger(AccessoryImpl.class);

    private String accessoryTable = "t_yqh_enterprise_accessory";

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

  /*  @Override
    public boolean save_Accessory(TYqhEnterpriseAccessory tYqhEnterpriseAccessory) {
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO " + accessoryTable + " (ACCESSORY_ID，USER_ID，ADVERTISER_ID，JOB_ID，COMPLETE_ID，ACCESSORY_TYPE，ACCESSORY_URL，ACCESSORY_STATUS，CREATE_TIME，UPDATE_TIME)" +
                        " VALUES ('" + tYqhEnterpriseAccessory.getAccessoryId() + "','" + tYqhEnterpriseAccessory.getUserId() + "','" + tYqhEnterpriseAccessory.getAdvertiserId() + "','" + tYqhEnterpriseAccessory.getJobId() + "','" + tYqhEnterpriseAccessory.getCompleteId() + "','" + tYqhEnterpriseAccessory.getAccessoryType() + "','" + tYqhEnterpriseAccessory.getAccessoryUrl() +
                        "','" + tYqhEnterpriseAccessory.getAccessoryStatus() + "','" + tYqhEnterpriseAccessory.getCreateTime() + "','" + tYqhEnterpriseAccessory.getUpdateTime() + "') ";
        long l = se.executeUpdate(sql);
        System.out.println("添加已成功");
        return l>0;
    }*/

    @Override
    public boolean save_Accessory1(long ACCESSORY_ID, String USER_ID, String ADVERTISER_ID, String JOB_ID, String COMPLETE_ID, int ACCESSORY_TYPE, String ACCESSORY_URL, int ACCESSORY_STATUS, Date CREATE_TIME, Date UPDATE_TIME) {
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO " + accessoryTable + "(ACCESSORY_ID，USER_ID，ADVERTISER_ID，JOB_ID，COMPLETE_ID，ACCESSORY_TYPE，ACCESSORY_URL，ACCESSORY_STATUS，CREATE_TIME，UPDATE_TIME)"
                        + " VALUES ('" + ACCESSORY_ID + "','" + USER_ID + "','" + ADVERTISER_ID + "','" + JOB_ID + "','" + COMPLETE_ID + "','" + ACCESSORY_TYPE + "','" + ACCESSORY_URL + "','" + ACCESSORY_STATUS + "','" + DateUtils.formatDate(CREATE_TIME,"yyyy-MM-dd HH:mm:ss") + "','" + DateUtils.formatDate(UPDATE_TIME,"yyyy-MM-dd HH:mm:ss") + "') ";
        long n = se.executeUpdate(sql);
        return n > 0;
    }

    @Override
    public RecordSet getAllAccessory() {
        SQLExecutor se = getWriteSqlExecutor();
        String sql0 = "SELECT * FROM " + accessoryTable + " ORDER BY ACCESSORY_ID";
        RecordSet recs = se.executeRecordSet(sql0, null);
        return recs;
    }

}
