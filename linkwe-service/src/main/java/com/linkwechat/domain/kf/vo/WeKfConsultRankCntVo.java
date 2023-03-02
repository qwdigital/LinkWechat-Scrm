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
public class WeKfConsultRankCntVo {

    @ApiModelProperty("总数")
    private Integer total;

    @ApiModelProperty("接待人员名称")
    private String userName;
}
