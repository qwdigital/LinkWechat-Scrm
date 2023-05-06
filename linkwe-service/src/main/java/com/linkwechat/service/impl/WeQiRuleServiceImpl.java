package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeQiRule;
import com.linkwechat.domain.WeQiRuleScope;
import com.linkwechat.domain.qirule.query.WeQiRuleAddQuery;
import com.linkwechat.domain.qirule.query.WeQiRuleListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleDetailVo;
import com.linkwechat.domain.qirule.vo.WeQiRuleListVo;
import com.linkwechat.domain.qirule.vo.WeQiRuleScopeVo;
import com.linkwechat.domain.qirule.vo.WeQiRuleUserVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeQiRuleMapper;
import com.linkwechat.service.IWeQiRuleScopeService;
import com.linkwechat.service.IWeQiRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 质检规则表(WeQiRule)
 *
 * @author danmo
 * @since 2023-05-05 16:57:30
 */
@Service
public class WeQiRuleServiceImpl extends ServiceImpl<WeQiRuleMapper, WeQiRule> implements IWeQiRuleService {

    @Autowired
    private IWeQiRuleScopeService weQiRuleScopeService;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Override
    public void addQiRule(WeQiRuleAddQuery query) {
        WeQiRule weQiRule = new WeQiRule();
        weQiRule.setName(query.getName());
        weQiRule.setChatType(query.getChatType());
        weQiRule.setManageUser(query.getManageUser());
        weQiRule.setTimeOut(query.getTimeOut());
        if (save(weQiRule)) {
            //保存范围数据
            weQiRuleScopeService.saveBatchByQiId(weQiRule.getId(), query.getQiUserInfos());
        }
    }

    @Override
    public void updateQiRule(WeQiRuleAddQuery query) {
        WeQiRule weQiRule = getById(query.getQiId());
        if (ObjectUtil.isNull(weQiRule)) {
            throw new WeComException("无效ID");
        }
        if (StringUtils.isNotBlank(query.getName())) {
            weQiRule.setName(query.getName());
        }
        if (StringUtils.isNotBlank(query.getManageUser())) {
            weQiRule.setManageUser(query.getManageUser());
        }
        if (StringUtils.isNotBlank(query.getTimeOut())) {
            weQiRule.setTimeOut(query.getTimeOut());
        }
        if (ObjectUtil.isNotNull(query.getChatType())) {
            weQiRule.setChatType(query.getChatType());
        }
        if (updateById(weQiRule)) {
            //保存范围数据
            weQiRuleScopeService.updateBatchByQiId(weQiRule.getId(), query.getQiUserInfos());
        }
    }

