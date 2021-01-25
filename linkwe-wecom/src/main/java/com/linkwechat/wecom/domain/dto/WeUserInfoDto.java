package com.linkwechat.wecom.domain.dto;

import lombok.Data;

/**
 * @description: 用户身份
 * @author: HaoN
 * @create: 2021-01-20 00:11
 **/
@Data
public class WeUserInfoDto extends WeResultDto{


    /**
     * a) 当用户为企业成员时返回字段
     */

    //成员UserID。若需要获得用户详情信息，可调用通讯录接口：读取成员。如果是互联企业或者企业互联，则返回的UserId格式如：CorpId/userid
    private String UserId;


    /**
     * b) 非企业成员时返回字段
     */

    //非企业成员的标识，对当前企业唯一。不超过64字节
    private String OpenId;


    //外部联系人id，当且仅当用户是企业的客户，且跟进人在应用的可见范围内时返回。如果是第三方应用调用，针对同一个客户，同一个服务商不同应用获取到的id相同
    private String external_userid;




    //手机设备号(由企业微信在安装时随机生成，删除重装会改变，升级不受影响)
    private String DeviceId;

}
