package com.yqh.marketing.common;

/**
 * Created by saiwengang on 2017/1/6.
 */
public enum OrderStatus {

    DELETED("已删除", -1), CANCELLED("已取消", 0), UNPAID_BILL("待支付", 1),
    UNCONFIRM("待确认", 2), PAID_CONFIRM("收款已确认", 3), COD_CONFIRM("COD已确认", 4),
    EXPORTED("已导出", 5), THIRD_CREATED("第三方生成成功", 6), DISTRIBUTION("配货中", 7),
    PART_SHIPPED("已经部分发货", 9), ALL_SHIPPED("已经全部发货", 10), COD_CONFIRM_PAID("COD收款确认", 11),
    DEL("已送达", 12), TRADE_ABORTED("交易异常终止", 13), TRADE_PART_COMPLETED("交易部分完成", 15),
    TRADE_ALL_COMPLETED("交易全部完成", 16), LOCKED("锁定", 18), RETURN_GOODS_APPLY("退货申请", 20),
    CHANGE_GOODS_APPLY("换货申请", 21), RETURN_GOODS_HANDLING("退货处理中", 22),CHANGE_GOODS_HANDLING("换货处理中", 23),
    REFUND_APPLY("退款申请", 25), REFUND_HANDLING("退款处理中", 26), REFUNDED("已退款", 27);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private OrderStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (OrderStatus c : OrderStatus.values()) {
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
