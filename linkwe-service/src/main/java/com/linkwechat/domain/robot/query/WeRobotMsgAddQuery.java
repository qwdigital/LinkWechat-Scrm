package com.linkwechat.domain.robot.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @date 2022年11月04日 22:33
 */
@ApiModel
@Data
public class WeRobotMsgAddQuery {

    /**
     * 消息标题
     */
    @NotBlank(message = "消息标题不能为空")
    @ApiModelProperty(value = "消息标题")
    private String msgTitle;

    @NotNull(message = "机器人ID不能为空")
    @ApiModelProperty(value = "机器人ID")
    private Long robotId;

    @NotNull(message = "消息不能为空")
    @ApiModelProperty(value = "消息体")
    private WeMessageTemplate weMessageTemplate;
}
