package com.yqh.marketing.service.advertiser;

public interface AdvertiserUserConstant {

    String APPLICATION_NAME = "ADVERTISER-USER-SERVICE";

    /**
     * 用户类型(品类)
     */
    interface UserType {
        /**
         *  COMPANY: 公司
         */
        String COMPANY = "1";
        /**
         *  PERSONAGE: 个人
         */
        String PERSONAGE = "2";
    }

    /**
     * 达人类别
     */
    interface EXPERTTYPE {
        /**
         * 写手
         */
        int WRITER = 1;
        /**
         * 视频剪辑
         */
        int VIDEO_CLIP = 2;
        /**
         * 人气网红
         */
        int POPULAR_WEB_CELEBRITY = 3;
    }


    /**
     * 达人性别
     */
    interface GENDER {
        /**
         * MAN: 男
         */
        String MAN = "1";
        /**
         * WOMAN: 女
         */
        String WOMAN = "2";
        /***/
    }


    /**
     * 发送邮件类型
     */
    interface LoginUserMailType {
        /**
         * FORGET_PASSWORD_TYPE:找回密码
         */
        String FORGET_PASSWORD_TYPE = "1";
        /**
         * BIND_MAIL_TYPE:绑定邮箱
         */
        String BIND_MAIL_TYPE = "2";
        /**
         * CANCEL_BIND_MAIL_TYPE:取消绑定邮箱
         */
        String CANCEL_BIND_MAIL_TYPE = "3";
    }

    /**
     * Redis Login User Mail Prefix
     */
    interface RedisLoginUserMailPrefix {
        /**
         * FORGET_PASSWORD_TYPE_PREFIX:找回密码前缀
         */
        String FORGET_PASSWORD_TYPE_PREFIX = "MAIL_FORGET_PASSWORD_";
        /**
         * BIND_MAIL_TYPE_PREFIX:绑定邮箱前缀
         */
        String BIND_MAIL_TYPE_PREFIX = "MAIL_BIND_MAIL_";
        /**
         * CANCEL_BIND_MAIL_TYPE_PREFIX:取消绑定邮箱前缀
         */
        String CANCEL_BIND_MAIL_TYPE_PREFIX = "MAIL_CANCEL_BIND_MAIL_";

    }

    /**
     * 用户存在状态类型
     */
    interface LoginUserStatusType {
        /**
         * 正常状态
         */
        String NORMAL_STATUS = "1";
        /**
         * 冻结
         */
        String FREEZE_STATUS = "2";
        /**
         * 密码次数输入过多
         */
        String MORE_TIMES_STSTUS = "3";
        /**
         * 废弃
         */
        String ABANDON_STATUS = "4";
    }
}
