package com.yqh.marketing.basedevss.base.context;


import com.yqh.marketing.basedevss.base.data.Record;

public class AdminContext implements java.io.Serializable {
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getApp_platform() {
        return app_platform;
    }

    public void setApp_platform(String app_platform) {
        this.app_platform = app_platform;
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

    public void setSubscribe ( Integer subscribe){this.subscribe = subscribe;}
    public Integer getSubscribe (){ return subscribe; }
    private Integer subscribe ;
    private String app_type;
    private String ticket;
    private String user_id="";
    private String device_id;
    private String app_platform;
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
    private String userName="";

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    private String display_name="";
    private String level;
    private String user_Image;
    private int accessSource;
    private String isCache="";
    private String addressId="";
    private int terminal=1;
    private String userDeptId;
    public void setUserDeptId(String userDeptId) {
        this.userDeptId = userDeptId;
    }
    public String getUserDeptId() {
        return userDeptId;
    }



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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public AdminContext() {
    }



    public static AdminContext dummy() {
        AdminContext ctx = new AdminContext();
        ctx.setApp_type("0");
        ctx.setDevice_id("");
        ctx.setLanguage("");
        ctx.setTicket("");
        ctx.setUser_agent("");
        ctx.setUser_id("0");
        ctx.setApp_platform("0");
        ctx.setCall_id("");
        ctx.setVersionCode("");
        ctx.setChannel_id("");
        ctx.setIp_addr("");
        ctx.setFrom_page("");
        ctx.setOpenId("");
        ctx.setLevel("0");
        ctx.setUserName("");
        ctx.setUser_Image("");
        return ctx;
    }
}
