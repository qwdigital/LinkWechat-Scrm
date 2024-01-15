package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
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
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 群发任务的类，1：表示发送给客户，2：表示发送给客户群
     */
    private Integer chatType;


    /**
     * 群发内容
     */
    private String content;


    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date sendTime;


    /**
     * 是否定时任务 0 立即发送 1 定时发送
     */
    private Integer isTask;


    /**
     * 是否执行 -1：失败  0：未执行 1：完成 2：取消
     */
    private Integer status;


    /**
     * 业务id
     */
    private Long businessId;


    /**
     * 来源 0-群发 1-任务宝 2-策略群发
     */
    private Integer source;


    /**
     * 刷新时间
     */
    private Date refreshTime;




    //多个业务id使用逗号隔开
    @TableField(exist = false)
    private String businessIds;


    /**
     * 删除标识 0 有效 1删除
     */
    private Integer delFlag;


    /**
     * 客户查询条件
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private WeCustomersQuery weCustomersQuery;
}
