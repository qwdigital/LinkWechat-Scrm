package com.linkwechat.domain.leads.leads.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 线索手动入线记录
 * </p>
 *
 * @author WangYX
 * @since 2023-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_leads_manual_add_record")
public class WeLeadsManualAddRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @TableId("id")
    private Long id;

    /**
     * 企微员工Id
     */
    private String weUserId;

    /**
     * 线索Id
     */
    private Long leadsId;


}
