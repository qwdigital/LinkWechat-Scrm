package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeChatSide;

import java.util.List;

/**
 * 聊天工具侧边栏
 * @author kewen
 */
public interface WeChatSideMapper extends BaseMapper<WeChatSide> {

    /**
     * 查询侧边栏列表
     *
     * @return {@link WeChatSide}s
     */
    public List<WeChatSide> selectWeChatSides();


    /**
     * 更新侧边栏信息
     *
     * @param weChatSide 侧边栏信息
     * @return 结果
     */
    public int updateWeChatSideById(WeChatSide weChatSide);

}
