package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCommunityNewGroup;
import com.linkwechat.wecom.domain.vo.WeCommunityNewGroupVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 社群运营 新客自动拉群 mapper接口
 *
 * @author kewen
 * @date 2021-02-19
 */
public interface WeCommunityNewGroupMapper extends BaseMapper<WeCommunityNewGroup> {

    /**
     * 查询新客自动拉群列表
     *
     * @param communityNewGroup 搜索信息
     * @return {@link WeCommunityNewGroup}s
     */
    List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(@Param("empleCodeName") String empleCodeName, @Param("createBy") String createBy
            ,@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 获取新客自动拉群详细信息
     * @param newGroupId 主键id
     * @return {@link WeCommunityNewGroupVo} 自动拉群信息
     */
    WeCommunityNewGroupVo selectWeCommunityNewGroupById(@Param("newGroupId") Long newGroupId);

    /**
     * 删除新客自动拉群
     *
     * @param idList id列表
     * @return
     */
    int batchRemoveWeCommunityNewGroupIds(@Param("ids") List<String> idList);

    /**
     * 通过id查询新客自动拉群信息列表
     *
     * @param ids id列表
     * @return {@link WeCommunityNewGroup} 新客自动拉群信息
     */
    List<WeCommunityNewGroupVo> selectWeCommunityNewGroupByIds(@Param("ids") List<String> ids);

}
