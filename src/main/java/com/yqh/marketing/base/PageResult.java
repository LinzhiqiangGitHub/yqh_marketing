package com.yqh.marketing.base;

import java.util.List;

/**
 * 分页结果数据集合。
 * @author jiang_nan
 * @version 1.0
 */
public class PageResult<T extends BaseResult> extends BaseResult{
    /**当前页*/

    private int currentPage;
    /**一共页数*/

    private int totalPage;
    /**一页显示页数*/

    private int pageSize;
    /**总行数*/

    private long totalCount;
    /**数据集*/

    private List<T> items;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
