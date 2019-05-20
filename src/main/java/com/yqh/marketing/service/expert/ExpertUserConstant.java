package com.yqh.marketing.service.expert;

public interface ExpertUserConstant {

    String APPLICATION_NAME = "EXPERT-USER-SERVICE";

    /**
     * 状态
     **/
    interface PLATEFORM_TYPE{
        //抖音
        int DOUYIN = 1;
        //微博
        int WEIBO = 2;
        //百家号(百度账号)
        int BAIDU = 3;
    }

    /**
     * 审核状态
     * */
    interface AUDIT_STATUS {
        //待审核
        int CHECK_PENDING = 1;
        //已审核通过
        int APPROVED = 2;
        //未审核通过
        int REJECTED = 3;
    }

    /**
     * 用户类型
     */
    interface UserType {
        /**
         * EXCEPTION: 达人用户
         */
        String EXCEPTION = "1";
        /**
         * ADVERTISER: 广告客户
         */
        String SHORT_MESSAGE = "2";
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
