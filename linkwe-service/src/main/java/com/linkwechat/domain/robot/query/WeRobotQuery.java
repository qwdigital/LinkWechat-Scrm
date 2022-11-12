package com.linkwechat.domain.robot.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author danmo 新增机器人入参
 * @date 2022年11月09日 11:25
 */
@ApiModel
@Data
public class WeRobotQuery {

    @ApiModelProperty("群名称")
    private String groupName;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;
}
