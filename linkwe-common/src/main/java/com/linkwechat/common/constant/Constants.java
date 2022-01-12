package com.linkwechat.common.constant;

/**
 * 通用常量信息
 * 
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    
    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";


    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";


    /** 是否为系统默认（是） */
    public static final int SERVICE_STATUS_ERROR = -1;


    /** 启用状态 */
    public static final String NORMAL_CODE=new String("0");


    /** 删除状态 */
    public static final String DELETE_CODE=new String("2");

    /** 业务判断成功状态 */
    public static final Integer SERVICE_RETURN_SUCCESS_CODE=0;


    /** 系统用户 */
    public static final String USER_TYPE_SYS="00";

    /** 企业微信用户 */
    public static final  String USER_TYPE_WECOME="11";


    /** 企业管理 */
    public static  final String USER_TYOE_CORP_ADMIN="22";


    /** 企业微信用户系统中默认用户 */
    public static final String DEFAULT_WECOME_ROLE_KEY="WeCome";


    /** 企业微信用户系统中默认用户 */
    public static final String DEFAULT_WECOME_CORP_ADMIN="CROP_ADMIN_ROLE";

    /**完成待办*/
    public static final String HANDLE_SUCCESS="3";



    /**
     * 正常状态(未删除)
     */
    public static final Integer COMMON_STATE = 0;


    /**
     * 删除移除状态
     */
    public static final Integer DELETE_STATE = 1;






}
