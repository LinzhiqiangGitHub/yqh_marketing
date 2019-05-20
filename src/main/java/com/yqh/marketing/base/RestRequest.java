package com.yqh.marketing.base;


public class RestRequest<T extends BaseParam> extends BaseRestRequest{

    private T param;

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
