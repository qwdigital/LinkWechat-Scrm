package com.linkwechat.domain.leads.record.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线索跟进记录内容附件表
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:35
 */
@Data
@Builder
@TableName(value = "we_leads_follow_record_attachment")
@NoArgsConstructor
@AllArgsConstructor
public class WeLeadsFollowRecordAttachment {

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 线索跟进记录内容Id
     */
    private Long contentId;

    /**
     * 附件类型（0视频，1图片，2文件）
     */
    private Integer type;

    /**
     * 附件名称
     */
    private String title;

    /**
     * 附件地址
     */
    private String url;
}

