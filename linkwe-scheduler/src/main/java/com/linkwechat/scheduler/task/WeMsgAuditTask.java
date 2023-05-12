package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.linkwechat.common.utils.thread.WeMsgAuditThreadExecutor;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.wecom.query.msgaudit.WeMsgAuditQuery;
import com.linkwechat.domain.wecom.vo.msgaudit.WeMsgAuditVo;
import com.linkwechat.fegin.QwMsgAuditClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeGroupMemberService;
import com.linkwechat.service.IWeGroupService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会话存档任务
 *
 * @author danmo
 * @date 2023年05月12日 10:21
 */
@Slf4j
@Component
public class WeMsgAuditTask {

    @Resource
    private QwMsgAuditClient qwMsgAuditClient;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupService weGroupService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;


    /**
     * 获取会话内容存档开启成员列表
     */
    @XxlJob("WePermitUserListTask")
    public void getPermitUserListHandle() {
        XxlJobHelper.log("获取会话内容存档开启成员列表-------------->>>>start");
        WeMsgAuditQuery auditQuery = new WeMsgAuditQuery();

        WeMsgAuditVo weMsgAuditVo = qwMsgAuditClient.getPermitUserList(auditQuery).getData();

        if (Objects.nonNull(weMsgAuditVo) && ObjectUtil.equal(0, weMsgAuditVo.getErrCode())) {
            List<String> userIds = weMsgAuditVo.getIds();
            SysUserQuery query = new SysUserQuery();
            query.setWeUserIds(userIds);
            qwSysUserClient.updateUserChatStatus(query);
        }
    }

