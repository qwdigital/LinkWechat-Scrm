package com.linkwechat.common.constant;

/**
 * 安全相关通用常量
 *
 * @author xueyi
 */
public class SecurityConstants {

    /** 请求来源 */
    public static final String FROM_SOURCE = "from-source";

    /** 授权信息 */
    public static final String AUTHORIZATION_HEADER = "authorization";

    /** 企业Id */
    public static final String CORP_ID = "corp_id";

    /** 企业账号 */
    public static final String CORP_NAME = "corp_name";

    /** 租户ID */
    public static final String TENANT_ID = "tenant_id";

    /** 用户Id */
    public static final String USER_ID = "user_id";

    /** 用户账号 */
    public static final String USER_NAME = "user_name";

    /** 用户类型 */
    public static final String USER_TYPE = "user_type";

    /** 用户标识 */
    public static final String USER_KEY = "user_key";

    /** 登录用户 */
    public static final String LOGIN_USER = "login_user";

    /** 登录类型 **/
    public static final String LOGIN_TYPE = "login_type";

    /** 内部请求 */
    public static final String INNER = "inner";

    /**公众号id*/
    public static final String WE_APP_ID="weAppId";

    /**公众号密钥*/
    public static final String WE_APP_SECRET="weAppSecret";

    /**Feign请求标识*/
    public static final String IS_FEGIN="isFeign";


    /** 授权字段 */
    public enum Details {

        CORP_ID("corp_id", "企业Id"),
        TENANT_ID("tenant_id", "租户Id"),
        CORP_NAME("corp_name", "企业账号"),
        IS_LESSOR("is_lessor", "企业类型"),
        USER_ID("user_id", "用户Id"),
        USER_NAME("user_name", "用户账号"),
        USER_TYPE("user_type", "用户类型"),
        USER_KEY("user_key", "用户标识"),
        LOGIN_USER("login_user", "登录用户");

        private final String code;
        private final String info;

        Details(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }
}