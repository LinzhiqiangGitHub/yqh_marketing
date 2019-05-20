package com.yqh.marketing.basedevss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 返回错误信息
 */
public class ServiceResult
{
    private Map<String,String> errors;
    private Map<String,String> description;
    private boolean success=true;
    private Object dynamicData;
    private String errCode="";
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    public Object getDynamicData() {
        return dynamicData;
    }
    private List<Throwable> throwable = new ArrayList<Throwable>();

    public void setDynamicData(Object dynamicData) {
        this.dynamicData = dynamicData;
    }

    public ServiceResult()
    {
        this.errors = new HashMap<String,String>();
        this.description=new  HashMap<String,String>();
    }
    /*
        是否成功
    **/
    public boolean success()
    {
        return this.errors.size()==0 && throwable.isEmpty()&&this.errCode.isEmpty();
    }
    /*
        错误码
    **/
    public void addErrorMessage(String key,String errorMessage)
    {
        this.errors.put(key,errorMessage);
        success=false;
    }

    public void addThrowable(Throwable throwable)
    {
        this.throwable.add(throwable);
        success=false;
    }

    public Throwable getFirstThrowable() {
        if(throwable.isEmpty())
            return null;
        return throwable.get(0);
    }



    public void addErrorMessage(String errorMessage)
    {
        log.debug("ServiceResult:"+errorMessage);
        this.errors.put(Integer.toString(errorMessage.hashCode()),errorMessage);
        this.description.put(Integer.toString(errorMessage.hashCode()),errorMessage);
        success=false;
    }
    public void addErrorMessage(String key,String errorMessage,String description)
    {
        log.debug("ServiceResult:"+errorMessage+",description:"+description);
        this.errors.put(key,errorMessage);
        this.description.put(key,description);
        success=false;
    }
    public Map<String,String> getErrors()
    {
        return this.errors;
    }
    public String getErrorByKey(String key)
    {
        if(this.errors.containsKey(key))
            return this.errors.get(key);
        else
            return "";
    }
    public KeyValue getFirstError()
    {
        KeyValue kv=null;
        for (String key : this.errors.keySet()) {
            kv=new KeyValue(key,this.errors.get(key));
            break;
        }
        return kv;
    }
    public String getFirstErrorMessage()
    {
        String s="";
        for (String key : this.errors.keySet()) {
            s=this.errors.get(key);
            break;
        }
        return s;
    }
    public String getFirstErrorDescription()
    {
        String s="";
        for (String key : this.description.keySet()) {
            s=this.description.get(key);
            break;
        }
        return s;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        success=false;
        this.errCode = errCode;
    }
}
