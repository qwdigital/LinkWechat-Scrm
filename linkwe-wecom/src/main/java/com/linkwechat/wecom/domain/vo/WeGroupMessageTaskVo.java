package com.linkwechat.wecom.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.wecom.domain.WeGroupMessageAttachments;
import com.linkwechat.wecom.domain.WeGroupMessageSendResult;
import com.linkwechat.wecom.domain.WeGroupMessageTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 群发成员任务出参
 * @date 2021/10/23 15:09
 **/
@ApiModel
@Data
public class WeGroupMessageTaskVo {

    /**
     * 企业服务人员的userid
     */
    @ApiModelProperty("企业服务人员的userid")
    private String userId;

    /**
     * 企业服务人员的名称
     */
    @ApiModelProperty("企业服务人员的名称")
    private String userName;

    /**
     * 发送时间
     */
    @ApiModelProperty("发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

    /**
     * 发送客户列表
     */
    @ApiModelProperty("发送客户列表")
    private List<String> customerList;

    /**
     * 发送群列表
     */
    @ApiModelProperty("发送群列表")
    private List<String> groupList;
}
