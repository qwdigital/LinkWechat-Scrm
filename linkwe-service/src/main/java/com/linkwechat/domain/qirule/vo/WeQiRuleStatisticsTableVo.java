package com.linkwechat.domain.qirule.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @date 2023年05月10日 15:03
 */
@ApiModel
@Data
public class WeQiRuleStatisticsTableVo {

    @ApiModelProperty("成员ID")
    private String userId;

    @ApiModelProperty("成员名称")
    private String userName;

    @ApiModelProperty("所属部门")
    private String deptName;

    @ApiModelProperty("所属会话")
    private String chatName;

    @ApiModelProperty("会话类型 1-客户 2-群聊")
    private Integer chatType;

    @ApiModelProperty("超时时长")
    private String timeout;

    @ApiModelProperty("群聊ID")
    private String roomId;

    @ApiModelProperty("发送人ID")
    private String fromId;

    @ApiModelProperty("发送时间")
    private String sendTime;

    @ApiModelProperty("回复时间")
    private String replyTime;

    @ApiModelProperty("触发时间")
    private String createTime;

    @ApiModelProperty("触发消息ID")
    private String msgId;

}
