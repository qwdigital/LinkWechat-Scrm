package com.linkwechat.domain.material.vo;

import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class WeContentCountVo {
    @ApiModelProperty(value = "发送总次数")
    private Integer sendTotalNum = 0;

    @ApiModelProperty(value = "今日发送次数")
    private Integer sendTodayNum = 0;

    @ApiModelProperty(value = "查看总次数")
    private Integer viewTotalNum;

    @ApiModelProperty(value = "查看总人数")
    private Integer viewByTotalNum;

    @ApiModelProperty(value = "今日查看总次数")
    private Integer viewTotalTodayNum;

    @ApiModelProperty(value = "今日查看总人数")
    private Integer viewByTotalTodayNum;

    @ApiModelProperty(value = "发送次数")
    private List<ContentAxisVo> contentAxisVoList;


}
