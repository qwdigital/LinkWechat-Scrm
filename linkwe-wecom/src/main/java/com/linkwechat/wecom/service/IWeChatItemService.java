package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeChatItem;
import com.linkwechat.wecom.domain.dto.WeChatItemDto;
import com.linkwechat.wecom.domain.vo.WeChatSideVo;
import org.apache.ibatis.annotations.Param;

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
    public int checkItems(WeChatItemDto chatItemDto);

    /**
     * h5素材列表
     *
     * @param sideId 侧边栏id
     * @return
     */
    public List<WeChatSideVo> chatItems(Long sideId,String keyword,String mediaType,String userId);


    /**
     * h5素材列表(海报)
     *
     * @param sideId  侧边栏id
     * @param keyword 关键词
     * @return
     */
    public List<WeChatSideVo> findChatPostsItems(Long sideId,String keyword,String mediaType,String userId);



}
