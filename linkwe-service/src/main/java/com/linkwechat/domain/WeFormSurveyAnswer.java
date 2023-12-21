package com.linkwechat.domain;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
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
@Data
@SuppressWarnings("serial")
@TableName("we_form_survey_answer")
@ColumnWidth(25)
public class WeFormSurveyAnswer extends BaseEntity {



    @TableId(type = IdType.AUTO)
    @TableField("id")
    @ExcelIgnore
    private Long id;


    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;


    /**
     * 用户姓名
     */
    @TableField("name")
    private String name;


    /**
     * 用户头像
     */
    @TableField("avatar")
    @ExcelIgnore
    private String avatar;


    /**
     * 详细地址
     */
    @TableField("addr")
    @ExcelIgnore
    private String addr;


    /**
     * 城市
     */
    @TableField("city")
    @ExcelIgnore
    private String city;


    /**
     * 微信openID
     */
    @TableField("open_id")
    private String openId;


    /**
     * 微信unionID
     */
    @TableField("union_id")
    private String unionId;


    /**
     * 答题开始时间
     */
    @TableField("an_time")

    private Date anTime;


    /**
     * 答题用时
     */
    @TableField("total_time")
    @ExcelIgnore
    private Float totalTime;


    /**
     * ip地址
     */
    @TableField("ip_addr")
    @ExcelIgnore
    private String ipAddr;


    /**
     * 答案
     */
    @TableField("answer")
    @ExcelIgnore
    private String answer;


    /**
     * 问卷id
     */
    @TableField("belong_id")
    @ExcelIgnore
    private Long belongId;


    /**
     * 是否完成;0完成，1未完成
     */
    @TableField("an_effective")
    @ExcelIgnore
    private Integer anEffective;


    /**
     * 答题数
     */
    @TableField("qu_num")
    @ExcelIgnore
    private Integer quNum;


    /**
     * 数据来源
     */
    @TableField("data_source")
    @ExcelIgnore
    private String dataSource;

    /**
     * 删除标识 0 正常 1 删除
     */
    @TableField("del_flag")
    @ExcelIgnore
    private Integer delFlag;


    /**
     * 是否为客户
     */
    @TableField(exist = false)
    private Boolean isOfficeCustomer;
}
