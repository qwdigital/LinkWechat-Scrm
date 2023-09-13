package com.linkwechat.domain.leads.sea.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 线索公海
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/10 16:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("we_leads_sea")
public class WeLeadsSea extends BaseEntity {

    /**
     * 主键Id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 公海名称
     */
    private String name;

    /**
     * 是否自动回收（1 表示是，0 表示否）
     */
    private Integer isAutoRecovery;

    /**
     * 公海线索数
     */
    private Integer num = 0;

    /**
     * 成员领取线索超过A天未能实现几次跟进，则回收至公海池。且必须在前几天内完成首次跟进。(A)
     */
    private Integer first;

    /**
     * 删除标识
     */
    private Integer delFlag;

    /**
     * 当前规则版本
     */
    private Integer version;

}
