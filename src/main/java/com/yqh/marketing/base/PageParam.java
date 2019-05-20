package com.yqh.marketing.base;

/**
 * 数据库分页参数.
 *
 * @author jiang_nan
 */
public class PageParam extends BaseParam {

    private int currentPage;

    private int pageSize;

    private boolean paging;

    private int rowNumber;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public PageParam() {

    }

    public PageParam(PageParam pageParam) {
        this.currentPage = pageParam.getCurrentPage();
        this.pageSize = pageParam.getPageSize();
        this.paging = pageParam.isPaging();
        this.rowNumber = (currentPage - NumberConstant.Int.INT_ONE) * pageSize;
    }


}
