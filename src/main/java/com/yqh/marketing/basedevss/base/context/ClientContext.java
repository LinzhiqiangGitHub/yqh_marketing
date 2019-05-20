package com.yqh.marketing.basedevss.base.context;

public class ClientContext implements java.io.Serializable {

    private String URL ;
    private String ID ;
    private String LOGIN_NAME;
    private String LOGIN_MOBILE;
    private String LOGIN_MAIL;
    private String PASSWORD;
    private String LOGIN_STATUS;
    private String LAST_LOGIN_TIME;
    private String LAST_LOGIN_IP;
    private String LAST_LOGIN_DEVICE;
    private String REGISTER_TIME;
    private String LOGIN_USER_TYPE;
    private String Url;
    private String From_page;
    private String App_id;
    private String App_name;
    private String App_key;
    private String App_type;

    public String getApp_id() {
        return App_id;
    }

    public void setApp_id(String app_id) {
        App_id = app_id;
    }

    public String getApp_name() {
        return App_name;
    }

    public void setApp_name(String app_name) {
        App_name = app_name;
    }

    public String getApp_key() {
        return App_key;
    }

    public void setApp_key(String app_key) {
        App_key = app_key;
    }

    public String getApp_type() {
        return App_type;
    }

    public void setApp_type(String app_type) {
        App_type = app_type;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getFrom_page() {
        return From_page;
    }

    public void setFrom_page(String from_page) {
        From_page = from_page;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getSetUrl() {
        return Url;
    }

    public void setSetUrl(String setUrl) {
        this.Url = setUrl;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLOGIN_NAME() {
        return LOGIN_NAME;
    }

    public void setLOGIN_NAME(String LOGIN_NAME) {
        this.LOGIN_NAME = LOGIN_NAME;
    }

    public String getLOGIN_MOBILE() {
        return LOGIN_MOBILE;
    }

    public void setLOGIN_MOBILE(String LOGIN_MOBILE) {
        this.LOGIN_MOBILE = LOGIN_MOBILE;
    }

    public String getLOGIN_MAIL() {
        return LOGIN_MAIL;
    }

    public void setLOGIN_MAIL(String LOGIN_MAIL) {
        this.LOGIN_MAIL = LOGIN_MAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getLOGIN_STATUS() {
        return LOGIN_STATUS;
    }

    public void setLOGIN_STATUS(String LOGIN_STATUS) {
        this.LOGIN_STATUS = LOGIN_STATUS;
    }

    public String getLAST_LOGIN_TIME() {
        return LAST_LOGIN_TIME;
    }

    public void setLAST_LOGIN_TIME(String LAST_LOGIN_TIME) {
        this.LAST_LOGIN_TIME = LAST_LOGIN_TIME;
    }

    public String getLAST_LOGIN_IP() {
        return LAST_LOGIN_IP;
    }

    public void setLAST_LOGIN_IP(String LAST_LOGIN_IP) {
        this.LAST_LOGIN_IP = LAST_LOGIN_IP;
    }

    public String getLAST_LOGIN_DEVICE() {
        return LAST_LOGIN_DEVICE;
    }

    public void setLAST_LOGIN_DEVICE(String LAST_LOGIN_DEVICE) {
        this.LAST_LOGIN_DEVICE = LAST_LOGIN_DEVICE;
    }

    public String getREGISTER_TIME() {
        return REGISTER_TIME;
    }

    public void setREGISTER_TIME(String REGISTER_TIME) {
        this.REGISTER_TIME = REGISTER_TIME;
    }

    public String getLOGIN_USER_TYPE() {
        return LOGIN_USER_TYPE;
    }

    public void setLOGIN_USER_TYPE(String LOGIN_USER_TYPE) {
        this.LOGIN_USER_TYPE = LOGIN_USER_TYPE;
    }




    public static ClientContext dummy() {
        ClientContext ctx = new ClientContext();
        ctx.setFrom_page("");
        ctx.setURL("");
        ctx.setID("");
        ctx.setLOGIN_NAME("");
        ctx.setLOGIN_MOBILE("");
        ctx.setPASSWORD("");
        ctx.setLOGIN_STATUS("");
        ctx.setLAST_LOGIN_TIME("");
        ctx.setLAST_LOGIN_IP("");
        ctx.setLAST_LOGIN_DEVICE("");
        ctx.setREGISTER_TIME("");
        ctx.setLOGIN_USER_TYPE("");
        ctx.setApp_id("");
        ctx.setApp_key("");
        ctx.setApp_name("");
        ctx.setApp_type("");
        return ctx;
    }
}
