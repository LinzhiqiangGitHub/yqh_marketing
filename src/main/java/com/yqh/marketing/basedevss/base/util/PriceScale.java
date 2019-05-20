/*
 * Copyright 2013 NINGPAI, Inc.All rights reserved.
 * NINGPAI PROPRIETARY / CONFIDENTIAL.USE is subject to licence terms.
 */
package com.yqh.marketing.basedevss.base.util;

import java.math.BigDecimal;

/**
 * 价格精度-实体类
 *
 * @author NINGPAI-WangHaiYang
 * @since 2014年02月27日 17:37:38
 */
public class PriceScale {

    public static final BigDecimal HUNDRED = new BigDecimal("100");

    /**
     * 无参构造函数
     */
    public PriceScale() {
    }

    /**
     * 格式化价格，保留两位小数，四舍五入
     *
     * @param price 价格
     * @return parsed
     */
    public static BigDecimal parse(BigDecimal price) {
        return null == price ? null : price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 格式化价格，保留指定位数，多余的直接抛弃
     *
     * @param price 价格
     * @return parsed
     */
    public static BigDecimal cut(BigDecimal price, int scale) {
        return null == price ? null : price.setScale(scale, BigDecimal.ROUND_FLOOR);
    }

    /**
     * 0.9 -> 9折 0.89 -> 8.9折
     *
     * @param discount 折扣 0~1之间的小数
     * @return 转换后的折扣形式
     */
    public static BigDecimal getDiscount(BigDecimal discount) {
        return discount.multiply(HUNDRED).remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) == 0
                ? cut(discount.multiply(BigDecimal.TEN), 0)
                : cut(discount.multiply(BigDecimal.TEN), 1);
    }

}
