package com.linkwechat.domain.sop.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * sop统计企业群列表
 */
@Data
public class WeSopDetailGroupVo {

    //群聊名称
    private String groupName;

    //SOP状态
    private Integer executeState;

    private String  executeTargetId;


    //客群成员id
    private String weUserId;

    //客群成员名称
    private String userName;

    //职位
    private String position;

    //所属部门
    private String deptName;

    //进入SOP时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //结束SOP时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date executeEndTime;

    //sop主键
    private String sopBaseId;

    //SOP执行效率
    private String efficiency;


    private String chatId;


}
