package com.yqh.marketing.base;

import java.util.List;

public class PageableRestRequest<T extends BaseParam> extends RestRequest<T>{

    private PageParam pageParam;

    private List<OrderFieldParam> orderFieldParamList;

    private T param;

    public PageParam getPageParam() {
        return pageParam;
    }

    public void setPageParam(PageParam pageParam) {
        this.pageParam = pageParam;
    }

    public List<OrderFieldParam> getOrderFieldParamList() {
        return orderFieldParamList;
    }

    public void setOrderFieldParamList(List<OrderFieldParam> orderFieldParamList) {
        this.orderFieldParamList = orderFieldParamList;
    }

    @Override
    public T getParam() {
        return param;
    }

    @Override
    public void setParam(T param) {
        this.param = param;
    }
}
