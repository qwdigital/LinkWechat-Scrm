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
 * 客服信息表(WeKfInfo)
 *
 * @author danmo
 * @since 2022-04-15 15:53:35
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_kf_info")
public class WeKfInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    @TableField("corp_id")
    private String corpId;


    /**
     * 客服名称
     */
    @ApiModelProperty(value = "客服名称")
    @TableField("name")
    private String name;


    /**
     * 客服头像
     */
    @ApiModelProperty(value = "客服头像")
    @TableField("avatar")
    private String avatar;


    /**
     * 客服帐号ID
     */
    @ApiModelProperty(value = "客服帐号ID")
    @TableField("open_kf_id")
    private String openKfId;


    /**
     * 接待方式: 1-人工客服 2-智能助手
     */
    @ApiModelProperty(value = "接待方式: 1-人工客服 2-智能助手")
    @TableField("reception_type")
    private Integer receptionType;


    /**
     * 是否分时段: 1-否 2-是
     */
    @ApiModelProperty(value = "是否分时段: 1-否 2-是")
    @TableField("split_time")
    private Integer splitTime;


    /**
     * 分配方式: 1-轮流 2-空闲
     */
    @ApiModelProperty(value = "分配方式: 1-轮流 2-空闲")
    @TableField("allocation_way")
    private Integer allocationWay;


    /**
     * 是否有限分配: 1-否 2-是
     */
    @ApiModelProperty(value = "是否有限分配: 1-否 2-是")
    @TableField("is_priority")
    private Integer isPriority;


    /**
     * 接待限制
     */
    @ApiModelProperty(value = "接待限制")
    @TableField("receive_limit")
    private Integer receiveLimit;


    /**
     * 排队提醒: 1-开启 2-关闭
     */
    @ApiModelProperty(value = "排队提醒: 1-开启 2-关闭")
    @TableField("queue_notice")
    private Integer queueNotice;


    /**
     * 排队提醒内容
     */
    @ApiModelProperty(value = "排队提醒内容")
    @TableField("queue_notice_content")
    private String queueNoticeContent;


    /**
     * 超时未回复提醒: 1-开启 2-关闭
     */
    @ApiModelProperty(value = "超时未回复提醒: 1-开启 2-关闭")
    @TableField("time_out_notice")
    private Integer timeOutNotice;


    /**
     * 超时时间
     */
    @ApiModelProperty(value = "超时时间")
    @TableField("time_out")
    private Integer timeOut;


    /**
     * 超时时间类型 1-分钟 2-小时
     */
    @ApiModelProperty(value = "超时时间类型 1-分钟 2-小时")
    @TableField("time_out_type")
    private Integer timeOutType;


    /**
     * 超时未回复提醒内容
     */
    @ApiModelProperty(value = "超时未回复提醒内容")
    @TableField("time_out_content")
    private String timeOutContent;


    @ApiModelProperty("客服超时未回复提醒: 1-开启 2-关闭")
    @TableField("kf_time_out_notice")
    private Integer kfTimeOutNotice;

    @ApiModelProperty("客户超时时间类型 1-分钟 2-小时")
    @TableField("kf_time_out_type")
    private Integer kfTimeOutType;

    @ApiModelProperty("客服超时时间")
    @TableField("kf_time_out")
    private Integer kfTimeOut;

    /**
     * 自动结束提醒: 1-开启 2-关闭
     */
    @ApiModelProperty(value = "自动结束提醒: 1-开启 2-关闭")
    @TableField("end_notice")
    private Integer endNotice;


    /**
     * 自动结束时间
     */
    @ApiModelProperty(value = "自动结束时间")
    @TableField("end_notice_time")
    private Integer endNoticeTime;


    /**
     * 自动结束时间类型 1-分钟 2-小时
     */
    @ApiModelProperty(value = "自动结束时间类型 1-分钟 2-小时")
    @TableField("end_time_type")
    private Integer endTimeType;

    /**
     * 自动结束提醒内容类型 1-会话质量评价 2-会话结束语
     */
    @ApiModelProperty(value = "自动结束提醒内容类型 1-会话质量评价 2-会话结束语")
    @TableField("end_content_type")
    private Integer endContentType;

    /**
     * 自动结束提醒内容
     */
    @ApiModelProperty(value = "自动结束提醒内容")
    @TableField("end_content")
    private String endContent;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
