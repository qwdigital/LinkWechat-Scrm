package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.AllArgsConstructor;
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
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeAllocateGroup
{
    private static final long serialVersionUID = 1L;


    /** 分配的群id */
    private String chatId;

    /** 新群主 */
    private String newOwner;

    /** 原群主 */
    private String oldOwner;

    /** 分配时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date allocateTime;

    //0:被接替成功 1:待接替 2:接替失败 3:正常状态
    private Integer status;


    private String errMsg;


}
