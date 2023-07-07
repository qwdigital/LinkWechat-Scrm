package com.linkwechat.domain.leads.template.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 线索模版配置表项内容
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 10:02
 */
@Data
public class WeLeadsTemplateTableEntryContentRequest {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 模版表id
     */
    @ApiModelProperty(value = "模版表id")
    private Long leadsTemplateSettingsId;

    /**
     * 表项内容
     */
    @ApiModelProperty(value = "表项内容")
    private String content;

}
