package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.*;
import com.linkwechat.domain.msgaudit.vo.WeChatContactMsgVo;
import com.linkwechat.domain.qirule.query.*;
import com.linkwechat.domain.qirule.vo.*;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeQiRuleMapper;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
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

    @Autowired
    private IWeQiRuleMsgService weQiRuleMsgService;

    @Autowired
    private IWeQiRuleMsgNoticeService weQiRuleMsgNoticeService;

    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Autowired
    private IWeQiRuleManageStatisticsService weQiRuleManageStatisticsService;

    @Autowired
    private IWeQiRuleUserStatisticsService weQiRuleUserStatisticsService;

    @Autowired
    private IWeQiRuleWeeklyUserDataService weQiRuleWeeklyUserDataService;

    @Transactional(rollbackFor = {Exception.class, WeComException.class})
    @Override
    public void addQiRule(WeQiRuleAddQuery query) {
        checkScope(query.getQiUserInfos());
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

    @Transactional(rollbackFor = {Exception.class, WeComException.class})
    @Override
    public void updateQiRule(WeQiRuleAddQuery query) {
        checkScope(query.getQiUserInfos());
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
        if (Objects.nonNull(query.getTimeOut())) {
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
    public PageInfo<WeQiRuleListVo> getQiRulePageList(WeQiRuleListQuery query) {
        List<WeQiRuleListVo> weQiRuleList = new LinkedList<>();
        List<Long> weQiRuleIdList = this.baseMapper.getQiIdsByQuery(query);
        if(CollectionUtil.isNotEmpty(weQiRuleIdList)){
            WeQiRuleListQuery ruleListQuery = new WeQiRuleListQuery();
            ruleListQuery.setQiRuleIds(weQiRuleIdList);
            weQiRuleList = getQiRuleList(ruleListQuery);
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
        }
        PageInfo<Long> pageIdInfo = new PageInfo<>(weQiRuleIdList);
        PageInfo<WeQiRuleListVo> pageInfo = new PageInfo<>(weQiRuleList);
        pageInfo.setTotal(pageIdInfo.getTotal());
        pageInfo.setPageNum(pageIdInfo.getPageNum());
        pageInfo.setPageSize(pageIdInfo.getPageSize());
        return pageInfo;
    }

    @Override
    public List<WeQiRuleListVo> getQiRuleList(WeQiRuleListQuery query) {
        return this.baseMapper.getQiRuleList(query);
    }

    @Override
    public void delQiRule(List<Long> ids) {
        boolean update = update(new LambdaUpdateWrapper<WeQiRule>().set(WeQiRule::getDelFlag, 1).in(WeQiRule::getId, ids));
        if (update) {
            weQiRuleScopeService.delBatchByQiIds(ids);
        }
    }

    @Override
    public List<WeQiRuleListVo> getQiRuleListByUserId(WeQiRuleListQuery query) {
        return this.baseMapper.getQiRuleListByUserId(query);
    }

    @Override
    public WeQiRuleStatisticsViewVo qiRuleViewStatistics(Long id) {
        WeQiRuleStatisticsViewVo viewVo = new WeQiRuleStatisticsViewVo();
        List<WeQiRuleMsg> ruleMsgList = weQiRuleMsgService.list(new LambdaQueryWrapper<WeQiRuleMsg>().eq(WeQiRuleMsg::getRuleId, id).eq(WeQiRuleMsg::getDelFlag, 0));
        if (CollectionUtil.isNotEmpty(ruleMsgList)) {
            List<WeQiRuleMsg> outTimeMsgList = ruleMsgList.stream().filter(ruleMsg -> ObjectUtil.notEqual(0, ruleMsg.getStatus())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(outTimeMsgList)) {
                viewVo.setTimeOutTotalNum(outTimeMsgList.size());
                BigDecimal timeOutTotalRate = new BigDecimal(outTimeMsgList.size() / ruleMsgList.size()).setScale(2, BigDecimal.ROUND_HALF_UP);
                viewVo.setTimeOutTotalRate(timeOutTotalRate.toString());

                List<WeQiRuleMsg> todayOutTimeMsgList = outTimeMsgList.stream()
                        .filter(outTimeMsg -> ObjectUtil.equal(DateUtil.formatDate(outTimeMsg.getCreateTime()), DateUtil.today()))
                        .collect(Collectors.toList());
                long todayMsgNum = ruleMsgList.stream()
                        .filter(outTimeMsg -> ObjectUtil.equal(DateUtil.formatDate(outTimeMsg.getCreateTime()), DateUtil.today()))
                        .count();
                if (CollectionUtil.isNotEmpty(todayOutTimeMsgList)) {
                    long todayTimeOutUserNum = todayOutTimeMsgList.stream().map(WeQiRuleMsg::getReceiveId).distinct().count();
                    viewVo.setTodayTimeOutUserNum(todayTimeOutUserNum);
                    viewVo.setTodayTimeOutNum(todayOutTimeMsgList.size());
                    if (todayMsgNum == 0L) {
                        viewVo.setTodayTimeOutRate("100");
                    } else {
                        BigDecimal todayTimeOutTotalRate = new BigDecimal(todayOutTimeMsgList.size() / todayMsgNum).setScale(2, BigDecimal.ROUND_HALF_UP);
                        viewVo.setTodayTimeOutRate(todayTimeOutTotalRate.toString());
                    }

                }
            }
        }
        return viewVo;
    }

    @Override
    public List<WeQiRuleStatisticsTableVo> qiRuleTableStatistics(WeQiRuleStatisticsTableListQuery query) {

        if (CollectionUtil.isNotEmpty(query.getDeptIds())) {
            SysUserQuery sysUserQuery = new SysUserQuery();
            sysUserQuery.setDeptIds(query.getDeptIds());
            List<SysUserVo> userVoList = qwSysUserClient.getUserListByWeUserIds(sysUserQuery).getData();
            if (CollectionUtil.isNotEmpty(userVoList)) {
                Set<String> sysUserIds = userVoList.stream().map(SysUserVo::getWeUserId).collect(Collectors.toSet());
                sysUserIds.addAll(query.getUserIds());
                query.setUserIds(new ArrayList<>(sysUserIds));
            }
        }
        List<WeQiRuleStatisticsTableVo> tableVoList = weQiRuleMsgService.getRuleTableStatistics(query);
        if (CollectionUtil.isNotEmpty(tableVoList)) {
            Set<String> customerIdSet = tableVoList.stream().filter(tableVo -> ObjectUtil.equal(1, tableVo.getChatType())).map(WeQiRuleStatisticsTableVo::getFromId).collect(Collectors.toSet());
            Set<String> roomIdSet = tableVoList.stream().filter(tableVo -> ObjectUtil.equal(2, tableVo.getChatType())).map(WeQiRuleStatisticsTableVo::getRoomId).collect(Collectors.toSet());
            Set<String> userIds = tableVoList.stream().map(WeQiRuleStatisticsTableVo::getUserId).collect(Collectors.toSet());
            SysUserQuery userQuery = new SysUserQuery();
            userQuery.setWeUserIds(new ArrayList<>(userIds));
            List<SysUserVo> userVoList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
            Map<String, SysUserVo> userIdMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(userVoList)) {
                userIdMap = userVoList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, Function.identity(), (key1, key2) -> key2));
            }
            for (WeQiRuleStatisticsTableVo tableVo : tableVoList) {
                if (userIdMap.containsKey(tableVo.getUserId())) {
                    SysUserVo sysUserVo = userIdMap.get(tableVo.getUserId());
                    tableVo.setUserName(sysUserVo.getUserName());
                    if (CollectionUtil.isNotEmpty(sysUserVo.getDeptList())) {
                        tableVo.setDeptName(sysUserVo.getDeptList().stream().map(SysDeptVo::getDeptName).collect(Collectors.joining(",")));
                    }
                }
                String outTime = DateUtil.formatBetween(tableVo.getSendTime(), tableVo.getReplyTime());
                tableVo.setTimeout(outTime);
            }
        }
        return tableVoList;
    }

    @Override
    public List<WeChatContactMsgVo> getQiRuleTableStatisticsMsg(WeQiRuleStatisticsTableMsgQuery query) {
        Map<String, SysUserVo> userIdMap = new HashMap<>();
        Map<String, WeCustomer> customerIdMap = new HashMap<>();
        List<WeChatContactMsgVo> resultList = new LinkedList<>();
        WeChatContactMsg currentMsg = weChatContactMsgService.getOne(new LambdaQueryWrapper<WeChatContactMsg>()
                .eq(WeChatContactMsg::getMsgId, query.getMsgId()));
        if (Objects.nonNull(currentMsg)) {
            List<WeChatContactMsg> allChatMsgList = CollectionUtil.newLinkedList();
            //客户
            if (StringUtils.isEmpty(currentMsg.getRoomId())) {

                if (ObjectUtil.equal(0, query.getPageType())) {
                    if (StringUtils.isEmpty(query.getMsgType()) || query.getMsgType().contains(currentMsg.getMsgType())) {
                        allChatMsgList.add(currentMsg);
                    }
                    List<WeChatContactMsg> beforeMsgList = getCustomerBeforeMsgList(query, currentMsg);
                    List<WeChatContactMsg> afterMsgList = getCustomerAfterMsgList(query, currentMsg);
                    allChatMsgList.addAll(beforeMsgList);
                    allChatMsgList.addAll(afterMsgList);
                } else if (ObjectUtil.equal(1, query.getPageType())) {
                    List<WeChatContactMsg> beforeMsgList = getCustomerBeforeMsgList(query, currentMsg);
                    allChatMsgList.addAll(beforeMsgList);
                } else if (ObjectUtil.equal(2, query.getPageType())) {
                    List<WeChatContactMsg> afterMsgList = getCustomerAfterMsgList(query, currentMsg);
                    allChatMsgList.addAll(afterMsgList);
                }
                if (CollectionUtil.isNotEmpty(allChatMsgList)) {
                    //获取员工和客户信息
                    SysUserQuery userQuery = new SysUserQuery();
                    userQuery.setWeUserId(query.getReceiveId());
                    List<SysUserVo> userVoList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
                    if (CollectionUtil.isNotEmpty(userVoList)) {
                        userIdMap.putAll(userVoList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, Function.identity(), (key1, key2) -> key2)));
                    }
                    WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                            .eq(WeCustomer::getExternalUserid, query.getFromId())
                            .eq(WeCustomer::getAddUserId, query.getReceiveId())
                            .eq(WeCustomer::getDelFlag, 0)
                            .last("limit 1"));
                    if (ObjectUtil.isNotNull(weCustomer)) {
                        customerIdMap.put(weCustomer.getExternalUserid(), weCustomer);
                    }

                    resultList = allChatMsgList.stream().map(chatMsg -> {
                        WeChatContactMsgVo weChatContactMsgVo = new WeChatContactMsgVo();
                        weChatContactMsgVo.setContact(chatMsg.getContact());
                        weChatContactMsgVo.setMsgTime(chatMsg.getMsgTime());
                        weChatContactMsgVo.setAction(chatMsg.getAction());
                        weChatContactMsgVo.setMsgType(chatMsg.getMsgType());
                        weChatContactMsgVo.setFromId(chatMsg.getFromId());
                        weChatContactMsgVo.setReceiver(chatMsg.getToList());
                        weChatContactMsgVo.setMsgId(chatMsg.getMsgId());
                        if (customerIdMap.containsKey(chatMsg.getFromId())) {
                            weChatContactMsgVo.setName(customerIdMap.get(chatMsg.getFromId()).getCustomerName());
                            weChatContactMsgVo.setAvatar(customerIdMap.get(chatMsg.getFromId()).getAvatar());
                        }
                        if (userIdMap.containsKey(chatMsg.getFromId())) {
                            weChatContactMsgVo.setName(customerIdMap.get(chatMsg.getFromId()).getCustomerName());
                            weChatContactMsgVo.setAvatar(customerIdMap.get(chatMsg.getFromId()).getAvatar());
                        }
                        return weChatContactMsgVo;
                    }).sorted(Comparator.comparing(WeChatContactMsgVo::getMsgTime)).collect(Collectors.toList());
                }
            } else {
                if (ObjectUtil.equal(0, query.getPageType())) {
                    if (StringUtils.isEmpty(query.getMsgType()) || query.getMsgType().contains(currentMsg.getMsgType())) {
                        allChatMsgList.add(currentMsg);
                    }
                    List<WeChatContactMsg> beforeMsgList = getRoomBeforeMsgList(query, currentMsg);
                    List<WeChatContactMsg> afterMsgList = getRoomAfterMsgList(query, currentMsg);
                    allChatMsgList.addAll(beforeMsgList);
                    allChatMsgList.addAll(afterMsgList);
                } else if (ObjectUtil.equal(1, query.getPageType())) {
                    List<WeChatContactMsg> beforeMsgList = getRoomBeforeMsgList(query, currentMsg);
                    allChatMsgList.addAll(beforeMsgList);
                } else if (ObjectUtil.equal(2, query.getPageType())) {
                    List<WeChatContactMsg> afterMsgList = getRoomAfterMsgList(query, currentMsg);
                    allChatMsgList.addAll(afterMsgList);
                }
                if (CollectionUtil.isNotEmpty(allChatMsgList)) {
                    Map<String, Object> groupMembers = weGroupMemberService.getMap(new LambdaQueryWrapper<WeGroupMember>()
                            .eq(WeGroupMember::getChatId, query.getRoomId())
                            .eq(WeGroupMember::getDelFlag, 0)
                            .groupBy(WeGroupMember::getUserId, WeGroupMember::getName));

                    resultList = allChatMsgList.stream().map(chatMsg -> {
                        WeChatContactMsgVo weChatContactMsgVo = new WeChatContactMsgVo();
                        weChatContactMsgVo.setContact(chatMsg.getContact());
                        weChatContactMsgVo.setMsgTime(chatMsg.getMsgTime());
                        weChatContactMsgVo.setAction(chatMsg.getAction());
                        weChatContactMsgVo.setMsgType(chatMsg.getMsgType());
                        weChatContactMsgVo.setFromId(chatMsg.getFromId());
                        weChatContactMsgVo.setReceiver(chatMsg.getToList());
                        weChatContactMsgVo.setMsgId(chatMsg.getMsgId());
                        if (groupMembers.containsKey(chatMsg.getFromId())) {
                            weChatContactMsgVo.setName(String.valueOf(groupMembers.get(chatMsg.getFromId())));
                            //weChatContactMsgVo.setAvatar(customerIdMap.get(chatMsg.getFromId()).getAvatar());
                        }
                        return weChatContactMsgVo;
                    }).sorted(Comparator.comparing(WeChatContactMsgVo::getMsgTime)).collect(Collectors.toList());

                }
            }
        }
        return resultList;
    }

    @Override
    public List<WeQiRuleNoticeListVo> getNoticeList(WeQiRuleNoticeListQuery query) {
        List<WeQiRuleNoticeListVo> noticeList = weQiRuleMsgNoticeService.getNoticeList(query);
        if (CollectionUtil.isNotEmpty(noticeList)) {
            Map<String, WeCustomer> customerMap = new HashMap<>();
            Map<String, String> groupMemberMap = new HashMap<>();
            Set<String> customerIds = noticeList.stream().filter(notice -> ObjectUtil.equal(1, notice.getChatType())).map(WeQiRuleNoticeListVo::getFromId).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
            if (CollectionUtil.isNotEmpty(customerIds)) {


                List<WeCustomer> customerList = weCustomerService.list(new QueryWrapper<WeCustomer>()
                        .select("distinct external_userid,customer_name,avatar,gender")
                        .in("external_userid", customerIds).eq("del_flag", 0));
                customerMap = Optional.ofNullable(customerList).orElseGet(ArrayList::new).stream().collect(Collectors.toMap(WeCustomer::getExternalUserid, Function.identity(), (key1, key2) -> key1));
            }


            Set<String> groupUserIds = noticeList.stream().filter(notice -> ObjectUtil.equal(2, notice.getChatType())).map(WeQiRuleNoticeListVo::getFromId).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
            if (CollectionUtil.isNotEmpty(groupUserIds)) {
                List<WeGroupMember> groupMemberList = weGroupMemberService.list(new LambdaQueryWrapper<WeGroupMember>().select(WeGroupMember::getUserId, WeGroupMember::getName).in(WeGroupMember::getUserId, groupUserIds).eq(WeGroupMember::getDelFlag, 0).groupBy(WeGroupMember::getUserId, WeGroupMember::getName));
                groupMemberMap = Optional.ofNullable(groupMemberList).orElseGet(ArrayList::new).stream().collect(Collectors.toMap(WeGroupMember::getUserId, WeGroupMember::getName, (key1, key2) -> key1));
            }


            for (WeQiRuleNoticeListVo noticeVo : noticeList) {
                if (CollectionUtil.isNotEmpty(customerMap) && ObjectUtil.equal(1, noticeVo.getChatType()) && customerMap.containsKey(noticeVo.getFromId())) {
                    WeCustomer weCustomer = customerMap.get(noticeVo.getFromId());
                    noticeVo.setFromAvatar(weCustomer.getAvatar());
                    noticeVo.setFromName(weCustomer.getCustomerName());
                    noticeVo.setFromGender(weCustomer.getGender());
                }
                if (CollectionUtil.isNotEmpty(groupMemberMap) && ObjectUtil.equal(2, noticeVo.getChatType()) && groupMemberMap.containsKey(noticeVo.getFromId())) {
                    String name = groupMemberMap.get(noticeVo.getFromId());
                    noticeVo.setFromName(name);
                }
            }
        }
        return noticeList;
    }

    @Override
    public void updateReplyStatus(Long qiRuleMsgId) {
        weQiRuleMsgService.update(new LambdaUpdateWrapper<WeQiRuleMsg>()
                .set(WeQiRuleMsg::getReplyTime, new Date())
                .set(WeQiRuleMsg::getReplyStatus, 2)
                .eq(WeQiRuleMsg::getId, qiRuleMsgId));
    }

    @Override
    public List<WeQiRuleWeeklyListVo> getWeeklyList(WeQiRuleWeeklyListQuery query) {
        if (StringUtils.isNotEmpty(query.getUserName())) {
            SysUserQuery sysUserQuery = new SysUserQuery();
            sysUserQuery.setUserName(query.getUserName());
            List<SysUserVo> userVoList = qwSysUserClient.getUserListByWeUserIds(sysUserQuery).getData();
            if (CollectionUtil.isNotEmpty(userVoList)) {
                Set<String> sysUserIds = userVoList.stream().map(SysUserVo::getWeUserId).collect(Collectors.toSet());
                query.setUserIds(new ArrayList<>(sysUserIds));
            }
        }
        List<WeQiRuleWeeklyListVo> weeklyList = weQiRuleManageStatisticsService.getWeeklyList(query);
        if (CollectionUtil.isNotEmpty(weeklyList)) {
            List<String> weUserIds = weeklyList.stream().map(WeQiRuleWeeklyListVo::getUserId).collect(Collectors.toList());
            SysUserQuery userQuery = new SysUserQuery();
            userQuery.setWeUserIds(weUserIds);
            List<SysUserVo> userVoList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
            Map<String, String> userIdMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(userVoList)) {
                userIdMap = userVoList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, SysUserVo::getUserName, (key1, key2) -> key2));
            }
            for (WeQiRuleWeeklyListVo weekly : weeklyList) {
                if (userIdMap.containsKey(weekly.getUserId())) {
                    weekly.setUserName(userIdMap.get(weekly.getUserId()));
                }
            }
        }
        return weeklyList;
    }

    @Override
    public WeQiRuleWeeklyDetailVo getWeeklyDetail(Long id) {
        WeQiRuleWeeklyDetailVo detail = new WeQiRuleWeeklyDetailVo();
        WeQiRuleManageStatistics manageStatistics = weQiRuleManageStatisticsService.getById(id);
        BeanUtil.copyProperties(manageStatistics, detail);
        String weekly = DateUtil.formatDate(manageStatistics.getStartTime()) + "-" + DateUtil.formatDate(manageStatistics.getFinishTime());
        detail.setWeeklyTime(weekly);
        return detail;
    }

    @Override
    public List<WeQiRuleWeeklyDetailListVo> getWeeklyDetailList(WeQiRuleWeeklyDetailListQuery query) {
        if (StringUtils.isNotEmpty(query.getUserName())
                || CollectionUtil.isNotEmpty(query.getUserIds())
                || CollectionUtil.isNotEmpty(query.getDeptIds())) {
            SysUserQuery sysUserQuery = new SysUserQuery();
            if (StringUtils.isNotEmpty(query.getUserName())) {
                sysUserQuery.setUserName(query.getUserName());
            }
            if (CollectionUtil.isNotEmpty(query.getDeptIds())) {
                sysUserQuery.setDeptIds(query.getDeptIds());
            }
            List<SysUserVo> userVoList = qwSysUserClient.getUserListByWeUserIds(sysUserQuery).getData();
            if (CollectionUtil.isNotEmpty(userVoList)) {
                Set<String> sysUserIds = userVoList.stream().map(SysUserVo::getWeUserId).collect(Collectors.toSet());
                query.setUserIds(new ArrayList<>(sysUserIds));
            } else {
                return new LinkedList<>();
            }
        }
        List<WeQiRuleWeeklyDetailListVo> weeklyDetailList = weQiRuleWeeklyUserDataService.getWeeklyDetailList(query);

        if (CollectionUtil.isNotEmpty(weeklyDetailList)) {
            Set<String> userIds = weeklyDetailList.stream().map(WeQiRuleWeeklyDetailListVo::getUserId).collect(Collectors.toSet());
            SysUserQuery userQuery = new SysUserQuery();
            userQuery.setWeUserIds(new ArrayList<>(userIds));
            List<SysUserVo> userVoList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
            Map<String, SysUserVo> userIdMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(userVoList)) {
                userIdMap = userVoList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, Function.identity(), (key1, key2) -> key2));
            }
            for (WeQiRuleWeeklyDetailListVo detailListVo : weeklyDetailList) {
                if (userIdMap.containsKey(detailListVo.getUserId())) {
                    SysUserVo sysUserVo = userIdMap.get(detailListVo.getUserId());
                    detailListVo.setUserName(sysUserVo.getUserName());
                    if (CollectionUtil.isNotEmpty(sysUserVo.getDeptList())) {
                        detailListVo.setDeptName(sysUserVo.getDeptList().stream().map(SysDeptVo::getDeptName).collect(Collectors.joining(",")));
                    }
                }
            }
        }

        return weeklyDetailList;
    }


    private List<WeChatContactMsg> getRoomAfterMsgList(WeQiRuleStatisticsTableMsgQuery query, WeChatContactMsg currentMsg) {
        return weChatContactMsgService.list(new LambdaQueryWrapper<WeChatContactMsg>()
                .eq(WeChatContactMsg::getRoomId, query.getRoomId())
                .in(StringUtils.isNotEmpty(query.getMsgType()), WeChatContactMsg::getMsgType, Arrays.stream(query.getMsgType().split(",")).collect(Collectors.toList()))
                .gt(WeChatContactMsg::getSeq, currentMsg.getSeq())
                .last("limit " + query.getNumber())
                .orderByAsc(WeChatContactMsg::getSeq)
        );
    }


    private List<WeChatContactMsg> getRoomBeforeMsgList(WeQiRuleStatisticsTableMsgQuery query, WeChatContactMsg currentMsg) {
        return weChatContactMsgService.list(new LambdaQueryWrapper<WeChatContactMsg>()
                .eq(WeChatContactMsg::getRoomId, query.getRoomId())
                .in(StringUtils.isNotEmpty(query.getMsgType()), WeChatContactMsg::getMsgType, Arrays.stream(query.getMsgType().split(",")).collect(Collectors.toList()))
                .lt(WeChatContactMsg::getSeq, currentMsg.getSeq())
                .last("limit " + query.getNumber())
                .orderByDesc(WeChatContactMsg::getSeq)
        );
    }

    private List<WeChatContactMsg> getCustomerAfterMsgList(WeQiRuleStatisticsTableMsgQuery query, WeChatContactMsg currentMsg) {
        return weChatContactMsgService.list(new LambdaQueryWrapper<WeChatContactMsg>()
                .and(item -> item.eq(WeChatContactMsg::getFromId, query.getFromId()).or().eq(WeChatContactMsg::getToList, query.getFromId()))
                .and(item -> item.eq(WeChatContactMsg::getFromId, query.getReceiveId()).or().eq(WeChatContactMsg::getToList, query.getReceiveId()))
                .in(StringUtils.isNotEmpty(query.getMsgType()), WeChatContactMsg::getMsgType, Arrays.stream(query.getMsgType().split(",")).collect(Collectors.toList()))
                .gt(WeChatContactMsg::getSeq, currentMsg.getSeq())
                .last("limit " + query.getNumber())
                .orderByAsc(WeChatContactMsg::getSeq)
        );
    }


    private List<WeChatContactMsg> getCustomerBeforeMsgList(WeQiRuleStatisticsTableMsgQuery query, WeChatContactMsg currentMsg) {
        return weChatContactMsgService.list(new LambdaQueryWrapper<WeChatContactMsg>()
                .and(item -> item.eq(WeChatContactMsg::getFromId, query.getFromId()).or().eq(WeChatContactMsg::getToList, query.getFromId()))
                .and(item -> item.eq(WeChatContactMsg::getFromId, query.getReceiveId()).or().eq(WeChatContactMsg::getToList, query.getReceiveId()))
                .in(StringUtils.isNotEmpty(query.getMsgType()), WeChatContactMsg::getMsgType, Arrays.stream(query.getMsgType().split(",")).collect(Collectors.toList()))
                .lt(WeChatContactMsg::getSeq, currentMsg.getSeq())
                .last("limit " + query.getNumber())
                .orderByDesc(WeChatContactMsg::getSeq)
        );
    }


    /**
     * 校验排期是否重复
     *
     * @param qiRuleUserInfos 范围参数
     */
    private void checkScope(List<WeQiUserInfoQuery> qiRuleUserInfos) {
        if (CollectionUtil.isNotEmpty(qiRuleUserInfos)) {
            for (int i = 0; i < qiRuleUserInfos.size() - 1; i++) {
                for (int j = i + 1; j < qiRuleUserInfos.size(); j++) {
                    int finalJ = j;
                    long userSum = qiRuleUserInfos.get(i).getUserIds().stream()
                            .filter(one -> qiRuleUserInfos.get(finalJ).getUserIds().stream()
                                    .anyMatch(two -> ObjectUtil.equal(two, one))).count();

                    if (userSum > 0) {
                        long workCycleSum = qiRuleUserInfos.get(i).getWorkCycle().stream()
                                .filter(one -> qiRuleUserInfos.get(finalJ).getWorkCycle().stream()
                                        .anyMatch(two -> ObjectUtil.equal(two, one))).count();
                        if (workCycleSum > 0) {
                            String beginTime1 = qiRuleUserInfos.get(i).getBeginTime();
                            String endTime1 = qiRuleUserInfos.get(i).getEndTime();
                            String beginTime2 = qiRuleUserInfos.get(finalJ).getBeginTime();
                            String endTime2 = qiRuleUserInfos.get(finalJ).getEndTime();
                            if (match(beginTime1, endTime1, beginTime2, endTime2)) {
                                throw new WeComException("员工时间排期有冲突!");
                            }
                        }
                    }
                }
            }
        }
    }


    private boolean match(String startTime1, String endTime1, String startTime2, String endTime2) {
        DateTime parseStartTime1 = DateUtil.parse(startTime1, "HH:mm");
        DateTime parseEndTime1 = DateUtil.parse(endTime1, "HH:mm");

        DateTime parseStartTime2 = DateUtil.parse(startTime2, "HH:mm");
        DateTime parseEndTime2 = DateUtil.parse(endTime2, "HH:mm");
        return !(parseStartTime2.isAfter(parseEndTime1) || parseStartTime1.isAfter(parseEndTime2));
    }
}
