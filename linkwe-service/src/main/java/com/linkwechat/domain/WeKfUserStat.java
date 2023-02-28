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
 * 客服员工统计表(WeKfUserStat)
 *
 * @author danmo
 * @since 2022-11-28 16:48:24
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_kf_user_stat")
public class WeKfUserStat extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    @TableField("date_time")
    private String dateTime;

    /**
     * 客服ID
     */
    @ApiModelProperty(value = "客服ID")
    @TableField("open_kf_id")
    private String openKfId;

    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    @TableField("user_id")
    private String userId;


    /**
     * 会话总数
     */
    @ApiModelProperty(value = "会话总数")
    @TableField("session_cnt")
    private Integer sessionCnt;


    /**
     * 参评总数
     */
    @ApiModelProperty(value = "参评总数")
    @TableField("evaluate_cnt")
    private Integer evaluateCnt;


    /**
     * 好评数
     */
    @ApiModelProperty(value = "好评数")
    @TableField("good_cnt")
    private Integer goodCnt;


    /**
     * 一般数
     */
    @ApiModelProperty(value = "一般数")
    @TableField("common_cnt")
    private Integer commonCnt;


    /**
     * 差评数
     */
    @ApiModelProperty(value = "差评数")
    @TableField("bad_cnt")
    private Integer badCnt;


    /**
     * 对话数
     */
    @ApiModelProperty(value = "对话数")
    @TableField("talk_cnt")
    private Integer talkCnt;


    /**
     * 超时数
     */
    @ApiModelProperty(value = "超时数")
    @TableField("time_out_cnt")
    private Integer timeOutCnt;


    /**
     * 超时时长
     */
    @ApiModelProperty(value = "超时时长")
    @TableField("time_out_duration")
    private Long timeOutDuration;


    /**
     * 是否删除:0有效,1删除
     */
    @ApiModelProperty(value = "是否删除:0有效,1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
