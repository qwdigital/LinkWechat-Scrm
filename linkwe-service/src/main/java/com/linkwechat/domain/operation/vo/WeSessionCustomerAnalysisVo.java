package com.linkwechat.domain.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author danmo
 * @description 客户联系
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeSessionCustomerAnalysisVo {

    @ApiModelProperty("昨日单聊总数")
    private int ydChatCnt;

    @ApiModelProperty("前日单聊总数")
    private int bydChatCnt;

    @ApiModelProperty("昨日发送消息数")
    private int ydMessageCnt;

    @ApiModelProperty("前日发送消息数")
    private int bydMessageCnt;

    @ApiModelProperty("昨日回复单聊占比")
    private double ydReplyPercentage;

    @ApiModelProperty("前日回复单聊占比")
    private double bydReplyPercentage;

    @ApiModelProperty("昨日平均首次回复时长(分)")
    private double ydAvgReplyTime;

    @ApiModelProperty("前日平均首次回复时长(分)")
    private double bydAvgReplyTime;

    public int getBydChatCnt() {
        return this.ydChatCnt - this.bydChatCnt;
    }

    public int getBydMessageCnt() {
        return this.ydMessageCnt - this.bydMessageCnt;
    }

    public double getYdAvgReplyTime() {
        BigDecimal b = new BigDecimal(this.ydAvgReplyTime);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getBydAvgReplyTime() {
        BigDecimal b = new BigDecimal(this.ydAvgReplyTime - this.bydAvgReplyTime);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getYdReplyPercentage() {
        BigDecimal b = new BigDecimal(this.ydReplyPercentage);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getBydReplyPercentage() {
        BigDecimal b = new BigDecimal(this.ydReplyPercentage - this.bydReplyPercentage);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
