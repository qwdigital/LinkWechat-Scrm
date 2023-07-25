package com.linkwechat.domain.leads.record.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 跟进记录内容
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/18 15:17
 */
@Data
public class WeLeadsFollowRecordContentVO {

    /**
     * id
     */
    private Long id;

    /**
     * 记录项目名
     */
    private String itemKey;

    /**
     * 记录项目值
     */
    private List<Content> itemValue;

    /**
     * 记录内容
     */
    @Data
    public static class Content {
        /**
         * 内容
         */
        private String content;

        /**
         * 是否存在附件 0否 1是
         */
        private Integer isAttachment;

        /**
         * 创建日期
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date createTime;

        /**
         * 附件列表
         */
        private List<WeLeadsFollowRecordAttachmentVO> attachments;

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
         * 子类数量
         */
        private Integer subNum;

    }
}
