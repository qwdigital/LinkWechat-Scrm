package com.linkwechat.domain.agent.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author danmo
 * @date 2022年11月04日 22:33
 */
@ApiModel
@Data
public class WeAgentMsgQuery {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("发送开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("发送结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
