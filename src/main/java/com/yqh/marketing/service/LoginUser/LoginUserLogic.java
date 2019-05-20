package com.yqh.marketing.service.LoginUser;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.domain.entity.LoginUser;

public interface LoginUserLogic {


    //手机用户登录
    String Short_login_user();
    //根据邮箱获取用户信息
    LoginUser getLoginUserByLoginMail(String loginMail);
    //根据手机获取用户信息
    Record getLoginUserByLoginMobile(String loginMobile);
    //根据OpenId获取用户信息
    LoginUser getLoginUserByOpenId(String openId, String thirdPartAccountType);
    //计算该邮箱登录的用户个数
    int countByLoginMail(String loginMail);
    //计算该手机号码登录的用户个数
    int countByLoginMobile(String loginMobile);
    //修改密码
    void modifyPassword(String loginId, String password);
    //登录用户使用姓名查询
    LoginUser getLoginUserByLoginUsername(String user_name);
    //查看用户存在数量
    int countByLoginUsername(String user_name);
}
