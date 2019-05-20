package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.data.RecordSet;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by acer01 on 2016/5/6/006.
 */
public class RecordCount<V> extends Record implements Comparator<String>{

    private Count count = new Count();
    private LinkedList<RecordCount> groups = new LinkedList<RecordCount>();


    public static <K, V extends Comparable<? super V>> Map<K, V>sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort(list, new Comparator<Map.Entry<K, V>>(){
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ){
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    //0是没有执行计算,1是加,2是减,3是乘,4是除
    private int countType = 0;

    public static final String ARRAY = "array",
            MULTIPLY_NODE_RESULT = "multiplyNodeResult",
            SUM_NODE_RESULT = "sumNodeResult";



    /**
     * 所有计算后的节点相加
     * @return
     */
    public BigDecimal sumResultAll(){
        BigDecimal result = BigDecimal.ZERO;
        for(BigDecimal decimal : count.nodeResultCache){
            result = result.add(decimal);
        }
        return result;
    }



    /**
     * 单个计算后的节点相加
     * @param key
     * @return
     */
    public BigDecimal sumResultNode(String key){
        String methodStr = getCountMethodStr(countType);
        BigDecimal result;
        Object val = get(key);
        if(val instanceof Map) {
            Map valMap = (Map) val;
            result = sumNode(methodStr, valMap);
        }else {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    /**
     * 计算所有节点相加
     * @param nodeKey
     * @return
     */
    public BigDecimal sumNodeAll(String nodeKey){
        BigDecimal result = BigDecimal.ZERO;
        for(Map.Entry entry :entrySet()){
            Object val = get(entry.getKey());
            if(val instanceof Map)
                result = result.add(sumNode(nodeKey, (Map) val));
        }
        return result;
    }

    private BigDecimal sumNode(String nodeKey,Map val){
        BigDecimal result = BigDecimal.ZERO;

        for(Map node :(List<Map>)val.get(ARRAY)){
            result = result.add(new BigDecimal(node.get(nodeKey).toString()));
        }
        val.put(nodeKey,result);
        return result;
    }


    public BigDecimal putGroupMapSumDecimal(String key1, String key2, BigDecimal value){
        Object old = get(key1);

        if(old instanceof RecordCount)
            return ((RecordCount) old).putGroupAutoSumDecimal(key2, value);

        RecordCount recordCount = RecordCount.of(key2, value);
        put(key1,recordCount);
        groups.addFirst(recordCount);
        return value;
    }

    public Long putGroupMapSumLong(String key1, String key2, Long value){
        Object old = get(key1);

        if(old instanceof RecordCount)
            return ((RecordCount) old).putGroupAutoSumLong(key2, value);

        RecordCount recordCount = RecordCount.of(key2, value);
        put(key1,recordCount);
        groups.addFirst(recordCount);
        return value;
    }

    public Count putGroupList(String key, Map value){
        Object old = get(key);
        if(null != old && old instanceof List){
            ((List)old).add(value);
        }else {
           List array = new ArrayList();
           array.add(value);
           put(key,array);
        }
       return count;
    }
    public Count putGroupList(String key, Record value){
        Object old = get(key);
        if(null != old && old instanceof RecordSet){
            ((RecordSet)old).add(value);
        }else {
            RecordSet array = new RecordSet();
            array.add(value);
            put(key,array);
        }
        return count;
    }

    public Count putGroupMap(String key, Map value) {
        Object old = get(key);
        List array;
        Map map;
        if (null != old && old instanceof Map) {
            map = (Map)old;
            array = (List) map.get(ARRAY);
        } else {
            map = new HashMap();
            array = new ArrayList();
        }

        array.add(value);
        map.put(ARRAY, array);
        put(key, map);


        count.map = map;
        count.value = value;
        return count;
    }

    public Long putGroupAutoSumLong(String key, Long value) {
        Object old = get(key);
        Long newVal ;

        if (null != old && old instanceof Long)
            newVal = value + (Long)old;
        else
            newVal = value;
        put(key,newVal);
        return newVal;
    }

    public BigDecimal putGroupAutoSumDecimal(String key, BigDecimal value) {
        Object old = get(key);
        BigDecimal newVal ;

        if (null != old && old instanceof BigDecimal)
            newVal = value.add((BigDecimal) old);
        else
            newVal = value;
        put(key,newVal);
        return newVal;
    }

    public LinkedList<RecordCount> getGroups(){
        return groups;
    }




   //------------------------Utils------------------------


    private String getCountMethodStr(int countType){
        switch (countType){
            case 1:
                return SUM_NODE_RESULT;
            case 3 :
                return MULTIPLY_NODE_RESULT;
        }
        return null;
    }

    public V get(String k){
        return (V) super.get(k);
    }


    public Integer getInteger(String col) {
        Integer v = Long.valueOf(get(col).toString()).intValue();
        return  v==null? 0 : v;
    }

    public Long getLong(String col) {
        return getInt(col, 0L);
    }

    public RecordCount set(String col, Object v) {
        put(col, v);
        return this;
    }
    public static RecordCount of(String c1, Object v1) {
        RecordCount rec = new RecordCount();
        rec.put(c1, v1);
        return rec;
    }

    public static RecordCount of(String c1, Object v1, String c2, Object v2) {
        RecordCount rec = new RecordCount();
        rec.put(c1, v1);
        rec.put(c2, v2);
        return rec;
    }

    public RecordCount createMonthVal(int month,Number count){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,month-1);
        int day = calendar.getActualMaximum(Calendar.DATE);

        if(count instanceof BigDecimal) {
            for (Integer i = 1; i <= day; i++) {
                putGroupAutoSumDecimal(i.toString(), (BigDecimal) count);
            }
        }else if(count instanceof Long){
            for(Integer i=1; i<=day; i++){
                putGroupAutoSumLong(i.toString(), (Long) count);
            }
        }
        return this;
    }


    @Override
    public int compare(String o1, String o2) {
        if(getInteger(o1) >= getInteger(o2))
            return -1;
        return 1;
    }

    public class Count{
        private Count(){}
        private Map value,
                map;
        private List<BigDecimal> nodeResultCache = new ArrayList<BigDecimal>();
        /**
         * 所有count相加
         * @param count
         */
        public void count(String... count){
            for(int i=0; i<count.length ;i++){

                Object oldVal = map.get(count[i]);
                BigDecimal newValDecimal;
                if(null == oldVal){
                    newValDecimal = BigDecimal.ZERO;
                }else {
                    newValDecimal = (BigDecimal)oldVal;
                    Object countNode = value.get(count[i]);
                    countNode = null == countNode? 0:countNode;
                    String countVal = countNode.toString();
                    newValDecimal = new BigDecimal(countVal).add(newValDecimal);
                }
                map.put(count[i],newValDecimal);
            }
        }

        /**
         * 所有count相乘
         * @param count
         * @return
         */
        public Count multiply(String... count){
            BigDecimal multiplyResult = BigDecimal.ONE;
            for(int i=0; i<count.length ;i++){
                Object countNode = value.get(count[i]);
                countNode = null == countNode? 0:countNode;
                multiplyResult = multiplyResult.multiply(new BigDecimal(countNode.toString()));
            }

            value.put(MULTIPLY_NODE_RESULT, multiplyResult);
            nodeResultCache.add(multiplyResult);

            countType = 3;
            return this;
        }




    }
}
