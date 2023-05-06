package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeQiRule;
import com.linkwechat.domain.qirule.query.WeQiRuleAddQuery;
import com.linkwechat.domain.qirule.query.WeQiRuleListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleDetailVo;
import com.linkwechat.domain.qirule.vo.WeQiRuleListVo;

import java.util.List;

/**
 * 质检规则表(WeQiRule)
 *
 * @author danmo
 * @since 2023-05-05 16:57:30
 */
public interface IWeQiRuleService extends IService<WeQiRule> {

    void addQiRule(WeQiRuleAddQuery query);

    void updateQiRule(WeQiRuleAddQuery query);

    WeQiRuleDetailVo getQiRuleDetail(Long id);

    List<WeQiRuleListVo> getQiRuleList(WeQiRuleListQuery query);

    void delQiRule(List<Long> ids);
}
