package com.linkwechat.domain.groupmsg.vo;

import com.linkwechat.domain.WeGroupMessageAttachments;
import com.linkwechat.domain.WeGroupMessageSendResult;
import com.linkwechat.domain.WeGroupMessageTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 群发详情出参
 * @date 2021/10/23 15:09
 **/
@ApiModel
@Data
public class WeGroupMessageListVo {

    private String msgTemplateId;


    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private List<WeGroupMessageAttachments> attachments;

    /**
     * 发送者
     */
    @ApiModelProperty("发送者信息")
    private List<WeGroupMessageTask> senders;

    /**
     * 发送客户
     */
    @ApiModelProperty("接收者信息")
    private List<WeGroupMessageSendResult> extralInfos;
}
