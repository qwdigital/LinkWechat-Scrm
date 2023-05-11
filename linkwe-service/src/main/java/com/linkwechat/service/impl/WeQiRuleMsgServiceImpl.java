package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeQiRuleMsg;
import com.linkwechat.domain.qirule.query.WeQiRuleStatisticsTableListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleStatisticsTableVo;
import com.linkwechat.mapper.WeQiRuleMsgMapper;
import com.linkwechat.service.IWeQiRuleMsgService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 质检规则会话表(WeQiRuleMsg)
 *
 * @author danmo
 * @since 2023-05-08 16:52:07
 */
@Service
public class WeQiRuleMsgServiceImpl extends ServiceImpl<WeQiRuleMsgMapper, WeQiRuleMsg> implements IWeQiRuleMsgService {


    @Override
    public List<WeQiRuleStatisticsTableVo> getRuleTableStatistics(WeQiRuleStatisticsTableListQuery query) {
       return this.baseMapper.getRuleTableStatistics(query);
    }
}
