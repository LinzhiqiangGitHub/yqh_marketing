package com.yqh.marketing.base;

import java.util.List;

public class ListRestResponse<T> extends BaseRestResponse{

    private List<T> itemList;

    public List<T> getItemList() {
        return itemList;
    }

    public void setItemList(List<T> itemList) {
        this.itemList = itemList;
    }
}
