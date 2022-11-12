package com.linkwechat.domain.wecom.query.msg;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author danmo
 */
@ApiModel
@Data
public class WeRecallMsgQuery extends WeBaseQuery {

    @ApiModelProperty("消息ID。从应用发送消息接口处获得")
   private String msgid;
}
