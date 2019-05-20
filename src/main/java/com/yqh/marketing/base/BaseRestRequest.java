package com.yqh.marketing.base;

import java.io.Serializable;

public class BaseRestRequest implements Serializable{

    private Long requestTime;

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }
}
