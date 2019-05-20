package com.yqh.marketing.common;

/**
 * Created by saiwengang on 2017/1/6.
 */
public enum DeliveryType {

    INNER("内部流转信息", 0), OUTER("外部物流信息", 1);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private DeliveryType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (DeliveryType c : DeliveryType.values()) {
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
