package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.query.WeLeadsUserStatisticRequest;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsUserFollowTop5VO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsUserStatisticVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 线索 Mapper 接口
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:43
 */
@Mapper
public interface WeLeadsFollowerMapper extends BaseMapper<WeLeadsFollower> {

    /**
     * 获取跟进人在公海中跟进数量
     *
     * @param userId 员工Id
     * @return {@link List<WeLeadsFollower>}
     * @author WangYX
     * @date 2023/07/11 17:10
     */
    List<WeLeadsFollower> getLeadsFollower(@Param("userId") Long userId);


    /**
     * 员工统计
     *
     * @param userIds 员工Id集合
     * @return {@link List<  WeLeadsUserFollowTop5VO >}
     * @author WangYX
     * @date 2023/07/19 17:58
     */
    List<WeLeadsFollowerVO> userStatistic(@Param("userIds") List<Long> userIds);

    /**
     * 获取跟进人名单
     *
     * @param leadsId 线索Id
     * @return {@link List< WeLeadsFollowerVO>}
     * @author WangYX
     * @date 2023/08/17 10:24
     */
    List<WeLeadsFollower> getFollowerList(@Param("leadsId") Long leadsId);

}
