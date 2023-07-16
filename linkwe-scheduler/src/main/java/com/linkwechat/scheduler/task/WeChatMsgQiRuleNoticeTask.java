package com.linkwechat.scheduler.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.QwAppMsgBusinessTypeEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.*;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.*;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 质检规则通知任务
 *
 * @author danmo
 * @date 2023年05月09日 13:45
 */
@Slf4j
@Component
public class WeChatMsgQiRuleNoticeTask {

    @Autowired
    private IWeQiRuleService weQiRuleService;

    @Autowired
    private IWeQiRuleMsgService weQiRuleMsgService;

    @Autowired
    private QwAppSendMsgService qwAppSendMsgService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Autowired
    private IWeQiRuleManageStatisticsService weQiRuleManageStatisticsService;

    @Value("${task-msg.qi-rule.title:会话质检}")
    private String title;

    @Value("${task-msg.qi-rule.desc:}")
    private String description;

    @Value("${task-msg.qi-rule.manage-desc:}")
    private String manageDescription;

    @Value("${task-msg.qi-rule.url:}")
    private String linkUrl;

    @Value("${task-msg.qi-rule.btnTxt:去处理}")
    private String userBtnTxt;

    @Value("${task-msg.qi-rule.manage-btnTxt:去查看}")
    private String manageBtnTxt;

    @Value("${task-msg.qi-rule.weeklyUrl:}")
    private String weeklyUrl;

    @XxlJob("weChatMsgQiRuleNoticeTask")
    public void execute() {
        List<WeQiRuleMsg> weQiRuleMsgList = weQiRuleMsgService.list(new LambdaQueryWrapper<WeQiRuleMsg>()
                .eq(WeQiRuleMsg::getReplyStatus,1)
                .eq(WeQiRuleMsg::getStatus, 0)
                .eq(WeQiRuleMsg::getDelFlag, 0));
        if (CollectionUtil.isNotEmpty(weQiRuleMsgList)) {
            for (WeQiRuleMsg weQiRuleMsg : weQiRuleMsgList) {
                if(DateUtil.compare(DateUtil.date(),weQiRuleMsg.getTimeOut()) < 0){
                    continue;
                }
                sendUserMsgNotice(weQiRuleMsg);
                sendManageMsgNotice(weQiRuleMsg);
            }
        }
    }

    /**
     * 发送员工通知
     */
    private void sendUserMsgNotice(WeQiRuleMsg weQiRuleMsg){
        QwAppMsgBody qwAppMsgBody = new QwAppMsgBody();
        //发送人指定员工
        qwAppMsgBody.setCorpUserIds(Collections.singletonList(weQiRuleMsg.getReceiveId()));
        //设置消息模板
        WeMessageTemplate template = new WeMessageTemplate();
        template.setMsgType(MessageType.TEXTCARD.getMessageType());
        template.setTitle(title);
        if (ObjectUtil.equal(1, weQiRuleMsg.getChatType())) {
            WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                    .select(WeCustomer::getCustomerName)
                    .eq(WeCustomer::getExternalUserid, weQiRuleMsg.getFromId())
                    .eq(WeCustomer::getAddUserId, weQiRuleMsg.getReceiveId())
                    .eq(WeCustomer::getDelFlag, 0).last("limit 1"));
            template.setDescription(StringUtils.format(description, weCustomer.getCustomerName(), "客户"));
        } else {
            WeGroupMember weGroupMember = weGroupMemberService.getOne(new LambdaQueryWrapper<WeGroupMember>()
                    .select(WeGroupMember::getName)
                    .eq(WeGroupMember::getUserId, weQiRuleMsg.getFromId())
                    .eq(WeGroupMember::getDelFlag, 0).last("limit 1"));
            template.setDescription(StringUtils.format(description, weGroupMember.getName(), "客群"));
        }

        template.setLinkUrl(StringUtils.format(linkUrl, weQiRuleMsg.getId(),1));
        template.setBtntxt(userBtnTxt);

