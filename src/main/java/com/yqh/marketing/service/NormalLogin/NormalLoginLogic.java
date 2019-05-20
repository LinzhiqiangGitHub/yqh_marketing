package com.yqh.marketing.service.NormalLogin;

import com.yqh.marketing.basedevss.base.data.Record;

/**
 *用户正常登陆
 */
public interface NormalLoginLogic{

    //普通用户登录
    Record getuserByUserNameAndPassword(String user_name);

    Record getUserByUserName(String user_name);
}
