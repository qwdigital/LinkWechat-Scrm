package com.linkwechat.domain.groupchat.vo;

import lombok.Data;

/**
 * 群简要信息
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/09/28 13:58
 */
@Data
public class WeGroupSimpleVo {

    /**
     * 群聊Id
     */
    private String chatId;

    /**
     * 群名
     */
    private String name;

    /**
     * 群头像
     */
    private String avatar;

}