    @Override
    public WeQiRuleDetailVo getQiRuleDetail(Long id) {
        WeQiRule weQiRule = getById(id);
        if (ObjectUtil.isNull(weQiRule)) {
            throw new WeComException("无效ID");
        }
        WeQiRuleDetailVo weQrCodeDetailVo = new WeQiRuleDetailVo();
        weQrCodeDetailVo.setId(weQiRule.getId());
        weQrCodeDetailVo.setName(weQiRule.getName());
        weQrCodeDetailVo.setTimeOut(weQiRule.getTimeOut());
        weQrCodeDetailVo.setChatType(weQiRule.getChatType());

        List<WeQiRuleScope> weQiRuleScopeList = weQiRuleScopeService.getQiRuleScopeByQiIds(Lists.newArrayList(id));

        Set<String> userIds = new HashSet<>();
        if (CollectionUtil.isNotEmpty(weQiRuleScopeList)) {
            List<String> scopeUsers = weQiRuleScopeList.stream().map(WeQiRuleScope::getUserId).collect(Collectors.toList());
            userIds.addAll(scopeUsers);
        }
        String manageUser = weQiRule.getManageUser();
        if (StringUtils.isNotBlank(manageUser)) {
            List<String> manageUserIds = Arrays.stream(manageUser.split(",")).collect(Collectors.toList());
            userIds.addAll(manageUserIds);
        }

        Map<String, String> userMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(userIds)) {
            SysUserQuery userQuery = new SysUserQuery();
            userQuery.setWeUserIds(new ArrayList<>(userIds));
            List<SysUserVo> userList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
            if (CollectionUtil.isNotEmpty(userList)) {
                userMap = userList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, SysUserVo::getUserName, (key1, key2) -> key2));
            }
        }

        List<WeQiRuleScopeVo> ruleScopeVoList = new LinkedList<>();
        if (CollectionUtil.isNotEmpty(weQiRuleScopeList)) {
            Map<String, List<WeQiRuleScope>> scopeUserMap = weQiRuleScopeList.stream().collect(Collectors.groupingBy(WeQiRuleScope::getScopeId));
            Map<String, String> finalUserMap = userMap;
            scopeUserMap.forEach((scopeId, ruleScopeList) -> {
                if (CollectionUtil.isNotEmpty(ruleScopeList)) {
                    WeQiRuleScopeVo ruleScope = new WeQiRuleScopeVo();
                    ruleScope.setQiId(ruleScopeList.get(0).getQiId());
                    ruleScope.setBeginTime(ruleScopeList.get(0).getBeginTime());
                    ruleScope.setEndTime(ruleScopeList.get(0).getEndTime());
                    ruleScope.setWorkCycle(ruleScopeList.get(0).getWorkCycle());
                    List<WeQiRuleUserVo> ruleUserVoList = new LinkedList<>();
                    for (WeQiRuleScope weQiRuleScope : ruleScopeList) {
                        WeQiRuleUserVo ruleUserVo = new WeQiRuleUserVo();
                        ruleUserVo.setUserId(weQiRuleScope.getUserId());
                        if (finalUserMap.containsKey(weQiRuleScope.getUserId())) {
                            ruleUserVo.setUserName(finalUserMap.get(weQiRuleScope.getUserId()));
                        }
                        ruleUserVoList.add(ruleUserVo);
                    }
                    ruleScope.setWeQiRuleUserList(ruleUserVoList);
                    ruleScopeVoList.add(ruleScope);
                }
            });
        }
        weQrCodeDetailVo.setQiRuleScope(ruleScopeVoList);


        if (StringUtils.isNotBlank(manageUser)) {
            Map<String, String> finalUserMap1 = userMap;
            List<WeQiRuleUserVo> manageUserInfoList = Arrays.stream(manageUser.split(",")).map(weManageUserId -> {
                WeQiRuleUserVo manageUserInfo = new WeQiRuleUserVo();
                manageUserInfo.setUserId(weManageUserId);
                if (finalUserMap1.containsKey(weManageUserId)) {
                    manageUserInfo.setUserName(finalUserMap1.get(weManageUserId));
                }
                return manageUserInfo;
            }).collect(Collectors.toList());
            weQrCodeDetailVo.setManageUserInfo(manageUserInfoList);
        }
        return weQrCodeDetailVo;
    }

    @Override
    public List<WeQiRuleListVo> getQiRuleList(WeQiRuleListQuery query) {
        List<WeQiRuleListVo> weQiRuleList = this.baseMapper.getQiRuleList(query);
        Set<String> userIds = new HashSet<>();
        if (CollectionUtil.isNotEmpty(weQiRuleList)) {
            for (WeQiRuleListVo weQiRuleListVo : weQiRuleList) {
                if (StringUtils.isNotBlank(weQiRuleListVo.getManageUser())) {
                    CollectionUtil.addAll(userIds, weQiRuleListVo.getManageUser().split(","));
                }
                if (CollectionUtil.isNotEmpty(weQiRuleListVo.getQiRuleScope())) {
                    List<String> scopeWeUserIds = weQiRuleListVo.getQiRuleScope().stream().map(WeQiRuleUserVo::getUserId).collect(Collectors.toList());
                    userIds.addAll(scopeWeUserIds);
                }
            }

            Map<String, String> userMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(userIds)) {
                SysUserQuery userQuery = new SysUserQuery();
                userQuery.setWeUserIds(new ArrayList<>(userIds));
                List<SysUserVo> userList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
                if (CollectionUtil.isNotEmpty(userList)) {
                    userMap = userList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, SysUserVo::getUserName, (key1, key2) -> key2));
                }
            }

            for (WeQiRuleListVo weQiRuleListVo : weQiRuleList) {
                Map<String, String> finalUserMap = userMap;
                String manageUser = weQiRuleListVo.getManageUser();
                if (StringUtils.isNotBlank(manageUser)) {

                    List<WeQiRuleUserVo> manageUserInfoList = Arrays.stream(manageUser.split(",")).map(weManageUserId -> {
                        WeQiRuleUserVo manageUserInfo = new WeQiRuleUserVo();
                        manageUserInfo.setUserId(weManageUserId);
                        if (finalUserMap.containsKey(weManageUserId)) {
                            manageUserInfo.setUserName(finalUserMap.get(weManageUserId));
                        }
                        return manageUserInfo;
                    }).collect(Collectors.toList());
                    weQiRuleListVo.setManageUserInfo(manageUserInfoList);
                }

                if (CollectionUtil.isNotEmpty(weQiRuleListVo.getQiRuleScope())) {
                    for (WeQiRuleUserVo weQiRuleUserVo : weQiRuleListVo.getQiRuleScope()) {
                        if (finalUserMap.containsKey(weQiRuleUserVo.getUserId())) {
                            weQiRuleUserVo.setUserName(finalUserMap.get(weQiRuleUserVo.getUserId()));
                        }
                    }
                }
            }

        }
        return weQiRuleList;
    }

    @Override
    public void delQiRule(List<Long> ids) {
        boolean update = update(new LambdaUpdateWrapper<WeQiRule>().set(WeQiRule::getDelFlag, 1).in(WeQiRule::getId, ids));
        if (update) {
            weQiRuleScopeService.delBatchByQiIds(ids);
        }
    }
}
