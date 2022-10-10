package com.linkwechat.domain.operation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 客群总数
 * @date 2022/1/9 17:09
 **/
@ApiModel
@Data
public class WeSessionGroupAnalysisVo {

    @ApiModelProperty("昨日群聊总数")
    private int ydChatTotal;

    @ApiModelProperty("前日群聊总数")
    private int bydChatTotal;

    @ApiModelProperty("昨日群聊消息数")
    private int ydMsgTotal;

    @ApiModelProperty("前日群聊消息数")
    private int bydMsgTotal;

    @ApiModelProperty("昨日发送消息群成员数")
    private int ydMemberHasMsg;

    @ApiModelProperty("前日发送消息群成员数")
    private int bydMemberHasMsg;

    public int getBydChatTotal() {
        return this.ydChatTotal - bydChatTotal;
    }

    public int getBydMsgTotal() {
        return this.ydMsgTotal - bydMsgTotal;
    }

    public int getBydMemberHasMsg() {
        return this.ydMemberHasMsg - bydMemberHasMsg;
    }
}
