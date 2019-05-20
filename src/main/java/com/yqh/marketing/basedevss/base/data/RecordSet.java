package com.yqh.marketing.basedevss.base.data;


import com.yqh.marketing.basedevss.base.io.IOUtils2;
import com.yqh.marketing.basedevss.base.io.Serializable;
import com.yqh.marketing.basedevss.base.util.Copyable;
import com.yqh.marketing.basedevss.base.util.json.JsonGenerateHandler;
import com.yqh.marketing.basedevss.base.util.json.JsonUtils;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.*;

public class RecordSet extends ArrayList<Record> implements Copyable<RecordSet>, Serializable, JsonSerializableWithType {
    private Record empty;
    //序列化ID
    private static final long serialVersionUID = -5754585567833571944L;
    public RecordSet() {
    }

    public RecordSet(int initialCapacity) {
        super(initialCapacity);
    }

    public RecordSet(Collection<? extends Record> c) {
        addAll(c);
    }

    public RecordSet(Record... recs) {
        if (ArrayUtils.isNotEmpty(recs))
            Collections.addAll(this, recs);
    }

    @Override
    public boolean add(Record record) {
        Validate.notNull(record);
        return super.add(record);
    }

    public RecordSet leftJoin(String equalsCol1,String equalsCol2, RecordSet rs){
        String col1 = equalsCol1;
        String col2 = equalsCol2;

        for(Record e : this){
            String key1 = e.getString(col1);
            String key2 = e.getString(col2);
            Record e0 = rs.findEq(col1,key1,col2,key2);
            if(e0.isEmpty())
                continue;
            for(Map.Entry<String,Object> entry : e0.entrySet()){
                if (e.get(entry.getKey())==null){
                    e.put(entry.getKey(), entry.getValue());
                }else{
                    e.putMissing(entry.getKey(),entry.getValue());
                }
//                e.putMissing(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }

    public RecordSet leftJoin(Map.Entry<String,String> equalsCol, RecordSet rs){
        String col1 = equalsCol.getKey();
        String col2 = equalsCol.getValue();

        for(Record e : this){
            String key = e.getString(col1);
            Record e0 = rs.findEq(col2,key);
            if(e0.isEmpty())
                continue;
            for(Map.Entry<String,Object> entry : e0.entrySet()){
                e.putMissing(entry.getKey(),entry.getValue());
            }
        }
        return this;
    }

    public void remove(Collection<Record> list){
        for (Record e : list){
            remove(e);
        }
    }
    public void remove(String col,Object val){
        Iterator<Record> it = iterator();
        if (val==null) {
            while (it.hasNext()) {
                if (it.next().get(col)==null) {
                    it.remove();
                }
            }
        } else {
            while (it.hasNext()) {
                if (val.equals(it.next().get(col))) {
                    it.remove();
                }
            }
        }
    }
    @Override
    public void add(int index, Record element) {
        Validate.notNull(element);
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends Record> c) {
        Validate.notNull(c);
        for (Record rec : c)
            Validate.notNull(rec);

        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Record> c) {
        Validate.notNull(c);
        for (Record rec : c)
            Validate.notNull(rec);

        return super.addAll(index, c);
    }

    @Override
    public void write(Encoder out, boolean flush) throws IOException {
        out.writeArrayStart();
        out.setItemCount(size());
        for (Record rec : this) {
            out.startItem();
            rec.write(out, false);
        }
        out.writeArrayEnd();
        if (flush)
            out.flush();
    }

    @Override
    public void readIn(Decoder in) throws IOException {
        clear();

        long l = in.readArrayStart();
        if (l > 0) {
            do {
                for (long i = 0; i < l; i++) {
                    Record rec = Record.read(in);
                    add(rec);
                }
            } while ((l = in.arrayNext()) > 0);
        }
    }

    public static RecordSet read(Decoder in) throws IOException {
        RecordSet recs = new RecordSet();
        recs.readIn(in);
        return recs;
    }

    public List<Boolean> getBooleanColumnValues(String col) {
        ArrayList<Boolean> l = new ArrayList<Boolean>();
        for (Record rec : this)
            l.add(rec.getBoolean(col, false));
        return l;
    }

    public List<Long> getIntColumnValues(String col) {
        ArrayList<Long> l = new ArrayList<Long>();
        for (Record rec : this)
            l.add(rec.getInt(col));
        return l;
    }

    public List<Double> getFloatColumnValues(String col) {
        ArrayList<Double> l = new ArrayList<Double>();
        for (Record rec : this)
            l.add(rec.getFloat(col));
        return l;
    }

    public List<String> getStringColumnValues(String col) {
        ArrayList<String> l = new ArrayList<String>();
        for (Record rec : this)
            l.add(rec.getString(col));
        return l;
    }

    public RecordSet removeColumns(Collection<String> cols) {
        for (Record rec : this)
            rec.removeColumns(cols);
        return this;
    }

    public RecordSet removeColumns(String... cols) {
        return removeColumns(Arrays.asList(cols));
    }

    public RecordSet retainColumns(Collection<String> cols) {
        for (Record rec : this)
            rec.retainColumns(cols);
        return this;
    }

/*
    */
/**
     *  参数is是true,values是过滤列表,
     *  参数is是false,values返回列表,
     * 不在自己本身过滤,返回是新对象
     *//*
    public RecordSet filterValue(String column,boolean is,String... values){
        RecordSet rs = new RecordSet();
        for(String val: values){
            for(Record e : this){
                if(is){
                    if(!val.equals(e.get(column))){
                        rs.add(e);
                    }
                }else {
                    if(val.equals(e.get(column))){
                        rs.add(e);
                    }
                }
            }
        }
        return rs;
    }
*/

    public RecordSet retainColumns(String... cols) {
        return retainColumns(Arrays.asList(cols));
    }


    public RecordSet renameColumn(String oldCol, String newCol) {
        for (Record rec : this)
            rec.renameColumn(oldCol, newCol);
        return this;
    }

    public Record getFirstRecord() {
        return isEmpty() ? new Record() : get(0);
    }

    public String joinColumnValues(String col, String sep) {
        StringBuilder buff = new StringBuilder();
        int n = 0;
        for (Record rec : this) {
            if (rec.containsKey(col)) {
                if (n > 0)
                    buff.append(sep);
                buff.append(rec.getString(col, ""));
                n++;
            }
        }
        return buff.toString();
    }

    public String[] getStringColumnValue(String col){
        int size = super.size();
        String [] re = new String[size];
        for(int i=0; i<size; i++){
            re[i] = get(i).getString(col);
        }
        return re;
    }
    public Map<String, Record> toRecordMap(String col) {
        LinkedHashMap<String, Record> recm = new LinkedHashMap<String, Record>();
        for (Record rec : this) {
            String k = rec.getString(col, null);
            if (k != null)
                recm.put(k, rec);
        }
        return recm;
    }

    public Record  toRecord(String key,String val) {
        Record  r = new Record();

        for (Record rec : this) {
            r.put(rec.getString(key), rec.get(val));
        }
        return r;
    }
    public Map<String,  RecordSet> toRecordSetMap(String col) {
        LinkedHashMap<String, RecordSet> recm = new LinkedHashMap<String,  RecordSet>();
        int size = super.size();
        for (int i=0; i<size; i++) {
            Record rec = get(i);

            String k = rec.getString(col, null);
            if (k != null) {

                Object list = recm.get(k);
                if(list instanceof List){
                    ((List)list).add(rec);
                }else {
                    RecordSet l = new RecordSet();
                    l.add(rec);
                    recm.put(k,l);
                }
            }
        }
        return recm;
    }

    public Map<String,  Map<String,Record>> toRecordMapping(String col,String mappingCol) {
        Map<String,Map<String,Record>> rs = new LinkedHashMap<String, Map<String,Record>>();

        Map<String, RecordSet> recm = toRecordSetMap(col);

        for(Map.Entry<String,RecordSet> e : recm.entrySet()){
            rs.put(e.getKey(),e.getValue().toRecordMap(mappingCol));
        }
        return rs;
    }
    public Map<String,  Map<String,RecordSet>> toRecordSetMapping(String col,String mappingCol) {
        Map<String,Map<String,RecordSet>> rs = new LinkedHashMap<String, Map<String,RecordSet>>();

        Map<String, RecordSet> recm = toRecordSetMap(col);

        for(Map.Entry<String,RecordSet> e : recm.entrySet()){
            rs.put(e.getKey(),e.getValue().toRecordSetMap(mappingCol));
        }
        return rs;
    }
    public Map<Long, Record> toIntRecordMap(String col) {
        LinkedHashMap<Long, Record> recm = new LinkedHashMap<Long, Record>();
        for (Record rec : this) {
            if (rec.has(col)) {
                long k = rec.getInt(col);
                recm.put(k, rec);
            }
        }
        return recm;
    }

    public RecordSet limit(int limit){
        if(this.size() <= limit)
            return this;

        int index = 1;
        Iterator<Record> it = iterator();
        while (it.hasNext()) {
            it.next();
            if(index > limit)
                it.remove();
            index++;
        }
        return this;
    }

    @Override
    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        jsonWrite(jsonGenerator, false);
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonWrite(jsonGenerator, false);
    }

    public void jsonWrite(JsonGenerator jg, boolean ignoreNull) throws IOException {
        jg.writeStartArray();
        for (Record rec : this) {
            rec.jsonWrite(jg, ignoreNull);
        }
        jg.writeEndArray();
    }

    @Override
    public String toString() {
        return toString(false, true);
    }

    public String toString(final boolean ignoreNull, boolean human) {
        return JsonUtils.toJson(new JsonGenerateHandler() {
            @Override
            public void generate(JsonGenerator jg) throws IOException {
                jsonWrite(jg, ignoreNull);
            }
        }, human);
    }

    public JsonNode toJsonNode() {
        return JsonUtils.parse(toString(false, false));
    }

    public static RecordSet fromJson(String json) {
        return JsonUtils.fromJson(json, RecordSet.class);
    }

    @Override
    public RecordSet copy() {
        RecordSet recs = new RecordSet();
        recs.addAll(this);
        return recs;
    }

//    public RecordSet copy(String...col) {
//        RecordSet rs = new RecordSet();
//        for(Record e : this){
//            Record r = new Record();
//            for(String key : col){
//                r.put(key,e.get(key));
//            }
//            rs.add(r);
//        }
//        return rs;
//    }
    public RecordSet sortCopy(String col, boolean asc) {
        return copy().sort(col, asc);
    }

    public RecordSet sort(String col, boolean asc) {
        Validate.notNull(col);
        final String col0 = col;
        final boolean asc0 = asc;
        Collections.sort(this, new Comparator<Record>() {
            @Override
            public int compare(Record rec1, Record rec2) {
                Object o1 = rec1.get(col0);
                Object o2 = rec2.get(col0);

                Comparable c1 = o1 instanceof Comparable ? (Comparable)o1 : (o1 != null ? o1.toString() : null);
                Comparable c2 = o2 instanceof Comparable ? (Comparable)o2 : (o2 != null ? o2.toString() : null);
                return asc0 ? ObjectUtils.compare(c1, c2) : ObjectUtils.compare(c2, c1);
            }
        });
        return this;
    }

    public RecordSet sliceCopy(int cursor, int count) {
        Validate.isTrue(cursor >= 0);
        if (cursor >= size() || count == 0)
            return new RecordSet();

        RecordSet recs = new RecordSet();
        ListIterator<Record> iter = listIterator(cursor);
        if (count > 0) {
            int n = 0;
            while (iter.hasNext() && n < count) {
                recs.add(iter.next());
                n++;
            }
        } else {
            while (iter.hasNext()) {
                recs.add(iter.next());
            }
        }
        return recs;
    }

    public RecordSet slice(int cursor, int count) {
        RecordSet recs = sliceCopy(cursor, count);
        clear();
        addAll(recs);
        return this;
    }

    public RecordSet sliceByPageCopy(int page, int count) {
        return sliceCopy(page * count, count);
    }

    public RecordSet sliceByPage(int page, int count) {
        return slice(page * count, count);
    }

    public RecordSet shuffleCopy() {
        return copy().shuffle();
    }

    public RecordSet shuffle() {
        Collections.shuffle(this);
        return this;
    }

    public RecordSet uniqueCopy() {
        LinkedHashSet<Record> recs = new LinkedHashSet<Record>(this);
        return new RecordSet(recs);
    }

    public RecordSet unique() {
        LinkedHashSet<Record> recs = new LinkedHashSet<Record>(this);
        clear();
        addAll(recs);
        return this;
    }
    
    public RecordSet uniqueCopy(String col) {
    	LinkedHashMap<Object, Record> recm = new LinkedHashMap<Object, Record>();
    	for (Record rec : this) 
    		recm.put(rec.checkGet(col), rec);
    	return new RecordSet(recm.values());
    }
    
    public RecordSet unique(String col) {
    	LinkedHashMap<Object, Record> recm = new LinkedHashMap<Object, Record>();
    	for (Record rec : this) 
    		recm.put(rec.checkGet(col), rec);
    	
    	clear();
    	addAll(recm.values());
    	return this;
    }

    public void foreach(RecordHandler handler) {
        foreach(handler, false);
    }

    public void foreach(RecordHandler handler, boolean errorResume) {
        Validate.notNull(handler);
        if (errorResume) {
            for (Record rec : this) {
                try {
                    handler.handle(rec);
                } catch (Throwable ignored) {
                }
            }
        } else {
            for (Record rec : this) {
                handler.handle(rec);
            }
        }
    }

    public void foreachIf(RecordPredicate pred, RecordHandler action) {
        foreachIf(pred, action, false);
    }

    public void foreachIf(RecordPredicate pred, RecordHandler action, boolean errorResume) {
        Validate.notNull(pred);
        Validate.notNull(action);
        if (errorResume) {
            for (Record rec : this) {
                try {
                    if (pred.predicate(rec))
                        action.handle(rec);
                } catch (Throwable ignored) {
                }
            }
        } else {
            for (Record rec : this) {
                if (pred.predicate(rec))
                    action.handle(rec);
            }
        }
    }
    public BigDecimal sumDecimal(String col) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Record rec : this) {
            sum = sum.add(rec.getDecimal(col, BigDecimal.ZERO));
        }
        return sum;
    }

