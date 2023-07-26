package com.linkwechat.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.MessageConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.common.enums.QwAppMsgBusinessTypeEnum;
import com.linkwechat.common.enums.WeMsgTypeEnum;
import com.linkwechat.common.enums.task.WeTasksTitleEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordCooperateUserRequest;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.task.entity.WeTasks;
import com.linkwechat.domain.task.query.WeTasksRequest;
import com.linkwechat.domain.task.vo.WeTasksVO;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeLeadsMapper;
import com.linkwechat.mapper.WeLeadsSeaMapper;
import com.linkwechat.mapper.WeTasksMapper;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeTasksService;
import com.linkwechat.service.QwAppSendMsgService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 待办任务 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-07-20
 */
@Service
public class WeTasksServiceImpl extends ServiceImpl<WeTasksMapper, WeTasks> implements IWeTasksService {
    @Resource
    private WeLeadsMapper weLeadsMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private WeLeadsSeaMapper weLeadsSeaMapper;
    @Resource
    private QwAppSendMsgService qwAppSendMsgService;
    @Resource
    private IWeCorpAccountService weCorpAccountService;
    @Resource
    private RabbitMQSettingConfig rabbitMQSettingConfig;
    @Resource
    private LinkWeChatConfig linkWeChatConfig;

    @Override
    public List<WeTasksVO> myList() {
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getUserId, SecurityUtils.getLoginUser().getUserId());
        queryWrapper.eq(WeTasks::getStatus, 0);
        queryWrapper.eq(WeTasks::getVisible, 1);
        queryWrapper.orderByDesc(WeTasks::getCreateTime);
        List<WeTasks> weTasks = this.baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(weTasks, WeTasksVO.class);
    }

    @Override
    public List<WeTasksVO> history() {
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getUserId, SecurityUtils.getLoginUser().getUserId());
        queryWrapper.ne(WeTasks::getStatus, 0);
        queryWrapper.eq(WeTasks::getVisible, 1);
        queryWrapper.orderByDesc(WeTasks::getCreateTime);
        List<WeTasks> weTasks = this.baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(weTasks, WeTasksVO.class);
    }

    @Override
    public void add(WeTasks weTasks) {
        weTasks.setId(IdUtil.getSnowflakeNextId());
        weTasks.setSendTime(LocalDateTime.now());
        weTasks.setStatus(0);
        weTasks.setDelFlag(Constants.COMMON_STATE);
        this.baseMapper.insert(weTasks);
    }

    @Override
    public void finish(Long id) {
        LambdaUpdateWrapper<WeTasks> updateWrapper = Wrappers.lambdaUpdate(WeTasks.class);
        updateWrapper.eq(WeTasks::getId, id);
        updateWrapper.set(WeTasks::getStatus, 1);
        this.update(updateWrapper);
    }

    @Override
    public void addLeadsLongTimeNotFollowUp(Long leadsId, String name, Long userId, String weUserId, Long seaId) {
        WeLeadsSea sea = weLeadsSeaMapper.selectById(seaId);
        //不自动回收
        if (sea.getIsAutoRecovery().equals(0)) {
            return;
        }
        Integer first = sea.getFirst();
        DateTime dateTime = DateUtil.offsetDay(DateUtil.beginOfDay(DateUtil.date()), first - 1);
        WeTasks build = WeTasks.builder()
                .id(IdUtil.getSnowflakeNextId())
                .userId(userId)
                .weUserId(weUserId)
                .type(WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getCode())
                .title(WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getTitle())
                .content(new JSONObject().put("客户", name).toString())
                .sendTime(dateTime.toLocalDateTime())
                //TODO 链接等待前端给
                .url(null)
                .status(0)
                .delFlag(Constants.COMMON_STATE)
                .visible(0)
                .leadsId(leadsId)
                .build();
        this.save(build);
    }

    @Override
    public void cancelLeadsLongTimeNotFollowUp(Long leadsId, Long userId) {
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getType, WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getCode());
        queryWrapper.eq(WeTasks::getLeadsId, leadsId);
        queryWrapper.eq(WeTasks::getUserId, userId);
        queryWrapper.eq(WeTasks::getStatus, 0);
        WeTasks one = this.getOne(queryWrapper);
        if (BeanUtil.isNotEmpty(one)) {
            if (one.getVisible().equals(0)) {
                this.removeById(one.getId());
            } else {
                LambdaUpdateWrapper<WeTasks> updateWrapper = Wrappers.lambdaUpdate(WeTasks.class);
                updateWrapper.eq(WeTasks::getId, one.getId());
                updateWrapper.set(WeTasks::getStatus, 1);
                this.update(updateWrapper);
            }
        }
    }

    @Override
    public void cancelAndAddLeadsLongTimeNotFollowUp(Long leadsId, String name, Long userId, String weUserId, Long seaId) {
        this.cancelLeadsLongTimeNotFollowUp(leadsId, userId);
        this.addLeadsLongTimeNotFollowUp(leadsId, name, userId, weUserId, seaId);
    }

    @Override
    public void handlerLeadsLongTimeNotFollowUp() {
        DateTime dateTime = DateUtil.beginOfDay(DateUtil.date());
        LambdaQueryWrapper<WeTasks> queryWrapper = Wrappers.lambdaQuery(WeTasks.class);
        queryWrapper.eq(WeTasks::getType, WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getCode());
        queryWrapper.eq(WeTasks::getSendTime, dateTime.toLocalDateTime());
        queryWrapper.eq(WeTasks::getStatus, 0);
        queryWrapper.eq(WeTasks::getVisible, 0);
        queryWrapper.eq(WeTasks::getDelFlag, Constants.COMMON_STATE);
        List<WeTasks> list = this.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(list)) {
            //修改线索状态为显示
            LambdaUpdateWrapper<WeTasks> updateWrapper = Wrappers.lambdaUpdate(WeTasks.class);
            updateWrapper.in(WeTasks::getId, list.stream().map(WeTasks::getId).collect(Collectors.toList()));
            updateWrapper.set(WeTasks::getVisible, 1);
            this.update(updateWrapper);
        }
        for (WeTasks weTasks : list) {
            //发送应用通知消
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
            QwAppMsgBody body = new QwAppMsgBody();
            body.setCorpId(weCorpAccount.getCorpId());
            body.setCorpUserIds(Arrays.asList(weTasks.getWeUserId()));
            //类型
            body.setBusinessType(QwAppMsgBusinessTypeEnum.COMMON.getType());
            WeMessageTemplate messageTemplate = new WeMessageTemplate();
            messageTemplate.setMsgType(WeMsgTypeEnum.TASKCARD.getMessageType());
            messageTemplate.setTitle(WeTasksTitleEnum.LEADS_LONG_TIME_NOT_FOLLOW_UP.getTitle());
            //描述
            JSONObject jsonObject = JSONObject.parseObject(weTasks.getContent());
            String desc = StrUtil.format(MessageConstants.LEADS_LONG_TIME_NOT_FOLLOW_UP, DateUtil.date().toDateStr(), jsonObject.get("客户"));
            messageTemplate.setDescription(desc);
            //TODO 链接，等待前端给, 参数要加上
            messageTemplate.setLinkUrl(null);
            messageTemplate.setBtntxt("详情");
            body.setMessageTemplates(messageTemplate);
            qwAppSendMsgService.appMsgSend(body);
        }
    }

    @Override
    public void appointItemWaitFollowUp(WeTasksRequest request) {
        this.appointItemWaitFollowUpSendMq(request, WeTasksTitleEnum.LEADS_COVENANT_WAIT_FOLLOW_UP.getCode());
    }

    @Override
    public void userAppointItemWaitFollowUp(WeTasksRequest request) {
        List<WeLeadsFollowRecordCooperateUserRequest> cooperateUsers = request.getCooperateUsers();
        if (CollectionUtil.isEmpty(cooperateUsers)) {
            return;
        }

        for (WeLeadsFollowRecordCooperateUserRequest cooperateUser : cooperateUsers) {
            WeTasksRequest req = new WeTasksRequest();
            req.setId(IdUtil.getSnowflakeNextId());
            req.setUserId(cooperateUser.getUserId());
            req.setWeUserId(cooperateUser.getWeUserId());
            req.setLeadsId(request.getLeadsId());
            req.setRecordId(request.getRecordId());
            req.setCooperateTime(request.getCooperateTime());
            this.appointItemWaitFollowUpSendMq(req, WeTasksTitleEnum.LEADS_USER_COVENANT_WAIT_FOLLOW_UP.getCode());
        }
    }

    @Override
    public void userFollowUp2You(WeTasksRequest request) {
        List<WeLeadsFollowRecordCooperateUserRequest> cooperateUsers = request.getCooperateUsers();
        if (CollectionUtil.isNotEmpty(cooperateUsers)) {
            for (WeLeadsFollowRecordCooperateUserRequest cooperateUser : cooperateUsers) {
                WeTasksRequest req = new WeTasksRequest();
                req.setId(IdUtil.getSnowflakeNextId());
                req.setUserId(cooperateUser.getUserId());
                req.setWeUserId(cooperateUser.getWeUserId());
                req.setUserName(request.getUserName());
                req.setLeadsId(request.getLeadsId());
                req.setRecordId(request.getRecordId());
                req.setCooperateTime(request.getCooperateTime());
                req.setType(WeTasksTitleEnum.LEADS_USER_FOLLOW_UP_2_YOU.getCode());
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeTasksDelayRk(), JSONObject.toJSONString(request), message -> {
                    //注意这里时间可使用long类型,毫秒单位，设置header
                    message.getMessageProperties().setHeader("x-delay", 5000L);
                    return message;
                });
            }
        }
    }

    @Override
    public void groupAddByLabel(WeTasksRequest request) {
        request.setType(WeTasksTitleEnum.GROUP_ADD_BY_LABEL.getCode());
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeTasksDelayRk(), JSONObject.toJSONString(request), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", 5000L);
            return message;
        });
    }

    /**
     * 线索约定事项待处理-发送mq
     *
     * @param request 请求参数
     * @param type    线索约定事项待跟进类型 {@link WeTasksTitleEnum}
     * @author WangYX
     * @date 2023/07/24 16:14
     */
    public void appointItemWaitFollowUpSendMq(WeTasksRequest request, Integer type) {
        request.setId(IdUtil.getSnowflakeNextId());
        //线索约定事项待跟进类型
        request.setType(type);
        //协作时间
        Date cooperateTime = request.getCooperateTime();
        //提醒时间
        DateTime remindTime = DateUtil.offsetHour(cooperateTime, -6);
        //取消时间
        DateTime cancelTime = DateUtil.offsetHour(cooperateTime, 6);

        DateTime now = DateTime.now();
        //提醒时间至当前时间的时间间隔
        long remindTimeInterval = DateUtil.between(remindTime, now, DateUnit.MS);
        request.setMode(0);
        if (remindTimeInterval < 21600000L) {
            //不足6小时，直接发送
            remindTimeInterval = 5000;
        }
        //线索约定事项待跟进
        long finalRemindTimeInterval = remindTimeInterval;
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeTasksDelayRk(), JSONObject.toJSONString(request), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", finalRemindTimeInterval);
            return message;
        });

        //提醒时间至当前时间的时间间隔
        long cancelTimeInterval = DateUtil.between(cancelTime, now, DateUnit.MS);
        request.setMode(1);
        //线索约定事项待跟进（取消）
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeTasksDelayRk(), JSONObject.toJSONString(request), message -> {
            //注意这里时间可使用long类型,毫秒单位，设置header
            message.getMessageProperties().setHeader("x-delay", cancelTimeInterval);
            return message;
        });
    }


    @Override
    public void handlerWeTasks(WeTasksRequest request) {
        switch (request.getType()) {
            case 1:
                this.handlerAppointItemWaitFollowUp(request);
                break;
            case 2:
                this.handlerUserFollowUp2You(request);
                break;
            case 4:
                this.handlerUserAppointItemWaitFollowUp(request);
                break;
            case 7:
                this.handlerGroupAddByLabel(request);
                break;
            default:
                break;
        }
    }


    /**
     * 处理-线索约定事项待跟进
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/24 13:49
     */
    public void handlerAppointItemWaitFollowUp(WeTasksRequest request) {
        WeLeads weLeads = weLeadsMapper.selectById(request.getLeadsId());
        if (BeanUtil.isEmpty(weLeads)) {
            return;
        }
        if (request.getMode().equals(0)) {
            //1.添加待办任务
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("客户", weLeads.getName());
            WeTasks build = WeTasks.builder()
                    .id(request.getId())
                    .userId(request.getUserId())
                    .weUserId(request.getWeUserId())
                    .type(WeTasksTitleEnum.LEADS_COVENANT_WAIT_FOLLOW_UP.getCode())
                    .title(WeTasksTitleEnum.LEADS_COVENANT_WAIT_FOLLOW_UP.getTitle())
                    .content(jsonObject.toJSONString())
                    .sendTime(LocalDateTime.now())
                    //TODO 线索约定事项待跟进的URL
                    .url(null)
                    .status(0)
                    .delFlag(Constants.COMMON_STATE)
                    .visible(1)
                    .leadsId(request.getLeadsId())
                    .recordId(request.getRecordId())
                    .build();
            this.save(build);

            //2.发送应用消息
            String desc = StrUtil.format(MessageConstants.LEADS_COVENANT_WAIT_FOLLOW_UP, DateUtil.date().toDateStr(), weLeads.getName());
            //TODO 链接URL等待前端给
            this.sendAppMsg(request.getWeUserId(), WeTasksTitleEnum.LEADS_COVENANT_WAIT_FOLLOW_UP.getTitle(), desc, null);
        } else {
            //取消线索约定事项待跟进
            WeTasks weTasks = this.getById(request.getId());
            if (!weTasks.getStatus().equals(1)) {
                //任务未完成，则取消执行
                WeTasks build = WeTasks.builder().id(request.getId()).status(2).build();
                this.updateById(build);
            }
        }
    }

    /**
     * 处理-成员的线索约定事项待跟进
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/24 16:09
     */
    public void handlerUserAppointItemWaitFollowUp(WeTasksRequest request) {
        this.handlerAppointItemWaitFollowUp(request);
    }

    /**
     * 处理-有成员的线索跟进@了你
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/24 17:16
     */
    public void handlerUserFollowUp2You(WeTasksRequest request) {
        WeLeads weLeads = weLeadsMapper.selectById(request.getLeadsId());
        if (BeanUtil.isEmpty(weLeads)) {
            return;
        }
        //内容
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("客户", weLeads.getName());
        jsonObject.put("成员", request.getUserName());

        //1.添加待办任务
        WeTasks build = WeTasks.builder()
                .id(IdUtil.getSnowflakeNextId())
                .userId(request.getUserId())
                .weUserId(request.getWeUserId())
                .type(WeTasksTitleEnum.LEADS_USER_FOLLOW_UP_2_YOU.getCode())
                .title(WeTasksTitleEnum.LEADS_USER_FOLLOW_UP_2_YOU.getTitle())
                .content(jsonObject.toJSONString())
                .sendTime(LocalDateTime.now())
                //TODO 有成员的线索跟进@了你 URL，这里的Url要包含记录内容Id（记录内容Id用户内容高亮）和待办任务Id（待办任务Id用于回复之后，设置待办任务为已完成）
                .url(null)
                .status(0)
                .delFlag(Constants.COMMON_STATE)
                .visible(1)
                .leadsId(request.getLeadsId())
                .recordId(request.getRecordId())
                .build();
        this.save(build);

        //2.发送应用消息
        String desc = StrUtil.format(MessageConstants.LEADS_USER_FOLLOW_UP_2_YOU, DateUtil.date().toDateStr(), weLeads.getName(), request.getUserName());
        //TODO 链接URL等待前端给
        this.sendAppMsg(request.getWeUserId(), WeTasksTitleEnum.LEADS_COVENANT_WAIT_FOLLOW_UP.getTitle(), desc, null);
    }

    /**
     * 处理-标签建群任务
     *
     * @param request 请求参数
     * @author WangYX
     * @date 2023/07/25 15:37
     */
    public void handlerGroupAddByLabel(WeTasksRequest request) {
        List<String> weUserIds = request.getWeUserIds();
        SysUserQuery query = SysUserQuery.builder().weUserIds(weUserIds).build();
        AjaxResult<List<SysUserVo>> result = qwSysUserClient.getUserListByWeUserIds(query);
        if (result.getCode() != HttpStatus.SUCCESS) {
            throw new ServiceException("获取员工数据异常：" + result.getMsg());
        }
        List<SysUserVo> data = result.getData();
        WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
        String tagRedirectUrl = linkWeChatConfig.getTagRedirectUrl();
        String url = URLEncoder.encode(String.format("%s?corpId=%s&agentId=%s&type=%s", tagRedirectUrl, weCorpAccount.getCorpId(), weCorpAccount.getAgentId(), MessageNoticeType.TAG.getType()));
        List<WeTasks> weTasksList = new ArrayList<>();
        for (SysUserVo datum : data) {
            String jsonString = JSONObject.toJSONString("请尽快处理");
            WeTasks build = WeTasks.builder()
                    .id(IdUtil.getSnowflakeNextId())
                    .userId(datum.getUserId())
                    .weUserId(datum.getWeUserId())
                    .type(WeTasksTitleEnum.GROUP_ADD_BY_LABEL.getCode())
                    .title(WeTasksTitleEnum.GROUP_ADD_BY_LABEL.getTitle())
                    .content(jsonString)
                    .url(url)
                    .status(0)
                    .delFlag(Constants.COMMON_STATE)
                    .visible(1)
                    .build();
            weTasksList.add(build);
        }
        if (CollectionUtil.isNotEmpty(weTasksList)) {
            this.saveBatch(weTasksList);
        }
    }

    /**
     * 发送应用消息-文本卡片消息
     *
     * @param weUserId 发送员工的企微Id
     * @param title    文本卡片消息的标题
     * @param desc     文本卡片消息的描述
     * @param url      文本卡片消息的链接
     * @author WangYX
     * @date 2023/07/24 14:08
     */
    private void sendAppMsg(String weUserId, String title, String desc, String url) {
        //发送应用通知消
        WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
        QwAppMsgBody body = new QwAppMsgBody();
        body.setCorpId(weCorpAccount.getCorpId());
        body.setCorpUserIds(Arrays.asList(weUserId));
        //类型
        body.setBusinessType(QwAppMsgBusinessTypeEnum.COMMON.getType());
        WeMessageTemplate messageTemplate = new WeMessageTemplate();
        messageTemplate.setMsgType(WeMsgTypeEnum.TASKCARD.getMessageType());
        messageTemplate.setTitle(title);
        messageTemplate.setDescription(desc);
        //TODO 链接，等待前端给, 参数要加上
        messageTemplate.setLinkUrl(url);
        messageTemplate.setBtntxt("详情");
        body.setMessageTemplates(messageTemplate);
        qwAppSendMsgService.appMsgSend(body);
    }

}
