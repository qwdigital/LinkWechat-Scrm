package com.linkwechat.domain.leads.template.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 线索模版配置请求参数
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 10:02
 */
@Data
public class WeLeadsTemplateSettingsRequest {

    /**
     * 表主键id
     */
    @ApiModelProperty(value = "表主键id")
    private Long id;

    /**
     * 表项名称
     */
    @ApiModelProperty(value = "表项名称")
    private String tableEntryName;

    /**
     * 表项id
     */
    @ApiModelProperty(value = "表项id")
    private String tableEntryId;

    /**
     * 表项属性 0 填写项 1 下拉项
     *
     * @see com.linkwechat.common.enums.leads.template.TableEntryAttrEnum
     */
    @ApiModelProperty(value = "表项属性 0 填写项 1 下拉项")
    private Integer tableEntryAttr;

    /**
     * 数据属性 0 文本 1 数字 2 日期
     *
     * @see com.linkwechat.common.enums.leads.template.DataAttrEnum
     */
    @ApiModelProperty(value = "数据属性 0 文本 1 数字 2 日期")
    private Integer dataAttr;

    /**
     * 日期类型 0 日期 1 日期+时间
     *
     * @see com.linkwechat.common.enums.leads.template.DatetimeTypeEnum
     */
    @ApiModelProperty(value = "日期类型 0 日期 1 日期+时间")
    private Integer datetimeType;

    /**
     * 表项内容
     */
    @ApiModelProperty(value = "表项内容")
    private List<WeLeadsTemplateTableEntryContentRequest> tableEntryContent;

    /**
     * 最大输入长度
     */
    @Min(value = 1, message = "最大输入长度最小值为1")
    @Max(value = 1000000, message = "最大输入长度的最大值不能超过1000000")
    @ApiModelProperty(value = "最大输入长度")
    private Integer maxInputLen;

    /**
     * 是否必填 0 非必填 1 必填
     *
     * @see com.linkwechat.common.enums.leads.template.RequiredEnum
     */
    @ApiModelProperty(value = "是否必填 0 非必填 1 必填")
    private Integer required;

    /**
     * 用于排序
     */
    @ApiModelProperty(value = "用于排序")
    private Integer rank;

}

