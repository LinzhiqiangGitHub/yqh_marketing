package com.yqh.marketing.common;
import com.yqh.marketing.basedevss.ServiceResult;
import com.yqh.marketing.basedevss.base.conf.Configuration;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.sql.ConnectionFactory;
import com.yqh.marketing.basedevss.base.sql.SQLExecutor;
import com.yqh.marketing.basedevss.base.sql.SQLExecutorExtend;
import com.yqh.marketing.basedevss.base.util.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Map;

/**
 * Created by chenguo on 2015/12/6.
 */
public class SQLExecutorBase  implements Initializable {
    private ConnectionFactory connectionFactory;
    private String db;
    private String readDb;
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private static int count=0;
    @Override
    public void init() {
        Configuration conf = GlobalConfig.get();
        this.connectionFactory = ConnectionFactory.getConnectionFactory("dbcp");
        this.db = conf.getString("write.service.db", null);
        int dbCount=2;
        String[] read =new String[dbCount];
        for(int i=0;i<dbCount;i++){
            if(i==0) {
                read[i] = conf.getString("read.service.db",this.db);
            }else{
                read[i] = conf.getString("read"+i+".service.db", read[i-1]);
            }
        }
        if(read.length==0){
            this.readDb=this.db;
        }
        //int i=(int)(Math.random()*100)%dbCount;//随机分配
        if(count>=dbCount){
            count=0;
        }
        this.readDb=read[count];
        count++;
    }
    public <V,T> Boolean exists(Class<V> entityClass,T pid){
        return exists(entityClass,pid,null);
    }
    public <V,T> Boolean exists(Class<V> entityClass,T pid,Connection defaultConn) {
        try {
            Method method=entityClass.getMethod("existsByIdentity", pid.getClass());
            String sql = (String) method.invoke(null, pid);
            long rec = getSqlExecutorExtend().executeIntScalar(sql, 0,defaultConn);
            return rec>0;
        }
        catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Boolean exists(String sql){
        return exists(sql,null);
    }
    public Boolean exists(String sql,Connection defaultConn) {
        long rec = getSqlExecutorExtend_Read().executeIntScalar(sql, 0,defaultConn);
        return rec>0;
    }
    public Boolean exists(String sql,int expiredSecond) {
        long rec = getSqlExecutorExtend_Read().executeIntScalar(sql,0,expiredSecond);
        return rec>0;
    }
    /**
     * 查找是否存在
     * @param entityClass
     * @param map
     * @param <V>
     * @return
     */
    public <V> Boolean exists(Class<V> entityClass,Map<String,String> map) {

        return existsRows(entityClass,map)>0;
    }
    public <V> Boolean exists(Class<V> entityClass,Map<String,String> map,Connection defaultConn){
        return existsRows(entityClass,map,defaultConn)>0;
    }
    public <V> long existsRows(Class<V> entityClass,Map<String,String> map){
        return existsRows(entityClass,map,null);
    }
    public <V> long existsRows(Class<V> entityClass,Map<String,String> map,Connection defaultConn) {
        try {
            Field field=entityClass.getDeclaredField("tableName");
            String tableName=(String)field.get(entityClass);
            String sql = "SELECT count(1) FROM "+tableName+" ";
            if(map.size()>0)
            {
                String where="";
                for(String k:map.keySet())
                {
                    if(where.isEmpty())
                        where=k+"="+getSqlValue(entityClass,map,k);
                    else
                        where+= " AND "+k+"="+getSqlValue(entityClass,map,k);
                }
                if(!where.isEmpty())
                    sql+=" WHERE "+where;
            }
            long rec = getSqlExecutorExtend_Read().executeIntScalar(sql, 0,defaultConn);
            return rec;
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private <T> String getSqlValue(Class<T> entityClass,Map<String,String> map,String key)
    {
        String ret="";
        try {
            String key1=formatField(key);
            Field f=entityClass.getDeclaredField(key1);
            if(f.getType()==Long.class || f.getType()==Boolean.class ||f.getType()==Float.class ||
                        f.getType()==Double.class||f.getType()==Integer.class||f.getType()== BigDecimal.class||f.getType()==Short.class)
            {
                ret=String.valueOf(map.get(key));
            }else
            if(f.getType()==String.class)
            {
                ret="'"+map.get(key).replace("'","''")+"'";
            }else
            if(f.getType()==java.sql.Timestamp.class){
                String v=String.valueOf(map.get(key));
                if(v==null||v.isEmpty()||v.equals("null")){
                    ret="null";
                }else {
                    ret = "'" + String.valueOf(map.get(key)) + "'";
                }
            }
        } catch (NoSuchFieldException e) {
            ret="'"+map.get(key)+"'";
            e.printStackTrace();
        }

        return ret;
    }
    private <T> String getSqlValue(T updateEntity,Field key,String value)
    {
        String ret="";
        try {



            if(key.getType()==Long.class || key.getType()==Boolean.class ||key.getType()==Float.class ||
                    key.getType()==Double.class||key.getType()==Integer.class||key.getType()== BigDecimal.class||key.getType()==Short.class)
            {
                ret=value.toString().replace("'","''");
            }
            else
            {
                ret="'"+value.toString().replace("'","''")+"'";
            }
        } catch (Exception e) {
            ret="''";
            e.printStackTrace();
        }

        return ret;
    }

    private String formatField(String key)
    {
        int i=0;
        boolean isUpper=false;
        StringBuilder s = new StringBuilder();
        key=key.toLowerCase();
        while(i<key.length())
        {
            if (isUpper)
            {
                s.append(key.substring(i, i+1).toUpperCase());
                isUpper = false;

            }
            else
                s.append(key.substring(i, i+1));
            if (key.substring(i, i+1).equals( "_"))
            {
                isUpper = true;
            }
            i++;
        }
        return s.toString().replace("_","");
    }
    public <V,T> Record queryIdentity(Class<V> entityClass,Map<String,String> map){
        return queryIdentity(entityClass,map,null);
    }
    public <V,T> Record queryIdentity(Class<V> entityClass,Map<String,String> map,Connection defaultConn) {
        try {
            Field field=entityClass.getDeclaredField("tableName");
            String tableName=(String)field.get(entityClass);
            String sql = "SELECT * FROM "+tableName+" ";
            if(map.size()>0)
            {
                String where="";
                for(String k:map.keySet())
                {

                    if(where.isEmpty())
                        where=k+"="+getSqlValue(entityClass,map,k);
                    else
                        where+= " AND "+k+"="+getSqlValue(entityClass,map,k);
                }
                if(!where.isEmpty())
                    sql+=" WHERE "+where;
            }
            Record rec = getSqlExecutorExtend().executeRecord(sql,null,defaultConn);
            return rec;
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return new Record();
    }
    public <V,T> Record queryIdentity(Class<V> entityClass,T pid){
        return queryIdentity(entityClass,pid,null);
    }
    public <V,T> Record queryIdentity(Class<V> entityClass,T pid,int expiredSecond) {
        try {
            Method method=entityClass.getMethod("queryByIdentity", pid.getClass());
            //Class<?> threadClazz = Class.forName(EntityName);
            //Method method = threadClazz.getMethod("queryByIdentity", pid.getClass());
            String sql = (String) method.invoke(null, pid);
            Record rec = getSqlExecutorExtend().executeRecord(sql,expiredSecond);
            return rec;
        }
        catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public <V,T> Record queryIdentity(Class<V> entityClass,T pid,Connection defaultConn) {
        try {
            //log.debug("查询"+pid);
            Method method=entityClass.getMethod("queryByIdentity", pid.getClass());
            //Class<?> threadClazz = Class.forName(EntityName);
            //Method method = threadClazz.getMethod("queryByIdentity", pid.getClass());
            String sql = (String) method.invoke(null, pid);
            //log.debug(sql);
            Record rec =new Record();
            if(defaultConn==null){
                rec = getSqlExecutorExtend_Read().executeRecord(sql);
            }else {
                rec = getSqlExecutorExtend().executeRecord(sql, null, defaultConn);
            }
            return rec;
        }
        catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <V,T> V queryEntity(Class<V> entityClass,T pid){
        return queryEntity(entityClass,pid,null);
    }
    public <V,T> V queryEntity(Class<V> entityClass,T pid,Connection defaultConn) {
        try {
            Method method=entityClass.getMethod("queryByIdentity", pid.getClass());
            //Class<?> threadClazz = Class.forName(EntityName);
            //Method method = threadClazz.getMethod("queryByIdentity", pid.getClass());
            String sql = (String) method.invoke(null, pid);
            return  getSqlExecutorExtend().query(entityClass,sql,defaultConn);

        }
        catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <V,T> V queryEntity(Class<V> entityClass,T pid,boolean readOnly,int expiredSecond){
        return queryEntity(entityClass,pid,readOnly,expiredSecond,null);
    }
    public <V,T> V queryEntity(Class<V> entityClass,T pid,boolean readOnly,int expiredSecond,Connection defaultConn) {
        try {
            Method method=entityClass.getMethod("queryByIdentity", pid.getClass());
            //Class<?> threadClazz = Class.forName(EntityName);
            //Method method = threadClazz.getMethod("queryByIdentity", pid.getClass());
            String sql = (String) method.invoke(null, pid);
            if(readOnly&&defaultConn==null){
                return getSqlExecutorExtend_Read().query(entityClass, sql, expiredSecond);
            }else {
                return getSqlExecutorExtend().query(entityClass, sql, expiredSecond,defaultConn);
            }

        }
        catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public <V,T> V queryEntity(Class<V> entityClass,T pid,boolean readOnly){
        return queryEntity(entityClass,pid,readOnly,null);
    }
    public <V,T> V queryEntity(Class<V> entityClass,T pid,boolean readOnly,Connection defaultConn) {
        try {
            Method method=entityClass.getMethod("queryByIdentity", pid.getClass());
            //Class<?> threadClazz = Class.forName(EntityName);
            //Method method = threadClazz.getMethod("queryByIdentity", pid.getClass());
            String sql = (String) method.invoke(null, pid);
            if(readOnly&&defaultConn==null){
                return getSqlExecutorExtend_Read().query(entityClass, sql);
            }else {
                return getSqlExecutorExtend().query(entityClass, sql,defaultConn);
            }

        }
        catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public <V,T> ServiceResult updateIdentity(Class<V> entityClass,T pid,Map<String,String> updateMap){
        return updateIdentity(entityClass,pid,updateMap,null);
    }
    public <V,T> ServiceResult updateIdentity(Class<V> entityClass,T pid,Map<String,String> updateMap,Connection defaultConn) {
        ServiceResult result=new ServiceResult();
        try {
            if(updateMap.isEmpty())
            {
                result.addErrorMessage(Long.toString("没有更新语句".hashCode()),"没有更新语句");
                return result;
            }
            String updateParam="";
            for(String k:updateMap.keySet())
            {
                //updateParam+= k+"='"+updateMap.get(k)+"',";
                updateParam+=k+"="+getSqlValue(entityClass,updateMap,k)+",";
            }
            if(!updateParam.isEmpty())
            {
                updateParam=updateParam.substring(0,updateParam.length()-1);
            }
            Method method=entityClass.getMethod("updateByIdentity", pid.getClass(),String.class);
            String sql = (String) method.invoke(null, pid,updateParam);
            result=getSqlExecutorExtend().updateWithTrans(sql,defaultConn);

        }
        catch(NoSuchMethodException e)
        {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    public <V,T,P> ServiceResult updateIdentity(Class<V> entityClass,P updateEntity){
        return updateIdentity0(entityClass,updateEntity,null);
    }
    public <V,T,P> ServiceResult updateIdentity0(Class<V> entityClass,P updateEntity,Connection defaultConn) {
        ServiceResult result=new ServiceResult();
        try {
            if(updateEntity==null)
            {
                result.addErrorMessage(Long.toString("没有更新语句".hashCode()),"没有更新语句");
                return result;
            }

            String updateParam="";
            String primaryKey="";
            Class<?> primaryKeyType=null;
            Object primaryKeyValue="";
            try {
                Field pk=entityClass.getDeclaredField("primaryKey");
                pk.setAccessible(true);
                primaryKey=pk.get(entityClass).toString();
                //primaryKeyType=pk.getType();

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            Class<?> fields=entityClass.getClasses()[0];

            for(Field k:updateEntity.getClass().getDeclaredFields())
            {
                if (Modifier.isStatic(k.getModifiers()) || Modifier.isFinal(k.getModifiers())) {
                    continue;
                }
                try {
                    Field f=fields.getDeclaredField(k.getName());
                    k.setAccessible(true);
                    Object value = k.get(updateEntity);
                    if(value==null)
                        continue;
                    if(f.get(fields).toString().equals(primaryKey))
                    {
                        primaryKeyValue=value;
                        primaryKeyType=k.getType();
                    }
                    else {
                        updateParam += f.get(fields).toString() + "=" + getSqlValue(updateEntity, k, value.toString()) + ",";
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                //updateParam+= k+"='"+updateMap.get(k)+"',";

            }
            if(!updateParam.isEmpty())
            {
                updateParam=updateParam.substring(0,updateParam.length()-1);
                Method method=entityClass.getMethod("updateByIdentity", primaryKeyType,String.class);
                String sql = (String) method.invoke(null, primaryKeyValue,updateParam);
                result = getSqlExecutorExtend().updateWithTrans(sql,defaultConn);

            }
            else
            {
                result.addErrorMessage("构建sql异常");
            }

        }
        catch(NoSuchMethodException e)
        {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    public <V,T> ServiceResult insertIdentity(Class<V> entityClass,Map<String,String> insertMap){
        return insertIdentity(entityClass,insertMap,null);
    }
    public <V,T> ServiceResult insertIdentity(Class<V> entityClass,Map<String,String> insertMap,Connection defaultConn) {
        ServiceResult result=new ServiceResult();
        try {
            if(insertMap.isEmpty())
            {
                result.addErrorMessage(Long.toString("没有插入语句".hashCode()),"没有插入语句");
                return result;
            }
            String values="";
            String fields="";
            for(String k:insertMap.keySet())
            {
                //values+= "'"+insertMap.get(k)+"',";
                values+=getSqlValue(entityClass,insertMap,k)+",";
                fields+=k+",";
            }
            if(!values.isEmpty())
            {
                values=values.substring(0,values.length()-1);
                fields=fields.substring(0,fields.length()-1);
            }
            Method method=entityClass.getMethod("insertByIdentity", String.class,String.class);
            String sql = (String) method.invoke(null, fields,values);
            result = getSqlExecutorExtend().updateWithTrans(sql,defaultConn);
        }
        catch(NoSuchMethodException e)
        {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    public <V,T> ServiceResult insertIdentity(Class<V> entityClass,T insertEnytity){
        return insertIdentity(entityClass,insertEnytity,null);
    }
    public <V,T> ServiceResult insertIdentity(Class<V> entityClass,T insertEnytity,Connection defaultConn) {
        ServiceResult result=new ServiceResult();
        try {
            if(insertEnytity==null)
            {
                result.addErrorMessage(Long.toString("没有插入语句".hashCode()),"没有插入语句");
                return result;
            }
            String values="";
            String fields="";
            Class<?> field=entityClass.getClasses()[0];
            for(Field k:insertEnytity.getClass().getDeclaredFields())
            {
                if (Modifier.isStatic(k.getModifiers()) || Modifier.isFinal(k.getModifiers())) {
                    continue;
                }
                try {
                    Field f=field.getDeclaredField(k.getName());
                    k.setAccessible(true);
                    Object value = k.get(insertEnytity);
                    if(value==null)
                        continue;

                    values+=getSqlValue(entityClass,k,value.toString())+",";
                    fields+=f.get(fields).toString()+",";

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }

            if(!values.isEmpty())
            {
                values=values.substring(0,values.length()-1);
                fields=fields.substring(0,fields.length()-1);
            }
            Method method=entityClass.getMethod("insertByIdentity", String.class,String.class);
            String sql = (String) method.invoke(null, fields,values);
            result = getSqlExecutorExtend().updateWithTrans(sql,defaultConn);
        }
        catch(NoSuchMethodException e)
        {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (Exception e)
        {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    public <V,T> ServiceResult deleteIdentity(Class<V> entityClass,T pid){
        return deleteIdentity(entityClass,pid,null);
    }
    public <V,T> ServiceResult deleteIdentity(Class<V> entityClass,T pid,Connection defaultConn) {
        ServiceResult result=new ServiceResult();
        try {
            Method method=entityClass.getMethod("deleteByIdentity", pid.getClass());
            //Class<?> threadClazz = Class.forName(EntityName);
            //Method method = threadClazz.getMethod("queryByIdentity", pid.getClass());
            String sql = (String) method.invoke(null, pid);
            result = getSqlExecutorExtend().updateWithTrans(sql,defaultConn);
        }
        catch(NoSuchMethodException e)
        {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            result.addErrorMessage(Integer.toString(e.getMessage().hashCode()),e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void destroy() {
        this.connectionFactory = ConnectionFactory.close(connectionFactory);
        this.db = null;
        this.readDb=null;

    }

    public SQLExecutor getSqlExecutor() {
        if(this.db==null ||this.db.isEmpty()||connectionFactory==null)
        {
            init();
        }
        return new SQLExecutorExtend(connectionFactory, db);
    }
    public SQLExecutorExtend getSqlExecutorExtend() {
        if(this.db==null ||this.db.isEmpty()||connectionFactory==null)
        {
            init();
        }
        return new SQLExecutorExtend(connectionFactory, db);
    }
    public SQLExecutor getSqlExecutor_Read() {
        if(this.readDb==null ||this.readDb.isEmpty()||connectionFactory==null)
        {
            init();
        }
        return new SQLExecutorExtend(connectionFactory, readDb);
    }
    public SQLExecutorExtend getSqlExecutorExtend_Read() {
        if(this.readDb==null ||this.readDb.isEmpty()||connectionFactory==null)
        {
            init();
        }
        return new SQLExecutorExtend(connectionFactory, readDb);
    }
}
