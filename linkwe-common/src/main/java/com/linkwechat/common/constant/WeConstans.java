package com.linkwechat.common.constant;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

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
     * 企业微信相关token
     */
    public static final String WE_COMMON_ACCESS_TOKEN = "we_common_access_token";


    /**
     * 自建应用token
     */
    public static final String WE_THIRD_APP_TOKEN = "we_third_app_token";


    /**
     * 获取外部联系人相关 token
     */
    public static final String WE_CONTACT_ACCESS_TOKEN = "we_contact_access_token";


    /**
     * 供应商相关token
     */
    public static final String WE_PROVIDER_ACCESS_TOKEN = "we_provider_access_token";

    /**
     * 会话存档相关token
     */
    public static final String WE_CHAT_ACCESS_TOKEN = "we_chat_access_token";

    /**
     * 应用相关token
     */
    public static final String WE_AGENT_ACCESS_TOKEN = "we_agent_access_token";


    public static final String WE_EMPLE_CODE_KEY = "we_emple_code_key";

    /**
     * 活码前缀
     */
    public static final String WE_QR_CODE_PREFIX = "we_qr";
    /**
     * 新客拉群
     */
    public static final String WE_QR_XKLQ_PREFIX = "we_xklq";


    /**
     * 企业微信接口返回成功code
     */
    public static final Integer WE_SUCCESS_CODE = 0;


    /**
     * 企业微信端根部门id
     */
    public static final Long WE_ROOT_DEPARMENT_ID = 1L;


    /**
     * 企业微信通讯录用户启用
     */
    public static final Integer WE_USER_START = 1;


    /**
     * 企业微信通讯录用户停用
     */
    public static final Integer WE_USER_STOP = 0;


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
     * 已激活
     */
    public static final Integer WE_USER_IS_ACTIVATE = 1;


    /**
     * 已禁用
     */
    public static final Integer WE_USER_IS_FORBIDDEN = 2;


    /**
     * 已离职
     */
    public static final Integer WE_USER_IS_LEAVE = 5;


    /**
     * 未激活
     */
    public static final Integer WE_USER_IS_NO_ACTIVATE = 4;


    /**
     * 企业微信素材目录根id
     */
    public static final Integer WE_ROOT_CATEGORY_ID = 0;

    /**
     * 实际群活码正在使用中
     */
    public static final Integer WE_GROUP_CODE_ENABLE = 0;

    /**
     * 群活码已禁用/达到扫码次数上限
     */
    public static final Integer WE_GROUP_CODE_DISABLE = 2;


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
     * 客户添加时无需经过确认自动成为好友,是
     */
    public static final Boolean IS_JOIN_CONFIR_MFRIENDS = true;

    /**
     * 客户添加时无需经过确认自动成为好友,否
     */
    public static final Boolean NOT_IS_JOIN_CONFIR_MFRIENDS = false;

    /**
     * 批量生成的单人码 活动场景
     */
    public static final String ONE_PERSON_CODE_GENERATED_BATCH = "批量生成的单人码";

    /**
     * 微信接口相应端错误字段
     */
    public static final String WE_ERROR_FIELD = "errcode";


    /**
     * 递归
     */
    public static final Integer YES_IS_RECURSION = 0;


    /**
     * 获取所有子部门数据
     */
    public static final Integer DEPARTMENT_SUB_WEUSER = 1;


    /**
     * 获取当前部门
     */
    public static final Integer DEPARTMENT_CURRENT_WEUSER = 0;


    /**
     * 通讯录用户激活
     */
    public static final Integer YES_IS_ACTIVATE = 1;


    /**
     * 通讯录用户未激活
     */
    public static final Integer NO_IS_ACTIVATE = 2;


    /**
     * 不存在外部联系人的关系
     */
    public static final Integer NOT_EXIST_CONTACT = 84061;

    public static final String COMMA = ",";

    public static final String USER_ID = "userid";

    public static final String CURSOR = "cursor";

    public static final String CORPID = "CORP_ID";

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
     * id类型 0:成员 1:客户,2:机器
     */
    public static final Integer ID_TYPE_USER = 0;
    public static final Integer ID_TYPE_EX = 1;
    public static final Integer ID_TYPE_MACHINE = 2;

    /**
     * 一次拉取的消息条数，最大值1000条，超过1000条会返回错误
     */
    public static final long LIMIT = 1_000L;

    /**
     * 敏感词过滤查询用户分片
     */
    public static final Integer SENSITIVE_USER_PIECE = 50;

    /**
     * 任务裂变用户活码state前缀
     */
    public static final String FISSION_PREFIX = "fis-";

    public static final String AppTicketKey = "ticket:AppGet";
    public static final String AgentTicketKey = "ticket:AgentGet";

    public static final String corpAccountKey = "we:corpAccount:{}";

    //性别，1表示男性，2表示女性
    //表示所在部门是否为上级，0-否，1-是，顺序与Department字段的部门逐一对应
    //激活状态：1=已激活 2=已禁用 4=未激活 已激活代表已激活企业微信或已关注微工作台（原企业号） 5=成员退出
    public static enum corpUserEnum {

        User_SEX_TYPE_MAN(1, "男性"),
        User_SEX_TYPE_WEMAN(2, "女性"),

        IS_DEPARTMENT_SUPERIOR_YES(1, "是"),
        IS_DEPARTMENT_SUPERIOR_NO(0, "否"),

        ACTIVE_STATE_ONE(1, "已激活"),
        ACTIVE_STATE_TWO(2, "已禁用"),
        ACTIVE_STATE_FOUR(4, "未激活"),
        ACTIVE_STATE_FIVE(5, "成员退出");

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


    //public static final String WECOM_FINANCE_INDEX = "finance";
    public static final String WECOM_FINANCE_INDEX = "finance_new";

    public static final String WECOM_SENSITIVE_HIT_INDEX = "sensitive";

    /**
     * 开启会话存档成员列表
     **/
    public static final String weMsgAuditKey = "wecom_msg_audit:user:ids";


    /**
     * 第三方应用ID，参数标实
     */
    public static final String THIRD_APP_PARAM_TIP = "agentId";

    public static final String WECUSTOMERS_KEY = "weCustomer";

    /**
     * 发给客户
     */
    public static final String SEND_MESSAGE_CUSTOMER = "0";

    /**
     * 发给客户群
     */
    public static final String SEND_MESSAGE_GROUP = "1";

    /**
     * 消息范围 1 指定客户
     */
    public static final String SEND_MESSAGE_CUSTOMER_ALL = "0";

    /**
     * 消息范围 1 指定客户
     */
    public static final String SEND_MESSAGE_CUSTOMER_PART = "1";

    /**
     * 会话消息订阅的频道
     */
    public static final String CONVERSATION_MSG_CHANNEL = "CONVERSATION_MSG_CHANNEL";


    /**
     * 群发消息任务key
     */
    public static final String WEGROUPMSGTIMEDTASK_KEY = "weGroupMessageTimedTask";
    /**
     * 群发消息任务开关
     */
    public static Boolean WEGROUPMSGTIMEDTASK_SWITCH = true;

    /**
     * 老客标签建群企业群发消息模板
     */
    public static final String MSG_TEMPLATE = "你有一个新社群运营任务，<a href='%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect'>请点击此链接查看详情</a>";

}
