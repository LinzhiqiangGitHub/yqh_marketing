package com.yqh.marketing.service.enums;

/**
 * Created by liqun on 2017/8/23.
 */
public class CustomerConstants {
    public enum LogType {
        subscribe,unsubscribe,updatebindcode,mobilelogin,accountMerge
    }

    public static class loginType{
        public static int mobile=2;
        public static int wechatscan=3;
        public static int wechatauth=1;
    }
    public static class customerSource{
        public static int wechat=1;//1微信，2小程序,3安卓，4ios,5wap;
        public static int miniProgram=2;
        public static int android=3;
        public static int ios=4;
        public static int wap=5;
    }

    public static int getCustomerSourceByuserTerminal(int terminal){
        //**1****表示微信商城，2表示APP，3表示PCWEB，4表示小程序，5表示wap******\
        //source 1微信商城，2表示小程序，3表示PCWEB，4表示app，5表示wap
        if(terminal==2){
            return 4;
        }
        if(terminal==4){
            return 2;
        }
        return terminal;
    }
    public enum SendMobileValidateType{
        login,bindMobile,updateMobile,validateMobile,erpUpdateMobile
    }
    public static int bindMobileReward=200;//绑定手机奖励金额，单位 分
    public static int getuserTerminalByCustomerSource(int source){
        //**1****表示微信商城，2表示APP，3表示PCWEB，4表示小程序，5表示wap******\
        //source 1微信商城，2表示小程序，3表示PCWEB，4表示app，5表示wap
        if(source==4){
            return 2;
        }
        if(source==2){
            return 4;
        }
        return source;
    }
}
