package com.linkwechat.wecom.domain.dto.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 群发成员发送任务列表
 * @date 2021/10/3 16:56
 **/
@Data
public class WeGroupMsgTaskDto {

    @ApiModelProperty("企业服务人员的userid")
    private String userId;

    @ApiModelProperty("发送状态：0-未发送 2-已发送")
    private Integer status;

    @ApiModelProperty("发送时间，未发送时不返回")
    private Long sendTime;
}