    public long sumInt(String col) {
        long sum = 0;
        for (Record rec : this) {
            sum += rec.getInt(col, 0);
        }
        return sum;
    }
    public long sumMoreInt(String col) {
        long sum = 0;
        for (Record rec : this) {
            long i = rec.getInt(col, 0);
            if(i >0)
                sum += i;
        }
        return sum;
    }
    //比对列值, 将没有的值返回
    public List contrastColumnVal(String col,List contrastList){
        List re = new ArrayList();
        ArrayList cols = new ArrayList();
        for (Record rec : this)
            cols.add(rec.get(col));

        for(Object e : contrastList){
            if(!cols.contains(e)){
                re.add(e);
            }
        }
        return re;
    }


    public Record findEq(String col,Object value) {
        for (Record rec : this) {
            Object e = rec.get(col);
            if(e == value)
                return rec;
            if(e == null)
                continue;
            if(e.equals(value) || String.valueOf(e).equals(String.valueOf(value))){
                return rec;
            }
        }
//        if(empty == null)
//            empty = new Record();
        return  new Record();
    }

    public RecordSet findsEq(String col,Object value) {
        RecordSet rs = new RecordSet();
        for (Record rec : this) {
            Object e = rec.get(col);
            if(e == null)
                continue;

            if (e.equals(value) || String.valueOf(e).equals(String.valueOf(value))) {
                rs.add(rec);
            }
        }
        return rs;
    }

