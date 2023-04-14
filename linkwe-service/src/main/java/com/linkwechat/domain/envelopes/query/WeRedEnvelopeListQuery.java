package com.linkwechat.domain.envelopes.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @description 红包列表入参
 * @date 2023/3/21 10:48
 **/

@ApiModel
@Data
public class WeRedEnvelopeListQuery {


    @ApiModelProperty("场景类型 1-客户 2-客群 3-客户与客群 (多选逗号相隔)")
    private String sceneType;

    @ApiModelProperty("红包名称")
    private String name;

    @ApiModelProperty("状态 0-启用 1-停用")
    private Integer status;

    @ApiModelProperty("红包类型 0-企业红包 1-个人红包")
    private Integer redEnvelopesType;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("结束时间")
    private Date endTime;
}

