package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeQiRuleMsg;
import com.linkwechat.domain.qirule.query.WeQiRuleStatisticsTableListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleStatisticsTableVo;

import java.util.List;

/**
 * 质检规则会话表(WeQiRuleMsg)
 *
 * @author danmo
 * @since 2023-05-08 16:52:07
 */
public interface IWeQiRuleMsgService extends IService<WeQiRuleMsg> {

    List<WeQiRuleStatisticsTableVo> getRuleTableStatistics(WeQiRuleStatisticsTableListQuery query);
}
