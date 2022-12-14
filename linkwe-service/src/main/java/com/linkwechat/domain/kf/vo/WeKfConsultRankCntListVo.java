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
public class WeKfConsultRankCntListVo {

    @ApiModelProperty("回复客户总数")
    private List<WeKfConsultRankCntVo> reply;

    @ApiModelProperty("平均响应时长")
    private List<WeKfConsultRankCntVo> avgReplyDuration;
}
