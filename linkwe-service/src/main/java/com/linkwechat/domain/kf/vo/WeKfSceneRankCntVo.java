package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客服客户场景排行数据
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeKfSceneRankCntVo {

    @ApiModelProperty("访问客户总数")
    private Integer total;

    @ApiModelProperty("场景值")
    private String scene;

    @ApiModelProperty("场景名称")
    private String sceneName;
}
