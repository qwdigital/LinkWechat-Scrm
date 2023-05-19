package com.linkwechat.domain.qirule.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会话质检周报列表出参
 *
 * @author danmo
 * @date 2023/05/05 18:22
 **/

@ApiModel
@Data
public class WeQiRuleWeeklyDetailVo {

    @ApiModelProperty("质检周报ID")
    private Long id;

    @ApiModelProperty("质检周期")
    private String weeklyTime;

    /**
     * 督导成员数
     */
    @ApiModelProperty(value = "督导成员数")
    private Integer staffNum;


    /**
     * 客户会话数
     */
    @ApiModelProperty(value = "客户会话数")
    private String chatNum;


    /**
     * 客群会话数
     */
    @ApiModelProperty(value = "客群会话数")
    private String groupChatNum;


    /**
     * 成员回复次数
     */
    @ApiModelProperty(value = "成员回复次数")
    private String replyNum;


    /**
     * 成员超时次数
     */
    @ApiModelProperty(value = "成员超时次数")
    private String timeOutNum;


    /**
     * 成员超时率
     */
    @ApiModelProperty(value = "成员超时率")
    private String timeOutRate;


    /**
     * 客户会话超时率
     */
    @ApiModelProperty(value = "客户会话超时率")
    private String chatTimeOutRate;


    /**
     * 客群会话超时率
     */
    @ApiModelProperty(value = "客群会话超时率")
    private String groupChatTimeOutRate;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "发送时间")
    private Date sendTime;
}
