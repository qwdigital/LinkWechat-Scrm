package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSeaVisibleRange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 公海可见范围 Mapper 接口
 * </p>
 *
 * @author WangYX
 * @since 2023-04-03
 */
public interface WeLeadsSeaVisibleRangeMapper extends BaseMapper<WeLeadsSeaVisibleRange> {

    /**
     * 获取员工公海的可见范围
     *
     * @param userId   员工Id
     * @param weUserId 员工企微Id
     * @return
     */
    List<WeLeadsSeaVisibleRange> getVisibleRange(@Param("userId") Long userId, @Param("weUserId") String weUserId);

}
