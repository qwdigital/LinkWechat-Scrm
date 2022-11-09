package com.linkwechat.domain.robot.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author danmo 新增机器人入参
 * @date 2022年11月09日 11:25
 */
@ApiModel
@Data
public class WeRobotAddQuery {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty("群名称")
    private String groupName;

    @NotBlank(message = "群机器人链接不能为空")
    @ApiModelProperty("群机器人链接")
    private String webHookUrl;
}
