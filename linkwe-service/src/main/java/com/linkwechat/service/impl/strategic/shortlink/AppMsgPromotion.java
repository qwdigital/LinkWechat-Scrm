package com.linkwechat.service.impl.strategic.shortlink;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.QwAppMsgBusinessTypeEnum;
import com.linkwechat.common.enums.WeMsgTypeEnum;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Long saveAndSend(WeShortLinkPromotionAddQuery query, WeShortLinkPromotion weShortLinkPromotion) {

        //1.保存短链推广
        WeShortLinkPromotionTemplateAppMsgAddQuery appMsgAddQuery = query.getAppMsg();
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

        //2.保存短链推广模板-应用消息
        WeShortLinkPromotionTemplateAppMsg appMsg = BeanUtil.copyProperties(appMsgAddQuery, WeShortLinkPromotionTemplateAppMsg.class);
        if (appMsgAddQuery.getSendScope().equals(0)) {
            WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit = appMsgAddQuery.getExecuteUserCondit();
            List<String> weUserIds = executeUserCondit.getWeUserIds();
            if (weUserIds != null && weUserIds.size() > 0) {
                String ids = StringUtils.join(weUserIds, ",");
                appMsg.setUserIds(ids);
            }
        } else if (appMsgAddQuery.getSendScope().equals(1)) {
            WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit = appMsgAddQuery.getExecuteDeptCondit();
            List<String> deptIds = executeDeptCondit.getDeptIds();
            List<String> posts = executeDeptCondit.getPosts();
            if (deptIds != null && deptIds.size() > 0) {
                String ids = StringUtils.join(deptIds, ",");
                appMsg.setDeptIds(ids);
            }
            if (posts != null && posts.size() > 0) {
                String ids = StringUtils.join(posts, ",");
                appMsg.setUserIds(ids);
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
        //发送类型：0立即发送 1定时发送
        //TODO 链接要替换
        String content = "【短链推广】\r\n\r\n  您有一条新的短链推广任务，请点击链接，进入任务页，分享短链内容给客户。\r\n\r\n <a href=\"http://www.baidu.com\">" + weShortLinkPromotion.getTaskName() + "</a>";
        String ids = null;
        String postIds = null;
        //应用消息发送类型 0成员 1部门或岗位
        if (appMsgAddQuery.getSendScope().equals(0)) {
            List<String> weUserIds = appMsgAddQuery.getExecuteUserCondit().getWeUserIds();
            ids = StringUtils.join(weUserIds, ",");
        } else if (appMsgAddQuery.getSendScope().equals(1)) {
            List<String> deptIds = appMsgAddQuery.getExecuteDeptCondit().getDeptIds();
            List<String> posts = appMsgAddQuery.getExecuteDeptCondit().getPosts();
            ids = StringUtils.join(deptIds, ",");
            postIds = StringUtils.join(posts, ",");
        }
        if (sendType.equals(0)) {
            //立即发送
            directSend(weShortLinkPromotion.getId(), appMsg.getId(), content, query.getAttachmentsList(), null, appMsgAddQuery.getSendScope(), ids, postIds);
        } else {
            //定时发送
            timingSend(weShortLinkPromotion.getId(), appMsg.getId(), content, Date.from(query.getClient().getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()), query.getAttachmentsList(), null, appMsgAddQuery.getSendScope(), ids, postIds);
        }
        //任务结束时间
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            timingEnd(weShortLinkPromotion.getId(),appMsg.getId(), weShortLinkPromotion.getType(), Date.from(taskEndTime.atZone(ZoneId.systemDefault()).toInstant()));
        });


        return weShortLinkPromotion.getId();
    }

    @Override
    public void updateAndSend(WeShortLinkPromotionUpdateQuery query, WeShortLinkPromotion weShortLinkPromotion) {

        WeShortLinkPromotionTemplateAppMsgUpdateQuery appMsgUpdateQuery = query.getAppMsg();

        //1.修改短链推广
        //发送类型：0立即发送 1定时发送
        Integer sendType = appMsgUpdateQuery.getSendType();
        if (sendType.equals(0)) {
            //任务状态: 0待推广 1推广中 2已结束 3暂存中
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

        //2.修改短链推广模板-应用消息
        //删除
        LambdaUpdateWrapper<WeShortLinkPromotionTemplateAppMsg> appMsgUpdateWrapper = Wrappers.lambdaUpdate();
        appMsgUpdateWrapper.eq(WeShortLinkPromotionTemplateAppMsg::getPromotionId, weShortLinkPromotion.getId());
        appMsgUpdateWrapper.set(WeShortLinkPromotionTemplateAppMsg::getDelFlag, 1);
        weShortLinkPromotionTemplateAppMsgService.update(appMsgUpdateWrapper);
        //添加
        WeShortLinkPromotionTemplateAppMsg appMsg = BeanUtil.copyProperties(appMsgUpdateQuery, WeShortLinkPromotionTemplateAppMsg.class);
        appMsg.setId(null);
        if (appMsgUpdateQuery.getSendScope().equals(0)) {
            WeSopExecuteUserConditVo.ExecuteUserCondit executeUserCondit = appMsgUpdateQuery.getExecuteUserCondit();
            List<String> weUserIds = executeUserCondit.getWeUserIds();
            if (weUserIds != null && weUserIds.size() > 0) {
                String ids = StringUtils.join(weUserIds, ",");
                appMsg.setUserIds(ids);
            }
        } else if (appMsgUpdateQuery.getSendScope().equals(1)) {
            WeSopExecuteUserConditVo.ExecuteDeptCondit executeDeptCondit = appMsgUpdateQuery.getExecuteDeptCondit();
            List<String> deptIds = executeDeptCondit.getDeptIds();
            List<String> posts = executeDeptCondit.getPosts();
            if (deptIds != null && deptIds.size() > 0) {
                String ids = StringUtils.join(deptIds, ",");
                appMsg.setDeptIds(ids);
            }
            if (posts != null && posts.size() > 0) {
                String ids = StringUtils.join(posts, ",");
                appMsg.setUserIds(ids);
            }
        }
        appMsg.setPromotionId(weShortLinkPromotion.getId());
        appMsg.setDelFlag(0);
        weShortLinkPromotionTemplateAppMsgService.save(appMsg);

        //发送mq
        String content = "【短链推广】\r\n\r\n  您有一条新的短链推广任务，请点击链接，进入任务页，分享短链内容给客户。\r\n\r\n <a href=\"http://www.baidu.com\">" + weShortLinkPromotion.getTaskName() + "</a>";
        String ids = null;
        String postIds = null;
        //应用消息发送类型 0成员 1部门或岗位
        if (appMsgUpdateQuery.getSendScope().equals(0)) {
            List<String> weUserIds = appMsgUpdateQuery.getExecuteUserCondit().getWeUserIds();
            ids = StringUtils.join(weUserIds, ",");
        } else if (appMsgUpdateQuery.getSendScope().equals(1)) {
            List<String> deptIds = appMsgUpdateQuery.getExecuteDeptCondit().getDeptIds();
            List<String> posts = appMsgUpdateQuery.getExecuteDeptCondit().getPosts();
            ids = StringUtils.join(deptIds, ",");
            postIds = StringUtils.join(posts, ",");
        }
        if (sendType.equals(0)) {
            //立即发送
            directSend(weShortLinkPromotion.getId(), appMsg.getId(), content, query.getAttachmentsList(), null, appMsgUpdateQuery.getSendScope(), ids, postIds);
        } else {
            //定时发送
            timingSend(weShortLinkPromotion.getId(), appMsg.getId(), content, Date.from(query.getClient().getTaskSendTime().atZone(ZoneId.systemDefault()).toInstant()), query.getAttachmentsList(), null, appMsgUpdateQuery.getSendScope(), ids, postIds);
        }
        //任务结束时间
        Optional.ofNullable(taskEndTime).ifPresent(o -> {
            timingEnd(weShortLinkPromotion.getId(),appMsg.getId(), weShortLinkPromotion.getType(), Date.from(taskEndTime.atZone(ZoneId.systemDefault()).toInstant()));
        });
    }

    @Override
    protected void directSend(Long id, Long businessId, String content, List<WeMessageTemplate> attachments, List<WeAddGroupMessageQuery.SenderInfo> senderList, Object... objects) {
        QwAppMsgBody qwAppMsgBody = new QwAppMsgBody();
        qwAppMsgBody.setCorpId(SecurityUtils.getCorpId());
        //应用消息发送类型 0成员 1部门或岗位
        Integer sendScope = (Integer) objects[0];
        if (sendScope.equals(0)) {
            String ids = (String) objects[1];
            String[] split = ids.split(",");
            List<String> list = Arrays.asList(split);
            qwAppMsgBody.setCorpUserIds(list);
        } else if (sendScope.equals(1)) {
            Object object = objects[1];
            if (BeanUtil.isNotEmpty(object)) {
                String ids = String.valueOf(object);
                String[] split = ids.split(",");
                List<String> list = Arrays.asList(split);
                qwAppMsgBody.setDeptIds(list);
            }
            Object object1 = objects[2];
            if (BeanUtil.isNotEmpty(object1)) {
                String postIds = String.valueOf(object1);
                AjaxResult<List<SysUser>> allSysUser = qwSysUserClient.findAllSysUser(null, postIds, null);
                if (allSysUser.getCode() == 200) {
                    List<SysUser> data = allSysUser.getData();
                    List<String> collect = data.stream().map(i -> i.getWeUserId()).collect(Collectors.toList());
                    if (collect != null && collect.size() > 0) {
                        qwAppMsgBody.setCorpUserIds(collect);
                    }
                }
            }
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
        Integer sendScope = (Integer) objects[0];
        if (sendScope.equals(0)) {
            String ids = (String) objects[1];
            String[] split = ids.split(",");
            List<String> list = Arrays.asList(split);
            qwAppMsgBody.setCorpUserIds(list);
        } else if (sendScope.equals(1)) {
            Object object = objects[1];
            if (BeanUtil.isNotEmpty(object)) {
                String ids = String.valueOf(object);
                String[] split = ids.split(",");
                List<String> list = Arrays.asList(split);
                qwAppMsgBody.setDeptIds(list);
            }
            Object object1 = objects[2];
            if (BeanUtil.isNotEmpty(object1)) {
                String postIds = String.valueOf(object1);
                AjaxResult<List<SysUser>> allSysUser = qwSysUserClient.findAllSysUser(null, postIds, null);
                if (allSysUser.getCode() == 200) {
                    List<SysUser> data = allSysUser.getData();
                    List<String> collect = data.stream().map(i -> i.getWeUserId()).collect(Collectors.toList());
                    if (collect != null && collect.size() > 0) {
                        qwAppMsgBody.setCorpUserIds(collect);
                    }
                }
            }
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

    @Override
    protected void timingEnd(Long promotionId, Long businessId, Integer type, Date taskEndTime) {

    }
}
