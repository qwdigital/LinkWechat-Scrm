package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 问卷-目录列表(WeFormSurveyCatalogue)
 *
 * @author danmo
 * @since 2022-09-20 18:02:56
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_form_survey_catalogue")
public class WeFormSurveyCatalogue extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 答卷人数
     */
    @ApiModelProperty(value = "答卷人数")
    @TableField("answer_num")
    private Integer answerNum;


    /**
     * 是否分享
     */
    @ApiModelProperty(value = "是否分享")
    @TableField("an_share")
    private Integer anShare;


    /**
     * 是否授权
     */
    @ApiModelProperty(value = "是否授权")
    @TableField("an_auth")
    private Integer anAuth;


    /**
     * 编码id
     */
    @ApiModelProperty(value = "编码id")
    @TableField("sid")
    private String sid;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @TableField("survey_name")
    private String surveyName;


    /**
     * 题数
     */
    @ApiModelProperty(value = "题数")
    @TableField("survey_qu_num")
    private Integer surveyQuNum;


    /**
     * 表单状态;0默认设计状态未发布，1收集中，2已暂停, 3已结束
     */
    @ApiModelProperty(value = "表单状态;0默认设计状态未发布，1收集中，2已暂停, 3已结束")
    @TableField("survey_state")
    private Integer surveyState;


    /**
     * 是否显示;0显示，1不显示
     */
    @ApiModelProperty(value = "是否显示;0显示，1不显示")
    @TableField("visibility")
    private Integer visibility;


    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id")
    @TableField("group_id")
    private Long groupId;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称")
    @TableField(exist = false)
    private String groupName;


    /**
     * 表单描述
     */
    @ApiModelProperty(value = "表单描述")
    @TableField("form_description")
    private String formDescription;


    /**
     * 表单logo
     */
    @ApiModelProperty(value = "表单logo")
    @TableField("form_logo")
    private String formLogo;


    /**
     * 是否定时
     */
    @ApiModelProperty(value = "是否定时")
    @TableField("an_timing")
    private Integer anTiming;


    /**
     * 定时开始时间
     */
    @ApiModelProperty(value = "定时开始时间")
    @TableField("timing_start")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timingStart;


    /**
     * 定时结束时间
     */
    @ApiModelProperty(value = "定时结束时间")
    @TableField("timing_end")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timingEnd;


    /**
     * 填写规则;0每人填写一次，1每人每天填写一次
     */
    @ApiModelProperty(value = "填写规则;0每人填写一次，1每人每天填写一次")
    @TableField("filling_rules")
    private Integer fillingRules;


    /**
     * 页面地址
     */
    @ApiModelProperty(value = "页面地址")
    @TableField("html_path")
    private String htmlPath;


    /**
     * 是否多渠道
     */
    @ApiModelProperty(value = "是否多渠道")
    @TableField("an_channels")
    private Integer anChannels;


    /**
     * 多渠道地址
     */
    @ApiModelProperty(value = "多渠道地址")
    @TableField("channels_path")
    private String channelsPath;


    /**
     * 表单样式
     */
    @ApiModelProperty(value = "表单样式")
    @TableField("styles")
    private Object styles;


    /**
     * 二维码
     */
    @ApiModelProperty(value = "二维码")
    @TableField("qr_code")
    private String qrCode;


    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    @TableField("channels_name")
    private String channelsName;

    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;

    @ApiModelProperty(value = "访问总数")
    @TableField(exist = false)
    private Integer totalVisits;

    @ApiModelProperty(value = "有效收集量")
    @TableField(exist = false)
    private Integer collectionVolume;


}
