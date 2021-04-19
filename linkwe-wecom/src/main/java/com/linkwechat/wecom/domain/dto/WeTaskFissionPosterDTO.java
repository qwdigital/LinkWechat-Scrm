package com.linkwechat.wecom.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/27 16:42
 */
@ApiModel
@Data
public class WeTaskFissionPosterDTO {
    @ApiModelProperty("客户id")
    private String unionId;

    @ApiModelProperty("任务id")
    private Long taskFissionId;

    @ApiModelProperty("裂变目标id")
    private String fissionTargetId;

    @ApiModelProperty("海报id")
    private Long posterId;
}
