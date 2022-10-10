package com.linkwechat.domain.storecode.vo.tab;

import lombok.Data;


/**
 * 门店群统计头部tab
 */
@Data
public class WeStoreGroupTabVo {
    //门店群活码扫码总次数
    private int totalStoreGroupScannNumber;

    //扫码进群总客数
    private int totalJoinGroupMemberNumber;

    //离群总客数
    private int totalExitGroupMemberNumber;


    //今日门店群活码扫码总次数
    private int tdStoreGroupScannNumber;


    //今日扫码进群总客数
    private int tdJoinGroupMemberNumber;


    //今日离群总客数
    private int tdExitGroupMemberNumber;

    //昨日门店群活码扫码总次数
    private int ydStoreGroupScannNumber;

    //昨日扫码进群总客数
    private int ydJoinGroupMemberNumber;

    //昨日离群总客数
    private int ydExitGroupMemberNumber;


}
