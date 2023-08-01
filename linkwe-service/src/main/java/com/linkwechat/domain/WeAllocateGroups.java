package com.linkwechat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: 已分配群信息
 * @author: HaoN
 * @create: 2020-10-28 00:13
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeAllocateGroups extends BaseEntity {
    /**群id*/
    private String chatId;

    /**群名称*/
    private String groupName;

    /**离职成员主键*/
    private Long leaveUserId;

    /**群客户数量*/
    private Integer memberNum;

    /**群主所在部门*/
    private String department;

    /**分配时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date allocateTime;

    /**新群主名称*/
    private String newOwnerName;

    /**原群主id*/
    private String oldOwner;

    private String deptNames;

}
