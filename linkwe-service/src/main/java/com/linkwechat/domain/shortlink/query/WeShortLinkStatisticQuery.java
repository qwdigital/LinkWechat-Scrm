package com.linkwechat.domain.shortlink.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @description 短链新增入参
 * @date 2022/12/19 13:49
 **/
@ApiModel
@Data
public class WeShortLinkStatisticQuery {

    @ApiModelProperty(value = "主键id")
    private Long id;


    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date beginTime;

    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;
}
