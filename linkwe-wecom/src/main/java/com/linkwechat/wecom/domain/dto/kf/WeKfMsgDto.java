package com.linkwechat.wecom.domain.dto.kf;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.wecom.domain.dto.WeResultDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author danmo
 * @description 客服消息
 * @date 2021/10/9 15:32
 **/
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeKfMsgDto extends WeResultDto {

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
}
