package com.yqh.marketing.common.aliyun;

public enum AliyunOSSDir {
    VIDEO_STORAGE(1,"videoStorage/"),
    IMG_STORAGE(2,"imgStorage/");


    // 成员变量
    private int index;
    private String path;

    // 构造方法
    AliyunOSSDir(int index, String path) {
        this.index = index;
        this.path = path;
    }

    public static String getPath(int index) {
        for (AliyunOSSDir c : AliyunOSSDir.values()) {
            if (c.getIndex() == index) {
                return c.path;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
