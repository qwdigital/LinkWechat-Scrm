package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.enums.MessageNoticeType;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.community.vo.WeCommunityTaskEmplVo;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.taggroup.WePresTagGroupTask;
import com.linkwechat.domain.taggroup.WePresTagGroupTaskScope;
import com.linkwechat.domain.taggroup.WePresTagGroupTaskStat;
import com.linkwechat.domain.taggroup.WePresTagGroupTaskTag;
import com.linkwechat.domain.taggroup.vo.WePresTagGroupTaskVo;
import com.linkwechat.domain.taggroup.vo.WePresTagTaskListVo;
import com.linkwechat.domain.task.query.WeTasksRequest;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.*;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WePresTagGroupTaskServiceImpl extends ServiceImpl<WePresTagGroupTaskMapper, WePresTagGroupTask> implements IWePresTagGroupTaskService {

    @Autowired
    WePresTagGroupTaskStatMapper taskStatMapper;

    @Autowired
    private IWePresTagGroupTaskStatService iWePresTagGroupTaskStatService;

    @Autowired
    WePresTagGroupTaskScopeMapper taskScopeMapper;

    @Autowired
    private IWePresTagGroupTaskScopeService iWePresTagGroupTaskScopeService;

    @Autowired
    private WePresTagGroupTaskTagMapper taskTagMapper;

    @Autowired
    private IWePresTagGroupTaskTagService iWePresTagGroupTaskTagService;

    @Autowired
    WeGroupCodeMapper groupCodeMapper;

    @Autowired
    IWeCorpAccountService corpAccountService;

    @Autowired
    IWeGroupMessageTemplateService iWeGroupMessageTemplateService;

    @Autowired
    private IWeMessagePushService weMessagePushService;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Autowired
    private IWeCustomerService iWeCustomerService;
    @Resource
    private IWeTasksService weTasksService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(WePresTagGroupTask task) {
        int rows = this.baseMapper.insert(task);
        if (rows > 0) {
            // 保存标签对象
            if (CollectionUtil.isNotEmpty(task.getTagList())) {
                List<WePresTagGroupTaskTag> taskTagList = task.getTagList().stream().map(id -> {
                    WePresTagGroupTaskTag taskTag = new WePresTagGroupTaskTag();
                    taskTag.setTaskId(task.getTaskId());
                    taskTag.setTagId(id);
                    taskTag.setCreateBy(task.getCreateBy());
                    taskTag.setUpdateBy(SecurityUtils.getUserName());
                    taskTag.setCreateById(SecurityUtils.getUserId());
                    taskTag.setUpdateById(SecurityUtils.getUserId());
                    taskTag.setUpdateTime(new Date());
                    taskTag.setCreateTime(new Date());
                    return taskTag;
                }).collect(Collectors.toList());
                taskTagMapper.batchBindsTaskTags(taskTagList);
            }

            // 保存员工信息
            if (CollectionUtil.isNotEmpty(task.getScopeList())) {
                List<WePresTagGroupTaskScope> wePresTagGroupTaskScopeList = task.getScopeList().stream().map(id -> {
                    WePresTagGroupTaskScope sc = new WePresTagGroupTaskScope();
                    sc.setTaskId(task.getTaskId());
                    sc.setWeUserId(id);
                    sc.setCreateBy(task.getCreateBy());
                    sc.setUpdateBy(SecurityUtils.getUserName());
                    sc.setCreateById(SecurityUtils.getUserId());
                    sc.setUpdateById(SecurityUtils.getUserId());
                    sc.setUpdateTime(new Date());
                    sc.setCreateTime(new Date());
                    return sc;
                }).collect(Collectors.toList());
                taskScopeMapper.batchBindsTaskScopes(wePresTagGroupTaskScopeList);
            }
        }
        this.sendMessage(task);

    }


    @Override
    public List<WePresTagTaskListVo> selectTaskList(String taskName, Integer sendType, String createBy,
                                                    String beginTime, String endTime) {
        List<WePresTagTaskListVo> list = baseMapper.selectListVO(taskName, sendType, createBy, beginTime, endTime);
        list.forEach(i -> {
            if (StringUtils.isNotEmpty(i.getTagListStr())) {
                i.setTagList(Arrays.asList(i.getTagListStr().split(",")));
            } else {
                i.setTagList(new ArrayList<>());
            }
        });
        return list;
    }

    @Override
    public WePresTagGroupTaskVo getTaskById(Long taskId) {
        return baseMapper.selectTaskById(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRemoveTaskByIds(Long[] idList) {
        List<Long> ids = Arrays.asList(idList);

        // 解除关联的标签
        LambdaUpdateWrapper<WePresTagGroupTaskTag> tagUpdateWrapper = new LambdaUpdateWrapper<>();
        tagUpdateWrapper.in(WePresTagGroupTaskTag::getTaskId, ids);
        tagUpdateWrapper.set(WePresTagGroupTaskTag::getDelFlag, 1);
        iWePresTagGroupTaskTagService.update(tagUpdateWrapper);

        // 解除关联的员工
        LambdaUpdateWrapper<WePresTagGroupTaskScope> scopeUpdateWrapper = new LambdaUpdateWrapper<>();
        scopeUpdateWrapper.in(WePresTagGroupTaskScope::getTaskId, ids);
        scopeUpdateWrapper.set(WePresTagGroupTaskScope::getDelFlag, 1);
        iWePresTagGroupTaskScopeService.update(scopeUpdateWrapper);

        // 删除其用户统计
        LambdaUpdateWrapper<WePresTagGroupTaskStat> statUpdateWrapper = new LambdaUpdateWrapper<>();
        statUpdateWrapper.in(WePresTagGroupTaskStat::getTaskId, ids);
        statUpdateWrapper.set(WePresTagGroupTaskStat::getDelFlag, 1);
        iWePresTagGroupTaskStatService.update(statUpdateWrapper);

        // 最后删除task
        LambdaUpdateWrapper<WePresTagGroupTask> taskUpdateWrapper = new LambdaUpdateWrapper<>();
        taskUpdateWrapper.in(WePresTagGroupTask::getTaskId, ids);
        taskUpdateWrapper.set(WePresTagGroupTask::getDelFlag, 1);
        return update(taskUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTask(WePresTagGroupTask task) {

        if (isNameOccupied(task)) {
            throw new CustomException("任务名已存在");
        }
        int rows = baseMapper.updateById(task);
        if (rows > 0) {
            // 更新标签 - 先删除旧标签
            LambdaUpdateWrapper<WePresTagGroupTaskTag> taskTagQueryWrapper = new LambdaUpdateWrapper<>();
            taskTagQueryWrapper.eq(WePresTagGroupTaskTag::getTaskId, task.getTaskId())
                    .set(WePresTagGroupTaskTag::getDelFlag, 1);
            taskTagMapper.update(null, taskTagQueryWrapper);
            // 更新标签 - 再添加新标签
            List<String> tagIdList = task.getTagList();
            if (CollectionUtil.isNotEmpty(tagIdList)) {
                List<WePresTagGroupTaskTag> wePresTagGroupTaskTagList = tagIdList.stream().map(id -> {
                    WePresTagGroupTaskTag taskTag = new WePresTagGroupTaskTag();
                    taskTag.setTaskId(task.getTaskId());
                    taskTag.setTagId(id);
                    taskTag.setCreateBy(task.getCreateBy());
                    taskTag.setCreateTime(new Date());
                    return taskTag;
                }).collect(Collectors.toList());
                taskTagMapper.batchBindsTaskTags(wePresTagGroupTaskTagList);
            }

            // 先解除旧的员工绑定信息
            LambdaUpdateWrapper<WePresTagGroupTaskScope> scopeQueryWrapper = new LambdaUpdateWrapper<>();
            scopeQueryWrapper.eq(WePresTagGroupTaskScope::getTaskId, task.getTaskId())
                    .set(WePresTagGroupTaskScope::getDelFlag, 1);
            taskScopeMapper.update(null, scopeQueryWrapper);

            // 再重新绑定员工信息
            List<String> userIdList = task.getScopeList();
            if (CollectionUtil.isNotEmpty(userIdList)) {
                List<WePresTagGroupTaskScope> wePresTagGroupTaskScopeList = userIdList.stream().map(id -> {
                    WePresTagGroupTaskScope sc = new WePresTagGroupTaskScope();
                    sc.setTaskId(task.getTaskId());
                    sc.setWeUserId(id);
                    sc.setCreateBy(task.getCreateBy());
                    return sc;
                }).collect(Collectors.toList());
                taskScopeMapper.batchBindsTaskScopes(wePresTagGroupTaskScopeList);
            }
        }
        return rows;
    }


    /**
     * 更新任务同时推送消息
     *
     * @param task
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskAndSendMsg(WePresTagGroupTask task) throws WeComException {
        try {
            this.updateTask(task);
            this.sendMessage(task);
        } catch (WeComException e) {
            throw e;
        }

    }

    @Override
    public List<WePresTagGroupTaskStat> getTaskStat(Long id, String customerName, Integer isInGroup, Integer isSent,
                                                    Integer sendType) {

        WePresTagGroupTaskStat query = new WePresTagGroupTaskStat();
        query.setTaskId(id);
        query.setCustomerName(customerName);
        query.setInGroup(isInGroup);
        query.setSent(isSent);

        // 企业群发统计
        if (sendType.equals(0)) {
            return taskStatMapper.cropSendResultList(query);
        }
        // 个人群发统计
        else {
            return taskStatMapper.singleSendResultList(query);
        }
    }

    @Override
    public List<WeCommunityTaskEmplVo> getScopeListByTaskId(Long taskId) {
        return taskScopeMapper.getScopeListByTaskId(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFollowerTaskStatus(Long taskId, String followerId) {
        // 更新所有对应客户的已送达为1
        LambdaUpdateWrapper<WePresTagGroupTaskStat> statLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        statLambdaUpdateWrapper.eq(WePresTagGroupTaskStat::getTaskId, taskId)
                .in(WePresTagGroupTaskStat::getUserId, followerId).set(WePresTagGroupTaskStat::getSent, 1);
        return taskStatMapper.update(null, statLambdaUpdateWrapper);
    }

    @Override
    public boolean isNameOccupied(WePresTagGroupTask task) {
        Long currentId = Optional.ofNullable(task.getTaskId()).orElse(-1L);
        LambdaQueryWrapper<WePresTagGroupTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WePresTagGroupTask::getDelFlag, 0).eq(WePresTagGroupTask::getTaskName, task.getTaskName());
        List<WePresTagGroupTask> queryRes = baseMapper.selectList(queryWrapper);
        return !queryRes.isEmpty() && !currentId.equals(queryRes.get(0).getTaskId());
    }

    @Override
    public void sendMessage(WePresTagGroupTask task) throws WeComException {
        Integer sendType = task.getSendType();
        try {
            // 企业群发逻辑
            if (sendType.equals(0)) {

                List<WeAddGroupMessageQuery.SenderInfo> senderInfoList = baseMapper.selectSenderInfo(task.getTaskId());
                if (StringUtils.isEmpty(senderInfoList)) {
                    throw new WeComException("找不到符合筛选条件的发送对象");
                }
                String codeUrl = groupCodeMapper.selectById(task.getGroupCodeId()).getCodeUrl();
//                WeMediaDto media = materialService.uploadTemporaryMaterial(codeUrl, MediaType.IMAGE.getMediaType(), "临时文件");
                List<WeMessageTemplate> attachmentList = Lists.newArrayList();
                WeMessageTemplate template = new WeMessageTemplate();
                template.setMsgType(MessageType.IMAGE.getMessageType());
//                template.setMediaId(media.getMedia_id());
                template.setPicUrl(codeUrl);
                attachmentList.add(template);

                List<WeAddGroupMessageQuery.SenderInfo> senderList = senderInfoList.stream().map(sender -> {
                    WeAddGroupMessageQuery.SenderInfo info = new WeAddGroupMessageQuery.SenderInfo();
                    info.setUserId(sender.getUserId());
                    info.setCustomerList(sender.getCustomerList());
                    return info;
                }).collect(Collectors.toList());
                WeAddGroupMessageQuery query = new WeAddGroupMessageQuery();
                query.setContent(task.getWelcomeMsg());
                query.setSenderList(senderList);
                query.setChatType(1);
                query.setIsTask(0);
                query.setAttachmentsList(attachmentList);

                iWeGroupMessageTemplateService.addGroupMsgTemplate(query);
                task.setMessageTemplateId(query.getId());
                baseMapper.updateById(task);
            }
            // 个人群发逻辑
            else {


                List<WeCustomer> externalIds = new ArrayList<>();
                if (task.getSendScope().equals(0)) { //全部客户
                    externalIds = iWeCustomerService.list();
                } else { //部分客户
                    externalIds = iWeCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                            .in(WeCustomer::getExternalUserid, task.getExternalUserIds()));
                }

                //查询当前员工下的
                List<String> followerIds = externalIds.stream().map(WeCustomer::getAddUserId).distinct().collect(Collectors.toList());

                if (StringUtils.isEmpty(followerIds)) {
                    throw new WeComException("消息无法推送，找不到符合筛选条件的员工");
                }

                // 删除旧统计对象
                LambdaUpdateWrapper<WePresTagGroupTaskStat> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(WePresTagGroupTaskStat::getTaskId, task.getTaskId())
                        .set(WePresTagGroupTaskStat::getDelFlag, 1);
                taskStatMapper.update(null, updateWrapper);

                // 保存新的统计对象
                List<WePresTagGroupTaskStat> statList = externalIds.stream().map(i -> {
                    WePresTagGroupTaskStat stat = new WePresTagGroupTaskStat();
                    stat.setTaskId(task.getTaskId());
                    stat.setSent(0);
                    stat.setUserId(i.getAddUserId());
                    stat.setExternalUserId(i.getExternalUserid());
                    stat.setCreateBy(task.getCreateBy());
                    return stat;
                }).collect(Collectors.toList());
                taskStatMapper.batchSave(statList);
                weMessagePushService.pushMessageSelfH5(followerIds, "【任务动态】<br/> 您有一项「标签建群」任务待完成，请尽快处理", MessageNoticeType.TAG.getType(), true);

                //待办任务
                WeTasksRequest request = WeTasksRequest.builder().weUserIds(followerIds).build();
                weTasksService.groupAddByLabel(request);
            }
        } catch (Exception e) {
            log.error("============> 老客标签建群任务发送失败, 任务明细: {}", task);
            log.error("错误信息: {}", e.getMessage());
            throw new WeComException(e.getMessage());
        }
    }

    @Override
    public List<String> selectExternalIds(Long taskId) {
        List<WeCustomer> customers = baseMapper.selectTaskExternalIds(taskId);
        return customers.stream().map(WeCustomer::getExternalUserid).collect(Collectors.toList());
    }

    @Override
    public List<String> selectFollowerIdByTaskId(Long taskId) {
        return baseMapper.selectTaskFollowerIds(taskId);
    }

    @Override
    public List<WePresTagGroupTaskVo> getFollowerTaskList(String emplId, Integer isDone) {

        return baseMapper.getTaskListByFollowerId(emplId, isDone);
    }

    private List<String> selectExternalIdsByFollowerId(Long taskId, String followerId) {
        return baseMapper.selectTaskExternalByFollowerId(taskId, followerId);
    }
}
