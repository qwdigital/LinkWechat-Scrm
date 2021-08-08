package com.linkwechat.wecom.domain.vo;

import lombok.experimental.SuperBuilder;
import lombok.Data;

/**
 * @description: 用户身份相关
 * @author: HaoN
 * @create: 2021-01-20 00:21
 **/
@Data
@SuperBuilder
public class WeUserInfoVo {


    /**
     * a) 当用户为企业成员时返回字段
     */
    private String userId;


    /**
     * b) 非企业成员时返回字段
     */

    //非企业成员的标识，对当前企业唯一。不超过64字节
    private String openId;


    //外部联系人id，当且仅当用户是企业的客户，且跟进人在应用的可见范围内时返回。如果是第三方应用调用，针对同一个客户，同一个服务商不同应用获取到的id相同
    private String externalUserId;


    //手机设备号(由企业微信在安装时随机生成，删除重装会改变，升级不受影响)
    private String deviceId;
}
