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
public class WeQiRuleWeeklyListVo {

    @ApiModelProperty("质检通知ID")
    private Long id;

    @ApiModelProperty("质检周期")
    private String weeklyTime;

    @ApiModelProperty("员工ID")
    private String userId;

    @ApiModelProperty("员工名称")
    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "发送时间")
    private Date sendTime;

    @ApiModelProperty("发送状态 0-未发送 1-已发送")
    private Integer status;
}
