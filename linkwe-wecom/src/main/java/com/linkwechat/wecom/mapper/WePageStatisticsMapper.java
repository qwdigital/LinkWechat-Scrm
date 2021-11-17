package com.linkwechat.wecom.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.linkwechat.wecom.domain.dto.WePageCountDto;
import com.linkwechat.wecom.domain.query.WePageStateQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.wecom.domain.WePageStatistics;

/**
 * 首页数据统计表(WePageStatistics)
 *
 * @author danmo
 * @since 2021-11-16 16:20:31
 */
@Repository()
@Mapper
public interface WePageStatisticsMapper extends BaseMapper<WePageStatistics> {
    /**
     * 按天维度查询数据统计
     * @param wePageStateQuery 入参
     * @return List<WeUserBehaviorDataDto>
     */
    public List<WePageCountDto> getDayCountData(WePageStateQuery wePageStateQuery);
    /**
     * 按周维度查询数据统计
     * @param wePageStateQuery 入参
     * @return List<WeUserBehaviorDataDto>
     */
    public List<WePageCountDto> getWeekCountData(WePageStateQuery wePageStateQuery);
    /**
     * 按月维度查询数据统计
     * @param wePageStateQuery 入参
     * @return List<WeUserBehaviorDataDto>
     */
    public List<WePageCountDto> getMonthCountData(WePageStateQuery wePageStateQuery);
}

