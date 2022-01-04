package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeCommunityNewGroup;
import com.linkwechat.wecom.domain.vo.WeCommunityNewGroupVo;
import com.linkwechat.wecom.domain.vo.WeCommunityWeComeMsgVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社群运营 新客自动拉群 mapper接口
 *
 * @author kewen
 * @date 2021-02-19
 */
@Mapper
public interface WeCommunityNewGroupMapper extends BaseMapper<WeCommunityNewGroup> {

    /**
     * 查询新客自动拉群列表
     *
     * @param /emplCodeName 员工名称
     * @param /createBy     创建人
     * @param /beginTime    开始时间
     * @param /endTime      结束时间
     * @return {WeCommunityNewGroupVo}s 列表
     */
//    List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(@Param("emplCodeName") String emplCodeName, @Param("createBy") String createBy
//            , @Param("beginTime") String beginTime, @Param("endTime") String endTime);
    List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(WeCommunityNewGroup weCommunityNewGroup);
    /**
     * 获取新客自动拉群详细信息
     *
     * @param id 主键id
     * @return {@link WeCommunityNewGroupVo} 自动拉群信息
     */
    Optional<WeCommunityNewGroupVo> selectWeCommunityNewGroupById(@Param("id") Long id);

    /**
     * 删除新客自动拉群
     *
     * @param idList id列表
     * @return
     */
    int batchRemoveWeCommunityNewGroupByIds(@Param("ids") List<Long> idList);

    /**
     * 通过id查询新客自动拉群信息列表
     *
     * @param ids id列表
     * @return {@link WeCommunityNewGroup} 新客自动拉群信息
     */
    List<WeCommunityNewGroupVo> selectWeCommunityNewGroupByIds(@Param("ids") List<Long> ids);

    /**
     * 通过员工活码id逻辑删除对应新客拉群信息
     * @param emplCodeId 员工活码id
     * @return 结果
     */
    int removeWeCommunityNewGroupByEmplCodeId(@Param("emplCodeId") Long emplCodeId);

    WeCommunityWeComeMsgVo getWelcomeMsgByState(@Param("state") String state);
}
