package com.yqh.marketing.basedevss.base.util;


import com.yqh.marketing.basedevss.ServerException;
import com.yqh.marketing.common.ErrorCodes;
import org.apache.commons.lang.ObjectUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StringMap extends LinkedHashMap<String, Object> {
    public String getString(String k) {
        return get(k) +"";
    }
    public String getString(String k, String def) {
        Object v = get(k);
        return v != null ? ObjectUtils.toString(v) : def;
    }
    public void setString(String k, String v) {
        put(k, v);
    }

    public boolean getBoolean(String k, boolean def) {
        String s = getString(k, null);
        return s != null ? Boolean.parseBoolean(s) : def;
    }

    public void setBoolean(String k, boolean v) {
        setString(k, Boolean.toString(v));
    }

    public long getInt(String k, long def) {
        String s = getString(k, null);
        return s != null ? Long.parseLong(s) : def;
    }

    public void setInt(String k, long v) {
        setString(k, Long.toString(v));
    }

    private static String checkValue(String k, String v) {
        if (v == null)
            throw new ServerException(ErrorCodes.SYSTEM_MISS_REQUIRED_PARAMETER, "Missing parameter '%s'", k);
        return v;
    }

    public String checkGetString(String k) {
        return checkValue(k, getString(k, null));
    }


    public long checkGetInt(String k) {
        String v = checkGetString(k);
        try {
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            throw new ServerException(ErrorCodes.SYSTEM_PARAMETER_TYPE_ERROR, "Invalid parameter '%s'", k);
        }
    }
    public float checkGetFloat(String k) {
        String v = checkGetString(k);
        try {
            return Float.parseFloat(v);
        } catch (NumberFormatException e) {
            throw new ServerException(ErrorCodes.SYSTEM_PARAMETER_TYPE_ERROR, "Invalid parameter '%s'", k);
        }
    }
    public boolean checkGetBoolean(String k) {
        String v = checkGetString(k);
        return Boolean.parseBoolean(v);
    }
    public Map<String,String> getAll(){
        Map<String,String> map=new HashMap<String, String>();
        for(String s:this.keySet()){
            map.put(s,getString(s));
        }
        //map=(Map<String, String>)this;
        return map;
    }

}
