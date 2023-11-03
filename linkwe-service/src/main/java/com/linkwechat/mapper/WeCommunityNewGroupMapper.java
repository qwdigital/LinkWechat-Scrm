package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.query.WeCommunityNewGroupQuery;
import com.linkwechat.domain.community.vo.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WeCommunityNewGroupMapper extends BaseMapper<WeCommunityNewGroup> {


    /**
     * 获取头部统计
     * @param newGroup
     * @return
     */
    WeCommunityNewGroupTabCountVo countTab(@Param("newGroup") WeCommunityNewGroup newGroup);


    /**
     * 获取折线统计
     * @param newGroup
     * @return
     */
    List<WeCommunityNewGroupTrendCountVo> findTrendCountVo(@Param("newGroup") WeCommunityNewGroup newGroup);


    /**
     * 获取相关客户
     * @param weCommunityNewGroupQuery
     * @return
     */
    List<WeCommunityNewGroupTableVo> findWeCommunityNewGroupTable(@Param("query") WeCommunityNewGroupQuery weCommunityNewGroupQuery);




}
