package com.linkwechat.wecom.domain.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 回调验证XML对象
 * @date 2021/11/19 18:39
 **/
@ApiModel
@Data
public class WeBackBaseVo {

    @ApiModelProperty("企业微信CorpID")
    private String ToUserName;

    @ApiModelProperty("此事件该值固定为sys")
    private String FromUserName;

    @ApiModelProperty("消息创建时间 （整型）")
    private Long CreateTime;

    @ApiModelProperty("消息的类型，此时固定为event")
    private String MsgType;

    @ApiModelProperty("事件的类型")
    private String Event;

    @ApiModelProperty("变更类型")
    private String ChangeType;

    @ApiModelProperty("token")
    private String Token;

    @ApiModelProperty("应用ID")
    private String AgentID;

}