    public RecordSet findsEqExt(String col,RecordSet rs1,String col1) {
        RecordSet rs = new RecordSet();
        for (Record rec : this) {
            Object e = rec.get(col);
            if(e == null)
                continue;

            Record r = rs1.findEq(col1,e);
            if (!r.isEmpty())
                rs.add(rec);
        }
        return rs;
    }

    public Record findEq(String col,Object value,String col1,Object value1) {
        for (Record rec : this) {
            Object o1;Object o2;
            if((o1 = rec.get(col)) != null && o1.toString().equals(value.toString())
                    && (o2 = rec.get(col1)) != null && o2.toString().equals(value1.toString())){
                return rec;
            }
        }
//        if(empty == null)
//            empty = new Record();
        return new Record();
    }

    public Record findEq(String col,Object value,String col1,Object value1,String col2,Object value2) {
        for (Record rec : this) {
            if( String.valueOf(rec.get(col)).equals( String.valueOf(value))
                    && String.valueOf(rec.get(col1)).equals( String.valueOf(value1))
                    &&  String.valueOf(rec.get(col2)).equals( String.valueOf(value2))){
                return rec;
            }
        }
        if(empty == null)
            empty = new Record();
        return empty;
    }

    public Record findEq(String col,Object value,
                         String col1,Object value1,
                         String col2,Object value2,
                         String col3,Object value3,
                         String col4,Object value4) {
        for (Record rec : this) {
            try {
                if (String.valueOf(rec.get(col)).equals(String.valueOf(value))
                        && String.valueOf(rec.get(col1)).equals(String.valueOf(value1))
                        && String.valueOf(rec.get(col2)).equals(String.valueOf(value2))
                        && String.valueOf(rec.get(col3)).equals(String.valueOf(value3))
                        && String.valueOf(rec.get(col4)).equals(String.valueOf(value4))) {
                    return rec;
                }
            }catch (Throwable t){
            }
        }
        if(empty == null)
            empty = new Record();
        return empty;
    }

