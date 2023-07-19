package com.linkwechat.domain.leads.sea.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 线索公海规则修改记录
 *
 * @author WangYX
 * @since 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_leads_sea_rule_record")
public class WeLeadsSeaRuleRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 公海id
     */
    private Long seaId;

    /**
     * 是否自动回收（1 表示是，0 表示否）
     */
    private Integer isAutoRecovery;

    /**
     * 成员领取线索超过A天未能实现几次跟进，则回收至公海池。且必须在前几天内完成首次跟进。(A)
     */
    private Integer first;

    /**
     * 历史规则版本
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建人Id
     */
    private Long createById;

}
