package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeQiRuleManageStatistics;
import com.linkwechat.domain.qirule.query.WeQiRuleWeeklyListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleWeeklyListVo;

import java.util.List;

/**
 * 会话质检督导数据表(WeQiRuleManageStatistics)
 *
 * @author danmo
 * @since 2023-05-17 13:50:44
 */
public interface IWeQiRuleManageStatisticsService extends IService<WeQiRuleManageStatistics> {

    List<WeQiRuleWeeklyListVo> getWeeklyList(WeQiRuleWeeklyListQuery query);
} 
