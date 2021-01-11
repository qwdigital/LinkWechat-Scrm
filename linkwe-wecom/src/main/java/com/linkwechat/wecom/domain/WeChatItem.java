package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 聊天工具侧边栏
 * @author kewen
 */
@Data
public class WeChatItem extends BaseEntity {

    /**
     * id
     */
    private Long itemId;

    /**
     * 侧边栏id
     */
    private Long sideId;

    /**
     * 素材id
     */
    private Long materialId;

}
