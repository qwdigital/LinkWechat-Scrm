package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.side.WeChatSide;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天工具侧边栏
 * @author kewen
 */
public interface WeChatSideMapper extends BaseMapper<WeChatSide> {

    /**
     * 查询侧边栏列表
     * @param h5 0 pc 1 h5
     * @return {@link WeChatSide}s
     */
     List<WeChatSide> selectWeChatSides(@Param("h5") String h5);


    /**
     * 更新侧边栏信息
     *
     * @param weChatSide 侧边栏信息
     * @return 结果
     */
     int updateWeChatSideById(WeChatSide weChatSide);



}
