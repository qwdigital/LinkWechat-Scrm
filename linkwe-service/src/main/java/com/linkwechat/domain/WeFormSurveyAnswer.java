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
import java.util.Date;

/**
 * 答题-用户主表(WeFormSurveyAnswer)
 *
 * @author danmo
 * @since 2022-09-20 18:02:56
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_form_survey_answer")
public class WeFormSurveyAnswer extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1


    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @TableField("mobile")
    private String mobile;


    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    @TableField("name")
    private String name;


    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    @TableField("avatar")
    private String avatar;


    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    @TableField("addr")
    private String addr;


    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    @TableField("city")
    private String city;


    /**
     * 微信openID
     */
    @ApiModelProperty(value = "微信openID")
    @TableField("open_id")
    private String openId;


    /**
     * 微信unionID
     */
    @ApiModelProperty(value = "微信unionID")
    @TableField("union_id")
    private String unionId;


    /**
     * 答题开始时间
     */
    @ApiModelProperty(value = "答题开始时间")
    @TableField("an_time")
    private Date anTime;


    /**
     * 答题用时
     */
    @ApiModelProperty(value = "答题用时")
    @TableField("total_time")
    private Float totalTime;


    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    @TableField("ip_addr")
    private String ipAddr;


    /**
     * 答案
     */
    @ApiModelProperty(value = "答案")
    @TableField("answer")
    private String answer;


    /**
     * 问卷id
     */
    @ApiModelProperty(value = "问卷id")
    @TableField("belong_id")
    private Long belongId;


    /**
     * 是否完成;0完成，1未完成
     */
    @ApiModelProperty(value = "是否完成;0完成，1未完成")
    @TableField("an_effective")
    private Integer anEffective;


    /**
     * 答题数
     */
    @ApiModelProperty(value = "答题数")
    @TableField("qu_num")
    private Integer quNum;


    /**
     * 数据来源
     */
    @ApiModelProperty(value = "数据来源")
    @TableField("data_source")
    private String dataSource;

    /**
     * 删除标识 0 正常 1 删除
     */
    @ApiModelProperty(value = "删除标识 0 正常 1 删除")
    @TableField("del_flag")
    private Integer delFlag;

    @ApiModelProperty(value = "是否为客户")
    @TableField(exist = false)
    private Boolean isOfficeCustomer;
}
