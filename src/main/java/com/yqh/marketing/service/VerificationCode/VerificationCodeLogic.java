package com.yqh.marketing.service.VerificationCode;

import com.yqh.marketing.base.NumberConstant;
import com.yqh.marketing.basedevss.base.data.Record;

import java.util.Date;

/**
 * 验证码操作
 */
public interface VerificationCodeLogic{

    int SHORT_MESSAGE_LENGTH = NumberConstant.Int.INT_SIX;

    int MAIL_LENGTH = NumberConstant.Int.INT_TWENTY;

    boolean NUMBER_FLAG_FALSE = Boolean.FALSE;
    boolean NUMBER_FLAG_TRUE = Boolean.TRUE;

    String NUMBER = "1234567890";

    String NUMBER_STRING = "1234567890abcdefghijkmnpqrstuvwxyz";

    /**
     *
     * @param loginMobile
     * @param verificationCode
     * @return
     */
    boolean checkLoginVerificationCode(String loginMobile, String verificationCode);

    /**
     * 创建登录验证码
     * @param loginMobile 登录手机号码
     * @return 登录验证码
     */
    String createLoginVerificationCode(String loginMobile);

    /**
     *检查验证码是否正确
     * @param loginMobile
     * @param verificationCode
     * @return
     */
    boolean checkBindMobileVerificationCode(String loginMobile, String verificationCode);
    boolean checkCancelBindMobileVerificationCode(String loginMobile, String verificationCode);
    /**
     * 创建绑定手机号码验证码
     * @param loginMobile 手机号码
     * @return 绑定手机验证码
     */
    String createBindMobileVerificationCode(String loginMobile);
    /**
     * 创建取消绑定手机号码验证码
     * @param loginMobile 手机号码
     * @return 绑定手机验证码
     */
    String createCancelBindMobileVerificationCode(String loginMobile);
    /**
     *
     * @param loginMobile
     * @param verificationCode
     * @return
     */
    boolean checkForgetPasswordVerificationCode(String loginMobile, String verificationCode);

    /**
     * 创建找回密码验证码
     * @param loginMobile 手机号码
     * @return 找回密码验证码
     */
    String createForgetPasswordVerificationCode(String loginMobile);

    /**
     *
     * @param loginMobile
     * @param verificationCode
     * @return
     */
    boolean checkRegisterVerificationCode(String loginMobile, String verificationCode);

    /**
     * 创建注册验证码
     * @param
     * @param id
     * @param ID
     * @param loginMobile 手机号码
     * @param end_date_time
     * @param date
     * @return 注册验证码
     */
    boolean createRegisterVerificationCode(String ID,String VerificationCode,String loginMobile,Date end_date_time,Date date,String registerTypePrefix);/**

    boolean checkBindMailVerificationCode(String loginMobile, String loginId, String verificationCode);
    boolean checkCancelBindMailVerificationCode(String loginMobile, String loginId, String verificationCode);


    /**
     * 创建绑定邮箱验证码
     * @param loginMail 登录邮箱
     * @return 邮箱验证码
     */
    String createBindMailVerificationCode(String loginMail);

    /**
     * 创建取消绑定邮箱验证码
     * @param loginMail
     * @return
     */
    String createCancelBindMailVerificationCode(String loginMail);

    boolean checkMailForgetPasswordVerificationCode(String loginMobile, String loginId, String verificationCode);


    /**
     * 创建邮箱找回密码验证码
     * @param loginMail 登录邮箱
     * @return 邮箱找回密码验证码
     */
    String createMailForgetPasswordVerificationCode(String loginMail);

    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    default String createRandom(boolean numberFlag, int length) {
        String retStr = null;
        String strTable = numberFlag ? NUMBER : NUMBER_STRING;
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }

    //删除验证码信息
    boolean deleteVerificationCodeByLoginMobile(String loginMobile);


    boolean updateVerificationCodeByID(String id);

    //查询过期时间
    String checkLoginVerificationCodeOutTime(String login_mobile);
    //按号码查询
    Record countByLoginMobile(String loginMobile);

    //查询手机号码对应的全部信息
    Record VerificationCodeByLoginMobile(String loginMobile);

    //修改有效时间信息
    boolean updateRegisterVerificationCode(String loginMobile, Date end_date_time, Date date);
    //修改时间
    boolean updateCodeByloginMobile(String loginMobile, String verificationCode, Date end_date_time_one, Date date);
}
