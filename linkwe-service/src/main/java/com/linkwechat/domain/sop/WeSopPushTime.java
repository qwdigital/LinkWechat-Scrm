package com.linkwechat.domain.sop;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.media.WeMessageTemplate;
import lombok.Data;

import java.util.List;

/**
 * 
 * @TableName we_sop_push_time
 */
@TableName(value ="we_sop_push_time")
@Data
public class WeSopPushTime extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 推送开时间
     */
    private String pushStartTime;


    /**
     * sop基础id
     */
    private Long sopBaseId;

    /**
     * 推送结束时间
     */
    private String pushEndTime;

    /**
     * 推送时间类型(1:特定时间推送，比如2022-08-21推送日期;
2:周期推送，数字字符串型1-7，对应周一到周日;3:相对推送时间,数字字符串型，比如2022-08-21添加的客户，那么相对这个时间第一天推送，则值为1，但是对应的实际推送时间为，2022-08-22)
     */
    private Integer pushTimeType;

    /**
     * 推送时间前缀，分为数字型跟日期格式行字符串
     */
    private String pushTimePre;

    /**
     * 删除标识 0 有效 1删除
     */
    @TableLogic
    private Integer delFlag;


    /**
     * 素材新增编辑传入
     */
    @TableField(exist = false)
    private List<WeMessageTemplate> attachments;

    /**
     * 语素材返回展示
     */
    @TableField(exist = false)
    private List<WeSopAttachments> weSopAttachments;


}