    public Record find(String col,Object value) {
        for (Record rec : this) {
            if(rec.getString(col).equals(String.valueOf(value))||rec.getString(col)==String.valueOf(value)){
                return rec;
            }
        }
        return null;
    }
    public Record findIsEmpty(String col) {
        for (Record rec : this) {
            if(rec.getString(col).equals("")){
                return rec;
            }
        }
        return null;
    }
    public Record findIsNotEmpty(String col) {
        for (Record rec : this) {
            if(!rec.getString(col).equals("")){
                return rec;
            }
        }
        return null;
    }
    public RecordSet find0(String col,Object value) {
        RecordSet rs=new RecordSet();
        for (Record rec : this) {
            if(rec.getString(col).equals(String.valueOf(value))||rec.getString(col)==String.valueOf(value)){
                rs.add(rec);
            }
        }
        return rs;
    }
    public RecordSet findStart(String col,Object value) {
        RecordSet rs=new RecordSet();
        for (Record rec : this) {
            if(rec.getString(col).startsWith(String.valueOf(value))||rec.getString(col).equals(String.valueOf(value))){
                rs.add(rec);
            }
        }
        return rs;
    }
    public RecordSet contains(String col,Object value) {
        RecordSet rs=new RecordSet();
        for (Record rec : this) {
            if(rec.getString(col).contains(String.valueOf(value))){
                rs.add(rec);
            }
        }
        return rs;
    }
    public RecordSet findFloat(String col,float value) {
        RecordSet rs=new RecordSet();
        for (Record rec : this) {
            if(Float.parseFloat(rec.get(col).toString())==value){
                rs.add(rec);
            }
        }
        return rs;
    }
    public double sumFloat(String col) {
        double sum = 0;
        for (Record rec : this) {
            sum += rec.getFloat(col, 0.0);
        }
        return sum;
    }

