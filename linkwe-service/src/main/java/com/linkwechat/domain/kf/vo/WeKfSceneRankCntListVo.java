package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 客服客户场景排行数据
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeKfSceneRankCntListVo {

    @ApiModelProperty("访问排行")
    private List<WeKfSceneRankCntVo> visit;

    @ApiModelProperty("咨询排行")
    private List<WeKfSceneRankCntVo> consult;
}
