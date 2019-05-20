package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.util.DateUtils;

/**
 * Created by liuhongjia on 2016/12/27.
 */
public class BaseReponse<T> {
    /**
     * 返回状态码
     */
    private int status;
    /**
     * 返回状态消息
     */
    private String msg;
    /**
     * unix 时间戳
     */
    private long timestamp;
    /**
     * 返回的数据
     */
    public T data;

    public BaseReponse() {
        this.timestamp = DateUtils.nowMillis();
        status=0;
    }

    /**
     * 成功存入数据
     * yang
     *
     * @param data
     */
    public BaseReponse(T data) {
        this.timestamp = DateUtils.nowMillis();
        this.status=data==null?201:1;
        this.msg = "";
        this.data = data;
    }

    /**
     * 存入数据
     * yang
     *
     * @param data
     * @param status
     */
    public BaseReponse(T data, int status) {
        this.timestamp = DateUtils.nowMillis();
        this.data = data;
        this.status = status;
        this.msg ="";
    }

    public int getstatus() {
        return status;
    }

    public void setstatus(int status) {
        this.status = status;
    }

    public String getmsg() {
            return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
