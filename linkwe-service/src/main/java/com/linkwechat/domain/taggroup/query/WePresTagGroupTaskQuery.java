package com.linkwechat.domain.taggroup.query;

import lombok.Data;

@Data
public class WePresTagGroupTaskQuery {

    /**
     * 主键
     */
    private String id;
    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 添加客户开始时间
     */
    private String startAddTime;


    /**
     * 添加客户结束时间
     */
    private String endAddTime;



    /**
     * 是否进群，1:是 0:否
     */
    private Integer isJoinGroup;


    /**
     * 入群开始时间
     */
    private String startJoinTime;


    /**
     * 入群结束时间
     */
    private String endJoinTime;


    /**
     * 群id
     */
    private String chatId;


    /**
     * 客户id
     */
    private String externalUserid;


    /**
     * 渠道标识
     */
    private String state;
}
