package com.yqh.marketing.service.expert;

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

public class ExpertImpl implements ExpertLogic,Initializable {

    private static final Logger L = Logger.getLogger(ExpertImpl.class);

    private String expertTable = "t_yqh_enterprise_expert";
    private String imgTable = "t_yqh_enterprise_img";
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
    public boolean create_expertLogic_information(String img_id, String img_type, String user_id, String expert_id, String logo, String nick_name, String gender, int expert_type, String admin_name, String v_id, String introduction, String email, String phone, String microsignal, String bank_card_number, Date create_time) {
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO "
                        + expertTable
                        + "(USER_ID,EXPERT_ID,LOGO,NICK_NAME,GENDER,EXPERT_TYPE,ADMIN_NAME,V_ID,INTRODUCTION,EMAIL,PHONE,MICROSIGNAL,BANK_CARD_NUMBER,CREATE_TIME) "
                + " VALUES('" + user_id + "','" + expert_id + "','" + logo + "','" + nick_name + "','" + gender + "'," + expert_type
                + ",'" + admin_name + "','" + v_id + "','" + introduction + "','" + email
                + "','"+ phone + "','"+ microsignal + "','"+ bank_card_number + "','" + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss") + "')";

        String sql10 =
                "INSERT INTO "
                        + imgTable
                        + "(IMG_ID,USER_ID,IMG_URL,IMG_TYPE,CREATED_TIME) "
                        + " VALUES('" + img_id
                        + "','"+ user_id + "','"+ logo + "','"+ img_type + "','" + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss") + "')";
        long n = se.executeUpdate(sql);
        long n10 = se.executeUpdate(sql10);

        if (n + n10 > 1){
            return true;
        }
        return false;
    }

