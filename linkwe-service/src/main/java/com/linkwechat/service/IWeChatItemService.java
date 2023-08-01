package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.side.WeChatItem;
import com.linkwechat.domain.side.dto.WeChatItemDto;
import com.linkwechat.domain.side.vo.WeChatSideVo;
import java.util.List;

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
     void checkItems(WeChatItemDto chatItemDto);

    /**
     * h5素材列表
     *
     * @param sideId 侧边栏id
     * @return
     */
     List<WeChatSideVo> chatItems(Long sideId, String keyword, String mediaType, String userId);





}
