package com.yqh.marketing.basedevss.base.backValue;


public class ResultValue implements java.io.Serializable {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private int status;
    private Object data;

    public ResultValue() {
    }



    public static ResultValue dummy() {
        ResultValue rsv = new ResultValue();
        rsv.setStatus(0);
        rsv.setData(null);
        return rsv;
    }
}
