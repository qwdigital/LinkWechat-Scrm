package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群发消息模板(WeGroupMessageTemplate)
 *
 * @author danmo
 * @since 2022-04-06 22:29:06
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_group_message_template")
public class WeGroupMessageTemplate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;


    /**
     * 群发任务的类，1：表示发送给客户，2：表示发送给客户群
     */
    @ApiModelProperty(value = "群发任务的类，1：表示发送给客户，2：表示发送给客户群")
    @TableField("chat_type")
    private Integer chatType;


    /**
     * 群发内容
     */
    @ApiModelProperty(value = "群发内容")
    @TableField("content")
    private String content;


    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @TableField("send_time")
    private Date sendTime;


    /**
     * 是否定时任务 0 立即发送 1 定时发送
     */
    @ApiModelProperty(value = "是否定时任务 0 立即发送 1 定时发送")
    @TableField("is_task")
    private Integer isTask;


    /**
     * 是否执行 -1：失败  0：未执行 1：完成 2：取消
     */
    @ApiModelProperty(value = "是否执行 -1：失败  0：未执行 1：完成 2：取消")
    @TableField("status")
    private Integer status;


    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    @TableField("business_id")
    private Long businessId;


    /**
     * 来源 0 群发 1 其他
     */
    @ApiModelProperty(value = "来源 0-群发 1-任务宝 2-策略群发")
    @TableField("source")
    private Integer source;


    /**
     * 刷新时间
     */
    @ApiModelProperty(value = "刷新时间")
    @TableField("refresh_time")
    private Date refreshTime;




    //多个业务id使用逗号隔开
    @TableField(exist = false)
    private String businessIds;


    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;
}
