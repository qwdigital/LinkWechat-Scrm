package com.linkwechat.domain.leads.template.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 线索模版配置
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 9:58
 */
@Data
public class WeLeadsTemplateSettingsVO {

    @ApiModelProperty(value = "表主键id")
    private Long id;

    @ApiModelProperty(value = "表项名称")
    private String tableEntryName;

    @ApiModelProperty(value = "表项id")
    private String tableEntryId;

    @ApiModelProperty(value = "表项属性 0 填写项 1 下拉项")
    private Integer tableEntryAttr;

    @ApiModelProperty(value = "数据属性 0 文本 1 数字 2 日期")
    private Integer dataAttr;

    @ApiModelProperty(value = "表项内容")
    private List<WeLeadsTemplateTableEntryContentVO> tableEntryContent;

    @ApiModelProperty(value = "日期类型 0 日期 1 日期+时间")
    private Integer datetimeType;

    @ApiModelProperty(value = "输入长度")
    private Integer maxInputLen;

    @ApiModelProperty(value = "是否必填 0 非必填 1 必填")
    private Integer required;

    @ApiModelProperty(value = "用于排序")
    private Integer rank;

    @ApiModelProperty(value = "是否可被编辑 0 可被编辑 1 不可被编辑")
    private Integer canEdit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建人id")
    private Long createById;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新人id")
    private Long updateById;

}