        JSONObject businessData = new JSONObject();
        businessData.put("type",1);
        businessData.put("isBack",true);
        qwAppMsgBody.setBusinessData(businessData);
        qwAppMsgBody.setCallBackId(weQiRuleMsg.getId());
        qwAppMsgBody.setMessageTemplates(template);
        qwAppMsgBody.setBusinessType(QwAppMsgBusinessTypeEnum.QI_RULE.getType());
        qwAppSendMsgService.appMsgSend(qwAppMsgBody);
    }

    /**
     * 发送h管理人员通知
     */
    private void sendManageMsgNotice(WeQiRuleMsg weQiRuleMsg){
        WeQiRule qiRule = weQiRuleService.getById(weQiRuleMsg.getRuleId());
        QwAppMsgBody qwAppMsgBody = new QwAppMsgBody();

        if(StringUtils.isEmpty(qiRule.getManageUser())){
            return;
        }
        qwAppMsgBody.setCorpUserIds(Arrays.stream(qiRule.getManageUser().split(",")).collect(Collectors.toList()));

        //设置消息模板
        WeMessageTemplate template = new WeMessageTemplate();

        String receiveId = weQiRuleMsg.getReceiveId();
        SysUserQuery userQuery = new SysUserQuery();
        userQuery.setWeUserIds(Collections.singletonList(receiveId));
        List<SysUserVo> userVos = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
        if(CollectionUtil.isNotEmpty(userVos)){
            if (ObjectUtil.equal(1, weQiRuleMsg.getChatType())) {
                template.setDescription(StringUtils.format(manageDescription, userVos.get(0).getUserName(),"客户"));
            }else {
                template.setDescription(StringUtils.format(manageDescription, userVos.get(0).getUserName(), "客群"));
            }
        }


        template.setMsgType(MessageType.TEXTCARD.getMessageType());
        template.setTitle(title);

        template.setLinkUrl(StringUtils.format(linkUrl, weQiRuleMsg.getId(),2));
        template.setBtntxt(manageBtnTxt);

        JSONObject businessData = new JSONObject();
        businessData.put("type",2);
        businessData.put("isBack",false);
        qwAppMsgBody.setBusinessData(businessData);
        qwAppMsgBody.setMessageTemplates(template);
        qwAppMsgBody.setCallBackId(weQiRuleMsg.getId());
        qwAppMsgBody.setBusinessType(QwAppMsgBusinessTypeEnum.QI_RULE.getType());
        qwAppSendMsgService.appMsgSend(qwAppMsgBody);
    }


    /**
     * 周报通知任务
     */
    @XxlJob("chatMsgQiRuleWeeklyNoticeTask")
    public void chatMsgQiRuleWeeklyNotice() {
        String jobParam = XxlJobHelper.getJobParam();
        List<WeQiRuleManageStatistics> list = weQiRuleManageStatisticsService.list(new LambdaQueryWrapper<WeQiRuleManageStatistics>()
                .eq(WeQiRuleManageStatistics::getStatus, 0)
                .in(StringUtils.isNotEmpty(jobParam),WeQiRuleManageStatistics::getWeUserId, Arrays.stream(jobParam.split(",")).collect(Collectors.toList()))
                .eq(WeQiRuleManageStatistics::getDelFlag, 0));
        if(CollectionUtil.isNotEmpty(list)){
            for (WeQiRuleManageStatistics manageStatistics : list) {
                QwAppMsgBody qwAppMsgBody = new QwAppMsgBody();
                //设置消息模板
                WeMessageTemplate template = new WeMessageTemplate();
                template.setMsgType(MessageType.TEXTCARD.getMessageType());
                template.setTitle("周质检报告");
                String weeklyTime = DateUtil.formatDate(manageStatistics.getStartTime()) + "~" + DateUtil.formatDate(manageStatistics.getFinishTime());
                template.setDescription("您有一份【"+weeklyTime+"】周质检报告需要查看");
                template.setLinkUrl(StringUtils.format(weeklyUrl, manageStatistics.getId()));
                template.setBtntxt("去查看");

                JSONObject businessData = new JSONObject();
                businessData.put("isBack",false);
                qwAppMsgBody.setCorpUserIds(Collections.singletonList(manageStatistics.getWeUserId()));
                qwAppMsgBody.setBusinessData(businessData);
                qwAppMsgBody.setMessageTemplates(template);
                qwAppMsgBody.setCallBackId(manageStatistics.getId());
                qwAppMsgBody.setBusinessType(QwAppMsgBusinessTypeEnum.QI_RULE.getType());
                qwAppSendMsgService.appMsgSend(qwAppMsgBody);

                weQiRuleManageStatisticsService.update(new LambdaUpdateWrapper<WeQiRuleManageStatistics>()
                        .set(WeQiRuleManageStatistics::getStatus,1)
                        .set(WeQiRuleManageStatistics::getSendTime,new Date())
                        .eq(WeQiRuleManageStatistics::getId,manageStatistics.getId()));
            }
        }
    }
}
