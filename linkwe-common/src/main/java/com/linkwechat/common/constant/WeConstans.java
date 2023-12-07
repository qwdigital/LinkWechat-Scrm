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
     * 微信公众号通用token
     */
    public static final String WX_ACCESS_TOKEN = "wx_access_token";

    public static final String WX_COMMON_ACCESS_TOKEN = "wx_common_access_token:{}:{}";

    /**
     * 微信小程序通用token
     */
    public static final String WX_APPLET_ACCESS_TOKEN = "wx_applet_access_token";

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
     * 同步功能提示语
     */
    public static final String SYNCH_TIP = "后台开始同步数据，请稍后关注进度";



    /**
     * 单人活码
     */
    public static final Integer SINGLE_EMPLE_CODE_TYPE = 1;



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

    /**
     * 客户流失通知开关 0:关闭 1:开启
     */
    public static final String DEL_FOLLOW_USER_SWITCH_CLOSE = "0";
    public static final String DEL_FOLLOW_USER_SWITCH_OPEN = "1";



    /**
     * 任务裂变用户活码state前缀
     */
    public static final String FISSION_PREFIX = "fis-";


    /**
     * 裂变-任务宝
     */
    public static final String FISSION_PREFIX_RWB="fis-rwb-";


    /**
     * 裂变-客服
     */
    public static final String FISSION_CUSTOMER_SERVICE="fis-kf-";


    /**
     * 裂变-群裂变
     */
    public static final String FISSION_PREFIX_QLB="fis-qlb-";


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
     * 识客码前缀
     */
    public static final String WE_KNOW_CUSTOMER_CODE_PREFIX="we_kn_code";


    /**
     * 新客拉群
     */
    public static final String WE_QR_XKLQ_PREFIX = "we_xklq";




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



    //短链统计缓存key值前缀
    public static final String  WE_SHORT_LINK_KEY = "we_short_link:statistics:";

    //通用短链统计缓存key前缀
    public static final String WE_SHORT_LINK_COMMON_KEY = "we_short_link:common:statistics:";

    /**
     * 短链缓存命名空间
     */
    public static final String PV = "pv:";

    /**
     * 短链缓存命名空间
     */
    public static final String UV = "uv:";

    /**
     * 短链缓存命名空间
     */
    public static final String OPEN_APPLET = "open:";

    /**
     * 短链推广统计缓存key前缀
     */
    public static final String WE_SHORT_LINK_PROMOTION_KEY = "we_short_link_promotion:statistics:";
}
