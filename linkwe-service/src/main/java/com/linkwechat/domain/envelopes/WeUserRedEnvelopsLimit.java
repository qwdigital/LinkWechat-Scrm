package com.linkwechat.domain.envelopes;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

/**
 * 员工发送红包限额
 */
@Data
@Builder
@TableName("we_user_red_envelops_limit")
@AllArgsConstructor
@NoArgsConstructor
public class WeUserRedEnvelopsLimit extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 员工id
     */
    private String weUserId;

    /**
     * 单日每员工发红包次数
     */
    private Integer singleCustomerReceiveNum;

    /**
     * 单日每员工发红包总额
     */
    private Integer singleCustomerReceiveMoney;

    /**
     * 0:正常;1:删除;
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 多个id，使用逗号隔开
     */
    @TableField(exist = false)
    private String ids;


    /**
     * 今日已发放金额
     */
    @TableField(exist = false)
    private Integer todayIssuedAmount;


    /**
     * 今日未发放金额
     */
    @TableField(exist = false)
    private Integer todayNoIssuedAmount;


    /**
     * 今日已发放次数
     */
    @TableField(exist = false)
    private Integer todayIssuedNum;


    /**
     * 今日未发放次数
     */
    @TableField(exist = false)
    private Integer todayNoIssuedNum;


    /**
     * 累计发放次数
     */
    @TableField(exist = false)
    private Integer totalIssuedNum;


    /**
     * 累计方法金额
     */
    @TableField(exist = false)
    private Integer totalIssuedAmount;

    /**
     * 员工姓名
     */
    @TableField(exist = false)
    private String userName;
}

