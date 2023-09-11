package com.linkwechat.domain.leads.record.vo;

import lombok.Data;

/**
 * 线索跟进记录内容附件
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:35
 */
@Data
public class WeLeadsFollowRecordAttachmentVO {

    /**
     * 附件类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file)
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

