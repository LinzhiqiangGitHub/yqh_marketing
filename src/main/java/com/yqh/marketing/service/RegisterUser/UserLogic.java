package com.yqh.marketing.service.RegisterUser;

import com.yqh.marketing.basedevss.base.data.Record;

import java.util.Date;

public interface UserLogic {


    //boolean short_save_login_user(LoginUser loginUser);

    //用户注册
    boolean sava_user(String user_id, String user_name, String user_password, String login_mobile, Date date);

    //手机名称重复验证
    Record getUserByUserName(String login_name);

    //手机登录名称重复验证
    Record getUserBylodinName(String loginName);

    //计算该手机号码登录的用户个数
    Record countByLoginMobile(String loginMobile);

    //手机用户注册
    boolean short_save_login_user(String userId, String loginName, String password, String loginMobile, String loginStatus, String loginUserType, Date registerTime);

    //用户名是否重复
    Record getUserByUserNameCount(String loginName);

}
