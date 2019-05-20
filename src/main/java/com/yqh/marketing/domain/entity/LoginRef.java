package com.yqh.marketing.domain.entity;

import com.yqh.marketing.base.BaseEntity;

/**
 * 用户影射实体类
 */

public class LoginRef extends BaseEntity {

    private String loginId;
    private String userId;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
