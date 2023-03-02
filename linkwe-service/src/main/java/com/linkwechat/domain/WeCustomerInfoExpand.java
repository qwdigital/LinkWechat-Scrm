package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.typeHandler.WeCustomerInfoExpandListTypeHandler;
import com.linkwechat.typeHandler.WeCustomerInfoValHandler;
import com.linkwechat.typeHandler.WeStrategicCrowdSwipeListTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 客户信息拓展表
 * @TableName we_customer_info_expand
 */
@TableName(value ="we_customer_info_expand",autoResultMap = true)
@Data
public class WeCustomerInfoExpand extends BaseEntity{
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 表单模版字段主键
     */
    private Long fieldTemplateId;

    /**
     * 客户表单字段信息值
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED,typeHandler = WeCustomerInfoValHandler.class)
    private List<CustomerInfoExpand> fieldCustomerInfoVal;


    @Data
    public static class CustomerInfoExpand{
        private String labelVal;

        private String infoVal;

    }

}