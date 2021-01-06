package com.linkwechat.wecom.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeChatContactMapping;

import java.util.List;

/**
 * 聊天关系映射Mapper接口
 *
 * @author ruoyi
 * @date 2020-12-27
 */
public interface WeChatContactMappingMapper extends BaseMapper<WeChatContactMapping> {
    /**
     * 查询聊天关系映射
     *
     * @param id 聊天关系映射ID
     * @return 聊天关系映射
     */
    public WeChatContactMapping selectWeChatContactMappingById(Long id);

    /**
     * 查询聊天关系映射列表
     *
     * @param weChatContactMapping 聊天关系映射
     * @return 聊天关系映射集合
     */
    public List<WeChatContactMapping> selectWeChatContactMappingList(WeChatContactMapping weChatContactMapping);

    /**
     * 新增聊天关系映射
     *
     * @param weChatContactMapping 聊天关系映射
     * @return 结果
     */
    public int insertWeChatContactMapping(WeChatContactMapping weChatContactMapping);

    /**
     * 修改聊天关系映射
     *
     * @param weChatContactMapping 聊天关系映射
     * @return 结果
     */
    public int updateWeChatContactMapping(WeChatContactMapping weChatContactMapping);

    /**
     * 删除聊天关系映射
     *
     * @param id 聊天关系映射ID
     * @return 结果
     */
    public int deleteWeChatContactMappingById(Long id);

    /**
     * 批量删除聊天关系映射
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeChatContactMappingByIds(Long[] ids);
}
