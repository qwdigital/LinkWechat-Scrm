package com.linkwechat.domain.know;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 识客码统计相关
 * @TableName we_know_customer_code_count
 */
@TableName(value ="we_know_customer_code_count")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeKnowCustomerCodeCount extends BaseEntity{
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 识客码主键id
     */
    private Long knowCustomerId;


    /**
     * 0:新客;1:老客
     */
    private Integer newOrOld;
//
//    /**
//     * 当前微信用户在当前企业下的external_userid
//     */
//    @TableField("external_userid")
//    private String externalUserid;

    /**
     * 客户在微信平台的unionid
     */
    private String unionid;




}