package com.linkwechat.service.impl.strategic.shortlink;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.QwAppMsgBusinessTypeEnum;
import com.linkwechat.common.enums.WeMsgTypeEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.WeShortLinkPromotionTemplateAppMsg;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.shortlink.dto.WeShortLinkPromotionAppMsgDto;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTemplateAppMsgAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionTemplateAppMsgUpdateQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.domain.sop.vo.WeSopExecuteUserConditVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.IWeShortLinkPromotionService;
import com.linkwechat.service.IWeShortLinkPromotionTemplateAppMsgService;
import com.linkwechat.service.QwAppSendMsgService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 短链推广方式-朋友圈
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/14 10:28
 */
@Service
public class AppMsgPromotion extends PromotionType {

    @Resource
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeShortLinkPromotionTemplateAppMsgService weShortLinkPromotionTemplateAppMsgService;
    @Resource
    private QwAppSendMsgService qwAppSendMsgService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RabbitMQSettingConfig rabbitMQSettingConfig;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private LinkWeChatConfig linkWeChatConfig;

    @Override
    public Long saveAndSend(WeShortLinkPromotionAddQuery query, WeShortLinkPromotion weShortLinkPromotion) throws IOException {
        WeShortLinkPromotionTemplateAppMsgAddQuery appMsgAddQuery = query.getAppMsg();
        //检查发送者
        checkSender(appMsgAddQuery.getExecuteUserCondit(), appMsgAddQuery.getExecuteDeptCondit());

        //1.保存短链推广
        //发送类型：0立即发送 1定时发送
        Integer sendType = appMsgAddQuery.getSendType();
        if (sendType.equals(0)) {
            //任务状态: 0待推广 1推广中 2已结束
            weShortLinkPromotion.setTaskStatus(1);
            weShortLinkPromotion.setTaskStartTime(LocalDateTime.now());
        } else {
            weShortLinkPromotion.setTaskStatus(0);
            weShortLinkPromotion.setTaskStartTime(appMsgAddQuery.getTaskSendTime());
        }

        //任务结束时间
        LocalDateTime taskEndTime = appMsgAddQuery.getTaskEndTime();
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            weShortLinkPromotion.setTaskEndTime(o);
        });
        //保存短链推广
        weShortLinkPromotionService.save(weShortLinkPromotion);

        //海报附件
        WeMessageTemplate posterMessageTemplate = getPromotionUrl(weShortLinkPromotion.getId(), weShortLinkPromotion.getShortLinkId(), weShortLinkPromotion.getStyle(), weShortLinkPromotion.getMaterialId());
        weShortLinkPromotion.setUrl(posterMessageTemplate.getPicUrl());
        weShortLinkPromotionService.updateById(weShortLinkPromotion);

        //用户Id
        String userIds = null;
        //部门Id
        String deptIds = null;
        //岗位Id
        String postIds = null;

        //2.保存短链推广模板-应用消息
        WeShortLinkPromotionTemplateAppMsg appMsg = BeanUtil.copyProperties(appMsgAddQuery, WeShortLinkPromotionTemplateAppMsg.class);
        WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit = appMsgAddQuery.getExecuteUserCondit();
        if (executeUserCondit.isChange()) {
            List<String> weUserIds = executeUserCondit.getWeUserIds();
            if (weUserIds != null && weUserIds.size() > 0) {
                userIds = StringUtils.join(weUserIds, ",");
                appMsg.setUserIds(userIds);
            }
        }
        WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit = appMsgAddQuery.getExecuteDeptCondit();
        if (executeDeptCondit.isChange()) {
            List<String> depts = executeDeptCondit.getDeptIds();
            List<String> posts = executeDeptCondit.getPosts();
            if (depts != null && depts.size() > 0) {
                deptIds = StringUtils.join(depts, ",");
                appMsg.setDeptIds(deptIds);
            }
            if (posts != null && posts.size() > 0) {
                postIds = StringUtils.join(posts, ",");
                appMsg.setPostIds(postIds);
            }
        }
        appMsg.setPromotionId(weShortLinkPromotion.getId());
        appMsg.setDelFlag(0);
        weShortLinkPromotionTemplateAppMsgService.save(appMsg);

        //3.保存员工短链推广任务
        //暂不需要

        //4.保存附件
        //暂不需要

        //5.保存短链推广发送结果
        //暂不需要

        //6.发送mq

        //发送内容
        String appMsgUrl = linkWeChatConfig.getAppMsgUrl();
        appMsgUrl = StringUtils.format(appMsgUrl, weShortLinkPromotion.getId());
        String content = "【短链推广】\r\n\r\n  您有一条新的短链推广任务，请点击链接，进入任务页，分享短链内容给客户。\r\n\r\n <a href=" + appMsgUrl + ">" + weShortLinkPromotion.getTaskName() + "</a>";
        //发送类型：0立即发送 1定时发送
        if (sendType.equals(0)) {
            //立即发送
            directSend(weShortLinkPromotion.getId(),
                    appMsg.getId(),
                    content,
                    null,
                    null,
                    userIds,
                    deptIds,
                    postIds);
        } else {
            //定时发送
            timingSend(weShortLinkPromotion.getId(),
                    appMsg.getId(),
                    content,
                    Date.from(query.getClient().getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()),
                    null,
                    null,
                    userIds,
                    deptIds,
                    postIds);
        }
        //任务结束时间
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            timingEnd(weShortLinkPromotion.getId(), appMsg.getId(), weShortLinkPromotion.getType(), Date.from(taskEndTime.atZone(ZoneId.systemDefault()).toInstant()));
        });


        return weShortLinkPromotion.getId();
    }

    @Override
    public void updateAndSend(WeShortLinkPromotionUpdateQuery query, WeShortLinkPromotion weShortLinkPromotion) throws IOException {
        WeShortLinkPromotionTemplateAppMsgUpdateQuery appMsgUpdateQuery = query.getAppMsg();

        //检查发送者
        checkSender(appMsgUpdateQuery.getExecuteUserCondit(), appMsgUpdateQuery.getExecuteDeptCondit());

        //1.修改短链推广
        //发送类型：0立即发送 1定时发送
        Integer sendType = appMsgUpdateQuery.getSendType();
        if (sendType.equals(0)) {
            //任务状态: 0待推广 1推广中 2已结束
            weShortLinkPromotion.setTaskStatus(1);
            weShortLinkPromotion.setTaskStartTime(LocalDateTime.now());
        } else {
            weShortLinkPromotion.setTaskStatus(0);
            weShortLinkPromotion.setTaskStartTime(appMsgUpdateQuery.getTaskSendTime());
        }
        //任务结束时间
        LocalDateTime taskEndTime = appMsgUpdateQuery.getTaskEndTime();
        Optional.ofNullable(taskEndTime).ifPresent(o -> weShortLinkPromotion.setTaskEndTime(o));
        //保存短链推广
        weShortLinkPromotionService.updateById(weShortLinkPromotion);

        //海报附件
        WeMessageTemplate posterMessageTemplate = getPromotionUrl(weShortLinkPromotion.getId(), weShortLinkPromotion.getShortLinkId(), weShortLinkPromotion.getStyle(), weShortLinkPromotion.getMaterialId());
        weShortLinkPromotion.setUrl(posterMessageTemplate.getPicUrl());
        weShortLinkPromotionService.updateById(weShortLinkPromotion);

        //用户Id
        String userIds = null;
        //部门Id
        String deptIds = null;
        //岗位Id
        String postIds = null;

        //2.修改短链推广模板-应用消息
        //删除
        LambdaUpdateWrapper<WeShortLinkPromotionTemplateAppMsg> appMsgUpdateWrapper = Wrappers.lambdaUpdate();
        appMsgUpdateWrapper.eq(WeShortLinkPromotionTemplateAppMsg::getPromotionId, weShortLinkPromotion.getId());
        appMsgUpdateWrapper.set(WeShortLinkPromotionTemplateAppMsg::getDelFlag, 1);
        weShortLinkPromotionTemplateAppMsgService.update(appMsgUpdateWrapper);
        //添加
        WeShortLinkPromotionTemplateAppMsg appMsg = BeanUtil.copyProperties(appMsgUpdateQuery, WeShortLinkPromotionTemplateAppMsg.class);
        appMsg.setId(null);

        WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit = appMsgUpdateQuery.getExecuteUserCondit();
        if (executeUserCondit.isChange()) {
            List<String> weUserIds = executeUserCondit.getWeUserIds();
            if (weUserIds != null && weUserIds.size() > 0) {
                userIds = StringUtils.join(weUserIds, ",");
                appMsg.setUserIds(userIds);
            }
        }

        WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit = appMsgUpdateQuery.getExecuteDeptCondit();
        if (executeDeptCondit.isChange()) {
            List<String> depts = executeDeptCondit.getDeptIds();
            if (depts != null && depts.size() > 0) {
                deptIds = StringUtils.join(depts, ",");
                appMsg.setDeptIds(deptIds);
            }

            List<String> posts = executeDeptCondit.getPosts();
            if (posts != null && posts.size() > 0) {
                postIds = StringUtils.join(posts, ",");
                appMsg.setPostIds(postIds);
            }
        }
        appMsg.setPromotionId(weShortLinkPromotion.getId());
        appMsg.setDelFlag(0);
        weShortLinkPromotionTemplateAppMsgService.save(appMsg);

        //发送mq
        //发送内容
        String appMsgUrl = linkWeChatConfig.getAppMsgUrl();
        appMsgUrl = StringUtils.format(appMsgUrl, weShortLinkPromotion.getId());
        String content = "【短链推广】\r\n\r\n  您有一条新的短链推广任务，请点击链接，进入任务页，分享短链内容给客户。\r\n\r\n <a href=" + appMsgUrl + ">" + weShortLinkPromotion.getTaskName() + "</a>";
        if (sendType.equals(0)) {
            //立即发送
            directSend(weShortLinkPromotion.getId(),
                    appMsg.getId(),
                    content,
                    null,
                    null,
                    userIds,
                    deptIds,
                    postIds);
        } else {
            //定时发送
            timingSend(weShortLinkPromotion.getId(),
                    appMsg.getId(),
                    content,
                    Date.from(query.getClient().getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()),
                    null,
                    null,
                    userIds,
                    deptIds,
                    postIds);
        }
        //任务结束时间
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            timingEnd(weShortLinkPromotion.getId(), appMsg.getId(), weShortLinkPromotion.getType(), Date.from(taskEndTime.atZone(ZoneId.systemDefault()).toInstant()));
        });
    }

    @Override
    protected void directSend(Long id, Long businessId, String content, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList, Object... objects) {
        QwAppMsgBody qwAppMsgBody = new QwAppMsgBody();
        qwAppMsgBody.setCorpId(SecurityUtils.getCorpId());

        //用户Id集合
        String[] userIds = objects[0] != null ? String.valueOf(objects[0]).split(",") : null;
        //部门Id集合
        String[] deptIds = objects[1] != null ? String.valueOf(objects[1]).split(",") : null;
        //岗位Id集合
        Object object = objects[2];
        String[] postIds = object != null ? String.valueOf(object).split(",") : null;

        if (deptIds != null && deptIds.length > 0) {
            qwAppMsgBody.setDeptIds(Arrays.asList(deptIds));
        }

        Set<String> userSet = new HashSet<>();
        if (userIds != null && userIds.length > 0) {
            userSet.addAll(Arrays.asList(userIds));
        }
        if (postIds != null && postIds.length > 0) {
            AjaxResult<List<SysUser>> allSysUser = qwSysUserClient.findAllSysUser(null, String.valueOf(object), null);
            if (allSysUser.getCode() == 200) {
                List<SysUser> data = allSysUser.getData();
                List<String> collect = data.stream().map(i -> i.getWeUserId()).collect(Collectors.toList());
                userSet.addAll(collect);
                qwAppMsgBody.setCorpUserIds(new ArrayList<>(userSet));
            }
        } else {
            qwAppMsgBody.setCorpUserIds(new ArrayList<>(userSet));
        }

        //附件
        WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
        weMessageTemplate.setMsgType(WeMsgTypeEnum.TEXT.getMessageType());
        weMessageTemplate.setContent(content);
        qwAppMsgBody.setMessageTemplates(weMessageTemplate);
        //类型
        qwAppMsgBody.setBusinessType(QwAppMsgBusinessTypeEnum.COMMON.getType());
        qwAppSendMsgService.appMsgSend(qwAppMsgBody);
    }

    @Override
    protected void timingSend(Long id, Long businessId, String content, Date sendTime, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList, Object... objects) {
        WeShortLinkPromotionAppMsgDto qwAppMsgBody = new WeShortLinkPromotionAppMsgDto();
        qwAppMsgBody.setShortLinkPromotionId(id);
        qwAppMsgBody.setBusinessId(businessId);

        qwAppMsgBody.setCorpId(SecurityUtils.getCorpId());

        //应用消息发送类型 0成员 1部门或岗位
        //用户Id集合
        String[] userIds = objects[0] != null ? String.valueOf(objects[0]).split(",") : null;
        //部门Id集合
        String[] deptIds = objects[1] != null ? String.valueOf(objects[1]).split(",") : null;
        //岗位Id集合
        Object object = objects[2];
        String[] postIds = object != null ? String.valueOf(object).split(",") : null;

        if (deptIds != null && deptIds.length > 0) {
            qwAppMsgBody.setDeptIds(Arrays.asList(deptIds));
        }

        Set<String> userSet = new HashSet<>();
        if (userIds != null && userIds.length > 0) {
            userSet.addAll(Arrays.asList(userIds));
        }
        if (postIds != null && postIds.length > 0) {
            AjaxResult<List<SysUser>> allSysUser = qwSysUserClient.findAllSysUser(null, String.valueOf(object), null);
            if (allSysUser.getCode() == 200) {
                List<SysUser> data = allSysUser.getData();
                List<String> collect = data.stream().map(i -> i.getWeUserId()).collect(Collectors.toList());
                userSet.addAll(collect);
                qwAppMsgBody.setCorpUserIds(new ArrayList<>(userSet));
            }
        } else {
            qwAppMsgBody.setCorpUserIds(new ArrayList<>(userSet));
        }

        //附件
        WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
        weMessageTemplate.setMsgType(WeMsgTypeEnum.TEXT.getMessageType());
        weMessageTemplate.setContent(content);
        qwAppMsgBody.setMessageTemplates(weMessageTemplate);
        //类型
        qwAppMsgBody.setBusinessType(QwAppMsgBusinessTypeEnum.COMMON.getType());

        long diffTime = DateUtils.diffTime(sendTime, new Date());
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeDelayAppMsgRk(), JSONObject.toJSONString(qwAppMsgBody), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", diffTime);
            return message;
        });


    }

    /**
     * 检查发送者
     *
     * @param executeUserCondit 用户
     * @param executeDeptCondit 部门或岗位
     */
    private void checkSender(WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit, WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit) {
        if (executeUserCondit.isChange()) {
            List<String> weUserIds = executeUserCondit.getWeUserIds();
            if (weUserIds.size() > 1000) {
                throw new ServiceException("接收消息的成员不能超过1000个！");
            }
        }
        if (executeDeptCondit.isChange()) {
            List<String> deptIds = executeDeptCondit.getDeptIds();
            if (deptIds.size() > 100) {
                throw new ServiceException("接收消息的部门不能超过100个！");
            }
            List<String> posts = executeDeptCondit.getPosts();
            AjaxResult<List<SysUser>> allSysUser = qwSysUserClient.findAllSysUser(null, StringUtils.join(posts, ","), null);
            if (allSysUser.getCode() == 200) {
                List<SysUser> data = allSysUser.getData();
                List<String> collect = data.stream().map(i -> i.getWeUserId()).collect(Collectors.toList());
                if (collect.size() > 1000) {
                    throw new ServiceException("接收消息的岗位成员不能超过1000个！");
                }
            }
        }
    }
}
