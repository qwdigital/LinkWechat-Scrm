package com.linkwechat.wecom.domain;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: HaoN
 * @create: 2021-03-08 17:04
 **/
@Data
public class WeCustomerAddGroup {
    //群id
    private String chatId;
    //群名
    private String groupName;
    //群主名
    private String ownerName;
    //群成员数
    private Integer groupMemberNum;
    //加入时间
    private Date joinTime;

    //是否共同群 0:不是共同群；1:是共同群
    private Integer commonGroup;

}
