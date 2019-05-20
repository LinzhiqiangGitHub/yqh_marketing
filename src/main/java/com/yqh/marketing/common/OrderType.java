package com.yqh.marketing.common;

/**
 * Created by saiwengang on 2017/1/6.
 */
public enum OrderType {

    MAIN_ORDER("主订单号", 0), SUB_ORDER("子订单号", 1);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private OrderType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (OrderType c : OrderType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
