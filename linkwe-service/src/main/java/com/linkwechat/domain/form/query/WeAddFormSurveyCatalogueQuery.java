package com.linkwechat.domain.form.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author danmo
 * @date 2022年09月20日 18:24
 */
@Data
public class WeAddFormSurveyCatalogueQuery {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;


    /**
     * 答卷人数
     */
    @ApiModelProperty(value = "答卷人数")
    private Integer answerNum;


    /**
     * 是否分享
     */
    @ApiModelProperty(value = "是否分享")
    private Integer anShare;


    /**
     * 是否授权
     */
    @ApiModelProperty(value = "是否授权")
    private Integer anAuth;


    /**
     * 编码id
     */
    @ApiModelProperty(value = "编码id")
    private String sid;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String surveyName;


    /**
     * 题数
     */
    @ApiModelProperty(value = "题数")
    private Integer surveyQuNum;


    /**
     * 表单状态;0默认设计状态未发布，1收集中，2已暂停, 3已结束
     */
    @ApiModelProperty(value = "表单状态;0默认设计状态未发布，1收集中，2已暂停, 3已结束")
    private Integer surveyState;


    /**
     * 是否显示;0显示，1不显示
     */
    @ApiModelProperty(value = "是否显示;0显示，1不显示")
    private Integer visibility;


    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id")
    private Long groupId;


    /**
     * 表单描述
     */
    @ApiModelProperty(value = "表单描述")
    private String formDescription;


    /**
     * 表单logo
     */
    @ApiModelProperty(value = "表单logo")
    private String formLogo;


    /**
     * 是否定时
     */
    @ApiModelProperty(value = "是否定时")
    private Integer anTiming;


    /**
     * 定时开始时间
     */
    @ApiModelProperty(value = "定时开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timingStart;


    /**
     * 定时结束时间
     */
    @ApiModelProperty(value = "定时结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timingEnd;


    /**
     * 填写规则;0每人填写一次，1每人每天填写一次
     */
    @ApiModelProperty(value = "填写规则;0每人填写一次，1每人每天填写一次, 2不限制")
    private Integer fillingRules;


    /**
     * 页面地址
     */
    @ApiModelProperty(value = "页面地址")
    private String htmlPath;


    /**
     * 是否多渠道
     */
    @ApiModelProperty(value = "是否多渠道")
    private Integer anChannels;


    /**
     * 多渠道地址
     */
    @ApiModelProperty(value = "多渠道地址")
    private String channelsPath;


    /**
     * 表单样式
     */
    @ApiModelProperty(value = "表单样式")
    private Object styles;


    /**
     * 二维码
     */
    @ApiModelProperty(value = "二维码")
    private String qrCode;


    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelsName;
}
