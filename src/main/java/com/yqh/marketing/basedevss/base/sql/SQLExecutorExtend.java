package com.yqh.marketing.basedevss.base.sql;

import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.ServiceResult;
import com.yqh.marketing.basedevss.base.BaseErrors;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;
import com.yqh.marketing.basedevss.base.data.Values;
import com.yqh.marketing.basedevss.base.util.ObjectHolder;
import com.yqh.marketing.basedevss.base.util.json.JsonUtils;
import com.yqh.marketing.common.cache.SpyMemcachedUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * Created by chenguo on 2015/12/9.
 */
public class SQLExecutorExtend extends SQLExecutor {
    private static final Logger L = LoggerFactory.getLogger(SQLExecutorExtend.class);
    private final List<String> dbs=Arrays.asList(super.getDbs().split(","));
    public SQLExecutorExtend(ConnectionFactory connectionFactory, String dbs) {
        super(connectionFactory, dbs);
    }
    public ServiceResult updateWithTrans(String sql) {
        List<String> sqls=new ArrayList<String>();
        sqls.add(sql);
        return updateWithTrans(sqls);
    }
    public ServiceResult updateWithTrans(String sql,Connection defaultConn) {
        List<String> sqls=new ArrayList<String>();
        sqls.add(sql);
        return updateWithTrans(sqls,defaultConn);
    }
    public ServiceResult updateWithTrans(List<String> sqls){
        return updateWithTrans(sqls,null);
    }
    public ServiceResult updateWithTrans(List<String> sqls,Connection defaultConn) {
        Validate.notNull(sqls);
        ServiceResult result=new ServiceResult();
        try {
            long effected = 0;
            for (String db : dbs) {
                try {
                    if(defaultConn!=null){
                        effected = executeUpdate(sqls,defaultConn);
                    }else {
                        effected = Update(super.getConnectionFactory(), db, sqls, null);
                    }
                } catch (ServerException e) {
                    String q=StringUtils.join(sqls.toArray(),";");
                    if (e.code == BaseErrors.PLATFORM_WEBMETHOD_GET_CONTENT_ERROR)
                        result.addErrorMessage(String.valueOf("更新失败".hashCode()), "更新失败", e.getLocalizedMessage()+e.toString()+q);
                    else {
                        L.debug(e.getLocalizedMessage() + e.toString() + q);
                        result.addErrorMessage(String.valueOf("事务处理异常，请联系管理员".hashCode()), "事务处理异常，请联系管理员", e.getLocalizedMessage() + e.toString() + q);
                    }
                    result.addThrowable(e);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public Record executeWithTrans(List<String> sqls) {
        Validate.notNull(sqls);
        Record rd=new Record();
        long effected = 0;
        for (String db : dbs) {
            try {
                rd = UpdateAndSelect(super.getConnectionFactory(), db, sqls, null);
            } catch (ServerException e) {
                rd=new Record();
                e.printStackTrace();
            }
        }

        return rd;
    }
    public String getRandomDb() {
        Random rand = new Random();
        int index = rand.nextInt(dbs.size());
        return dbs.get(index);
    }
    private static long Update(ConnectionFactory cf, String db, List<String> sqls, final ObjectHolder<Long> genKeyHolder) {
        Validate.notNull(sqls);
        if (sqls.isEmpty())
        {
            return 0L;
        }

        final List<String> sqls0 = sqls;
        final ObjectHolder<Long> effected = new ObjectHolder<Long>(0L);
        executeStatementTrans(cf, db, new SQLStatementHandler() {
            @Override
            public void handle(Statement stmt) throws SQLException {
                for (String sql : sqls0) {
                    //if lock db ,on this

                     int n=executeUpdate0(stmt, sql, genKeyHolder);
//                     if(n<=0)
//                         throw new ServerException(BaseErrors.PLATFORM_WEBMETHOD_GET_CONTENT_ERROR);
                     effected.value+=n;
                }
            }
        });
        return effected.value;
    }
    private static Record UpdateAndSelect(ConnectionFactory cf, String db, List<String> sqls, final ObjectHolder<Long> genKeyHolder) {
        Validate.notNull(sqls);
        if (sqls.isEmpty())
        {
            return null;
        }
        final ObjectHolder<Map<String,RecordSet>> map=new ObjectHolder<Map<String, RecordSet>>();
        final ObjectHolder<Record> rd=new ObjectHolder<Record>();
        final ObjectHolder<RecordSet> rs=new ObjectHolder<RecordSet>();
        final List<String> sqls0 = sqls;
        //final ObjectHolder<Long> effected = new ObjectHolder<Long>(0L);
        executeStatementTrans(cf, db, new SQLStatementHandler() {
            @Override
            public void handle(Statement stmt) throws SQLException {
                Record r=new Record();
                for (String sql : sqls0) {
                    //if lock db ,on this
                    if(sql.trim().split(" ")[0].toLowerCase().equals("select"))
                    {
                        if(rs!=null)
                        {
                            rs.value=executeRecordSet(stmt,sql,null);
                            r.put(Integer.toString(sql.hashCode()),rs.value);
                            rd.value=r;
                        }

                    }
                    else {
                        int n = executeUpdate0(stmt, sql, genKeyHolder);
                        if(n<=0)
                            throw new ServerException(BaseErrors.PLATFORM_WEBMETHOD_GET_CONTENT_ERROR);
                    }
                    //effected.value+=n;

                }
            }
        });
        return rd.value;
    }
    protected static int executeUpdate0(Statement stmt, String sql, ObjectHolder<Long> genKeyHolder) throws SQLException {
        int n = 0;
        n = stmt.executeUpdate(sql);
        if (genKeyHolder != null) {
            ResultSet genKeysRs = stmt.getGeneratedKeys();
            try {
                if (genKeysRs.next()) {
                    genKeyHolder.value = Values.toInt(genKeysRs.getLong(1));
                }
            } finally {
                genKeysRs.close();
            }
        }
        return n;
    }
    public static void executeStatementTrans(ConnectionFactory cf, String db, SQLStatementHandler handler) {
        Validate.notNull(cf);
        Validate.notNull(db);
        Validate.notNull(handler);

        try {
            Connection conn = null;
            try {
                conn = cf.getConnection(db);
                //设置事务的提交方式为非自动提交：
                conn.setAutoCommit(false);
                Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    handler.handle(stmt);
                }
                catch (Exception ex)
                {
                    throw new ServerException(BaseErrors.PLATFORM_SQL_ERROR,ex.getMessage(),ex.getMessage());
                    //ex.printStackTrace();
                }
                finally {
                    if (stmt != null)
                        stmt.close();
                }
                conn.commit();
            } catch (SQLException e) {
                try {
                    //在catch块内添加回滚事务，表示操作出现异常，撤销事务：
                    conn.rollback();

                } catch (SQLException e1) {
                    // TODO Auto-generatedcatch block
                    e1.printStackTrace();
                }
                e.printStackTrace();
                L.debug("connection db error:db=" + db + ",e=" + e.toString());
                throw new ServerException(BaseErrors.PLATFORM_SQL_ERROR, e);
            } finally {
                if (conn != null)
                    conn.close();

            }
        } catch (SQLException e) {
            L.debug("connection db error:db=" + db + ",e=" + e.toString());
            throw new ServerException(BaseErrors.PLATFORM_SQL_ERROR, e);
        }
    }

    public <T> T executePrepareStatement(List<T> lst,Class<T> t,ConnectionFactory cf, String db,Connection defaultConn,String sql,Object[] params) {
        Validate.notNull(cf);
        Validate.notNull(db);
        T obj=null;
        boolean isSimpleDataType=false;
        try {
            obj = t.newInstance();
        } catch (Exception e) {
            obj=null;
        }
        if(obj==null){
            isSimpleDataType=true;
        }
        try {
            Connection conn = null;
            try {
                conn =(defaultConn==null? cf.getConnection(db):defaultConn);
                if(defaultConn!=null){
                    conn.setAutoCommit(false);
                }
                PreparedStatement stmt=null;
                try {
                    stmt = conn.prepareStatement(sql);
                    if(params!=null) {
                        for (int i = 0; i < params.length; i++) {
                            stmt.setObject((i + 1), params[i]);
                        }
                    }
                    ResultSet rs = null;
                    try {
                        rs = stmt.executeQuery();
                        if(obj instanceof Record){
                            if (rs.next()) {
                                Record re = bindRecord(rs, null);
                                if (re==null||re.size()==0){
                                    re=null;
                                }
                                obj=(T)re;
                            }
                        }else
                        if(obj instanceof RecordSet) {
                            RecordSet recs=new RecordSet();
                            while (rs.next()) {
                                Record rec = bindRecord(rs, null);
                                recs.add(rec);
                            }
                            if(recs.size()==0){
                                recs=null;
                            }
                            obj=(T)recs;
                        }else
                        if((obj instanceof  Object&&obj.getClass().getName().equals("java.lang.Object"))||isSimpleDataType){
                            if (rs.next())
                                obj=(T) rs.getObject(1);
                            else
                                obj= null;
                        }
                        else
                        {
                            if(lst==null) {
                                String json = JsonUtils.toJson(obj, false);
                                bindEntity(rs, obj);
                                if (JsonUtils.toJson(obj, false).equals(json))
                                    obj = null;
                            }else{
                                bindEntityList(rs,lst,obj);
                            }
                        }
                        return obj;
                    } finally {
                        if (rs != null)
                            rs.close();
                    }
                } finally {
                    if (stmt != null)
                        stmt.close();
                }
            } finally {
                if(defaultConn==null) {
                    if (conn != null)
                        conn.close();
                }

            }
        } catch (SQLException e) {
            L.debug("connection db error:db=" + db.toString() + ",e=" + e.toString());
            throw new ServerException(BaseErrors.PLATFORM_SQL_ERROR, e);
        }
    }
    public static void executeStatement(ConnectionFactory cf, String db,Connection defaultConn, SQLStatementHandler handler) {
        Validate.notNull(cf);
        Validate.notNull(db);
        Validate.notNull(handler);
        try {
            Connection conn = null;
            try {
                conn =(defaultConn==null? cf.getConnection(db):defaultConn);
                if(defaultConn!=null){
                    conn.setAutoCommit(false);
                }
                Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                    handler.handle(stmt);
                } finally {
                    if (stmt != null)
                        stmt.close();
                }
            } finally {
                if(defaultConn==null) {
                    if (conn != null)
                        conn.close();
                }

            }
        } catch (SQLException e) {
            L.debug("connection db error:db=" + db.toString() + ",e=" + e.toString());
            throw new ServerException(BaseErrors.PLATFORM_SQL_ERROR, e);
        }
    }
    public <T> T query(Class<T> t,String sql){
        return query(t,sql,null);
    }
    public <T> T query(Class<T> t,String sql,Connection defaultConn) {
        String db = getRandomDb();
        ConnectionFactory cf=super.getConnectionFactory();
        final String sql0 = sql;
        T obj=null;
        try {
            obj=t.newInstance();
        } catch (Exception e) {
            obj=null;
        }
        final T rec0=obj;
        String json= JsonUtils.toJson(rec0,false);
        executeStatement(cf, db,defaultConn, new SQLStatementHandler() {
            @Override
            public void handle(Statement stmt) throws SQLException {
                executeQuery(stmt, sql0, rec0);
            }
        });
        if(JsonUtils.toJson(rec0,false).equals(json))
            return null;
        return rec0;
    }
    public <T> T query(Class<T> t,String sql,int expiredSecond){
        return query(t,sql,expiredSecond,null);
    }
    public <T> T query(Class<T> t,String sql,int expiredSecond,Connection defaultConn) {
        T v= SpyMemcachedUtil.getInstance().get("query_"+sql.hashCode());
        if(v!=null){
            //L.debug("缓存命中");
            return v;
        }
        v= query(t,sql,defaultConn);
        if(v!=null){
            SpyMemcachedUtil.getInstance().put("query_"+sql.hashCode(),v,expiredSecond);
        }
        return v;
    }

    public <T> List<T> queryList(Class<T> t,String sql){
        return queryList(t,sql,null);
    }
    public <T> List<T> queryList(Class<T> t,String sql,Connection defaultConnn) {
        String db = getRandomDb();
        ConnectionFactory cf=super.getConnectionFactory();
        final String sql0 = sql;
        T obj=null;
        try {
            obj=t.newInstance();
        } catch (Exception e) {
            obj=null;
        }
        final T rec0=obj;
        final List<T> lst=new ArrayList<T>();
        executeStatement(cf, db,defaultConnn, new SQLStatementHandler() {
            @Override
            public void handle(Statement stmt) throws SQLException {
                executeQueryList(stmt, sql0,lst, rec0);
            }
        });
        return lst;
    }
    public <T> List<T> queryList(Class<T> t,String sql,int expiredSecond){
        return queryList(t,sql,expiredSecond,null);
    }
    public <T> List<T> queryList(Class<T> t,String sql,int expiredSecond,Connection defaultConn) {
        List<T> v= SpyMemcachedUtil.getInstance().get("queryList_"+sql.hashCode());
        if(v!=null){
            //L.debug("缓存命中");
            return v;
        }
        v= queryList(t,sql,defaultConn);
        if(v!=null && v.size()>0){
            SpyMemcachedUtil.getInstance().put("queryList_"+sql.hashCode(),v,expiredSecond);
        }
        return v;
    }

    private <T> void executeQueryList(Statement stmt, String sql, List<T> lst,T rec) throws SQLException {
        if (rec == null)
            throw new SQLException("T is NULL");

        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
            bindEntityList(rs,lst, rec);
        } finally {
            if (rs != null)
                rs.close();
        }
    }
    protected  <T> void  bindEntityList(ResultSet rs, List<T> lst,T rec) {
        ResultSetMetaData rsmd = null;
        String temp = "";
        Method s = null;
        Field f=null;
        String field;
        try {
            rsmd = rs.getMetaData();

            List<Method> m =new ArrayList<Method>();


            boolean isFirst=true;
            while (rs.next()) {
                T r= null;
                if(rec.equals("")){
                    r=(T)"";
                }else {
                    r = (T) rec.getClass().newInstance();
                }
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    temp = rsmd.getColumnName(i);
                    try {
                        if(r.equals("")){
                            r= (T)rs.getObject(temp).toString();
                        }else {
                            if (isFirst) {
                                field = StringHelper.toJavaAttributeName(temp);
                                f = r.getClass().getDeclaredField(field);
                                s = r.getClass().getDeclaredMethod(StringHelper
                                        .asserSetMethodName(field), f.getType());
                                m.add(s);
                            }
                            Object v = rs.getObject(temp);
                            if (v != null)
                                m.get(i - 1).invoke(r, v);
                        }
                    }catch (Exception e)
                    {
                        //r= (T)rs.getObject(temp).toString();
                        //e.printStackTrace();
                    }
                }
                isFirst=false;
                lst.add(r);
            }
        } catch (SQLException e) {
            throw new ServerException(BaseErrors.PLATFORM_SQL_ERROR, e);
        }catch (InstantiationException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    private <T> void executeQuery(Statement stmt, String sql, T rec) throws SQLException {
        if (rec == null)
            throw new SQLException("T is NULL");

        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);

            bindEntity(rs, rec);

        } finally {
            if (rs != null)
                rs.close();
        }
    }
    protected <T> void  bindEntity(ResultSet rs, T rec) {
        ResultSetMetaData rsmd = null;
        String temp = "";
        Method s = null;
        Field f=null;
        String field;
        try {
            rsmd = rs.getMetaData();
            if (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    temp = rsmd.getColumnName(i);
                    field = StringHelper.toJavaAttributeName(temp);
                    try {
                        f = rec.getClass().getDeclaredField(field);

                        s = rec.getClass().getDeclaredMethod(StringHelper
                                .asserSetMethodName(field), f.getType());
                        Object v = rs.getObject(temp);

                        if (v != null)
                            s.invoke(rec, v);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException(e);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        //e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
        throw new ServerException(BaseErrors.PLATFORM_SQL_ERROR, e);
        }



    }
    public <T> List<T> queryListWithPreparedSql(Class<T> t,String sql,Object[] params,Connection defaultConn) {
        T v=null;
        List<T> lst=new ArrayList<T>();
        try {
            String db = getRandomDb();
            ConnectionFactory cf = super.getConnectionFactory();
            v = executePrepareStatement(lst,t, cf, db, defaultConn, sql, params);
        }catch (Exception e){

        }finally {
            return lst;
        }
    }
    public <T> List<T> queryListWithPreparedSql(Class<T> t,String sql,Object[] params) {
        return queryListWithPreparedSql (t, sql, params,null);
    }
    public <T> List<T> queryListWithPreparedSql(Class<T> t,String sql,Object[] params,int expiredSecond,Connection defaultConn) {
        List<T> v=null;
        if(expiredSecond>0){
            v=SpyMemcachedUtil.getInstance().get("queryListWithPreparedSql_"+t.getName().hashCode()+sql.hashCode()+params.hashCode());
        }
        if(v==null) {
            String db = getRandomDb();
            v= queryListWithPreparedSql( t, sql, params, defaultConn);
            if(v!=null&&v.size()>0&&expiredSecond>0){
                SpyMemcachedUtil.getInstance().put("queryListWithPreparedSql_"+t.getName().hashCode()+sql.hashCode()+params.hashCode(),v,expiredSecond);
            }
        }
        return  v;
    }
    public <T> List<T> queryListWithPreparedSql(Class<T> t,String sql,Object[] params,int expiredSecond) {
        return queryListWithPreparedSql(t,sql,params,expiredSecond,null);
    }
    public <T> T queryWithPreparedSql(Class<T> t,String sql,Object[] params,Connection defaultConn) {
        T v=null;
        try {
            String db = getRandomDb();
            ConnectionFactory cf = super.getConnectionFactory();
            v = executePrepareStatement(null,t, cf, db, defaultConn, sql, params);
            try {
                if(v==null&&(t.newInstance() instanceof Record||t.newInstance() instanceof RecordSet)) {
                    v = t.newInstance();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            L.info(e.toString());
        }finally {
            return v;
        }
    }
    public <T> T queryWithPreparedSql(Class<T> t,String sql,Object[] params) {
        return queryWithPreparedSql( t, sql, params, null);
    }
    public <T> T queryWithPreparedSql(Class<T> t,String sql,Object[] params,int expiredSecond,Connection defaultConn) {
        T v=null;
        if(expiredSecond>0){
            v=SpyMemcachedUtil.getInstance().get("queryWithPreparedSql_"+t.getName().hashCode()+sql.hashCode()+params.hashCode());
        }
        if(v==null) {
            String db = getRandomDb();
            v= queryWithPreparedSql( t, sql, params, defaultConn);
            if(v!=null&&expiredSecond>0){
                if((v instanceof Record)){
                    if(((Record)v).size()>0){
                        SpyMemcachedUtil.getInstance().put("queryWithPreparedSql_"+t.getName().hashCode()+sql.hashCode()+params.hashCode(),v,expiredSecond);
                    }
                }else
                if((v instanceof RecordSet)){
                    if(((RecordSet)v).size()>0){
                        SpyMemcachedUtil.getInstance().put("queryWithPreparedSql_"+t.getName().hashCode()+sql.hashCode()+params.hashCode(),v,expiredSecond);
                    }
                }else {
                    SpyMemcachedUtil.getInstance().put("queryWithPreparedSql_" + t.getName().hashCode() + sql.hashCode() + params.hashCode(), v, expiredSecond);
                }
            }
        }
        return  v;
    }
    public <T> T queryWithPreparedSql(Class<T> t,String sql,Object[] params,int expiredSecond) {
        return queryWithPreparedSql( t, sql, params,expiredSecond, null);
    }

    public Record executeRecord(String sql,Record rec,Connection defaultConn) {
        String db = getRandomDb();
        ConnectionFactory cf=super.getConnectionFactory();
        final String sql0 = sql;
        final Record rec0 =rec!=null?rec:new Record();
        executeStatement(cf, db,defaultConn, new SQLStatementHandler() {
            @Override
            public void handle(Statement stmt) throws SQLException {
                executeRecord(stmt, sql0, rec0);
            }
        });
        return rec0;
    }
    public Record executeRecord(String sql, int expiredSecond,Connection defaultConn) {
        Record v=SpyMemcachedUtil.getInstance().get("executeRecord_"+sql.hashCode());
        if(v==null||v.size()==0) {
            String db = getRandomDb();
            v= executeRecord(sql,null, defaultConn);
            if(v.size()>0){
                SpyMemcachedUtil.getInstance().put("executeRecord_"+sql.hashCode(),v,expiredSecond);
            }
        }
        return  v;
    }


    public RecordSet executeRecordSet( String sql,RecordSet res, Connection defaultConn) {
        String db = getRandomDb();
        ConnectionFactory cf=super.getConnectionFactory();
        final String sql0 = sql;
        final RecordSet recs0 =res!=null?res: new RecordSet();
        executeStatement(cf, db,defaultConn, new SQLStatementHandler() {
            @Override
            public void handle(Statement stmt) throws SQLException {
                executeRecordSet(stmt, sql0, recs0);
            }
        });
        return recs0;
    }
    public RecordSet executeRecordSet(String sql, int expiredSecond, Connection defaultConn) {
        RecordSet v=SpyMemcachedUtil.getInstance().get("executeRecordSet_"+sql.hashCode());
        if(v==null||v.size()==0) {
            String db = getRandomDb();
            v= executeRecordSet( sql,null, defaultConn);
            if(v.size()>0){
                SpyMemcachedUtil.getInstance().put("executeRecordSet_"+sql.hashCode(),v,expiredSecond);
            }
        }
        return v;
    }

    public Object executeScalar( String sql,Connection defaultConn) {
        String db = getRandomDb();
        ConnectionFactory cf=super.getConnectionFactory();
        final String sql0 = sql;
        final ObjectHolder<Object> r = new ObjectHolder<Object>(null);
        executeStatement(cf, db,defaultConn, new SQLStatementHandler() {
            @Override
            public void handle(Statement stmt) throws SQLException {
                r.value = executeScalar(stmt, sql0);
            }
        });
        return r.value;
    }
    public Object executeScalar(String sql,int expiredSecond,Connection defaultConn) {
        Object o=SpyMemcachedUtil.getInstance().get("executeScalar_"+sql.hashCode());
        if(o==null) {
            String db = getRandomDb();
            o= executeScalar(sql,defaultConn);
            if(o!=null){
                SpyMemcachedUtil.getInstance().put("executeScalar_"+sql.hashCode(),o,expiredSecond);
            }
        }
        return o;
    }

    public long executeIntScalar(String sql, long def,Connection defaultConn) {
        Object v = executeScalar(sql,defaultConn);
        return v != null ? Values.toInt(v) : def;
    }
    public long executeIntScalar(String sql, long def,int expiredSecond,Connection defalutConn) {
        Object v=SpyMemcachedUtil.getInstance().get("executeIntScalar_"+sql.hashCode());
        if(v==null) {
            v = executeScalar(sql,defalutConn);
            if(v!=null){
                SpyMemcachedUtil.getInstance().put("executeIntScalar_"+sql.hashCode(),v,expiredSecond);
            }
        }
        return v != null ? Values.toInt(v) : def;
    }

    public long executeUpdate(List<String> sqls,Connection defaultConn) {
        Validate.notNull(sqls);
        ConnectionFactory cf=super.getConnectionFactory();
        long effected1 = 0;
        ArrayList<ServerException> errors = new ArrayList<ServerException>();
        for (String db : dbs) {
            try {
                if (sqls.isEmpty())
                    return 0L;
                final List<String> sqls0 = sqls;
                final ObjectHolder<Long> effected = new ObjectHolder<Long>(0L);
                executeStatement(cf, db,defaultConn, new SQLStatementHandler() {
                    @Override
                    public void handle(Statement stmt) throws SQLException {
                        for (String sql : sqls0) {
                            //if lock db ,on this
                            int n = executeUpdate0(stmt, sql, null);
                            effected.value += n;
                        }
                    }
                });
                effected1= effected.value;


                errors.add(null);
            } catch (ServerException e) {
                errors.add(e);
            }
        }

        if (!errors.isEmpty() && !errors.contains(null))
            throw errors.get(0);

        return effected1;
    }

    public long executeUpdate(String sql,Connection defaultConn) {
        return executeUpdate(Arrays.asList(sql),defaultConn);
    }

}
