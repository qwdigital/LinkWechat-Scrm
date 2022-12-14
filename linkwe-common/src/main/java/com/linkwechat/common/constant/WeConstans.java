package com.linkwechat.common.constant;


import lombok.Getter;

/**
 * @description: 企业微信相关常量
 * @author: HaoN
 * @create: 2020-08-26 17:01
 **/
public class WeConstans {

    /**
     * 微信授权token
     */
    public static final String WX_AUTH_ACCESS_TOKEN = "wx_auth_access_token";

    public static final String WX_AUTH_REFRESH_ACCESS_TOKEN = "wx_auth_refresh_access_token";

    /**
     * 微信通用token
     */
    public static final String WX_ACCESS_TOKEN = "wx_access_token";

    /**
     * 企微应用token
     */
    public static final String WE_COMMON_ACCESS_TOKEN = "we_common_access_token:{}";

    public static final String WE_AGENT_ACCESS_TOKEN = "we_agent_access_token:{}:{}";

    /**
     * 获取外部联系人相关 token
     */
    public static final String WE_CONTACT_ACCESS_TOKEN = "we_contact_access_token:{}";

    public static final String WE_ADDRESS_BOOK_ACCESS_TOKEN = "we_address_book_access_token:{}";

    /**
     * 供应商相关token
     */
    public static final String WE_PROVIDER_ACCESS_TOKEN = "we_provider_access_token:{}";

    /**
     * 会话存档相关token
     */
    public static final String WE_CHAT_ACCESS_TOKEN = "we_chat_access_token:{}";

    /**
     * 客服token
     */
    public static final String WE_KF_ACCESS_TOKEN = "we_kf_access_token:{}";

    /**
     * 直播token
     */
    public static final String WE_LIVE_ACCESS_TOKEN = "we_live_access_token:{}";

    /**
     * 对外收款token
     */
    public static final String WE_BILL_ACCESS_TOKEN = "we_bill_access_token:{}";


    /**
     * 企业微信接口返回成功code
     */
    public static final Integer WE_SUCCESS_CODE = 0;


    /**
     * 企业微信端根部门id
     */
    public static final Long WE_ROOT_DEPARMENT_ID = 1L;



    /**
     * 同步功能提示语
     */
    public static final String SYNCH_TIP = "后台开始同步数据，请稍后关注进度";


    /**
     * 离职未分配
     */
    public static final Integer LEAVE_NO_ALLOCATE_STATE = 0;


    /**
     * 离职已分配分配
     */
    public static final Integer LEAVE_ALLOCATE_STATE = 1;


    /**
     * 单人活码
     */
    public static final Integer SINGLE_EMPLE_CODE_TYPE = 1;


    /**
     * 多人活码
     */
    public static final Integer MANY_EMPLE_CODE_TYPE = 2;


    /**
     * 批量单人活码
     */
    public static final Integer BATCH_SINGLE_EMPLE_CODE_TYPE = 3;


    /**
     * 在小程序中联系场景
     */
    public static final Integer SMALL_ROUTINE_EMPLE_CODE_SCENE = 1;


    /**
     * 通过二维码联系场景
     */
    public static final Integer QR_CODE_EMPLE_CODE_SCENE = 2;


    /**
     * 不存在外部联系人的关系
     */
    public static final Integer NOT_EXIST_CONTACT = 84061;

    public static final String COMMA = ",";


    /**
     * 业务id类型1:组织机构id,2:成员id
     */
    public static final Integer USE_SCOP_BUSINESSID_TYPE_USER = 2;
    public static final Integer USE_SCOP_BUSINESSID_TYPE_ORG = 1;
    public static final Integer USE_SCOP_BUSINESSID_TYPE_ALL = 3;

    /**
     * 客户流失通知开关 0:关闭 1:开启
     */
    public static final String DEL_FOLLOW_USER_SWITCH_CLOSE = "0";
    public static final String DEL_FOLLOW_USER_SWITCH_OPEN = "1";

    public static final String CONTACT_SEQ_KEY = "seq";


    /**
     * 任务裂变用户活码state前缀
     */
    public static final String FISSION_PREFIX = "fis-";


    @Getter
    public enum sendMessageStatusEnum {
        NOT_SEND("0", "未发送"),
        SEND("1", "已发送"),
        NOT_FRIEND_SEND("2", "因客户不是好友导致发送失败"),
        RECEIVE_OTHER_MESSAGE("3", "-因客户已经收到其他群发消息导致发送失败"),
        ;

        private String status;
        private String desc;

        /**
         * 构造方法
         *
         * @param status
         * @param desc
         */
        sendMessageStatusEnum(String status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    /**
     * 活码前缀
     */
    public static final String WE_QR_CODE_PREFIX = "we_qr";


    /**
     * 门店活码前缀
     */
    public static final String WE_STORE_CODE_PREFIX="we_sc";


    /**
     * 门店导购员或群前缀
     */
    public static final String WE_STORE_CODE_CONFIG_PREFIX="we_sc_conf";


    /**
     * 新客拉群
     */
    public static final String WE_QR_XKLQ_PREFIX = "we_xklq";


    /**
     * 客服账号信息
     */
    public static final String KF_ACCOUNT_SET_UP_KEY = "we:kf:account:info:{}:{}";
    /**
     * 会话变更msgCode
     */
    public static final String KF_SESSION_MSG_CODE_KEY = "we:kf:session:msg:code:{}:{}:{}";

    /**
     * 客服接待人员轮询分配标识
     */
    public static final String KF_SERVICER_POLLING_KEY = "we:kf:session:servicer:polling:{}:{}";

    /**
     * 二维码地址
     */
    public static final String JOINCORPQR="joinCorpQr";

    /**
     * 二维码有效时间（默认6天）
     */
    public static final Integer JOINCORPQR_EFFETC_TIME=8640;

    //性别，1表示男性，2表示女性
    //表示所在部门是否为上级，0-否，1-是，顺序与Department字段的部门逐一对应
    //激活状态：1=已激活 2=已禁用 4=未激活 已激活代表已激活企业微信或已关注微工作台（原企业号） 5=成员退出
    public static enum corpUserEnum {

       NO_IS_ALLOCATE(0,"未分配"),
       YES_IS_ALLOCATE(1,"已分配");

        private int key;
        private String value;

        /**
         * 构造方法
         *
         * @param key
         * @param value
         */
        corpUserEnum(int key, String value) {
            this.setKey(key);
            this.setValue(value);
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * id类型 0:成员 1:客户,2:机器
     */
    public static final Integer ID_TYPE_USER = 0;
    public static final Integer ID_TYPE_EX = 1;
    public static final Integer ID_TYPE_MACHINE = 2;
}
