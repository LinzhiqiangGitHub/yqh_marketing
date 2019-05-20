package com.yqh.marketing.basedevss.base.sms;

public interface SmsConstant {

    /*
     * 发送短信状态
     *
     * SMS_TYPE  1为验证码 2为登录 3物流 4营销
     * */
    interface SMS_TYPE  {

        //验证码
        String VERIFICATION_CODE = "1";
        //登录
        String LOGIN = "2";
        //物流
        String LOGISTICS = "3";
        //营销
        String MARKETING = "4";

    }
}
