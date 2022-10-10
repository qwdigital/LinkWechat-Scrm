package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.query.WeCommunityNewGroupQuery;
import com.linkwechat.domain.community.vo.WeCommunityNewGroupVo;
import com.linkwechat.domain.community.vo.WeCommunityWeComeMsgVo;

import java.util.List;
import java.util.Optional;

/**
 *  新客自动拉群
 */
public interface IWeCommunityNewGroupService extends IService<WeCommunityNewGroup> {
    /**
     * 添加新客自动拉群信息
     *
     * @param weCommunityNewGroupQuery 信息
     * @return 结果
     */
    void add(WeCommunityNewGroupQuery weCommunityNewGroupQuery);


    /**
     * 查询新客自动拉群列表
     *
     * @param weCommunityNewGroup 新科拉群过滤条件
     * @return WeCommunityNewGroupVo
     */
    List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(WeCommunityNewGroup weCommunityNewGroup);



    /**
     * 修改新客自动拉群
     *
     * @param weCommunityNewGroupQuery 信息
     * @return 结果
     */
    void updateWeCommunityNewGroup(WeCommunityNewGroupQuery weCommunityNewGroupQuery);


    WeCommunityWeComeMsgVo getWelcomeMsgByState(String state);
}