    /**
     * 获取会话中外部成员的同意情况
     */
    @XxlJob("WeCheckSingleAgreeTask")
    public void checkSingleAgreeTaskHandle() {
        XxlJobHelper.log("获取会话中外部成员的同意情况任务-------------->>>>start");
        int total = weCustomerService.count(new LambdaQueryWrapper<WeCustomer>()
                .select(WeCustomer::getExternalUserid, WeCustomer::getAddUserId)
                .eq(WeCustomer::getDelFlag, 0)
                .groupBy(WeCustomer::getExternalUserid, WeCustomer::getAddUserId));

        XxlJobHelper.log("获取会话中外部成员的同意情况 >>>>>>>>>>>>>>count：{}", total);
        // 手动分页
        int pageSize = 100;
        int pageNum = total / pageSize;
        // 是不是整除
        int surplus = total % pageSize;
        if (surplus > 0) {
            pageNum = pageNum + 1;
        }
        for (int i = 0; i < pageNum; i++) {

            int finalI = i;
            WeMsgAuditThreadExecutor.getInstance().execute(() -> {
                try {
                    List<WeCustomer> externalUserIdAndUserIdList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                            .select(WeCustomer::getId,WeCustomer::getExternalUserid, WeCustomer::getAddUserId)
                            .eq(WeCustomer::getDelFlag, 0)
                            .groupBy(WeCustomer::getExternalUserid, WeCustomer::getAddUserId)
                            .last("limit " + finalI * pageSize + "," + pageSize));
                    if (CollectionUtil.isNotEmpty(externalUserIdAndUserIdList)) {
                        List<WeMsgAuditQuery.MsgAuditInfo> msgAuditInfos = externalUserIdAndUserIdList.stream().map(item -> {
                            WeMsgAuditQuery.MsgAuditInfo msgAuditInfo = new WeMsgAuditQuery.MsgAuditInfo();
                            msgAuditInfo.setUserid(item.getAddUserId());
                            msgAuditInfo.setExteranalopenid(item.getExternalUserid());
                            return msgAuditInfo;
                        }).collect(Collectors.toList());

                        WeMsgAuditQuery auditQuery = new WeMsgAuditQuery();
                        auditQuery.setInfo(msgAuditInfos);
                        WeMsgAuditVo weMsgAuditVo = qwMsgAuditClient.checkSingleAgree(auditQuery).getData();

                        if (Objects.nonNull(weMsgAuditVo) && ObjectUtil.equal(0, weMsgAuditVo.getErrCode())) {

                            Map<String, List<WeCustomer>> customerMap = externalUserIdAndUserIdList.stream().collect(Collectors.groupingBy(item -> item.getExternalUserid() + "_" + item.getAddUserId()));

                            List<WeMsgAuditVo.AgreeInfo> agreeInfoList = weMsgAuditVo.getAgreeInfo();
                            if (CollectionUtil.isNotEmpty(agreeInfoList)) {
                                Map<String, List<WeMsgAuditVo.AgreeInfo>> agreeInfoMap = agreeInfoList.stream().collect(Collectors.groupingBy(item -> item.getExteranalOpenId() + "_" + item.getUserId()));

                                List<WeCustomer> updateCustomerList = new LinkedList<>();
                                customerMap.forEach((externalUserIdAndUserId,customerList) ->{
                                    List<WeMsgAuditVo.AgreeInfo> agreeInfos = agreeInfoMap.get(externalUserIdAndUserId);
                                    if(CollectionUtil.isNotEmpty(agreeInfos)){
                                        WeCustomer weCustomer = customerList.get(0);
                                        WeMsgAuditVo.AgreeInfo agreeInfo = agreeInfos.get(0);
                                        weCustomer.setOpenChatTime(new Date(agreeInfo.getStatusChangeTime() * 1000));
                                        weCustomer.setIsOpenChat(ObjectUtil.equal("Agree", agreeInfo.getAgreeStatus()) ? 1 : 0);
                                        updateCustomerList.add(weCustomer);
                                    }
                                });
                                weCustomerService.updateBatchById(updateCustomerList);
                            }
                        }
                    }
                } catch (Exception e) {
                    XxlJobHelper.log("获取会话中外部成员的同意情况异常", e);
                }
            });
        }
    }

    /**
     * 获取会话内容存档外部群外部联系人的同意情况
     */
    @XxlJob("WeCheckRoomAgreeTask")
    public void checkRoomAgreeHandle() {
        XxlJobHelper.log("外部群-外部联系人的同意情况-------------->>>>start");

        List<WeGroup> groupList = weGroupService.list(new LambdaQueryWrapper<WeGroup>().select(WeGroup::getChatId).groupBy(WeGroup::getChatId));

        Optional.ofNullable(groupList).orElseGet(ArrayList::new).stream().map(WeGroup::getChatId).forEach(roomId ->{
            WeMsgAuditThreadExecutor.getInstance().execute(() ->{
                try {
                    XxlJobHelper.log("外部群-外部联系人的同意情况 当前roomId:{}",roomId);
                    WeMsgAuditQuery auditQuery = new WeMsgAuditQuery();
                    auditQuery.setRoomid(roomId);
                    WeMsgAuditVo weMsgAuditVo = qwMsgAuditClient.checkRoomAgree(auditQuery).getData();
                    XxlJobHelper.log("外部群-外部联系人的同意情况 当前weMsgAuditVo:{}", JSONObject.toJSONString(weMsgAuditVo));
                    if (Objects.nonNull(weMsgAuditVo) && ObjectUtil.equal(0, weMsgAuditVo.getErrCode())) {
                        List<WeMsgAuditVo.AgreeInfo> agreeInfoList = weMsgAuditVo.getAgreeInfo();
                        if(CollectionUtil.isNotEmpty(agreeInfoList)){
                            Map<String, List<WeMsgAuditVo.AgreeInfo>> memberUserMap = agreeInfoList.stream().collect(Collectors.groupingBy(WeMsgAuditVo.AgreeInfo::getExteranalOpenId));
                            List<WeGroupMember> weGroupMembers = weGroupMemberService.list(new LambdaQueryWrapper<WeGroupMember>()
                                    .eq(WeGroupMember::getChatId, roomId)
                                    .in(WeGroupMember::getUserId, memberUserMap.keySet())
                                    .eq(WeGroupMember::getDelFlag, 0));

                            for (WeGroupMember weGroupMember : weGroupMembers) {
                                List<WeMsgAuditVo.AgreeInfo> agreeInfos = memberUserMap.get(weGroupMember.getUserId());
                                if(CollectionUtil.isNotEmpty(agreeInfos)){
                                    weGroupMember.setOpenChatTime(new Date(agreeInfos.get(0).getStatusChangeTime() * 1000));
                                    weGroupMember.setIsOpenChat(ObjectUtil.equal("Agree", agreeInfos.get(0).getAgreeStatus()) ? 1 : 0);
                                }
                            }
                            weGroupMemberService.updateBatchById(weGroupMembers);
                        }
                    }
                } catch (Exception e) {
                    XxlJobHelper.log("外部群-外部联系人的同意情况异常 roomId:{}",roomId, e);
                }
            });

        });


    }
}
