package com.linkwechat.wecom.domain.dto.customer;

import lombok.Data;

import java.util.Date;

/**
 * @description: 客户群成员
 * @author: HaoN
 * @create: 2020-10-20 22:37
 **/
@Data
public class CustomerGroupMember {
    /**群成员id*/
    private String userid;

    /**1 - 企业成员;2 - 外部联系人*/
    private Integer type;

    /**入群时间*/
    private long join_time;

    /**1 - 由成员邀请入群（直接邀请入群）;2 - 由成员邀请入群（通过邀请链接入群）;3 - 通过扫描群二维码入群*/
    private Integer join_scene;

    /**外部联系人在微信开放平台的唯一身份标识（微信unionid）;通过此字段企业可将外部联系人与公众号/小程序用户关联起来。*/
    private String unionid;
}
