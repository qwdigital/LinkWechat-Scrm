package com.linkwechat.wecom.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.domain.elastic.ElasticSearchEntity;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.wecom.domain.WeChatContactMapping;
import com.linkwechat.wecom.domain.WeCustomer;

import java.util.List;

/**
 * 聊天关系映射Service接口
 *
 * @author ruoyi
 * @date 2020-12-27
 */
public interface IWeChatContactMappingService extends IService<WeChatContactMapping> {
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
     * 批量删除聊天关系映射
     *
     * @param ids 需要删除的聊天关系映射ID
     * @return 结果
     */
    public int deleteWeChatContactMappingByIds(Long[] ids);

    /**
     * 删除聊天关系映射信息
     *
     * @param id 聊天关系映射ID
     * @return 结果
     */
    public int deleteWeChatContactMappingById(Long id);

    /**
     * 保存
     *
     * @param query
     */
    public List<JSONObject> saveWeChatContactMapping(List<JSONObject> query);

    /**
     * 按客户查询关系映射列表
     */
    PageInfo<WeCustomer> listByCustomer();
}
