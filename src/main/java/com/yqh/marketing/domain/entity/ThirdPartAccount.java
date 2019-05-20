package com.yqh.marketing.domain.entity;


/**
 * 第三方登录实体类
 */

public class ThirdPartAccount {
    private String loginId;
    private String openId;
    private String thirdPartyAccountType;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getThirdPartyAccountType() {
        return thirdPartyAccountType;
    }

    public void setThirdPartyAccountType(String thirdPartyAccountType) {
        this.thirdPartyAccountType = thirdPartyAccountType;
    }
}
