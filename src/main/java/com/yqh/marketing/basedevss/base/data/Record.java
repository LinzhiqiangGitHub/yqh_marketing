package com.yqh.marketing.basedevss.base.data;


import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.basedevss.base.BaseErrors;
import com.yqh.marketing.basedevss.base.io.IOUtils2;
import com.yqh.marketing.basedevss.base.io.Serializable;
import com.yqh.marketing.basedevss.base.util.CollectionUtils2;
import com.yqh.marketing.basedevss.base.util.Copyable;
import com.yqh.marketing.basedevss.base.util.StringUtils2;
import com.yqh.marketing.basedevss.base.util.json.JsonGenerateHandler;
import com.yqh.marketing.basedevss.base.util.json.JsonUtils;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Record extends LinkedHashMap<String, Object>  implements Copyable<Record>, Serializable, JsonSerializableWithType,Cloneable {
    public Record() {
    }
    //序列化ID
    private static final long serialVersionUID = -2626047433043769286L;
    public Record(Map<String, ? extends Object> values) {
        super(values);
    }

    public boolean has(String col) {
        return containsKey(col);
    }

    public String[] getColumns() {
        Set<String> cols = keySet();
        return cols.toArray(new String[cols.size()]);
    }

    public Object getScalar() {
        return isEmpty() ? Values.NULL : values().iterator().next();
    }

    public void putMissing(String col, Object o) {
        if (!has(col))
            put(col, o);
    }

    public void putIf(String col, Object o, boolean b) {
        if (b)
            put(col, o);
    }

    @Override
    public void write(Encoder out, boolean flush) throws IOException {
        out.writeMapStart();
        out.setItemCount(size());
        for (Map.Entry<String, Object> e : entrySet()) {
            out.startItem();
            out.writeString(e.getKey());
            IOUtils2.writeVariant(out, e.getValue(), false);
        }
        out.writeMapEnd();

        if (flush)
            out.flush();
    }

    @Override
    public void readIn(Decoder in) throws IOException {
        clear();
        long l = in.readMapStart();
        if (l > 0) {
            do {
                for (int i = 0; i < l; i++) {
                    String k = in.readString(null).toString();
                    Object v = IOUtils2.readVariant(in);
                    put(k, v);
                }
            } while ((l = in.mapNext()) > 0);
        }
    }

    public static Record read(Decoder in) throws IOException {
        Record rec = new Record();
        rec.readIn(in);
        return rec;
    }

    public byte[] toBytes() {
        return IOUtils2.toBytes(this);
    }

    public ByteBuffer toByteBuffer() {
        return IOUtils2.toByteBuffer(this);
    }

    public static Record fromByteBuffer(ByteBuffer byteBuff) {
        return IOUtils2.fromByteBuffer(Record.class, byteBuff);
    }

    public static Record fromBytes(byte[] bytes, int off, int len) {
        return IOUtils2.fromBytes(Record.class, bytes, off, len);
    }

    public static Record fromBytes(byte[] bytes) {
        return IOUtils2.fromBytes(Record.class, bytes);
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
        jg.writeStartObject();
        for (Map.Entry<String, Object> e : entrySet()) {
            String col = e.getKey();
            Object val = e.getValue();
            if (ignoreNull) {
                if (val != null && !(val instanceof Null)) {
                    jg.writeFieldName(col);
                    JsonUtils.writeValue(jg, val, null);
                }
            } else {
                jg.writeFieldName(col);
                JsonUtils.writeValue(jg, val, null);
            }
        }
        jg.writeEndObject();
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

    public static Record fromJson(String json) {
        return JsonUtils.fromJson(json, Record.class);
    }

    public JsonNode toJsonNode() {
        return JsonUtils.parse(toString(false, false));
    }

    public String getString(String col, String def) {
        try {
            Object v = get(col);
            return v != null ? Values.toString(v) : def;
        } catch (Exception e) {
            return def;
        }
    }
    public boolean getBoolean(String col) {
       return getBoolean(col,false);
    }
    public boolean getBoolean(String col, boolean def) {
        try {
            Object v = get(col);
            return v != null ? Values.toBoolean(v) : def;
        } catch (Exception e) {
            return def;
        }
    }

    public long getInt(String col, long def) {
        try {
            Object v = get(col);
            return v != null ? Values.toInt(v) : def;
        } catch (Exception e) {
            return def;
        }
    }

    public Integer getInteger(String col, Integer def){
        try {
            Object v = get(col);
            return v != null ? Integer.valueOf(String.valueOf(v)) : def;
        } catch (Exception e) {
            return def;
        }
    }
    public RecordSet getRecordSet(String col){
        try {
            return (RecordSet) get(col);
        }catch (Exception e){
            return null;
        }
    }
    public Record getRecord(String col){
        try {
            Object obj = get(col);
            if (null != obj)
                return (Record) get(col);
            return null;
        }catch (Exception e){
            return null;
        }
    }
    public Record getRecordDef(String col){
        try {
            Record obj =getRecord(col);
            if(null == obj)
                return new Record();
            return obj;
        }catch (Exception e){
            return new Record();
        }
    }
    public double getFloat(String col, double def) {
        try {
            Object v = get(col);
            return v != null ? Float.parseFloat(Values.toString(v)) : def;
        } catch (Exception e) {
            return def;
        }
    }
    public float getFloat0(String col, float def) {
        try {
            Object v = get(col);
            if (v ==  null){
                return def;
            }else{
                try {
                    return  Float.parseFloat(Values.toString(v));
                }catch (Exception e){
                    return def;
                }
            }
        } catch (Exception e) {
            return def;
        }
    }
	public Record putSum(String key,BigDecimal val){
        String countKey = "count";
        String valKey = "total";

        Record old = getRecord(key);
        if(old == null)
            old = new Record();

        long putCount = old.getInt(countKey) + 1;
        BigDecimal oldVal = old.getDecimal(valKey);
        BigDecimal newVal = oldVal.add(val);

        old.put(valKey,newVal);
        old.put(countKey,putCount);
        put(key,old);
        return old;
    }

    public String getString(String col) {
        return getString(col, "");
    }

    public BigDecimal getDecimal(String col){
       return new BigDecimal(getString(col,"0"));
    }
    public double getFloat(String col,int scale){
        return new BigDecimal(getString(col,"0")).setScale(scale,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public BigDecimal getDecimal(String col,BigDecimal def){
        try {
            return new BigDecimal(getString(col, null));
        }catch (Throwable t){
            return def;
        }
    }

    public long getInt(String col) {
        return getInt(col, 0L);
    }

    public LocalDateTime getDateTime(String col) {
        try {
            Object v = get(col);
            if (v ==  null){
                return null;
            }else{
                try {
                    if (Values.toString(v).equals("NULL") || Values.toString(v).equals("null") || Values.toString(v).equals("0000-00-00 00:00:00"))
                        return null;
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    return LocalDateTime.parse(Values.toString(v), df);
                }catch (Exception e){
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public double getFloat(String col) {
        return getFloat(col, 0.0);
    }

    public float getFloat0(String col) {
        return getFloat0(col, 0.00f);
    }

    private void checkColumn(String col) {
        if (!has(col))
            throw new ServerException(BaseErrors.PLATFORM_RECORD_ERROR, "Missing column '%s'", col);
    }

    public Object checkGet(String col) {
        checkColumn(col);
        return get(col);
    }

    public String checkGetString(String col) {
        checkColumn(col);
        return getString(col);
    }

    public long checkGetInt(String col) {
        checkColumn(col);
        return getInt(col);
    }

    public boolean checkGetBoolean(String col) {
        checkColumn(col);
        return getBoolean(col, false);
    }

    public double checkGetFloat(String col) {
        checkColumn(col);
        return getFloat(col);
    }

    public Record renameColumn(String oldCol, String newCol) {
        if (!StringUtils.equals(oldCol, newCol) && has(oldCol)) {
            Object o = get(oldCol);
            remove(oldCol);
            put(newCol, o);
        }
        return this;
    }

    @Override
    public Record copy() {
        Record rec = new Record();
        rec.putAll(this);
        return rec;
    }

    private Record copyRecord;
    public Record copy(String...copy) {
        String[] keys = new TreeSet<String>(keySet()).toArray(new String[size()]);

        if(copyRecord == null)
            copyRecord = new Record();
        int i = copyRecord.size();
        for(String k : copy){
            if(Arrays.binarySearch(keys,k) < 0)
                copyRecord.put("copy"+i ,k);
            else {
                Object v = get(k);
                if(v == null)
                    v = "";
                else if(v instanceof Number)
                    v = StringUtils2.doubleFormat(((Number) v).doubleValue());
                copyRecord.put(k, v);
            }
            i++;
        }
        return copyRecord;
    }
    public Record copy(Record rec, String...keys) {
        for(String k : keys){
            rec.put(k,get(k));
        }
        return rec;
    }


    public Record set(String col, Object v) {
        put(col, v);
        return this;
    }

    public Record setMissing(String col, Object v) {
        putMissing(col, v);
        return this;
    }

    public Record setIf(String col, Object v, boolean b) {
        if (b)
            put(col, v);
        return this;
    }

    public Record replace(String col, Object v) {
        if (has(col))
            put(col, v);
        return this;
    }

    public Record removeColumns(Collection<String> cols) {
        for (String col : cols)
            remove(col);
        return this;
    }

    public Record removeColumns(String... cols) {
        return removeColumns(Arrays.asList(cols));
    }

    public Record retainColumns(Collection<String> cols) {
        ArrayList<String> allCols = new ArrayList<String>(keySet());
        for (String col : allCols) {
            if (!cols.contains(col))
                remove(col);
        }
        return this;
    }

    public Record retainColumns(String... cols) {
        return retainColumns(Arrays.asList(cols));
    }

    public void copyTo(Record rec) {
        if (rec != null)
            rec.putAll(this);
    }


    public static Record of(String c1, Object v1) {
        Record rec = new Record();
        rec.put(c1, v1);
        return rec;
    }

    public static Record of(String c1, Object v1, String c2, Object v2) {
        Record rec = new Record();
        rec.put(c1, v1);
        rec.put(c2, v2);
        return rec;
    }

    public static Record of(String c1, Object v1, String c2, Object v2, String c3, Object v3) {
        Record rec = new Record();
        rec.put(c1, v1);
        rec.put(c2, v2);
        rec.put(c3, v3);
        return rec;
    }

    public static Record of(String c1, Object v1, String c2, Object v2, String c3, Object v3, String c4, Object v4) {
        Record rec = new Record();
        rec.put(c1, v1);
        rec.put(c2, v2);
        rec.put(c3, v3);
        rec.put(c4, v4);
        return rec;
    }

    public static Record of(String c1, Object v1, String c2, Object v2, String c3, Object v3, String c4, Object v4, String c5, Object v5) {
        Record rec = new Record();
        rec.put(c1, v1);
        rec.put(c2, v2);
        rec.put(c3, v3);
        rec.put(c4, v4);
        rec.put(c5, v5);
        return rec;
    }
    public static Record of(String c1, Object v1, String c2, Object v2, String c3, Object v3, String c4, Object v4, String c5, Object v5, String c6, Object v6) {
        Record rec = new Record();
        rec.put(c1, v1);
        rec.put(c2, v2);
        rec.put(c3, v3);
        rec.put(c4, v4);
        rec.put(c5, v5);
        rec.put(c6, v6);
        return rec;
    }
    public static Record of(String c1, Object v1, String c2, Object v2, String c3, Object v3, String c4, Object v4, String c5, Object v5, String c6, Object v6, String c7, Object v7) {
        Record rec = new Record();
        rec.put(c1, v1);
        rec.put(c2, v2);
        rec.put(c3, v3);
        rec.put(c4, v4);
        rec.put(c5, v5);
        rec.put(c6, v6);
        rec.put(c7, v7);
        return rec;
    }
    public static Record of(String c1, Object v1, String c2, Object v2, String c3, Object v3, String c4, Object v4, String c5, Object v5, String c6, Object v6, String c7, Object v7, String c8, Object v8) {
        Record rec = new Record();
        rec.put(c1, v1);
        rec.put(c2, v2);
        rec.put(c3, v3);
        rec.put(c4, v4);
        rec.put(c5, v5);
        rec.put(c6, v6);
        rec.put(c7, v7);
        rec.put(c8, v8);
        return rec;
    }
    public static Record of(Object[][] values) {
        Record rec = new Record();
        rec.putAll(CollectionUtils2.arraysToMap(values));
        return rec;
    }
}
