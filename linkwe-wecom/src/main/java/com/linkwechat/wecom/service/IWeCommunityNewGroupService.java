package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCommunityNewGroup;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;
import com.linkwechat.wecom.domain.vo.WeCommunityNewGroupVo;
import com.linkwechat.wecom.domain.vo.WeCommunityWeComeMsgVo;

import java.util.List;
import java.util.Optional;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
public interface IWeCommunityNewGroupService extends IService<WeCommunityNewGroup> {

    /**
     * 添加新客自动拉群信息
     *
     * @param communityNewGroupDto 信息
     * @return 结果
     */
    int add(WeCommunityNewGroupDto communityNewGroupDto);

    /**
     * 查询新客自动拉群列表
     *
     * @param weCommunityNewGroup 新科拉群过滤条件
     * @return {@link WeCommunityNewGroupVo}s
     */
    List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(WeCommunityNewGroup weCommunityNewGroup);

    /**
     * 获取新客自动拉群详细信息
     *
     * @param id 主键id
     * @return {@link WeCommunityNewGroupVo} 自动拉群信息
     */
    Optional<WeCommunityNewGroupVo> selectWeCommunityNewGroupById(Long id);

    /**
     * 修改新客自动拉群
     *
     * @param id                   新客拉群id
     * @param communityNewGroupDto 信息
     * @return 结果
     */
    int updateWeCommunityNewGroup(Long id, WeCommunityNewGroupDto communityNewGroupDto);

    /**
     * 删除新客自动拉群
     *
     * @param idList id列表
     * @return
     */
    int batchRemoveWeCommunityNewGroupByIds(List<Long> idList);
//
//    /**
//     * 通过id查询新客自动拉群信息
//     *
//     * @param id id
//     * @return {@link WeCommunityNewGroup} 新客自动拉群信息
//     */
//    WeCommunityNewGroupVo selectWeCommunityNewGroupById(long id);

    /**
     * 通过id查询新客自动拉群信息列表
     *
     * @param ids id列表
     * @return {@link WeCommunityNewGroup} 新客自动拉群信息
     */
    List<WeCommunityNewGroupVo> selectWeCommunityNewGroupByIds(List<Long> ids);

    WeCommunityWeComeMsgVo getWelcomeMsgByState(String state);
}
