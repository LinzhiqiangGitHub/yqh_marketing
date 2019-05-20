package com.yqh.marketing.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenguo on 2016/1/4.
 */
public class FieldMap {
    public static Map<String,String> getMap(String fieldName,String fieldValue)
    {
        Map<String,String> map=new HashMap<String,String>();
        map.put(fieldName,fieldValue);
        return map;
    }
    /**
     * 获取map中第一个数据key
     *
     * @param <K> Key的类型
     * @param <V> Value的类型
     * @param map 数据源
     * @return 返回的值
     */
    public static <K, V> K getFirstKey(Map<K, V> map) {
        K obj = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }
    /**
     * 获取map中最后一个数据key
     *
     * @param <K> Key的类型
     * @param <V> Value的类型
     * @param map 数据源
     * @return 返回的值
     */
    public static <K, V> K getTailKey(Map<K, V> map) {
        K obj = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            obj = entry.getKey();
        }
        return obj;
    }
}
