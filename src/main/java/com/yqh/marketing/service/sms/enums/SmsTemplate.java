package com.yqh.marketing.service.sms.enums;

/**
 * 短信模板
 * Created by aqlu on 15/12/4.
 */
public enum SmsTemplate {
    REGISTRY("您本次注册的验证码是：%s，有效期5分钟，打死也不告诉别人"),
    SET_PAY_PASSWORD("您本次设置支付密码的验证码是：%s，有效期5分钟，打死也不告诉别人"),
    REFUND_PASSWORD("您本次找回密码的验证码是：%s，有效期5分钟，打死也不告诉别人"),
    CHANGE_PAY_PASSWORD("您本次修改支付密码的验证码是：%s，有效期5分钟，打死也不告诉别人"),
    CHANGE_PASSWORD("您本次修改密码的验证码是：%s，有效期5分钟，打死也不告诉别人"),
    CHANGE_PHONE("您本次更改绑定手机的验证码是：%s，有效期5分钟，打死也不告诉别人");

    private String content;

    SmsTemplate(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }
}