    public double average(String col) {
        if (isEmpty())
            return 0.0;

        double sum = 0;
        for (Record rec : this) {
            sum += rec.getFloat(col, 0.0);
        }
        return sum / size();
    }

    //和mysql的聚合函数SUM()一样
    public RecordSet groupSum(String group1, String sumCol) {
        Map<String, RecordSet> re = this.toRecordSetMap(group1);

        RecordSet rs = new RecordSet();
        for(Map.Entry<String, RecordSet> e : re.entrySet()){
            rs.add(new Record()
                    .set(group1,e.getKey())
                    .set(sumCol,e.getValue().sumInt(sumCol)));
        }
        return rs;
    }


    public RecordSet groupSum(String group1, String group2, String sumCol) {
        Map<String,  Map<String,RecordSet>> re = this.toRecordSetMapping(group1,group2);

        RecordSet rs = new RecordSet();
        for(Map.Entry<String,  Map<String,RecordSet>> e : re.entrySet()){
            for(Map.Entry<String,RecordSet> e1 : e.getValue().entrySet()){
                rs.add(new Record()
                        .set(group1,e.getKey())
                        .set(group2,e1.getKey())
                        .set(sumCol,e1.getValue().sumInt(sumCol)));
                }
            }
        return rs;
    }
    public RecordSet mergeByKeys(String keyCol, RecordSet other, String otherKeyCol, Record def) {
        Map<String, Record> otherMap = other.toRecordMap(otherKeyCol);
        for (Record rec : this) {
            String key = rec.checkGetString(keyCol);
            Record otherRec = otherMap.get(key);
            if (otherRec != null) {
                rec.putAll(otherRec);
            } else {
                if (def != null)
                    rec.putAll(def);
            }
        }
        return this;
    }

