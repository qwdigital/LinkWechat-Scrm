package com.linkwechat.domain.side;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 聊天工具侧边栏
 * @author kewen
 */
@Data
@TableName("we_chat_item")
public class WeChatItem extends BaseEntity {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 侧边栏id
     */
    private Long sideId;

    /**
     * 素材id
     */
    private Long materialId;

}

