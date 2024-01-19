package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName(value="we_group_message_template",autoResultMap = true)
public class WeGroupMessageTemplate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @TableId
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
     * 是否全部发送 true 是 false 否
     */
    private boolean allSend;


    /*
     *客户或客群查询条件
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = FastjsonTypeHandler.class)
    private WeCustomersOrGroupQuery weCustomersOrGroupQuery;




    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeCustomersOrGroupQuery{

        /**
         * 群发送查询条件
         */
        private WeGroupQuery weGroupQuery;

        /**
         * 客户发送查询条件
         */
        private WeCustomersQuery weCustomersQuery;


    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeGroupQuery{

        /**
         * 群主id,多个使用逗号隔开
         */
        private String owners;

        /**
         * 群主名称，多个使用逗号隔开
         */
        private String ownerNames;

    }
}
