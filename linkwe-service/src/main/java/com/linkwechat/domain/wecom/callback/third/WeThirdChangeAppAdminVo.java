package com.linkwechat.domain.wecom.callback.third;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 应用管理员变更通知
 * @date 2022/3/7 22:50
 **/
@ApiModel
@Data
public class WeThirdChangeAppAdminVo{

    @ApiModelProperty("企业微信CorpID")
    private String ToUserName;

    @ApiModelProperty("此处固定为sys")
    private String FromUserName;

    @ApiModelProperty("消息创建时间（整型）")
    private Long CreateTime;

    @ApiModelProperty("消息类型，此时固定为：event")
    private String MsgType;

    @ApiModelProperty("事件类型，此处固定为change_app_admin")
    private String Event;

    @ApiModelProperty("企业应用的id，整型")
    private Integer AgentID;

}
