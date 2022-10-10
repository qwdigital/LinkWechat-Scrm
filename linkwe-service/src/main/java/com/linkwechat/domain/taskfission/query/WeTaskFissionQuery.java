package com.linkwechat.domain.taskfission.query;

import com.linkwechat.domain.wx.WxBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @version 1.0
 * @date 2022/6/27 16:42
 */
@ApiModel
@Data
public class WeTaskFissionQuery extends WxBaseQuery {

    @ApiModelProperty("员工ID")
    private String userId;

    @ApiModelProperty("任务id")
    private Long taskFissionId;

    @ApiModelProperty("裂变任务id")
    private Long recordId;

    @ApiModelProperty("目标id")
    private String targetId;
}
