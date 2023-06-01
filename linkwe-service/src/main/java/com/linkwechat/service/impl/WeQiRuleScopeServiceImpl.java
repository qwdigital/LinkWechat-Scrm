package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeQiRuleScope;
import com.linkwechat.domain.qirule.query.WeQiUserInfoQuery;
import com.linkwechat.mapper.WeQiRuleScopeMapper;
import com.linkwechat.service.IWeQiRuleScopeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 质检规则范围表(WeQiRuleScope)
 *
 * @author danmo
 * @since 2023-05-05 16:57:31
 */
@Service
public class WeQiRuleScopeServiceImpl extends ServiceImpl<WeQiRuleScopeMapper, WeQiRuleScope> implements IWeQiRuleScopeService {

    @Override
    public void saveBatchByQiId(Long qiId, List<WeQiUserInfoQuery> qiUserInfos) {
        if (CollectionUtil.isNotEmpty(qiUserInfos)) {
            List<WeQiRuleScope> scopeList = new ArrayList<>();
            for (WeQiUserInfoQuery userInfo : qiUserInfos) {
                String uuId = RandomUtil.randomString(18);
                List<WeQiRuleScope> scopeUserList = Optional.ofNullable(userInfo.getUserIds()).orElseGet(ArrayList::new).stream().map(userId -> {
                    WeQiRuleScope qiRuleScope = new WeQiRuleScope();
                    qiRuleScope.setScopeId(uuId);
                    qiRuleScope.setQiId(qiId);
                    qiRuleScope.setUserId(userId);
                    if (CollectionUtil.isNotEmpty(userInfo.getWorkCycle())) {
                        qiRuleScope.setWorkCycle(userInfo.getWorkCycle().stream().map(String::valueOf).collect(Collectors.joining(",")));
                    }
                    qiRuleScope.setBeginTime(userInfo.getBeginTime());
                    qiRuleScope.setEndTime(userInfo.getEndTime());
                    return qiRuleScope;
                }).collect(Collectors.toList());
                scopeList.addAll(scopeUserList);
            }
            saveBatch(scopeList);
        }
    }

    @Override
    public Boolean delBatchByQiIds(List<Long> qiIds) {
        WeQiRuleScope weQiRuleScope = new WeQiRuleScope();
        weQiRuleScope.setDelFlag(1);
        return this.update(weQiRuleScope, new LambdaQueryWrapper<WeQiRuleScope>().in(WeQiRuleScope::getQiId, qiIds));
    }

    @Override
    public void updateBatchByQiId(Long qiId, List<WeQiUserInfoQuery> qiUserInfos) {
        if (CollectionUtil.isNotEmpty(qiUserInfos)) {
            delBatchByQiIds(ListUtil.toList(qiId));
            this.saveBatchByQiId(qiId, qiUserInfos);
        }
    }

    @Override
    public List<WeQiRuleScope> getQiRuleScopeByQiIds(List<Long> qiIds) {
        return list(new LambdaQueryWrapper<WeQiRuleScope>().in(WeQiRuleScope::getQiId,qiIds).eq(WeQiRuleScope::getDelFlag,0));
    }
}
