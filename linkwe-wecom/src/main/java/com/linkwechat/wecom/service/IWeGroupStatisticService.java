package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeGroupStatistic;
import com.linkwechat.wecom.domain.dto.WePageCountDto;
import com.linkwechat.wecom.domain.query.WePageStateQuery;

import java.util.List;

/**
 * 群聊数据统计数据
Service接口
 *
 * @author ruoyi
 * @date 2021-02-24
 */
public interface IWeGroupStatisticService extends IService<WeGroupStatistic> {

    /**
     * 查询列表
     */
    List<WeGroupStatistic> queryList(WeGroupStatistic weGroupStatistic);

    /**
     * 按日期查询当天数据统计结果
     * @param dateTime
     * @return WeUserBehaviorDataDto
     */
    WePageCountDto getCountDataByDay(String dateTime, String type);

    /**
     * 按天维度查询数据统计
     * @param wePageStateQuery 入参
     * @return List<WeUserBehaviorDataDto>
     */
    List<WePageCountDto> getDayCountData(WePageStateQuery wePageStateQuery);
    /**
     * 按周维度查询数据统计
     * @param wePageStateQuery 入参
     * @return List<WeUserBehaviorDataDto>
     */
    List<WePageCountDto> getWeekCountData(WePageStateQuery wePageStateQuery);

    /**
     * 按月维度查询数据统计
     * @param wePageStateQuery 入参
     * @return List<WeUserBehaviorDataDto>
     */
    List<WePageCountDto> getMonthCountData(WePageStateQuery wePageStateQuery);
}
