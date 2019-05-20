package com.yqh.marketing.common.cache;
import com.yqh.marketing.basedevss.base.conf.GlobalConfig;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.DisposableBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenguo on 2016/4/28.
 */
public class SpyMemcachedUtil implements DisposableBean {
    private static MemcachedClient mc;
    private static SpyMemcachedUtil memcachedUtil = null;
    private static String keyPre=GlobalConfig.get().getString("memcached.keyprev","");
    public  static SpyMemcachedUtil getInstance() {
        if (memcachedUtil == null) {
            // 同步块，线程安全的创建实例
            synchronized (SpyMemcachedUtil.class) {
                // 再次检查实例是否存在，如果不存在才真正的创建实例
                if (memcachedUtil == null) {
                    memcachedUtil = new SpyMemcachedUtil();
                }
            }
        }
        return memcachedUtil;
    }
    private SpyMemcachedUtil() {
        try {
            String serverStr= GlobalConfig.get().getString("memcached.servers","");
            if(serverStr.isEmpty()){
                mc=null;
                return;
            }
//            AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"}, new PlainCallbackHandler("cfe26fc9725744f6", "qazxswLLK123"));
//            mc = new MemcachedClient(new ConnectionFactoryBuilder().setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
//                    .setAuthDescriptor(ad).build(),
//                    AddrUtil.getAddresses(serverStr));
            mc = new MemcachedClient(
                    AddrUtil.getAddresses(serverStr));
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    public <T> void put(String key, T value, int expire){
        if(expire>2592000){
            expire=2592000;
        }
        if(mc==null)
            return ;
        try {
            mc.set(keyPre+key, expire, value);
        }catch (Exception e){

        }

    }
    public <T> void put(String key, T value){
         put(key,value,0);
    }
    public <T> void push(String key, T value){
        push(key,value,0);
    }
    public <T> void push(String key, T value,int expire){
        Object o=get(key);
        List<T> e=new ArrayList<T>();
        if(o==null){
            o=new ArrayList<T>();
        }
        if(o instanceof List){
            e=(List<T>)o;
        }else{
            e.add((T)o);
        }
        if(e.contains(value)){
            return;
        }
        e.add(value);
        put(key,e,expire);
    }
    public <T> List<T> pull(String key){
        Object o=get(key);
        List<T> e=new ArrayList<T>();
        if(o==null){
            o=new ArrayList<T>();
        }
        if(o instanceof List){
            e=(List<T>)o;
        }else{
            e.add((T)o);
        }
        return e;
    }

    public <T> T get(String key){
        if(mc==null)
            return null;
        try {
            return (T) mc.get(keyPre+key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 安全的Set方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.
     */
    public boolean safePut(String key, int expiration, Object value) {
        if(mc==null)
            return false;
        try {

            Future<Boolean> future = mc.set(keyPre+key, expiration,value);
            try {
                return future.get(300, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                future.cancel(false);
            }
        }catch (Exception e){

        }
        return false;
    }
    /**
     * GetBulk方法, 转换结果类型并屏蔽异常.
     */
    public <T> Map<String, T> getBulk(Collection<String> keys) {
        if(mc==null)
            return null;
        try {
            return (Map<String, T>) mc.getBulk(keyPre+keys);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 异步 Delete方法, 不考虑执行结果.
     */
    public void remove(String key) {
        if(mc==null)
            return ;
        try {
            mc.delete(keyPre+key);
        }catch (Exception e){

        }
    }

    /**
     * 安全的Delete方法, 保证在updateTimeout秒内返回执行结果, 否则返回false并取消操作.
     */
    public boolean safeRemove(String key) {
        if(mc==null)
            return false;
        try {
            Future<Boolean> future = mc.delete(keyPre+key);
            try {
                return future.get(500, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                future.cancel(false);
            }
        }catch (Exception e){

        }
        return false;
    }
    @Override
    public void destroy() throws Exception {
        if (mc != null) {
            mc.shutdown(2500, TimeUnit.MILLISECONDS);
        }
    }
    public long decr(String key,int count,long def,int exp){
        return mc.decr(keyPre+key,count,def,exp);
    }
    public long incr(String key,int count,long def,int exp){
        return mc.incr(keyPre+key,count,def,exp);
    }
    public boolean exists(String key){
        return mc.get(keyPre+key)!=null?true:false;
    }
}
