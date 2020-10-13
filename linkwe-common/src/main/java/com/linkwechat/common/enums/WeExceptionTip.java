package com.linkwechat.common.enums;

/**
 * 企业微信接口错误码提示
 */
public enum WeExceptionTip {

    WE_EXCEPTION_TIP_LOSS_1(-1,"系统繁忙","服务器暂不可用，建议稍候重试。建议重试次数不超过3次。"),
    WE_EXCEPTION_TIP_40001(40001,"不合法的secret参数","secret在应用详情/通讯录管理助手可查看"),
    WE_EXCEPTION_TIP_40003(40003,"无效的UserID","1）有效的UserID需要满足：长度1~64字符，由英文字母、数字、中划线、下划线以及点号构成。2）除了创建用户，其余使用UserID的接口，还要保证UserID必须在通讯录中存在。"),
    WE_EXCEPTION_TIP_40004(40004,"不合法的媒体文件类型","不满足系统文件要求【图片（image）、语音（voice）、视频（video），普通文件（file）】。"),
    WE_EXCEPTION_TIP_40005(40005,"不合法的type参数","合法的type类型【媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件（file）】"),
    WE_EXCEPTION_TIP_40006(40006,"",""),
    WE_EXCEPTION_TIP_40007(40007,"",""),
    WE_EXCEPTION_TIP_40008(40008,"",""),
    WE_EXCEPTION_TIP_40009(40009,"",""),
    WE_EXCEPTION_TIP_40011(40011,"",""),
    WE_EXCEPTION_TIP_40013(40013,"",""),
    WE_EXCEPTION_TIP_40014(40014,"",""),
    WE_EXCEPTION_TIP_40016(40016,"",""),
    WE_EXCEPTION_TIP_40017(40017,"",""),
    WE_EXCEPTION_TIP_40018(40018,"",""),
    WE_EXCEPTION_TIP_40019(40019,"","");



//    CUSTOMER_SOURCE_WZLY(0,""),
//    CUSTOMER_SOURCE_SMEWM(1,),
//    CUSTOMER_SOURCE_SSSJH(2,),
//    CUSTOMER_SOURCE_MPFX(3,),
//    CUSTOMER_SOURCE_QL(4,),
//    CUSTOMER_SOURCE_SJTXL(5,),
//    CUSTOMER_SOURCE_WXLXR(6,),
//    CUSTOMER_SOURCE_TJHYSQ(7,),
//    CUSTOMER_SOURCE_DSFRJTJ(8,),
//    CUSTOMER_SOURCE_YXSS(9,),
//    CUSTOMER_SOURCE_NBCYGX(201,),
//    CUSTOMER_SOURCE_GLRYFP(202,)
//    ;
//
    private Integer code;

    private String tipMsg;

    private String solveTipMsg;

    private WeExceptionTip(Integer code, String tipMsg,String solveTipMsg){
        this.code = code;
        this.tipMsg = tipMsg;
        this.solveTipMsg=solveTipMsg;
    }
//
//    public Integer getCode() {
//        return code;
//    }
//
//    public OrderType setCode(Integer code) {
//        this.code = code;
//        return this;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public OrderType setLabel(String label) {
//        this.label = label;
//        return this;
//    }
//
//    public OrderType getOrderTypeByCode(Integer code){
//        OrderType[] orderTypes = OrderType.values();
//        for(OrderType orderType:orderTypes){
//            if(orderType.getCode().equals(code)){
//                return orderType;
//            }
//        }
//        throw new RuntimeException("未知的订单类型");
//    }
}
