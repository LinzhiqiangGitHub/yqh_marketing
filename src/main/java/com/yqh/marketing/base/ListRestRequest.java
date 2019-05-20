package com.yqh.marketing.base;



import java.io.Serializable;
import java.util.List;

public class ListRestRequest<T extends Serializable> extends BaseRestRequest{

    private List<T> paramList;

    public List<T> getParamList() {
        return paramList;
    }

    public void setParamList(List<T> paramList) {
        this.paramList = paramList;
    }
}
