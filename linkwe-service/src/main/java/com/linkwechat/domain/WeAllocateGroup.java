package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 分配的群租对象 we_allocate_group
 *
 * @author ruoyi
 * @date 2020-10-24
 */
@Data
@TableName("we_allocate_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeAllocateGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    @TableId
    private Long id;

    /** 分配的群id */
    private String chatId;

    /**离职成员主键*/
    private Long leaveUserId;

    /**群名称*/
    private String chatName;
    /**接替员工名称*/
    private String takeoverName;
    /**接替员工部门名称*/
    private String takeoverDeptName;

    /** 新群主 */
    private String newOwner;

    /** 原群主 */
    private String oldOwner;

    /** 分配时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date allocateTime;

    //1:等待接替 2:接替中(等待微信接替) 3:接替成功 4:接替失败
    private Integer status;


    private String errMsg;


}

