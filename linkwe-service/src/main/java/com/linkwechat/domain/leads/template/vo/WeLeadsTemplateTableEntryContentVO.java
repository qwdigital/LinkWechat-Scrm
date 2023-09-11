package com.linkwechat.domain.leads.template.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 线索模版配置表项内容
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 9:59
 */
@Data
public class WeLeadsTemplateTableEntryContentVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "模版表id")
    private Long leadsTemplateSettingsId;

    @ApiModelProperty(value = "表项内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
