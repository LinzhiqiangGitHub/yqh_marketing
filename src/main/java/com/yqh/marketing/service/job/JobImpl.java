package com.yqh.marketing.service.job;

import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.log.Logger;
import com.yqh.marketing.basedevss.base.redis.RedisUtil;
import com.yqh.marketing.basedevss.base.sql.ConnectionFactory;
import com.yqh.marketing.basedevss.base.sql.SQLExecutor;
import com.yqh.marketing.basedevss.base.util.Initializable;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

public class JobImpl implements JobLogic,Initializable {

    private static final Logger L = Logger.getLogger(JobImpl.class);

    private String jobTable = "t_yqh_enterprise_job";
    private String jobappTable = "t_yqh_enterprise_job_apply_for";
    private String imgTable = "t_yqh_enterprise_img";
    private String getTable = "t_yqh_enterprise_job_get";
    private String numberTable = "t_yqh_enterprise_kol_number";

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
    public boolean create_job(String job_id, String advertiser_id, String logo, String job_name, Date jobperiod_start, Date jobperiod_end
            , long jobperiod_count, String job_titly, int job_terrace, float job_commission, Date create_time) {
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO "
                        + jobTable
                        + "( JOB_ID,ADVERTISER_ID,LOGO,JOB_NAME,JOBPERIOD_START,JOBPERIOD_END,JOBPERIOD_COUNT,JOB_TITLY,JOB_TERRACE,JOB_COMMISSION,CREATE_TIME) "
                        + " VALUES('"
                        + job_id + "','"
                        + advertiser_id + "','"
                        + logo + "','"
                        + job_name + "','"
                        + DateFormatUtils.format(jobperiod_start,"yyyy-MM-dd HH:mm:ss") + "','"
                        + DateFormatUtils.format(jobperiod_end,"yyyy-MM-dd HH:mm:ss") + "',"
                        + jobperiod_count + ",'"
                        + job_titly + "',"
                        + job_terrace + ","
                        + job_commission + ",'"
                        + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss") + "')";

        long n = se.executeUpdate(sql);
        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean create_job_apply(String jobapplyfor_id, String job_id, String advertiser_id, int apply_for_status, Date create_time) {
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO "
                        + jobappTable
                        + "( JOBAPPLYFOR_ID,JOB_ID,ADVERTISER_ID,APPLY_FOR_STATUS,APPLY_FOR_TIME ) "
                        + " VALUES('" + jobapplyfor_id + "','" + job_id + "','" + advertiser_id + "'," + apply_for_status + ",'" + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss") + "')";
        long n = se.executeUpdate(sql);
        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public RecordSet get_all_jobByUserId(String user_id) {
        SQLExecutor se = getReadSqlExecutor();
        String sql = "SELECT job.JOB_ID,job.LOGO,job.JOB_NAME,job.JOBPERIOD_START,job.JOBPERIOD_END,job.JOBPERIOD_COUNT,job.JOB_TITLY,job.JOB_TERRACE,job.JOB_COMMISSION,job.CREATE_TIME,job_apply.APPLY_FOR_STATUS  " +
                " FROM " + jobTable + " job INNER JOIN " + jobappTable + " job_apply ON job.JOB_ID = job_apply.JOB_ID " +" WHERE job.ADVERTISER_ID = " + user_id;
        RecordSet records = se.executeRecordSet(sql);
        return records;
    }

    @Override
    public Record jopById(String job_id) {
        SQLExecutor se = getReadSqlExecutor();
        String sql = "SELECT job.JOB_ID,job.LOGO,job.JOB_NAME,job.JOBPERIOD_START,job.JOBPERIOD_END,job.JOBPERIOD_COUNT,job.JOB_TITLY,job.JOB_TERRACE,job.JOB_COMMISSION,job.CREATE_TIME,job_apply.APPLY_FOR_STATUS  " +
                " FROM " + jobTable + " job INNER JOIN " + jobappTable + " job_apply ON job.JOB_ID = job_apply.JOB_ID " +" WHERE job.JOB_ID = " + job_id;
        Record records = se.executeRecord(sql);
        return records;
    }


    @Override
    public boolean update_job(String job_id, String logo, String job_name, Date jobperiod_start
            , Date jobperiod_end, long jobperiod_count, String job_titly, int job_terrace, float job_commission, Date update_time) {
        SQLExecutor se = getWriteSqlExecutor();

        String sql =

                "UPDATE " + jobTable
                        + " SET LOGO = '" + logo
                        + "',JOB_NAME = '" + job_name
                        + "',JOBPERIOD_START = '" +  DateFormatUtils.format(jobperiod_start,"yyyy-MM-dd HH:mm:ss")
                        + "',JOBPERIOD_END = '" +  DateFormatUtils.format(jobperiod_end,"yyyy-MM-dd HH:mm:ss")
                        + "',JOBPERIOD_COUNT = " + jobperiod_count
                        + ",JOB_TITLY = '" + job_titly
                        + "',JOB_TERRACE = " + job_terrace
                        + ",JOB_COMMISSION = " + job_commission
                        + ",UPDATE_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")
                        + "' WHERE JOB_ID = '" + job_id + "';";

        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean update_job_apply(String job_id, int apply_for_status, Date update_time) {
        SQLExecutor se = getWriteSqlExecutor();

        String sql =

                "UPDATE " + jobappTable
                        + " SET APPLY_FOR_STATUS = " + apply_for_status
                        + " ,UPDATE_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")
                        + "' WHERE JOB_ID = '" + job_id + "';";

        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public RecordSet get_all_job(int approved) {

        SQLExecutor se = getReadSqlExecutor();
        String sql = "SELECT job.JOB_ID,job.LOGO,job.JOB_NAME,job.JOBPERIOD_START,job.JOBPERIOD_END,job.JOBPERIOD_COUNT,job.JOB_TITLY,job.JOB_TERRACE,job.JOB_COMMISSION,job.CREATE_TIME,job_apply.APPLY_FOR_STATUS  " +
                " FROM " + jobTable + " job INNER JOIN " + jobappTable + " job_apply ON job.JOB_ID = job_apply.JOB_ID " +" WHERE job_apply.APPLY_FOR_STATUS = " + approved;
        RecordSet records = se.executeRecordSet(sql);
        return records;

    }

    @Override
    public boolean updateByjobId(String job_id, int job_status, Date update_time) {
        SQLExecutor se = getWriteSqlExecutor();

        String sql =

                "UPDATE " + jobappTable
                + " SET APPLY_FOR_STATUS=" + job_status
                + ",UPDATE_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")
                + "' WHERE JOB_ID = '" + job_id + "';";

        long n = se.executeUpdate(sql);
        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean create_Image(String img_id, String job_id, String logo, String img_type, Date create_time) {
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO "
                        + imgTable
                        + "(IMG_ID,JOB_ID,IMG_URL,IMG_TYPE,CREATED_TIME) "
                        + " VALUES('" + img_id
                        + "','"+ job_id + "','"+ logo + "','"+ img_type + "','" + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss") + "')";
        long n = se.executeUpdate(sql);
        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean update_Image(String job_id, String logo,String img_type, Date update_time) {

        SQLExecutor se = getWriteSqlExecutor();

        String sql =

                "UPDATE " + imgTable
                        + " SET IMG_URL = '" + logo
                        + "',IMG_TYPE = '" + img_type
                        + "',UPDATED_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")
                        + "' WHERE JOB_ID ='" + job_id + "';";

        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean saveCommit(String complete_id, String user_id, String number_id, String job_id, int job_complete_status, Date git_time) {
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO "
                        + getTable
                        + "(COMPLETE_ID,USER_ID,NUMBER_ID,JOB_ID,JOB_COMPLETE_STATUS,GIT_TIME) "
                        + " VALUES('"
                        + complete_id + "','"
                        + user_id + "','"
                        + number_id + "','"
                        + job_id + "',"
                        + job_complete_status + ",'"
                        + DateFormatUtils.format(git_time,"yyyy-MM-dd HH:mm:ss") + "')";

        long n = se.executeUpdate(sql);
        if (n > 0){
            return true;
        }
        return false;
    }


    @Override
    public int JobByjobId(String job_id) {

        SQLExecutor se = getReadSqlExecutor();
        String sql = "SELECT JOB_TERRACE FROM " + jobTable
                + " WHERE JOB_ID = '" + job_id + "';";
        Record record = se.executeRecord(sql);
        return (int) record.getInt("JOB_TERRACE");

    }

    @Override
    public RecordSet NumberByUserid(String user_id) {

        SQLExecutor se = getReadSqlExecutor();

        String sql = "SELECT NUMBER_ID,PLATEFORM_TYPE FROM " + numberTable
                + " WHERE USER_ID = '" + user_id + "';";
        RecordSet records = se.executeRecordSet(sql);

        return records;
    }
}
