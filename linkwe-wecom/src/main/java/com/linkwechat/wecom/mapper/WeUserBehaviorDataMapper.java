package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeUserBehaviorData;
import com.linkwechat.wecom.domain.dto.WePageCountDto;
import com.linkwechat.wecom.domain.query.WePageStateQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 联系客户统计数据 Mapper接口
 *
 * @author ruoyi
 * @date 2021-02-24
 */
public interface WeUserBehaviorDataMapper extends BaseMapper<WeUserBehaviorData> {

    /**
     * 按日期查询当天数据统计结果
     * @param dateTime
     * @return WeUserBehaviorDataDto
     */
    public WePageCountDto getCountDataByDay(@Param("dateTime") String dateTime, @Param("type") String type);

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