    public static RecordSet mergeGroupSum(String mergeCol,String sumCol, RecordSet recs) {
        RecordSet merSumRs = new RecordSet();
        for (Record rec : recs){//all
            boolean h =false;
            b:
            for (Record r : merSumRs){
                if (r.getString(mergeCol).equals(rec.getString(mergeCol))){
                    h=true;
                    break b;
                }
            }
            if (h){
                for (Record r : merSumRs){
                    if (r.getString(mergeCol).equals(rec.getString(mergeCol))){
                        r.put(sumCol,r.getInt(sumCol)+rec.getInt(sumCol));
                    }
                }
            }else{
                Record t0 = new Record();
                t0.put(mergeCol,rec.getString(mergeCol));
                t0.put(sumCol,rec.getInt(sumCol));
                merSumRs.add(t0);
            }


        }
        return merSumRs;
    }

    public RecordSet mergeJsonByKeys(String keyCol, RecordSet other, String otherKeyCol, Record def) {
        Map<String, Record> otherMap = other.toRecordMap(otherKeyCol);
        for (Record rec : this) {
            String key = rec.checkGetString(keyCol);
            Record otherRec = otherMap.get(key);
            if (otherRec != null) {
                rec.put(keyCol, otherRec.toJsonNode());
            } else {
                if (def != null)
                    rec.put(keyCol, otherRec.toJsonNode());
            }
        }
        return this;
    }

    public static RecordSet of(Record rec) {
        RecordSet recs = new RecordSet();
        recs.add(rec);
        return recs;
    }

    public static RecordSet of(Record... recArray) {
        RecordSet recs = new RecordSet();
        Collections.addAll(recs, recArray);
        return recs;
    }



    public byte[] toBytes() {
        return IOUtils2.toBytes(this);
    }

    public ByteBuffer toByteBuffer() {
        return IOUtils2.toByteBuffer(this);
    }

    public static RecordSet fromByteBuffer(ByteBuffer byteBuff) {
        return IOUtils2.fromByteBuffer(RecordSet.class, byteBuff);
    }

    public static RecordSet fromBytes(byte[] bytes, int off, int len) {
        return IOUtils2.fromBytes(RecordSet.class, bytes, off, len);
    }

    public static RecordSet fromBytes(byte[] bytes) {
        return IOUtils2.fromBytes(RecordSet.class, bytes);
    }


    public RecordSet fullJoin(Map.Entry<String,String> equalsCol, RecordSet rs){
        if(this==null||rs==null){
            return this;
        }
        String col1 = equalsCol.getKey();
        String col2 = equalsCol.getValue();
        Record defaultRec=rs.getFirstRecord().copy();
        rs.addColumns("isexistsLeftJoin",0);
        for(Map.Entry<String,Object> entry : defaultRec.entrySet()){
            if(!entry.getKey().equals(col2)) {
                defaultRec.put(entry.getKey(),"0");
            }
        }
        for(Record e : this){
            String key = e.getString(col1);
            Record e0 = rs.findEq(col2,key);
            if(e0.isEmpty()) {
                e0=defaultRec;
                e0.put(col2,e.getString(col1));
            }else{
                e0.put("isexistsLeftJoin",1);
            }
            for(Map.Entry<String,Object> entry : e0.entrySet()){
                e.put(entry.getKey(),String.valueOf(entry.getValue()));
            }
        }
        defaultRec=this.getFirstRecord().copy();
        RecordSet right=rs.find0("isexistsLeftJoin",0).copy();
        right.removeColumns("isexistsLeftJoin");
        rs.removeColumns("isexistsLeftJoin");
        for(Map.Entry<String,Object> entry : defaultRec.entrySet()){
            if(!entry.getKey().equals(col1)) {
                defaultRec.put(entry.getKey(), "0");
            }
        }
        for(Record r:right){
            Record d=defaultRec.copy();
            for(Map.Entry<String,Object> entry : r.entrySet()){
                d.put(entry.getKey(),String.valueOf(entry.getValue()));
            }
            this.add(d);
        }
        this.removeColumns("isexistsLeftJoin");
        return this;
    }
    public RecordSet addColumns(String col,Object defaultValue) {
        for (Record rec : this)
            rec.putMissing(col,defaultValue);
        return this;
    }
}
