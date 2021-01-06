package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeChatItem;
import com.linkwechat.wecom.domain.dto.WeChatItemDto;

/**
 * 聊天工具侧边栏
 * @author kewen
 */
public interface IWeChatItemService extends IService<WeChatItem> {

    /**
     * 侧边栏抓取素材
     *
     * @param chatItemDto 侧边栏素材
     * @return 结果
     */
    public int checkItems(WeChatItemDto chatItemDto);

}
