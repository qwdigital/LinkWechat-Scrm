package com.linkwechat.domain.leads.sea.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公海配置
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 16:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "we_leads_sea_base_settings")
public class WeLeadsSeaBaseSettings extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 员工每日领取上限
     */
    @TableField(value = "max_claim")
    private Integer maxClaim;

    /**
     * 成员客户存量上限
     */
    @TableField(value = "stock_max_claim")
    private Integer stockMaxClaim;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableField("del_flag")
    private Integer delFlag;
}
