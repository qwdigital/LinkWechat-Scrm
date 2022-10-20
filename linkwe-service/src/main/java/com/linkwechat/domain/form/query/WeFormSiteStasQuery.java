package com.linkwechat.domain.form.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 智能表单站点统计
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/14 10:24
 */
@Data
public class WeFormSiteStasQuery {

    /**
     * 问卷id
     */
    @ApiModelProperty(value = "问卷id")
    @NotNull(message = "问卷ID不能为空")
    private Long belongId;

    /**
     * 数据来源
     */
    @NotBlank(message = "数据来源不能为空")
    @ApiModelProperty(value = "数据来源")
    private String dataSource;

    /**
     * Ip地址
     */
    @NotBlank(message = "Ip地址")
    @ApiModelProperty(value = "Ip地址")
    private String ipAddr;

}
