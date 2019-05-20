package com.yqh.marketing.basedevss.base.context;


public class OtherSystemContext implements java.io.Serializable {

    private String app_id="";

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public int getApp_type() {
        return app_type;
    }

    public void setApp_type(int app_type) {
        this.app_type = app_type;
    }

    private String app_name="";
    private String app_key="";
    private int app_type=0;


    public OtherSystemContext() {
    }



    public static OtherSystemContext dummy() {
        OtherSystemContext ctx = new OtherSystemContext();
        ctx.setApp_type(0);
        ctx.setApp_id("");
        ctx.setApp_key("");
        ctx.setApp_name("");
        return ctx;
    }
}
