package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.util.InitUtils;
import com.yqh.marketing.basedevss.base.web.JettyServer;
import com.yqh.marketing.service.accessory.AccessoryImpl;
import com.yqh.marketing.service.accessory.AccessoryLogic;
import com.yqh.marketing.service.advertiser.AdvertiserImpl;
import com.yqh.marketing.service.advertiser.AdvertiserLogic;
import com.yqh.marketing.service.expert.ExpertImpl;
import com.yqh.marketing.service.expert.ExpertLogic;
import com.yqh.marketing.service.job.JobImpl;
import com.yqh.marketing.service.job.JobLogic;
import com.yqh.marketing.service.job_pack.packImpl;
import com.yqh.marketing.service.job_pack.packLogic;
import com.yqh.marketing.service.LoginUser.LoginUserImpl;
import com.yqh.marketing.service.LoginUser.LoginUserLogic;
import com.yqh.marketing.service.NormalLogin.NormalLoginImpl;
import com.yqh.marketing.service.NormalLogin.NormalLoginLogic;
import com.yqh.marketing.service.RegisterUser.UsersImpl;
import com.yqh.marketing.service.RegisterUser.UserLogic;
import com.yqh.marketing.service.VerificationCode.VerificationCodeImpl;
import com.yqh.marketing.service.VerificationCode.VerificationCodeLogic;

public class GlobalLogics{

    private static AccessoryLogic accessoryLogic = new AccessoryImpl();
    private static AdvertiserLogic advertiserLogic = new AdvertiserImpl();
    private static ExpertLogic expertLogic = new ExpertImpl();
    private static packLogic packLogic = new packImpl();


    //注册or登录
    private static UserLogic userLogic = new UsersImpl();
    private static LoginUserLogic loginUserLogic = new LoginUserImpl();
    private static NormalLoginLogic normalLoginLogic = new NormalLoginImpl();
    private static VerificationCodeLogic verificationCodeLogic = new VerificationCodeImpl();
    private static JobLogic jobLogic = new JobImpl();

    public static void init() {
        InitUtils.batchInit(userLogic,loginUserLogic,normalLoginLogic
                ,verificationCodeLogic,expertLogic,advertiserLogic,jobLogic);
    }
    public static void destroy() {
        InitUtils.batchDestroy(accessoryLogic);
    }



    public static JobLogic getJobLogic() {
        return jobLogic;
    }

    public static void setJobLogic(JobLogic jobLogic) {
        GlobalLogics.jobLogic = jobLogic;
    }

    public static NormalLoginLogic getNormalLoginLogic() {
        return normalLoginLogic;
    }

    public static void setNormalLoginLogic(NormalLoginLogic normalLoginLogic) {
        GlobalLogics.normalLoginLogic = normalLoginLogic;
    }

    public static LoginUserLogic getLoginUserLogic() {
        return loginUserLogic;
    }

    public static void setLoginUserLogic(LoginUserLogic loginUserLogic) {
        GlobalLogics.loginUserLogic = loginUserLogic;
    }

    public static VerificationCodeLogic getVerificationCodeLogic() {
        return verificationCodeLogic;
    }

    public static void setVerificationCodeLogic(VerificationCodeLogic verificationCodeLogic) {
        GlobalLogics.verificationCodeLogic = verificationCodeLogic;
    }

    public static ExpertLogic getExpertLogic() {
        return expertLogic;
    }

    public static void setExpertLogic(ExpertLogic expertLogic) {
        GlobalLogics.expertLogic = expertLogic;
    }

    public static UserLogic getUserLogic() {
        return userLogic;
    }

    public static void setUserLogic(UserLogic userLogic) {
        GlobalLogics.userLogic = userLogic;
    }

    public static packLogic getPackLogic() {
        return packLogic;
    }

    public static void setPackLogic(packLogic packLogic) {
        GlobalLogics.packLogic = packLogic;
    }

    public static AdvertiserLogic getAdvertiserLogic() {
        return advertiserLogic;
    }

    public static void setAdvertiserLogic(AdvertiserLogic advertiserLogic) {
        GlobalLogics.advertiserLogic = advertiserLogic;
    }

    public static AccessoryLogic getAccessoryLogic() {
        return accessoryLogic;
    }

    public static void setAccessoryLogic(AccessoryLogic accessoryLogic) {
        GlobalLogics.accessoryLogic = accessoryLogic;
    }

    public static class ServerLifeCycle implements JettyServer.LifeCycle {
        @Override
        public void before() {
            GlobalLogics.init();
        }

        @Override
        public void after() {
            GlobalLogics.destroy();
        }
    }


}
