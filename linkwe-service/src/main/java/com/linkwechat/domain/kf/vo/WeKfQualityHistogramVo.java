package com.linkwechat.domain.kf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2022年11月29日 11:20
 */
@ApiModel
@Data
public class WeKfQualityHistogramVo {

    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    private String userId;

    /**
     * 员工名称
     */
    @ApiModelProperty(value = "员工名称")
    private String userName;

    /**
     * 参评总数
     */
    @ApiModelProperty(value = "参评总数")
    private Integer evaluateCnt = 0;

    /**
     * 参评率
     */
    @ApiModelProperty(value = "参评率")
    private String evaluateRate = "0";

    /**
     * 好评率
     */
    @ApiModelProperty(value = "好评率")
    private String goodRate = "0";


    /**
     * 一般率
     */
    @ApiModelProperty(value = "一般率")
    private String commonRate = "0";


    /**
     * 差评率
     */
    @ApiModelProperty(value = "差评率")
    private String badRate = "0";
}
