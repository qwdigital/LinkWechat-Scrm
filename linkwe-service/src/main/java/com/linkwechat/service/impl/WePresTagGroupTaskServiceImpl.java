package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeComeStateContants;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.uuid.UUID;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import com.linkwechat.domain.community.vo.WeCommunityTaskEmplVo;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.taggroup.WePresTagGroupTask;
import com.linkwechat.domain.taggroup.WePresTagGroupTaskScope;
import com.linkwechat.domain.taggroup.WePresTagGroupTaskStat;
import com.linkwechat.domain.taggroup.WePresTagGroupTaskTag;
import com.linkwechat.domain.taggroup.query.WePresTagGroupTaskQuery;
import com.linkwechat.domain.taggroup.vo.*;
import com.linkwechat.domain.task.query.WeTasksRequest;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatUpdateJoinWayQuery;
import com.linkwechat.domain.wecom.query.customer.msg.WeCancelGroupMsgSendQuery;
import com.linkwechat.domain.wecom.query.customer.msg.WeGetGroupMsgListQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatGetJoinWayVo;
import com.linkwechat.domain.wecom.vo.customer.msg.WeGroupMsgListVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.*;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WePresTagGroupTaskServiceImpl extends ServiceImpl<WePresTagGroupTaskMapper, WePresTagGroupTask> implements IWePresTagGroupTaskService {


    @Autowired
    IWePresTagGroupTaskStatService iWePresTagGroupTaskStatService;

    @Autowired
    WePresTagGroupTaskScopeMapper taskScopeMapper;


    @Autowired
    WeGroupCodeMapper groupCodeMapper;

    @Autowired
    IWeCorpAccountService corpAccountService;

    @Autowired
    IWeGroupMessageTemplateService iWeGroupMessageTemplateService;

    @Autowired
    IWeMessagePushService weMessagePushService;


    @Autowired
    IWeCustomerService iWeCustomerService;


    @Autowired
    IWeGroupCodeService iWeGroupCodeService;

    @Autowired
    RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Autowired
    QwCustomerClient qwCustomerClient;


    @Autowired
    IWeGroupMemberService iWeGroupMemberService;


    @Autowired
    IWeGroupService iWeGroupService;


    @Autowired
    LinkWeChatConfig linkWeChatConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WePresTagGroupTask task) {


        String tagRedirectUrl = linkWeChatConfig.getTagRedirectUrl();

        if(StringUtils.isEmpty(tagRedirectUrl)){
            throw new WeComException("老客标签建群H5链接未配置");
        }


        //配置进群方式
        WeGroupChatGetJoinWayVo addJoinWayVo = iWeGroupCodeService.builderGroupCodeUrl(
                WeGroupCode.builder()
                        .autoCreateRoom(task.getAutoCreateRoom())
                        .roomBaseId(task.getRoomBaseId())
                        .roomBaseName(task.getRoomBaseName())
                        .chatIdList(task.getChatIdList())
                        .state(task.getGroupCodeState())
                        .build()
        );


        if(null != addJoinWayVo && null != addJoinWayVo.getJoin_way()
                && StringUtils.isNotEmpty(addJoinWayVo.getJoin_way().getQr_code())){

            task.setGroupCodeConfigId(addJoinWayVo.getJoin_way().getConfig_id());
            task.setGroupCodeState(WeComeStateContants.BQJQ_STATE + UUID.get16UUID());
            task.setGroupCodeUrl(addJoinWayVo.getJoin_way().getQr_code());
            task.setId(SnowFlakeUtil.nextId());
            task.setTagRedirectUrl(MessageFormat.format(tagRedirectUrl, task.getId().toString()));
            if(this.save(task)){
                //群发消息通知
                WeAddGroupMessageQuery messageQuery=new WeAddGroupMessageQuery();
                messageQuery.setChatType(1);
                messageQuery.setIsTask(0);
                messageQuery.setCurrentUserInfo(SecurityUtils.getLoginUser());
                List<WeAddGroupMessageQuery.SenderInfo> senderInfos=new ArrayList<>();
                WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
                senderInfo.setUserId("haon");
                senderInfo.setCustomerList(ListUtil.toList("wmbhUTLgAAXk6UIvbU9XaCZuPq17zOIA","wmbhUTLgAALohIZCHUega3fzMhsUvUSA"));
                senderInfos.add(senderInfo);

                messageQuery.setSenderList(senderInfos);
                List<WeMessageTemplate> templates = new ArrayList<>();
                WeMessageTemplate textAtt = new WeMessageTemplate();
                textAtt.setMsgType(MessageType.TEXT.getMessageType());
                textAtt.setContent(task.getWelcomeMsg());
                templates.add(textAtt);
                WeMessageTemplate linkTpl = new WeMessageTemplate();
                linkTpl.setMsgType(MessageType.LINK.getMessageType());
                linkTpl.setTitle(task.getLinkTitle());
                linkTpl.setPicUrl(task.getLinkCoverUrl());
                linkTpl.setDescription(task.getLinkDesc());
                templates.add(linkTpl);
                messageQuery.setAttachmentsList(templates);
                messageQuery.setMsgSource(6);
                if (ObjectUtil.equal(0, messageQuery.getIsTask()) && messageQuery.getSendTime() == null) {

                    //todo 立即发送
                    rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeGroupMsgRk(), JSONObject.toJSONString(messageQuery));
                }


            }

        }else{
            throw new WeComException(WeErrorCodeEnum.parseEnum(addJoinWayVo.getErrCode().intValue()).getErrorMsg());
        }


    }


    @Override
    public List<WePresTagGroupTask> selectTaskList(WePresTagGroupTask groupTask) {

        List<WePresTagGroupTask> groupTasks = this.list(new LambdaQueryWrapper<WePresTagGroupTask>()
                .like(StringUtils.isNotEmpty(groupTask.getTaskName())
                        , WePresTagGroupTask::getTaskName,
                        groupTask.getTaskName())
                .orderByDesc(WePresTagGroupTask::getCreateTime));
        if(CollectionUtil.isNotEmpty(groupTasks)){

            groupTasks.forEach(this::getTaskCodeInfo);

        }



        return groupTasks;
    }



    private void getTaskCodeInfo(WePresTagGroupTask groupTask){

        //触发客户数
        groupTask.setTouchWeCustomerNumber(
                iWePresTagGroupTaskStatService.count(new LambdaQueryWrapper<WePresTagGroupTaskStat>()
                        .eq(WePresTagGroupTaskStat::getTaskId,groupTask)
                        .eq(WePresTagGroupTaskStat::getSent,1))
        );


        //进群客户数
        groupTask.setJoinGroupCustomerNumber(
                iWeGroupMemberService.count(new LambdaQueryWrapper<WeGroupMember>()
                        .eq(WeGroupMember::getState,groupTask.getGroupCodeState()))
        );

        //实际群聊
        String chatIdList = groupTask.getChatIdList();
        if(StringUtils.isNotEmpty(chatIdList)){
            List<WeGroup> weGroups = iWeGroupService.list(new LambdaQueryWrapper<WeGroup>()
                    .in(WeGroup::getChatId, chatIdList.split(",")));
            if(CollectionUtil.isNotEmpty(weGroups)){
                groupTask.setGroupNames(
                        weGroups.stream().map(WeGroup::getGroupName).collect(Collectors.joining(","))
                );
            }
        }



    }

    @Override
    public WePresTagGroupTask getTaskById(Long taskId) {
        WePresTagGroupTask groupTask = this.getById(taskId);
        this.getTaskCodeInfo(groupTask);
        return groupTask;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveTaskByIds(Long[] idList) {
        List<WePresTagGroupTask> wePresTagGroupTasks = this.listByIds(Arrays.asList(idList));
        if(CollectionUtil.isNotEmpty(wePresTagGroupTasks)){

            wePresTagGroupTasks.stream().forEach(task->{

                List<WePresTagGroupTaskStat> tagGroupTaskStats = iWePresTagGroupTaskStatService.list(new LambdaQueryWrapper<WePresTagGroupTaskStat>()
                        .eq(WePresTagGroupTaskStat::getTaskId, task.getId()));

                if(CollectionUtil.isNotEmpty(tagGroupTaskStats)){
                    Set<String> msgIds = tagGroupTaskStats.stream()
                            .map(WePresTagGroupTaskStat::getMsgId)
                            .collect(Collectors.toSet());
                    if(CollectionUtil.isNotEmpty(msgIds)){

                        msgIds.stream().forEach(msgId->{
                            qwCustomerClient.cancelGroupMsgSend(WeCancelGroupMsgSendQuery.builder().msgid(msgId).build());
                        });

                    }
                    //停止原有群发，构建新群发
                    this.removeByIds(
                            tagGroupTaskStats.stream().map(WePresTagGroupTaskStat::getId).collect(Collectors.toList())
                    );
                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTask(WePresTagGroupTask task) {

        //更新群活码
        WeResultVo weResultVo = qwCustomerClient.updateJoinWayForGroupChat(
                WeGroupChatUpdateJoinWayQuery.builder()
                        .config_id(task.getGroupCodeConfigId())
                        .scene(2)
                        .auto_create_room(task.getAutoCreateRoom())
                        .room_base_id(task.getRoomBaseId())
                        .room_base_name(task.getRoomBaseName())
                        .chat_id_list(Arrays.asList(task.getChatIdList().split(",")))
                        .build()
        ).getData();


        if(null != weResultVo && weResultVo.getErrCode()
                .equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())){

            if(updateById(task)){
                //群发消息通知
                WeAddGroupMessageQuery messageQuery=new WeAddGroupMessageQuery();
                messageQuery.setChatType(1);
                messageQuery.setIsTask(0);
                messageQuery.setCurrentUserInfo(SecurityUtils.getLoginUser());
                messageQuery.setSenderList(task.getSenderList());
                List<WeMessageTemplate> templates = new ArrayList<>();
                WeMessageTemplate textAtt = new WeMessageTemplate();
                textAtt.setMsgType(MessageType.TEXT.getMessageType());
                textAtt.setContent(task.getWelcomeMsg());
                templates.add(textAtt);
                WeMessageTemplate linkTpl = new WeMessageTemplate();
                linkTpl.setMsgType(MessageType.LINK.getMessageType());
                linkTpl.setTitle(task.getLinkTitle());
                linkTpl.setPicUrl(task.getLinkCoverUrl());
                linkTpl.setDescription(task.getLinkDesc());
                templates.add(linkTpl);
                messageQuery.setAttachmentsList(templates);
                messageQuery.setMsgSource(6);
                if (ObjectUtil.equal(0, messageQuery.getIsTask()) && messageQuery.getSendTime() == null) {
                    //todo 立即发送
                    rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeDelayEx(), rabbitMQSettingConfig.getWeGroupMsgRk(), JSONObject.toJSONString(messageQuery));
                }
            }

        }else{
            throw new WeComException(WeErrorCodeEnum.parseEnum(weResultVo.getErrCode().intValue()).getErrorMsg());
        }

    }

    @Override
    public WePresTagGroupTaskTabCountVo countTab(String id) {
        WePresTagGroupTask task = this.getById(id);

        return this.baseMapper.countTab(task);
    }

    @Override
    public List<WePresTagGroupTaskTrendCountVo> findTrendCountVo(WePresTagGroupTask task) {

        WePresTagGroupTask tagGroupTask = this.getById(task.getId());
        tagGroupTask.setBeginTime(task.getBeginTime());
        tagGroupTask.setEndTime(task.getEndTime());
        return this.baseMapper.findTrendCountVo(tagGroupTask);
    }

    @Override
    public List<WePresTagGroupTaskTableVo> findWePresTagGroupTaskTable(WePresTagGroupTaskQuery wePresTagGroupTaskQuery) {
        WePresTagGroupTask wePresTagGroupTask = this.getById(wePresTagGroupTaskQuery.getId());
        if(null != wePresTagGroupTask){
            wePresTagGroupTaskQuery.setState(wePresTagGroupTask.getGroupCodeState());
        }
        return this.baseMapper.findWePresTagGroupTaskTable(wePresTagGroupTaskQuery);
    }

    @Override
    @Async
    public void synchExecuteResult(String id) {
        List<WePresTagGroupTaskStat> tagGroupTaskStats = iWePresTagGroupTaskStatService.list(new LambdaQueryWrapper<WePresTagGroupTaskStat>()
                .eq(WePresTagGroupTaskStat::getTaskId, id));
        if(CollectionUtil.isNotEmpty(tagGroupTaskStats)){
            tagGroupTaskStats.stream().forEach(k->{

                WeGetGroupMsgListQuery listQuery=new WeGetGroupMsgListQuery();
                listQuery.setMsgid(k.getMsgId());
                listQuery.setUserid(k.getUserId());

                WeGroupMsgListVo groupMsgSendResult = qwCustomerClient.getGroupMsgSendResult(listQuery).getData();

                Optional.ofNullable(groupMsgSendResult).map(WeGroupMsgListVo::getSendList).orElseGet(ArrayList::new).forEach(sendResult -> {

                    //设置任务执行状态
                    k.setSent(sendResult.getStatus());
                });

            });
            iWePresTagGroupTaskStatService.updateBatchById(tagGroupTaskStats);
        }

    }


//    /**
//     * 更新任务同时推送消息
//     *
//     * @param task
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateTaskAndSendMsg(WePresTagGroupTask task) throws WeComException {
//        try {
//            this.updateTask(task);
//            this.sendMessage(task);
//        } catch (WeComException e) {
//            throw e;
//        }
//
//    }

//    @Override
//    public List<WePresTagGroupTaskStat> getTaskStat(Long id, String customerName, Integer isInGroup, Integer isSent,
//                                                    Integer sendType) {
//
//        WePresTagGroupTaskStat query = new WePresTagGroupTaskStat();
//        query.setTaskId(id);
//        query.setCustomerName(customerName);
//        query.setInGroup(isInGroup);
//        query.setSent(isSent);
//
//        // 企业群发统计
//        if (sendType.equals(0)) {
//            return taskStatMapper.cropSendResultList(query);
//        }
//        // 个人群发统计
//        else {
//            return taskStatMapper.singleSendResultList(query);
//        }
//    }
//
//    @Override
//    public List<WeCommunityTaskEmplVo> getScopeListByTaskId(Long taskId) {
//        return taskScopeMapper.getScopeListByTaskId(taskId);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public int updateFollowerTaskStatus(Long taskId, String followerId) {
//        // 更新所有对应客户的已送达为1
//        LambdaUpdateWrapper<WePresTagGroupTaskStat> statLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//        statLambdaUpdateWrapper.eq(WePresTagGroupTaskStat::getTaskId, taskId)
//                .in(WePresTagGroupTaskStat::getUserId, followerId).set(WePresTagGroupTaskStat::getSent, 1);
//        return taskStatMapper.update(null, statLambdaUpdateWrapper);
//    }
//
//    @Override
//    public boolean isNameOccupied(WePresTagGroupTask task) {
//        Long currentId = Optional.ofNullable(task.getTaskId()).orElse(-1L);
//        LambdaQueryWrapper<WePresTagGroupTask> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(WePresTagGroupTask::getDelFlag, 0).eq(WePresTagGroupTask::getTaskName, task.getTaskName());
//        List<WePresTagGroupTask> queryRes = baseMapper.selectList(queryWrapper);
//        return !queryRes.isEmpty() && !currentId.equals(queryRes.get(0).getTaskId());
//    }
//
//    @Override
//    public void sendMessage(WePresTagGroupTask task) throws WeComException {
//        Integer sendType = task.getSendType();
//        try {
//            // 企业群发逻辑
//            if (sendType.equals(0)) {
//
//                List<WeAddGroupMessageQuery.SenderInfo> senderInfoList = baseMapper.selectSenderInfo(task.getTaskId());
//                if (StringUtils.isEmpty(senderInfoList)) {
//                    throw new WeComException("找不到符合筛选条件的发送对象");
//                }
//                String codeUrl = groupCodeMapper.selectById(task.getGroupCodeId()).getCodeUrl();
////                WeMediaDto media = materialService.uploadTemporaryMaterial(codeUrl, MediaType.IMAGE.getMediaType(), "临时文件");
//                List<WeMessageTemplate> attachmentList = Lists.newArrayList();
//                WeMessageTemplate template = new WeMessageTemplate();
//                template.setMsgType(MessageType.IMAGE.getMessageType());
////                template.setMediaId(media.getMedia_id());
//                template.setPicUrl(codeUrl);
//                attachmentList.add(template);
//
//                List<WeAddGroupMessageQuery.SenderInfo> senderList = senderInfoList.stream().map(sender -> {
//                    WeAddGroupMessageQuery.SenderInfo info = new WeAddGroupMessageQuery.SenderInfo();
//                    info.setUserId(sender.getUserId());
//                    info.setCustomerList(sender.getCustomerList());
//                    return info;
//                }).collect(Collectors.toList());
//                WeAddGroupMessageQuery query = new WeAddGroupMessageQuery();
//                query.setContent(task.getWelcomeMsg());
//                query.setSenderList(senderList);
//                query.setChatType(1);
//                query.setIsTask(0);
//                query.setAttachmentsList(attachmentList);
//
//                iWeGroupMessageTemplateService.addGroupMsgTemplate(query);
//                task.setMessageTemplateId(query.getId());
//                baseMapper.updateById(task);
//            }
//            // 个人群发逻辑
//            else {
//
//
//                List<WeCustomer> externalIds = new ArrayList<>();
//                if (task.getSendScope().equals(0)) { //全部客户
//                    externalIds = iWeCustomerService.list();
//                } else { //部分客户
//                    externalIds = iWeCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
//                            .in(WeCustomer::getExternalUserid, task.getExternalUserIds()));
//                }
//
//                //查询当前员工下的
//                List<String> followerIds = externalIds.stream().map(WeCustomer::getAddUserId).distinct().collect(Collectors.toList());
//
//                if (StringUtils.isEmpty(followerIds)) {
//                    throw new WeComException("消息无法推送，找不到符合筛选条件的员工");
//                }
//
//                // 删除旧统计对象
//                LambdaUpdateWrapper<WePresTagGroupTaskStat> updateWrapper = new LambdaUpdateWrapper<>();
//                updateWrapper.eq(WePresTagGroupTaskStat::getTaskId, task.getTaskId())
//                        .set(WePresTagGroupTaskStat::getDelFlag, 1);
//                taskStatMapper.update(null, updateWrapper);
//
//                // 保存新的统计对象
//                List<WePresTagGroupTaskStat> statList = externalIds.stream().map(i -> {
//                    WePresTagGroupTaskStat stat = new WePresTagGroupTaskStat();
//                    stat.setTaskId(task.getTaskId());
//                    stat.setSent(0);
//                    stat.setUserId(i.getAddUserId());
//                    stat.setExternalUserId(i.getExternalUserid());
//                    stat.setCreateBy(task.getCreateBy());
//                    return stat;
//                }).collect(Collectors.toList());
//                taskStatMapper.batchSave(statList);
//                weMessagePushService.pushMessageSelfH5(followerIds, "【任务动态】<br/> 您有一项「标签建群」任务待完成，请尽快处理", MessageNoticeType.TAG.getType(), true);
//
//            }
//        } catch (Exception e) {
//            log.error("============> 老客标签建群任务发送失败, 任务明细: {}", task);
//            log.error("错误信息: {}", e.getMessage());
//            throw new WeComException(e.getMessage());
//        }
//    }
//
//    @Override
//    public List<String> selectExternalIds(Long taskId) {
//        List<WeCustomer> customers = baseMapper.selectTaskExternalIds(taskId);
//        return customers.stream().map(WeCustomer::getExternalUserid).collect(Collectors.toList());
//    }
//
//    @Override
//    public List<String> selectFollowerIdByTaskId(Long taskId) {
//        return baseMapper.selectTaskFollowerIds(taskId);
//    }
//
//    @Override
//    public List<WePresTagGroupTaskVo> getFollowerTaskList(String emplId, Integer isDone) {
//
//        return baseMapper.getTaskListByFollowerId(emplId, isDone);
//    }
//
//    private List<String> selectExternalIdsByFollowerId(Long taskId, String followerId) {
//        return baseMapper.selectTaskExternalByFollowerId(taskId, followerId);
//    }
}
