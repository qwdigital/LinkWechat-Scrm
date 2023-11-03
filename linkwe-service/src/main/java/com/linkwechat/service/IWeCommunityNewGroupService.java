package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.query.WeCommunityNewGroupQuery;
import com.linkwechat.domain.community.vo.WeCommunityNewGroupTabCountVo;
import com.linkwechat.domain.community.vo.WeCommunityNewGroupTableVo;
import com.linkwechat.domain.community.vo.WeCommunityNewGroupTrendCountVo;
import org.apache.ibatis.annotations.Param;

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



    /**
     * 获取头部统计
     * @param id
     * @return
     */
    WeCommunityNewGroupTabCountVo countTab(String id);


    /**
     * 获取折线统计
     * @param newGroup
     * @return
     */
    List<WeCommunityNewGroupTrendCountVo> findTrendCountVo(WeCommunityNewGroup newGroup);


    /**
     * 获取相关客户
     * @param weCommunityNewGroupQuery
     * @return
     */
    List<WeCommunityNewGroupTableVo> findWeCommunityNewGroupTable(WeCommunityNewGroupQuery weCommunityNewGroupQuery);


}
