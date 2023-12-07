package com.linkwechat.domain.leads.record.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 线索跟进记录内容
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 18:41
 */
@Data
@Builder
@TableName(value = "we_leads_record_content")
@NoArgsConstructor
@AllArgsConstructor
public class WeLeadsFollowRecordContent {

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 线索跟进记录id
     */
    private Long recordId;

    /**
     * 记录项目名
     */
    private String itemKey;

    /**
     * 记录项目值
     */
    private String itemValue;

    /**
     * 用于排序
     */
    @TableField(value = "`rank`")
    private Integer rank;

    /**
     * 是否显示 0 显示 1 隐藏
     */
    @TableField(value = "is_visible")
    private Integer visible;

    /**
     * 是否存在附件 0否 1是
     */
    @TableField(value = "is_attachment")
    private Integer attachment;

    /**
     * 父类id,无父类默认值为0
     */
    private Long parentId;

    /**
     * 回复者id
     */
    private Long replierFromId;

    /**
     * 回复者企微Id
     */
    private String replierFromWeUserId;

    /**
     * 回复者
     */
    private String replierFrom;

    /**
     * 回复者头像
     */
    private String replierFromAvatar;

    /**
     * 回复对象id
     */
    private Long replierToId;

    /**
     * 回复对象企微id
     */
    private String replierToWeUserId;

    /**
     * 回复对象
     */
    private String replierTo;

    /**
     * 回复对象头像
     */
    private String replierToAvatar;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 子类数量
     */
    private Integer subNum;
}

