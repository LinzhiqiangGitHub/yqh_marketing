package com.yqh.marketing.common;
import com.yqh.marketing.basedevss.Errors;

public class ErrorCodes extends Errors {
    //SYSTEM ERROR
    public static final int SYSTEM_ERROR_CODE = 10000;
    public static final int SYSTEM_ERROR = SYSTEM_ERROR_CODE + 1;// 10001 //System error //系统错误
    public static final int SYSTEM_DB_ERROR = SYSTEM_ERROR_CODE + 2;//10002//DB error//数据库操作错误
    public static final int SYSTEM_IP_LIMIT = SYSTEM_ERROR_CODE + 4;//10004 //IP limit //IP限制不能请求该资源
    public static final int SYSTEM_PERMISSION_DENIED = SYSTEM_ERROR_CODE + 5;//10005 //Permission denied, need a high level app id//该资源需要appkey拥有授权
    public static final int SYSTEM_MISSING_PARAMETER = SYSTEM_ERROR_CODE + 6;//10006 //Source paramter (app id) is missing //缺少app id
    public static final int SYSTEM_TOO_MANY_TASKS = SYSTEM_ERROR_CODE + 9;//10009 //Too many pending tasks, system is busy //任务过多，系统繁忙
    public static final int SYSTEM_JOB_EXPIRED = SYSTEM_ERROR_CODE + 10;//10010 //Job expired //任务超时
    public static final int SYSTEM_MISS_REQUIRED_PARAMETER = SYSTEM_ERROR_CODE + 12;//10016 //Miss required parameter (%s) //缺失必选参数 (%s)
    public static final int SYSTEM_PARAMETER_TYPE_ERROR = SYSTEM_ERROR_CODE + 17;//10017 //Parameter (%s)'s value invalid, expect (%s) , but get (%s) , see doc for more info //参数值非法，需为 (%s)，实际为 (%s)
    public static final int SYSTEM_REQUEST_BODY_OVER_LIMIT = SYSTEM_ERROR_CODE + 18;//10018 //Request body length over limit //请求长度超过限制
    public static final int SYSTEM_REQUEST_API_NOT_FOUND = SYSTEM_ERROR_CODE + 20;//10020 //Request api not found //接口不存在
    public static final int SYSTEM_HTTP_METHOD_NOT_SUPPORT = SYSTEM_ERROR_CODE + 21;//10021 //HTTP method is not supported for this request //请求的HTTP METHOD不支持，请检查是否选择了正确的POST/GET方式

    public static final int SYSTEM_HTTP_REQUEST_DELAY = SYSTEM_ERROR_CODE + 22;//10022 //请求超时，客户端的时间跟服务器时间相差10分钟以上



    public static final int SERVICE_ERROR_CODE = 20000;

    //AUTH ERROR   20100
    public static final int AUTH_SERVICE_ERROR_CODE = SERVICE_ERROR_CODE + 100;
    public static final int AUTH_FAILED = AUTH_SERVICE_ERROR_CODE + 1;//20101//Auth faild //认证失败
    public static final int AUTH_LOGIN_ERROR = AUTH_SERVICE_ERROR_CODE + 2;//20102//Username or password error //用户名或密码不正确
    public static final int AUTH_OUT_OF_LIMIT = AUTH_SERVICE_ERROR_CODE + 3;//20103//Username and pwd auth out of rate limit //用户名密码认证超过请求限制
    public static final int AUTH_SIGNATURE_ERROR = AUTH_SERVICE_ERROR_CODE + 4;//20104//Signature invalid //签名值不合法
    public static final int AUTH_SIGNATURE_METHOD_ERROR = AUTH_SERVICE_ERROR_CODE + 5;//20105//Signature invalid //签名方式错误
    public static final int AUTH_TICKET_EXPIRED = AUTH_SERVICE_ERROR_CODE + 6;//20106//Token expired //Token已经过期
    public static final int AUTH_TICKET_INVALID = AUTH_SERVICE_ERROR_CODE + 7;//20107//Ticket不合法

    public static final int AUTH_USERNAME_EXISTS = AUTH_SERVICE_ERROR_CODE + 9;//20109//用户名已经存在
    public static final int AUTH_USERNAME_NOT_EXISTS = AUTH_SERVICE_ERROR_CODE + 10;//20110//重置密码的邮箱不存在
    public static final int AUTH_OLD_PASSWORD_ERROR = AUTH_SERVICE_ERROR_CODE + 11;//20111//用户原密码错误
    public static final int AUTH_NEED_TOKEN = AUTH_SERVICE_ERROR_CODE + 12;//20112//本API需要ticket
    public static final int AUTH_HAS_BIND_OTHER_ALREADY = AUTH_SERVICE_ERROR_CODE + 13;//20113//已经跟其他账号绑定了
    public static final int AUTH_HAS_NOT_BIND_OTHER = AUTH_SERVICE_ERROR_CODE + 14;//20114//已经跟其他账号绑定了
    public static final int AUTH_UPDATE_IMG_MUST_UPLOAD = AUTH_SERVICE_ERROR_CODE + 15;//20115//m必须上传文件才能更改头像
    public static final int AUTH_LOGIN_EXPIRE_ERROR = AUTH_SERVICE_ERROR_CODE + 16;//20116//Username or password error //用户名或密码不正确
    public static final int AUTH_LOGIN_USER_DELETE_ERROR = AUTH_SERVICE_ERROR_CODE + 17;//20117//账户已被停用
    public static final int USER_ID_NOT_EXISTS = AUTH_SERVICE_ERROR_CODE + 18;//20118//用户ID不存在
    public static final int AUTH_AGENCYUSER_NOT_NULL = AUTH_SERVICE_ERROR_CODE + 100;//20200//管理用户下有用户，不能删除

