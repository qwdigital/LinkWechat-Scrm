package com.linkwechat.domain.wx.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author danmo
 * @date 2023年03月15日 14:18
 */
@ApiModel
@Data
public class WxCouponListQuery {

    @ApiModelProperty(value = "分页页码,页码从0开始，默认第0页。", example = "0")
    private Integer offset;

    @ApiModelProperty(value = "分页大小,分页大小，最大10。", example = "10")
    private Integer limit;

    @ApiModelProperty(value = "创建批次的商户号",hidden = true)
    private String stockCreatorMchid;

    @ApiModelProperty(value = "起始时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createStartTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createEndTime;

    @ApiModelProperty(value = "批次状态 unactivated-未激活 audit-审核中 running-运行中 stoped-已停止 paused-暂停发放", example = "paused")
    private String status;
}
