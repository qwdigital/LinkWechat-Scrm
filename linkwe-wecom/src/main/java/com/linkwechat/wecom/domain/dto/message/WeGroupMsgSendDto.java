package com.linkwechat.wecom.domain.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 群成员发送结果列表
 * @date 2021/10/3 17:02
 **/
@ApiModel
@Data
public class WeGroupMsgSendDto {

    @ApiModelProperty("外部联系人userid，群发消息到企业的客户群不返回该字段")
    private String externalUserId;

    @ApiModelProperty("外部客户群id，群发消息到客户不返回该字段")
    private String chatId;

    @ApiModelProperty("企业服务人员的userid")
    private String userId;

    @ApiModelProperty("发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer status;

    @ApiModelProperty("发送时间，发送状态为1时返回")
    private Long sendTime;
}
