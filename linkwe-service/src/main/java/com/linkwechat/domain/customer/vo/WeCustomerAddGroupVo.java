package com.linkwechat.domain.customer.vo;

import lombok.Data;

import java.util.Date;


/**
 * @description:
 * @author: HaoN
 * @create: 2021-03-08 17:04
 **/
@Data
public class WeCustomerAddGroupVo {
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


    //入群方式入群方式。
    //1 - 由群成员邀请入群（直接邀请入群）
    //2 - 由群成员邀请入群（通过邀请链接入群）
    //3 - 通过扫描群二维码入群
    private Integer joinScene;
}
