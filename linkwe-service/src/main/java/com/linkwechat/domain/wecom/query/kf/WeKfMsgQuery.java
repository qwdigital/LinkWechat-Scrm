package com.linkwechat.domain.wecom.query.kf;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author danmo
 * @Description 客服消息接口入参
 * @date 2021/12/13 10:27
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfMsgQuery extends WeBaseQuery {

    @ApiModelProperty("指定接收消息的客户UserID")
    @NotNull(message = "客户UserID不能为空")
    private String touser;

    @ApiModelProperty("指定发送消息的客服帐号ID")
    @NotNull(message = "客服帐号ID不能为空")
    private String open_kfid;


    @ApiModelProperty(value = "指定接收消息的客户UserID",allowEmptyValue = true)
    private String msgid;

    @ApiModelProperty("指定接收消息的客户UserID")
    @NotNull(message = "消息类型不能为空")
    private String msgtype;

    @ApiModelProperty("消息内容")
    private JSONObject context;

    private String code;


}
