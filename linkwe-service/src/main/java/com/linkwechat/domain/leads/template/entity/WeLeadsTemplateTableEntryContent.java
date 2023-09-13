package com.linkwechat.domain.leads.template.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 线索模版配置表项内容表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/06 19:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "we_leads_template_table_entry_content")
public class WeLeadsTemplateTableEntryContent {

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 模版表id
     */
    @TableField(value = "leads_template_settings_id")
    private Long leadsTemplateSettingsId;

    /**
     * 表项内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", update = "now()", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 删除标识 0 正常 1 删除
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

}