    @Override
    public Record getExpertByUserNameCount(String loginName) {
        return null;
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
    public Record expertByUserId(String user_id) {

        SQLExecutor se = getReadSqlExecutor();
        String sql =
                "SELECT"
                        + " LOGO,NICK_NAME,GENDER,EXPERT_TYPE,ADMIN_NAME,V_ID,INTRODUCTION,EMAIL,PHONE,MICROSIGNAL,BANK_CARD_NUMBER,UPDATE_TIME FROM "
                        + expertTable + " WHERE USER_ID = '" + user_id +"'";
        Record record = se.executeRecord(sql);
        return record;

    }


    @Override
    public boolean update_expertLogic_information(String user_id, String logo, String nick_name, String gender, int expert_type, String admin_name, String v_id, String introduction, String email, String phone, String microsignal, String bank_card_number, Date update_time) {

        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "UPDATE "
                        +expertTable+ " SET LOGO = '"+logo+"',NICK_NAME = '"+nick_name+"',EXPERT_TYPE = "+expert_type
                        +",ADMIN_NAME = '"+admin_name+"',V_ID = '"+v_id+"',INTRODUCTION = '"+introduction
                        +"',EMAIL = '"+email+"',PHONE = '"+phone+"',MICROSIGNAL = '"+microsignal+"'" +
                        ",BANK_CARD_NUMBER = '"+bank_card_number+"',UPDATE_TIME = '"+DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")+"'" +
                        " WHERE USER_ID = '"+user_id+"';";
        String sql10 =

                "UPDATE " + imgTable
                        + " SET IMG_URL = '" + logo
                        + "',USER_ID = '" + user_id
                        + "',UPDATED_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")
                        + "' WHERE USER_ID ='" + user_id + "';";

        long n = se.executeUpdate(sql);
        long n1 = se.executeUpdate(sql10);

        if (n+n1 > 1){
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
    public boolean create_expert_number(String number_id, String user_id, int plateform_type
            , String number_name, String fans_count, String number_titly, int number_status, Date create_time) {
        SQLExecutor se = getWriteSqlExecutor();
        String sql =
                "INSERT INTO "
                        + numberTable
                        + "(NUMBER_ID,EXPERT_ID,PLATEFORM_TYPE,NUMBER_NAME,FANS_COUNT,NUMBER_TITLY,NUMBER_STATUS,CREATE_TIME) "
                        + " VALUES('" + number_id + "','"
                        + user_id + "',"
                        + plateform_type + ",'"
                        + number_name + "','"
                        + fans_count + "','"
                        + number_titly + "',"
                        + number_status + ",'"
                        + DateFormatUtils.format(create_time,"yyyy-MM-dd HH:mm:ss") + "')";
        long n = se.executeUpdate(sql);
        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean update_expert_number(String number_id, int plateform_type, String number_name, String fans_count, String number_titly, int number_status, Date update_time) {
        SQLExecutor se = getWriteSqlExecutor();
        //NUMBER_ID,EXPERT_ID,PLATEFORM_TYPE,NUMBER_NAME,FANS_COUNT,NUMBER_TITLY,NUMBER_STATUS,CREATE_TIME,UPDATE_TIME
        String sql =

                "UPDATE " + numberTable
                        + " SET PLATEFORM_TYPE = " + plateform_type
                        + ",NUMBER_NAME = '" + number_name
                        + "',FANS_COUNT = '" + fans_count
                        + "',NUMBER_TITLY = '" + number_titly
                        + "',NUMBER_STATUS = " + number_status
                        + ",UPDATE_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")
                        + "' WHERE NUMBER_ID = '" + number_id + "';";

        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;
    }

    @Override
    public RecordSet expertnumberByUserId(String user_id) {
        SQLExecutor se = getReadSqlExecutor();
        String sql =
                "SELECT "
                        + " NUMBER_ID,EXPERT_ID,PLATEFORM_TYPE,NUMBER_NAME,FANS_COUNT,NUMBER_TITLY,NUMBER_STATUS,CREATE_TIME,UPDATE_TIME FROM "
                        + numberTable + " WHERE EXPERT_ID = '" + user_id +"'";
        RecordSet recordSet = se.executeRecordSet(sql,null);
        return recordSet;
    }

    @Override
    public boolean expertnumberByStatus(String number_id,int user_status,Date update_time) {

        SQLExecutor se = getReadSqlExecutor();

        String sql =
                "UPDATE " + numberTable
                + " SET NUMBER_STATUS = " + user_status
                + ",STATUS_UPDATE_TIME = '" + DateFormatUtils.format(update_time,"yyyy-MM-dd HH:mm:ss")
                + "' WHERE NUMBER_ID = '" + number_id + "';";

        long n = se.executeUpdate(sql);

        if (n > 0){
            return true;
        }
        return false;

    }

    @Override
    public Record get_all_expertnumber_page(String NICK_NAME, String START_TIME, String END_TIME, String nick_name, int STATE, int page, int count) {
        SQLExecutor se = getReadSqlExecutor();
        String filter = "";
        if (START_TIME.length() > 0)
            filter += " AND p.CREATE_TIME >= '" + START_TIME + "' ";
        if (END_TIME.length() > 0)
            filter += " AND p.CREATE_TIME <= '" + END_TIME + "' ";
        if (STATE != 9 && STATE != 999)
            filter += " AND p.STATE = '" + STATE + "' ";
        if (nick_name.length() > 0)
            filter += " AND u.customer_nickname like '%" + nick_name + "%' ";

        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM "
                + numberTable + " WHERE 1=1 ";
        sql0 += filter;

        int rowNum = (int) se.executeRecord(sql0, null).getInt("COUNT1");
        int page_count = 0;
        if (rowNum > 0) {
            if ((rowNum % count) == 0) {
                page_count = rowNum / count;
            } else {
                page_count = (rowNum / count) + 1;
            }
        }
        String sql = "SELECT n.* FROM "
                + numberTable
                + "n WHERE 1=1 ";
        sql += filter;

        int p = 0;
        if (page == 0 || page == 1) {
            p = 0;
        } else {
            p = (page - 1) * count;
        }
        sql += " ORDER BY p.CREATE_TIME DESC LIMIT " + p + "," + count + " ";
        RecordSet recs = se.executeRecordSet(sql, null);
//        for (Record rec : recs) {
//           Record u = GlobalLogics.getCustomerLogic().getUserById(rec.getString("CUSTOMER_ID"));
//           u.copyTo(rec);
//        }
        Record out_rec = new Record();
        out_rec.put("ROWS_COUNT", rowNum);
        out_rec.put("PAGE_COUNT", page_count);
        if (page == 0 || page == 1) {
            out_rec.put("CURRENT_PAGE", 1);
        } else {
            out_rec.put("CURRENT_PAGE", page);
        }
        out_rec.put("PAGE_SIZE", count);
        out_rec.put("DATAS", recs);
        return out_rec;
    }

    @Override
    public Record get_all_expert_page(String NICK_NAME, String start_time, String end_time, String nick_name, int state, int page, int count) {
        SQLExecutor se = getReadSqlExecutor();
        String filter = "";
        if (start_time.length() > 0)
            filter += " AND p.CREATE_TIME >= '" + start_time + "' ";
        if (end_time.length() > 0)
            filter += " AND p.CREATE_TIME <= '" + end_time + "' ";
        if (state != 9 && state != 999)
            filter += " AND p.STATE = '" + state + "' ";
        if (nick_name.length() > 0)
            filter += " AND u.customer_nickname like '%" + nick_name + "%' ";

        String sql0 = "SELECT COUNT(*) AS COUNT1 FROM "
                + expertTable + " WHERE 1=1 ";
        sql0 += filter;

        int rowNum = (int) se.executeRecord(sql0, null).getInt("COUNT1");
        int page_count = 0;
        if (rowNum > 0) {
            if ((rowNum % count) == 0) {
                page_count = rowNum / count;
            } else {
                page_count = (rowNum / count) + 1;
            }
        }
        String sql = "SELECT e.* FROM "
                + expertTable + "e WHERE 1=1 ";
        sql += filter;

        int p = 0;
        if (page == 0 || page == 1) {
            p = 0;
        } else {
            p = (page - 1) * count;
        }
        sql += " ORDER BY p.CREATE_TIME DESC LIMIT " + p + "," + count + " ";
        RecordSet recs = se.executeRecordSet(sql, null);
        Record out_rec = new Record();
        out_rec.put("ROWS_COUNT", rowNum);
        out_rec.put("PAGE_COUNT", page_count);
        if (page == 0 || page == 1) {
            out_rec.put("CURRENT_PAGE", 1);
        } else {
            out_rec.put("CURRENT_PAGE", page);
        }
        out_rec.put("PAGE_SIZE", count);
        out_rec.put("DATAS", recs);
        return out_rec;
    }

    @Override
    public RecordSet get_all_expertnumber() {
        SQLExecutor se = getReadSqlExecutor();
        String sql  = "SELECT e.* FROM " +
                numberTable + " e WHERE 1=1 ;";
        RecordSet records = se.executeRecordSet(sql);
        return records;
    }

}
