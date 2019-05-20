package com.yqh.marketing.service.job_pack;

import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.redis.RedisUtil;
import com.yqh.marketing.basedevss.base.sql.ConnectionFactory;
import com.yqh.marketing.basedevss.base.sql.SQLExecutor;
import com.yqh.marketing.basedevss.base.util.DateUtils;
import com.yqh.marketing.basedevss.base.util.Initializable;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.DateFormat;
import java.util.Date;

public class packImpl implements packLogic,Initializable {

    private static final Logger L = Logger.getLogger(packImpl.class);

    private String packTable = "t_yqh_enterprise_pack\n";

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
    public boolean Save_job_pack(String PACK_ID, String EXPERT_ID, String JOB_ID, byte CHECKED, Date CREATED_TIME, Date UPDATED_TIME) {

        SQLExecutor se = getWriteSqlExecutor();

        String sql =
                "INSERT INTO " +
                        packTable + " (PACK_ID,EXPERT_ID,JOB_ID,CHECKED,CREATED_TIME,UPDATED_TIME) VALUES ('"+PACK_ID+"','"+EXPERT_ID+"','"+JOB_ID+"', "+CHECKED+",'"+ DateFormatUtils.format(CREATED_TIME, "yyyy-MM-dd HH:mm:ss") + "','" + DateFormatUtils.format(UPDATED_TIME, "yyyy-MM-dd HH:mm:ss") +"')";
        long l = se.executeUpdate(sql);
        //System.out.println("添加已成功");
        return l > 0;

    }

    @Override
    public RecordSet getallpack() {

        SQLExecutor se = getWriteSqlExecutor();

        String sql0 = "SELECT * FROM " + packTable + " ORDER BY PACK_ID";
        RecordSet recs = se.executeRecordSet(sql0, null);
        return recs;
    }

}
