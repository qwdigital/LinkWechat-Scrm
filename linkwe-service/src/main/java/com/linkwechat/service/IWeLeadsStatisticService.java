package com.linkwechat.service;

import com.linkwechat.domain.leads.leads.vo.WeLeadsDataTrendVO;

import java.util.List;
import java.util.Map;

/**
 * 线索统计 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 10:21
 */
public interface IWeLeadsStatisticService {

    /**
     * 统计
     *
     * @return {@link Map<String,Object>}
     * @author WangYX
     * @date 2023/07/19 10:24
     */
    Map<String, Object> statistic();

    /**
     * 数据趋势
     *
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime   结束时间 yyyy-MM-dd
     * @return {@link List<WeLeadsDataTrendVO>}
     * @author WangYX
     * @date 2023/07/19 11:15
     */
    List<WeLeadsDataTrendVO> dataTrend(String beginTime, String endTime);
}
