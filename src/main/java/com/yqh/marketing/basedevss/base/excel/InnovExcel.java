package com.yqh.marketing.basedevss.base.excel;

import java.util.List;

public class InnovExcel {
    private JExcelUtils jExcelUtils = new JExcelUtils();

    /**
     * dataList style : List<List<String>>
     * @param dataList
     */

    public byte[] makeInboundPackage(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createInboundPackages(TITLE,sheetName,dataList);
    }
    public byte[] makeTurnTableLuckyUser(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createTurnTableLuckyUser(TITLE,sheetName,dataList);
    }
    public byte[] makeOrder(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createOrderExcel(TITLE,sheetName,dataList);
    }
    public byte[] makeSoldProducts(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createSoldProductsExcel(TITLE,sheetName,dataList);
    }
    public byte[] makeGoodsInfoExport(String TITLE,List<List<String>> dataList,String sheetName) {
        return jExcelUtils.createGoodsInfoExcel(TITLE,sheetName,dataList);
    }
    private volatile static InnovExcel innnvovExcel;

    public static InnovExcel getNewInstance() {
        if (innnvovExcel == null) {
            synchronized (InnovExcel.class) {
                if (innnvovExcel == null) {
                    innnvovExcel = new InnovExcel();
                    }
                }
            }
        return innnvovExcel;
    }

}