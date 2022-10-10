package com.linkwechat.domain.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客户单聊总数
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeSessionUserAvgReplyTimeRankVo {

    @ApiModelProperty("员工名称")
    private String userName;

    @ApiModelProperty("单聊总数")
    private Double avgReplyTime;

}
