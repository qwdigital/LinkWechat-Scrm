package com.linkwechat.wecom.domain.vo;

import com.linkwechat.common.annotation.Excel;
import com.linkwechat.wecom.domain.WeGroupMessageAttachments;
import com.linkwechat.wecom.domain.WeGroupMessageList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author sxw
 * @description 群发列表出参
 * @date 2021/10/23 15:27
 **/
@ApiModel
@Data
public class WeGroupMessageListVo extends WeGroupMessageList {
    @ApiModelProperty("发送成员总数")
    @Excel(name = "发送成员总数")
    private Integer senderTotalNums;

    @ApiModelProperty("待发送成员数")
    @Excel(name = "待发送成员数")
    private Integer senderNums;

    @ApiModelProperty("待发送客户数")
    @Excel(name = "待发送客户数")
    private Integer serviceMember;

    @ApiModelProperty("发送客户总数")
    @Excel(name = "发送客户总数")
    private Integer serviceTotalMember;

    @ApiModelProperty("附件")
    private List<WeGroupMessageAttachments> attachments;
}
