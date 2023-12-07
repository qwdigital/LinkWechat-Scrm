package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 群发消息成员发送任务表(WeGroupMessageTask)
 *
 * @author danmo
 * @since 2022-04-06 22:29:05
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_group_message_task")
public class WeGroupMessageTask extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 消息模板id
     */
    @ApiModelProperty(value = "消息模板id")
    @TableField("msg_template_id")
    private Long msgTemplateId;


    /**
     * 企业群发消息的id
     */
    @ApiModelProperty(value = "企业群发消息的id")
    @TableField("msg_id")
    private String msgId;


    /**
     * 企业服务人员的userid
     */
    @ApiModelProperty(value = "企业服务人员的userid")
    @TableField("user_id")
    private String userId;


    /**
     * 发送状态：0-未发送 2-已发送
     */
    @ApiModelProperty(value = "发送状态：0-未发送 2-已发送")
    @TableField("status")
    private Integer status;


    /**
     * 发送时间，未发送时不返回
     */
    @ApiModelProperty(value = "发送时间，未发送时不返回")
    @TableField("send_time")
    private Date sendTime;


    
    
    


    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;

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


    /**
     * 客户查询条件
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private WeCustomersQuery weCustomersQuery;
}
