package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeChatSide;
import com.linkwechat.wecom.domain.vo.WeChatSideVo;
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
    public List<WeChatSide> selectWeChatSides(@Param("h5") String h5);


    /**
     * 更新侧边栏信息
     *
     * @param weChatSide 侧边栏信息
     * @return 结果
     */
    public int updateWeChatSideById(WeChatSide weChatSide);



}
