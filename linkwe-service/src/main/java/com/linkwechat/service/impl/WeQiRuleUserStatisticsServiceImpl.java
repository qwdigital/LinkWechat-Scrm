package com.linkwechat.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeQiRuleUserStatisticsMapper;
import com.linkwechat.domain.WeQiRuleUserStatistics;
import com.linkwechat.service.IWeQiRuleUserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 会话质检员工数据统计表(WeQiRuleUserStatistics)
 *
 * @author danmo
 * @since 2023-05-17 13:50:44
 */
@Service
public class WeQiRuleUserStatisticsServiceImpl extends ServiceImpl<WeQiRuleUserStatisticsMapper, WeQiRuleUserStatistics> implements IWeQiRuleUserStatisticsService {


}
