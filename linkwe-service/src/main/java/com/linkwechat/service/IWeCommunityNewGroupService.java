package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import java.util.List;

/**
 *  新客自动拉群
 */
public interface IWeCommunityNewGroupService extends IService<WeCommunityNewGroup> {
    /**
     * 添加新客自动拉群信息
     *
     * @param communityNewGroup 信息
     * @return 结果
     */
    void add(WeCommunityNewGroup communityNewGroup);


    /**
     * 根据主键获取详情
     * @param id
     * @return
     */
    WeCommunityNewGroup findWeCommunityNewGroupById(String id);


    /**
     * 查询新客自动拉群列表
     *
     * @param weCommunityNewGroup 新科拉群过滤条件
     * @return WeCommunityNewGroupVo
     */
    List<WeCommunityNewGroup> selectWeCommunityNewGroupList(WeCommunityNewGroup weCommunityNewGroup);



    /**
     * 修改新客自动拉群
     *
     * @param communityNewGroup 信息
     * @return 结果
     */
    void updateWeCommunityNewGroup(WeCommunityNewGroup communityNewGroup);


}
