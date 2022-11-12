package com.linkwechat.domain.strategic.crowd.query;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.strategic.crowd.WeStrategicCrowdSwipe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author danmo
 * @description 策略人群新增入参
 * @date 2021/11/7 13:49
 **/
@ApiModel
@Data
public class WeAddStrategicCrowdQuery {

    @ApiModelProperty("Id")
    private Long id;

    @ApiModelProperty("名称")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @ApiModelProperty("分组ID")
    @NotNull(message = "分组ID不能为空")
    private Long groupId;

    @ApiModelProperty("更新方式 1：手动 2：自动")
    private Integer type = 1;

    @ApiModelProperty("筛选条件")
    private List<WeStrategicCrowdSwipe> swipe;

    public String getSwipe2Str() {
        return JSONObject.toJSONString(swipe);
    }
}
