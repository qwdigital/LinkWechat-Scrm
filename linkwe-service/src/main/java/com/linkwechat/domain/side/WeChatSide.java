package com.linkwechat.domain.side;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("we_chat_side")
public class WeChatSide extends BaseEntity {
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     *  0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报
     */
    private String mediaType;

    /**
     * 聊天工具栏名称
     */
    private String sideName;

    /**
     * 已抓取素材数量
     */
    @TableField(exist = false)
    private Integer total;

    /**
     * 是否启用 0 启用 1 未启用
     */
    private Integer using;

    /**
     * 0 未删除 1 已删除
     */
    private int delFlag;
}
