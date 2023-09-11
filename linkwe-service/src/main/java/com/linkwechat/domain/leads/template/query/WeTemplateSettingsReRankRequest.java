package com.linkwechat.domain.leads.template.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改排序请求参数
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 10:59
 */
@Data
public class WeTemplateSettingsReRankRequest {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private Integer rank;

}