    public static final int AUTH_LOGIN_USER_ERROR_DEVICE = AUTH_SERVICE_ERROR_CODE + 19;//20119//已经在其他地方登录了

    public static final int AUTH_REGIST_ERROR_MOBILE = AUTH_SERVICE_ERROR_CODE + 20;//20120//手机号码格式不正确
    public static final int AUTH_REGIST_ERROR_EMAIL = AUTH_SERVICE_ERROR_CODE + 21;//20121//EMAIL格式不正确

    public static final int AUTH_REGIST_ERROR_USER = AUTH_SERVICE_ERROR_CODE + 22;//20122//注册用户名不正确
    public static final int AUTH_REGIST_VERIFY_CODE_EXPIRE = AUTH_SERVICE_ERROR_CODE + 23;//20123//验证码超时
    public static final int AUTH_LOGIN_ERROR_NOT_VERIFY = AUTH_SERVICE_ERROR_CODE + 24;//20124//未验证
    public static final int AUTH_LOGIN_ERROR_LOCKED= AUTH_SERVICE_ERROR_CODE + 25;//20125//会员被锁定

    public static final int AUTH_TOKEN_ERROR= AUTH_SERVICE_ERROR_CODE + 26;//20112//本API需要ticket
    public static final int AUTH_TOKEN_EXPIRED = AUTH_SERVICE_ERROR_CODE + 27;//20112//本API需要ticket

    public static final int AUTH_TOKEN_GET_ERROR= AUTH_SERVICE_ERROR_CODE + 28;//20128//本API需要ticket
    public static final int AUTH_TOKEN_SAVE_ERROR= AUTH_SERVICE_ERROR_CODE + 29;//20129//保存token失败
    public static final int AUTH_TOKEN_WINXIN_TOKEN_GET_ERROR= AUTH_SERVICE_ERROR_CODE + 30;//20130//获取微信token错误
    public static final int AUTH_TOKEN_WINXIN_USERINFO_GET_ERROR= AUTH_SERVICE_ERROR_CODE + 31;//20131//获取微信用户信息错误
    public static final int AUTH_SMS_VERIFY_CODE_VERIFY_ERROR= AUTH_SERVICE_ERROR_CODE + 32;//20132//短信验证码验证失败
    public static final int AUTH_CLIENT_TYPE_ERROR= AUTH_SERVICE_ERROR_CODE + 33;//客户端类型错误
    public static final int AUTH_NEED_CODE= AUTH_SERVICE_ERROR_CODE + 34;//需要code
    public static final int AUTH_WEIXIN_AUTH_ERROR= AUTH_SERVICE_ERROR_CODE + 35;//微信授权失败
    public static final int AUTH_MINIPROGRAM_GET_OPENID_ERROR= AUTH_SERVICE_ERROR_CODE + 36;//小程序获取openid失败


    public static final int CARDS_SERVICE_ERROR_CODE = SERVICE_ERROR_CODE + 200;
    public static final int CARDS_EXCEL_INPUT_FILE_ERROR = CARDS_SERVICE_ERROR_CODE + 1;//20201//excel文件错误
    public static final int CARDS_STATE_DATA_DATE_ERROR = CARDS_SERVICE_ERROR_CODE + 2;//20202//卡状态日期格式不对

    public static final int ORDER_ERROR_CODE = SERVICE_ERROR_CODE + 300;
    public static final int ORDER_ERROR_MONEY_ERROR = ORDER_ERROR_CODE + 1;//商品金额错误
    public static final int ORDER_ERROR_NO_PAY_RECORD = ORDER_ERROR_CODE + 2;//商品金额错误
    public static final int ORDER_ERROR_NO_ORDER_RECORD = ORDER_ERROR_CODE + 3;//商品金额错误
    public static final int ORDER_ERROR_NOT_OWN_ORDER_PAY = ORDER_ERROR_CODE + 4;//商品金额错误
    public static final int ORDER_ERROR_PRICE_ERROR = ORDER_ERROR_CODE + 5;//商品金额错误
    public static final int ORDER_ERROR_PAY_STATUS_ERROR = ORDER_ERROR_CODE + 6;//商品金额错误
    public static final int ORDER_ERROR_PAY_ERROR_BACK_CODE = ORDER_ERROR_CODE + 7;//商品金额错误

    public static final int USER_CARDS_ERROR_CODE = SERVICE_ERROR_CODE + 400;

    public static final int REQUEST_METHOD_ERROR= SERVICE_ERROR_CODE + 500;




}
