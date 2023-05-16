package com.linkwechat.domain.qirule.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("发送时间")
    private Date sendTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("回复时间")
    private Date replyTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("触发时间")
    private Date createTime;

    @ApiModelProperty("触发消息ID")
    private String msgId;

}
