package com.linkwechat.domain.know;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 员工活码标签
 * @TableName we_know_customer_code_tag
 */
@TableName(value ="we_know_customer_code_tag")
@Data
public class WeKnowCustomerCodeTag extends BaseEntity{
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标签id
     */
    private String tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 识客码id
     */
    private Long knowCustomerCodeId;

    /**
     * 是否是老客,1:是;0否
     */
    private Integer isOldCustomer;


}