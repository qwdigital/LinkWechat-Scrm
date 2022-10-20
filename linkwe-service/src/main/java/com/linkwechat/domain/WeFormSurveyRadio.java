package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 问卷-单选、多选题-选项(WeFormSurveyRadio)
 *
 * @author danmo
 * @since 2022-09-20 18:02:57
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_form_survey_radio")
public class WeFormSurveyRadio extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1


    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 表单控件Id
     */
    @ApiModelProperty(value = "表单控件Id")
    @TableField("form_code_id")
    private String formCodeId;


    /**
     * 控件名称
     */
    @ApiModelProperty(value = "控件名称")
    @TableField("label")
    private String label;


    /**
     * 表单Id
     */
    @ApiModelProperty(value = "表单Id")
    @TableField("form_id")
    private String formId;


    /**
     * 选择内容
     */
    @ApiModelProperty(value = "选择内容")
    @TableField("default_value")
    private String defaultValue;


    /**
     * 所有选项
     */
    @ApiModelProperty(value = "所有选项")
    @TableField("options")
    private String options;


    /**
     * 渠道
     */
    @ApiModelProperty(value = "渠道")
    @TableField("data_source")
    private String dataSource;


    /**
     * 题号
     */
    @ApiModelProperty(value = "题号")
    @TableField("question_number")
    private String questionNumber;


    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;
}
