package com.linkwechat.wecom.domain;

import com.linkwechat.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 聊天工具 侧边栏栏 素材收藏
 *
 * @author kwen
 */
@Data
public class WeChatCollection extends BaseEntity {

    /**
     * id
     */
    private Long collectionId;

    /**
     * 素材id
     */
    private Long materialId;

    /**
     * 用户id
     */
    private String userId;

}
