package com.yqh.marketing.base;


public class RestResponse<T> extends BaseRestResponse{

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
