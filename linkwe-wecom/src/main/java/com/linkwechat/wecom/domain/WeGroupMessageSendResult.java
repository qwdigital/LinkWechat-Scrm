package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 群发消息成员执行结果对象 we_group_message_send_result
 *
 * @author ruoyi
 * @date 2021-10-19
 */
@ApiModel
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("we_group_message_send_result")
public class WeGroupMessageSendResult extends BaseEntity{

    /**
     * 主键id
     */
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 群发消息模板id
     */
    @ApiModelProperty("群发消息模板id")
    @Excel(name = "群发消息模板id")
    private Long msgTemplateId;

    /**
     * 企业群发消息的id
     */
    @ApiModelProperty("企业群发消息的id")
    @Excel(name = "企业群发消息的id")
    private String msgId;

    /**
     * 企业服务人员的userid
     */
    @ApiModelProperty("企业服务人员的userid")
    @Excel(name = "企业服务人员的userid")
    private String userId;

    /**
     * 外部联系人userid
     */
    @ApiModelProperty("外部联系人userid")
    @Excel(name = "外部联系人userid")
    private String externalUserid;

    /**
     * 外部客户群id
     */
    @ApiModelProperty("外部客户群id")
    @Excel(name = "外部客户群id")
    private String chatId;

    /**
     * 发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
     */
    @ApiModelProperty("发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    @Excel(name = "发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败")
    private Integer status;

    /**
     * 发送时间，发送状态为1时返回
     */
    @ApiModelProperty("发送时间")
    @Excel(name = "发送时间，发送状态为1时返回", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 企业服务人员名称
     */
    @ApiModelProperty("企业服务人员名称")
    @TableField(exist = false)
    private String userName;

    /**
     * 外部联系人姓名
     */
    @ApiModelProperty("外部联系人姓名")
    @TableField(exist = false)
    private String customerName;

    /**
     * 外部客户群名称
     */
    @ApiModelProperty("外部客户群名称")
    @TableField(exist = false)
    private String chatName;
}
