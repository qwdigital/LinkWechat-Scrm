package com.linkwechat.wecom.domain.vo;

import com.linkwechat.wecom.domain.WeGroupMessageAttachments;
import com.linkwechat.wecom.domain.WeGroupMessageSendResult;
import com.linkwechat.wecom.domain.WeGroupMessageTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author sxw
 * @description 群发详情出参
 * @date 2021/10/23 15:09
 **/
@ApiModel
@Data
public class WeGroupMessageDetailVo {
    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private List<WeGroupMessageAttachments> attachments;

    /**
     * 待发送者列表
     */
    @ApiModelProperty("待发送者列表")
    private List<WeGroupMessageTaskVo> toBeSentList;

    /**
     * 已发送者列表
     */
    @ApiModelProperty("已发送者列表")
    private List<WeGroupMessageTaskVo> alreadySentList;
}
