package com.yqh.marketing.base;

import java.io.Serializable;

public class BaseRestResponse implements Serializable{

    private String status = RestResponseStatus.SUCCESS;

    private String message = "success";

    public BaseRestResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseRestResponse(){

    }
}
