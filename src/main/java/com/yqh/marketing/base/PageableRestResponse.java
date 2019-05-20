package com.yqh.marketing.base;

public class PageableRestResponse<T extends BaseResult> extends BaseRestResponse{

    private PageResult<T> result;

    public PageResult<T> getResult() {
        return result;
    }

    public void setResult(PageResult<T> result) {
        this.result = result;
    }
}
