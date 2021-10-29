package com.linkwechat.wecom.domain.dto.message;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.wecom.domain.query.WeAddMsgTemplateQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 消息
 * @date 2021/10/3 16:35
 **/
@ApiModel
@Data
public class WeGroupMsgDto {

    @ApiModelProperty("企业群发消息的id")
    private String msgId;

    @ApiModelProperty("群发消息创建者userid，API接口创建的群发消息不返回该字段")
    private String creator;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("群发消息创建来源。0：企业 1：个人")
    private Integer createType;

    @ApiModelProperty("消息文本内容，最多4000个字节")
    private WeAddMsgTemplateQuery.Text text;

    @ApiModelProperty("企业群发消息的id")
    private List<JSONObject> attachments;
}
