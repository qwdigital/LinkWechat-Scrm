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
 * 群发消息成员发送任务表 we_group_message_task
 *
 * @author ruoyi
 * @date 2021-10-19
 */
@ApiModel
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("we_group_message_task")
public class WeGroupMessageTask extends BaseEntity{

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
     * 发送状态：0-未发送 2-已发送
     */
    @ApiModelProperty("发送状态：0-未发送 2-已发送")
    @Excel(name = "发送状态：0-未发送 2-已发送")
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


    @ApiModelProperty(value = "待发送客户",hidden = true)
    @TableField(exist = false)
    private String toBeCustomerName;


    @ApiModelProperty(value = "已发送客户",hidden = true)
    @TableField(exist = false)
    private String alreadyCustomerName;
}
