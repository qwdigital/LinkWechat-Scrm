package com.linkwechat.domain.robot.query;

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
public class WeRobotMsgListQuery {

    @ApiModelProperty("机器人ID")
    private Long robotId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("消息状态：0-草稿 1-待发送 2-已发送 3-发送失败")
    private Integer status;

    @ApiModelProperty("发送开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("发送结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
