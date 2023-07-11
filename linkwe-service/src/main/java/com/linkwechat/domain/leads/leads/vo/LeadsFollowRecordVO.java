package com.linkwechat.domain.leads.leads.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 线索跟进表(LeadsFollowRecord)表VO
 *
 * @author zhaoyijie
 * @since 2023-05-17 13:49:05
 */
@Data
public class LeadsFollowRecordVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "跟进记录ID")
    private Long followId;

    @ApiModelProperty(value = "线索ID")
    private Long leadsId;

    @ApiModelProperty(value = "跟进人ID")
    private Long followUserId;

    @ApiModelProperty(value = "跟进时间")
    private Date followTime;

    @ApiModelProperty(value = "当前版本第几次跟进")
    private Integer followSort;

    @ApiModelProperty(value = "是否是最新版本跟进")
    private Integer isRecent;

    @ApiModelProperty(value = "分配记录的版本，发生线索退回|线索转接，版本+1")
    private Integer followVersion;

}

