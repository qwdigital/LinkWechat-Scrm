package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.side.WeChatItem;
import com.linkwechat.domain.side.vo.WeChatSideVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 聊天工具侧边栏
 *
 * @author kewen
 */
public interface WeChatItemMapper extends BaseMapper<WeChatItem> {

    /**
     * 批量添加侧边栏素材
     *
     * @param items 侧边栏素材列表
     * @return 结果
     */
     int addItem(@Param("items") List<WeChatItem> items);

    /**
     * 删除侧边栏素材
     *
     * @param sideId 侧边栏id
     * @return 结果
     */
     int dropItem(@Param("sideId") Long sideId);


    /**
     * h5素材列表
     *
     * @param sideId  侧边栏id
     * @param keyword 关键词
     * @return
     */
     List<WeChatSideVo> findChatItems(@Param("sideId") Long sideId, @Param("keyword") String keyword, @Param("mediaType") String mediaType, @Param("userId") String userId);






}

