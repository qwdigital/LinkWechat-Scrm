package com.linkwechat.domain.leads.leads.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 线索退回表(LeadsReturnRecord)表VO
 *
 * @author zhaoyijie
 * @since 2023-05-17 13:49:06
 */
@Data
public class LeadsReturnRecordVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "线索ID")
    private Long leadsId;

    @ApiModelProperty(value = "退回人ID，系统自动退回设置0")
    private Long returnUserId;

    @ApiModelProperty(value = "退回时间")
    private Date returnTime;

    @ApiModelProperty(value = "退回原因")
    private String returnReason;

    @ApiModelProperty(value = "是否最新一次退回")
    private Integer isRecent;

    @ApiModelProperty(value = "第几次退回")
    private Integer returnSort;

}

