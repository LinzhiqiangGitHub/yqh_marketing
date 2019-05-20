package com.yqh.marketing.service.NormalLogin;

import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.redis.RedisUtil;
import com.yqh.marketing.basedevss.base.sql.ConnectionFactory;
import com.yqh.marketing.basedevss.base.sql.SQLExecutor;
import com.yqh.marketing.basedevss.base.util.Initializable;

public class NormalLoginImpl implements NormalLoginLogic,Initializable {

    private static final Logger L = Logger.getLogger(NormalLoginImpl.class);

    private String userTable = "t_yqh_enterprise_user";
    private String loginuserTable = "t_yqh_sys_login_user";

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

    private SQLExecutor read_getSqlExecutor() {
        return new SQLExecutor(read_connectionFactory, read_db);
    }


    @Override
    public Record getuserByUserNameAndPassword(String user_name) {
        String sql =
                "SELECT "
                        + " ID,LOGIN_NAME,PASSWORD FROM " + userTable +" WHERE LOGIN_NAME = '"
                        + user_name+"'";
        SQLExecutor se = read_getSqlExecutor();
        Record record = se.executeRecord(sql);
        return record;
    }

    @Override
    public Record getUserByUserName(String user_name) {
        return null;
    }
}
