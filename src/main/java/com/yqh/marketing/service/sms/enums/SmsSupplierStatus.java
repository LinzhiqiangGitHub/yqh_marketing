package com.yqh.marketing.service.sms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 是否开启(0未开启 1已开启)
 * Created by aqlu on 15/12/4.
 */
public enum SmsSupplierStatus {
    DISABLE, ENABLE;

    @JsonCreator
    public static SmsSupplierStatus forValue(String ordinal) {
        return SmsSupplierStatus.values()[Integer.parseInt(ordinal)];
    }

    @JsonValue
    public String toValue() {
        return String.valueOf(this.ordinal());
    }
}
