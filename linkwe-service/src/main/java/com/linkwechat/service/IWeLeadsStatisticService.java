package com.linkwechat.service;

import com.linkwechat.domain.leads.leads.vo.WeLeadsConversionRateVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsDataTrendVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsUserFollowTop5VO;

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

    /**
     * 线索转化率Top5
     *
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime   结束时间 yyyy-MM-dd
     * @return {@link List< WeLeadsConversionRateVO>}
     * @author WangYX
     * @date 2023/07/19 15:14
     */
    List<WeLeadsConversionRateVO> conversionTop5(String beginTime, String endTime);

    /**
     * 员工线索跟进top5
     *
     * @param beginTime
     * @param endTime
     * @return {@link List< WeLeadsUserFollowTop5VO>}
     * @author WangYX
     * @date 2023/07/19 16:15
     */
    List<WeLeadsUserFollowTop5VO> userFollowTop5(String beginTime, String endTime);
}
