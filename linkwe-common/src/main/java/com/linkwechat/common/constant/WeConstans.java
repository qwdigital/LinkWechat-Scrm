package com.linkwechat.common.constant;

/**
 * @description: 企业微信相关常量
 * @author: HaoN
 * @create: 2020-08-26 17:01
 **/
public class WeConstans {

    /**
     * 企业微信相关token
     */
    public static final String WE_COMMON_ACCESS_TOKEN = "we_common_access_token";


    /**
     * 获取外部联系人相关 token
     */
    public static final String WE_CONTACT_ACCESS_TOKEN = "we_contact_access_token";


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
    public static final Integer LEAVE_NO_ALLOCATE_STATE = 5;

    /**
     * 离职已分配分配
     */
    public static final Integer LEAVE_ALLOCATE_STATE = 6;


    /**
     * 企业微信素材目录根id
     */
    public static final Integer WE_ROOT_CATEGORY_ID = 0;


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
    public static final Integer SMALL_ROUTINE_EMPLE_CODE_SCENE=1;


    /**
     * 通过二维码联系场景
     */
    public static final Integer QR_CODE_EMPLE_CODE_SCENE=2;


    /**
     * 微信接口相应端错误字段
     */
    public static  final String WE_ERROR_FIELD="errcode";

}
