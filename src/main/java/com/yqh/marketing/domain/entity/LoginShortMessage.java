package com.yqh.marketing.domain.entity;
;


public class LoginShortMessage {
    private String loginId;
    private String loginMobile;
    private String verificationCode;
    private String status;
    private long sendDateTime;
    private long endDateTime;
    private String shortMessageType;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginMobile() {
        return loginMobile;
    }

    public void setLoginMobile(String loginMobile) {
        this.loginMobile = loginMobile;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(long sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    public long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(long endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getShortMessageType() {
        return shortMessageType;
    }

    public void setShortMessageType(String shortMessageType) {
        this.shortMessageType = shortMessageType;
    }
}
