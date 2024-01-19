package com.linkwechat.domain.groupmsg.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.WeGroupMessageAttachments;
import com.linkwechat.domain.WeGroupMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author danmo
 * @description 群发详情出参
 * @date 2021/10/23 15:09
 **/
@ApiModel
@Data
public class WeGroupMessageDetailVo {

    @ApiModelProperty("发送内容")
    private String content;

    @ApiModelProperty("发送状态 -1：失败  0：未执行 1：完成 2：取消")
    private Integer status;

    @ApiModelProperty("发送类型")
    private Integer chatType;

    @ApiModelProperty("发送时间")
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("刷新时间")
    private Date refreshTime;

    @ApiModelProperty("附件")
    private List<WeGroupMessageAttachments> attachments;

    @ApiModelProperty("待发送者数")
    private Integer toBeSendNum;

    @ApiModelProperty("待发送者列表")
    private List<WeGroupMessageTaskVo> toBeSendList;

    @ApiModelProperty("已发送者数量")
    private Integer alreadySendNum;

    @ApiModelProperty("已发送者列表")
    private List<WeGroupMessageTaskVo> alreadySendList;

    @ApiModelProperty("已送达客户数")
    private Integer alreadySendCustomerNum;

    @ApiModelProperty("未送达客户数")
    private Integer toBeSendCustomerNum;


    /**
     * 是否全部发送
     */
    private Boolean isAll=true;


    private WeGroupMessageTemplate.WeCustomersOrGroupQuery weCustomersOrGroupQuery;
}
