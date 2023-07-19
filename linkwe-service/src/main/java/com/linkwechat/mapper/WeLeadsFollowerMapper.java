package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
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

}
