package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群SOP规则
 */
@Data
@TableName("we_group_sop")
@EqualsAndHashCode(callSuper = true)
public class WeGroupSop extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long ruleId;

    /**
     * 规则名
     */
    private String ruleName;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 开始执行时间
     */
    @TableField(value = "start_time")
    private String startExeTime;

    /**
     * 结束执行时间
     */
    @TableField(value = "end_time")
    private String stopExeTime;

    /**
     * 逻辑删除字段
     */
    @TableLogic(value = "0", delval = "1")
    private Integer delFlag;

}
