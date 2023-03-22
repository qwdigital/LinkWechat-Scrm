package com.linkwechat.domain.groupmsg.vo;

import lombok.Data;

import java.util.Date;

/**
 * 群发执行成员条件
 */
@Data
public class WeGroupMessageExecuteUsertipVo {
   //执行成员id，多个使用逗号隔开(或群主id)
    private String weUserIds;
    //执行成员名称多个使用逗号隔开(或群主名)
    private String weUserName;
    //性别
    private Integer gender;
    //多个标签id使用逗号隔开
    private String tagIds;
    //多个标签名使用逗号隔开
    private String tagNames;
    //添加开始时间
    private Date beginTime;
    //添加结束时间
    private Date endTime;
    //跟进状态
    private Integer trackState;



}
