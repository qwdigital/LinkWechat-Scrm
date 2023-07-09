package com.linkwechat.service;

import java.util.Map;

/**
 * 朋友圈统计
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/20 9:53
 */
public interface IWeMomentsTaskStatisticService {

    /**
     * 员工统计
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @return {@link Map<String,Integer>}
     * @author WangYX
     * @date 2023/06/20 11:00
     */
    Map<String, Long> userStatistic(Long weMomentsTaskId);

    /**
     * 客户统计
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @return {@link Map< String, Integer>}
     * @author WangYX
     * @date 2023/06/20 13:54
     */
    Map<String, Long> customerStatistic(Long weMomentsTaskId);

    /**
     * 互动统计
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @return {@link Map<String, Long>}
     * @author WangYX
     * @date 2023/06/20 16:09
     */
    Map<String, Long> interactStatistic(Long weMomentsTaskId);


}
