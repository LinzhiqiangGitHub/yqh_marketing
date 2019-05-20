package com.yqh.marketing.basedevss.base.context;


import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.common.Constants;

public class Context implements java.io.Serializable {

    public String getApp_type() {
        return app_type;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }
    public String getIp_addr() {
        return ip_addr;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setVersionCode(String version_code) {
        this.version_code = version_code;
    }

    public String getVersionCode() {
        return version_code;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    private String app_type;
    private String token;
    private String customerId ="";

    public String getThird_id() {
        return third_id;
    }

    public void setThird_id(String third_id) {
        this.third_id = third_id;
    }

    private String third_id ="";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerUserName() {
        return customerUserName;
    }

    public void setCustomerUserName(String customerUserName) {
        this.customerUserName = customerUserName;
    }

    public String getCustomerNickName() {
        return customerNickName;
    }

    public void setCustomerNickName(String customerNickName) {
        this.customerNickName = customerNickName;
    }

    private String customerUserName ="";
    private String customerNickName ="";
    private String device_id;

    private String user_agent;
    private String location;
    private String language;
    private String version_code;
    private String channel_id;
    private String call_id;
    private String ip_addr;
    private String url;
    private String from_page;
    private String user_type;
    private String openId="";
    private String level;
    private String user_Image;
    private int accessSource;
    private String isCache="";
    private String addressId="";
    private int terminal=1;
    private String app_id="20181008001";

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getSold_channel() {
        return sold_channel;
    }

    public void setSold_channel(String sold_channel) {
        this.sold_channel = sold_channel;
    }

    private String sold_channel="1";

    public int getClient_type() {
        return client_type;
    }

    public void setClient_type(int client_type) {
        this.client_type = client_type;
    }

    public String getClient_platform() {
        return client_platform;
    }

    public void setClient_platform(String client_platform) {
        this.client_platform = client_platform;
    }

    private int client_type= Constants.CLIENT_TYPE_WEIXINPROGRAM;//  1网站         2微信H5页面   3微信小程序  4手机 5 平板
    private String client_platform;



    public int getTerminal() {
        return terminal;
    }

    public void setTerminal(int terminal) {
        this.terminal = terminal;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getIsCache() {
        return isCache;
    }

    public void setIsCache(String isCache) {
        this.isCache = isCache;
    }

    public int getAccessSource() {
        return accessSource;
    }
    public void setAccessSource(int accessSource) {
        this.accessSource = accessSource;
    }

    public String getUser_Image() {
        return user_Image;
    }

    public void setUser_Image(String user_Image) {
        this.user_Image = user_Image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(String call_id) {
        this.call_id = call_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getFrom_page() {
        return from_page;
    }

    public void setFrom_page(String from_page) {
        this.from_page = from_page;
    }
    public String getUser_type() {
        return user_type;
    }
    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
    private Record record = new Record();

    public Context() {
    }



    public static Context dummy() {
        Context ctx = new Context();
        ctx.setApp_type("0");
        ctx.setDevice_id("");
        ctx.setLanguage("");
        ctx.setUser_agent("");
        ctx.setCall_id("");
        ctx.setVersionCode("");
        ctx.setChannel_id("");
        ctx.setIp_addr("");
        ctx.setFrom_page("");
        ctx.setOpenId("");
        ctx.setLevel("0");
        ctx.setUser_Image("");
        ctx.setToken("");
        ctx.setCustomerId("");
        ctx.setCustomerUserName("");
        ctx.setCustomerNickName("");
        return ctx;
    }
}
