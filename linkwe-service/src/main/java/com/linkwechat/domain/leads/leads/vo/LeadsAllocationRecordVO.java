package com.linkwechat.domain.leads.leads.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 线索分配表(LeadsAllocationRecord)表VO
 *
 * @author zhaoyijie
 * @since 2023-05-17 13:49:05
 */
@Data
public class LeadsAllocationRecordVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "线索ID")
    private Long leadsId;

    @ApiModelProperty(value = "分配人ID")
    private Long allocateUserId;

    @ApiModelProperty(value = "分配时间")
    private Date allocateTime;

    @ApiModelProperty(value = "接收人ID")
    private Long receivedUserId;

    @ApiModelProperty(value = "是否最新一次分配")
    private Integer isRecent;

    @ApiModelProperty(value = "第几次分配")
    private Integer allocateSort;

    @ApiModelProperty(value = "分配记录的版本，发生线索退回|线索转接，版本+1")
    private Integer allocateVersion;

}

