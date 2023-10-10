package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.thread.WeMsgAuditThreadExecutor;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.query.msgaudit.WeMsgAuditQuery;
import com.linkwechat.domain.wecom.vo.msgaudit.WeMsgAuditVo;
import com.linkwechat.fegin.QwMsgAuditClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.*;
import com.tencent.wework.FinanceService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeChatContactMsgService weChatContactMsgService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;



    /**
     * 会话拉取定时任务
     */
    @XxlJob("weChatMsgPullTask")
    public void eChatMsgPullHandle(String message) {
        WeChatMsgAuditTaskParams auditTaskParam=new WeChatMsgAuditTaskParams(false);

        String jobParam = XxlJobHelper.getJobParam();

        if(StringUtils.isNotEmpty(jobParam)){
            auditTaskParam=JSONObject.parseObject(jobParam,WeChatMsgAuditTaskParams.class);
        }

        String corpId = XxlJobHelper.getJobParam();

        XxlJobHelper.log("会话拉取定时任务--------------{}",corpId);
        Long seqLong = 0L;

        if(StringUtils.isNotEmpty(message)){
            WeBackBaseVo weBackBaseVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackBaseVo.class);
            corpId = weBackBaseVo.getToUserName();
        }

        WeCorpAccount corpAccount = weCorpAccountService.getCorpAccountByCorpId(corpId);
        if (corpAccount != null) {




            if(!auditTaskParam.synchAll){
                if(redisService.keyIsExists("we:chat:seq:" + corpAccount.getCorpId())){
                    seqLong = (Long) redisService.getCacheObject("we:chat:seq:" + corpAccount.getCorpId());
                }else {
                    LambdaQueryWrapper<WeChatContactMsg> wrapper = new LambdaQueryWrapper<WeChatContactMsg>().orderByDesc(WeChatContactMsg::getSeq).last("limit 1");
                    WeChatContactMsg weChatContactMsg = weChatContactMsgService.getOne(wrapper);
                    if (weChatContactMsg != null) {
                        seqLong = weChatContactMsg.getSeq();
                    }
                }

            }


            if(StringUtils.isNotEmpty(corpAccount.getCorpId()) && StringUtils.isNotEmpty(corpAccount.getChatSecret()) && StringUtils.isNotEmpty(corpAccount.getFinancePrivateKey())){
                FinanceService financeService = new FinanceService(corpAccount.getCorpId(), corpAccount.getChatSecret(), corpAccount.getFinancePrivateKey(),
                        linkWeChatConfig.getFincaceProxyConfig().getProxy(),linkWeChatConfig.getFincaceProxyConfig().getPaswd());
                financeService.setRedisService(redisService);
                financeService.getChatData(seqLong, (data) -> rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeChatMsgAuditEx(), "" ,data.toJSONString()));
                log.info("会话存档定时任务执行完成----------------->");
            }



        }


    }


    /**
     * 获取会话内容存档开启成员列表
     */
    @XxlJob("wePermitUserListTask")
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
    @XxlJob("weCheckSingleAgreeTask")
    public void checkSingleAgreeTaskHandle() {
        XxlJobHelper.log("获取会话中外部成员的同意情况任务-------------->>>>start");
        int total = weCustomerService.count(new QueryWrapper<WeCustomer>()
                .select("distinct external_userid,add_user_id")
                .eq("del_flag", 0));

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
                                        WeCustomer weCustomer = new WeCustomer();
                                        weCustomer.setId(customerList.get(0).getId());
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
    @XxlJob("weCheckRoomAgreeTask")
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

                            List<WeGroupMember> agree = weGroupMembers.stream().filter(item -> CollectionUtil.isNotEmpty(memberUserMap.get(item.getUserId()))).map(item -> {
                                List<WeMsgAuditVo.AgreeInfo> agreeInfos = memberUserMap.get(item.getUserId());
                                WeGroupMember weGroupMember = new WeGroupMember();
                                weGroupMember.setId(item.getId());
                                weGroupMember.setOpenChatTime(new Date(agreeInfos.get(0).getStatusChangeTime() * 1000));
                                weGroupMember.setIsOpenChat(ObjectUtil.equal("Agree", agreeInfos.get(0).getAgreeStatus()) ? 1 : 0);
                                return weGroupMember;
                            }).collect(Collectors.toList());
                            weGroupMemberService.updateBatchById(agree);
                        }
                    }
                } catch (Exception e) {
                    XxlJobHelper.log("外部群-外部联系人的同意情况异常 roomId:{}",roomId, e);
                }
            });

        });


    }


    @Data
    private static class WeChatMsgAuditTaskParams {
        //是否同步所有数据,默认不同步
        private Boolean synchAll=false;

        WeChatMsgAuditTaskParams(){

        }
        WeChatMsgAuditTaskParams(Boolean synchAllP){
            synchAll=synchAllP;
        }
    }
}
