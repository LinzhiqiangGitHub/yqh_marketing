package com.yqh.marketing.domain.result;

import lombok.Data;

import java.util.List;


/**
 * 用户登录返回值
 */
@Data
public class LoginResult {
    private String loginId;
    private String loginUserType;
    private String userType;
    private String userId;
    private boolean isRegister;
    private String token;
    private List<ThirdPartAccountResult> thirdPartAccountResults;
}
