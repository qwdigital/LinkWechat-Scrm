package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2022年11月21日 13:59
 */
@Data
@ApiModel
public class WeKfEvaluationVo {

    @ApiModelProperty("评价类型 101-好评 102-一般 103-差评")
    private String evaluationType;

    @ApiModelProperty("评价内容")
    private String content;

}
