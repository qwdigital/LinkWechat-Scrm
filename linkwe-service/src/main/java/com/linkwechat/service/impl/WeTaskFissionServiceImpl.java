package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.TaskFissionType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.bean.BeanUtils;
import com.linkwechat.domain.*;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.customer.vo.WeCustomerDetailInfoVo;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import com.linkwechat.domain.material.ao.WePoster;
import com.linkwechat.domain.material.ao.WePosterSubassembly;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.taskfission.query.WeAddTaskFissionQuery;
import com.linkwechat.domain.taskfission.query.WeTaskFissionQuery;
import com.linkwechat.domain.taskfission.vo.*;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatAddJoinWayQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatJoinWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatAddJoinWayVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatGetJoinWayVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.mapper.WeTaskFissionMapper;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务宝表(WeTaskFission)
 *
 * @author danmo
 * @since 2022-06-28 13:48:53
 */
@Slf4j
@Service
public class WeTaskFissionServiceImpl extends ServiceImpl<WeTaskFissionMapper, WeTaskFission> implements IWeTaskFissionService {

    @Autowired
    private IWeTaskFissionStaffService weTaskFissionStaffService;


    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeGroupMessageTemplateService weGroupMessageTemplateService;

    @Autowired
    private IWeTaskFissionRecordService weTaskFissionRecordService;

    @Autowired
    private IWeTaskFissionRewardService weTaskFissionRewardService;

    @Autowired
    private IWeTaskFissionCompleteRecordService weTaskFissionCompleteRecordService;

    @Resource
    private IWeMaterialService materialService;

    @Autowired
    private IWeGroupMemberService weGroupMemberService;

    @Autowired
    private QwCustomerClient qwCustomerClient;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    /**
     * 查询任务宝列表
     *
     * @param weTaskFission 任务宝
     * @return 任务宝
     */
    @Override
    public List<WeTaskFission> selectWeTaskFissionList(WeTaskFission weTaskFission) {
        return this.baseMapper.selectWeTaskFissionList(weTaskFission);
    }

    /**
     * 查询任务宝
     *
     * @param id 任务宝ID
     * @return 任务宝
     */
    @Override
    public WeTaskFissionVo selectWeTaskFissionById(Long id) {
        return this.baseMapper.selectWeTaskFissionById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertWeTaskFission(WeAddTaskFissionQuery query) {
        WeTaskFission weTaskFission = new WeTaskFission();
        BeanUtils.copyPropertiesASM(query, weTaskFission);
        if (query.getStartTime() != null && query.getStartTime().getTime() <= System.currentTimeMillis()) {
            weTaskFission.setFissStatus(1);
        }
        if (save(weTaskFission)) {
            if (CollectionUtils.isNotEmpty(query.getTaskFissionStaffs())) {
                query.getTaskFissionStaffs().forEach(staff -> staff.setTaskFissionId(weTaskFission.getId()));
                weTaskFissionStaffService.saveBatch(query.getTaskFissionStaffs());
            }
            query.setId(weTaskFission.getId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateWeTaskFission(WeAddTaskFissionQuery query) {
        if (ObjectUtils.isEmpty(query.getId())) {
            throw new WeComException("数据id为空");
        }
        WeTaskFission fissionTask = getById(query.getId());
        if (ObjectUtils.isEmpty(fissionTask)) {
            throw new WeComException("数据不存在");
        }
        WeTaskFission weTaskFission = new WeTaskFission();
        BeanUtils.copyPropertiesASM(query, weTaskFission);
        if (updateById(weTaskFission)) {
            if (CollectionUtils.isNotEmpty(query.getTaskFissionStaffs())) {
                log.info("发起成员信息：【{}】", JSONObject.toJSONString(query.getTaskFissionStaffs()));
                weTaskFissionStaffService.delStaffByTaskId(weTaskFission.getId());
                query.getTaskFissionStaffs().forEach(staff -> {
                    staff.setTaskFissionId(weTaskFission.getId());
                });
                weTaskFissionStaffService.saveBatch(query.getTaskFissionStaffs());
            }
        }
    }

    @Override
    public void deleteWeTaskFissionByIds(Long[] ids) {
        List<Long> taskIds = Arrays.stream(ids).collect(Collectors.toList());
        WeTaskFission weTaskFission = new WeTaskFission();
        weTaskFission.setDelFlag(1);
        update(weTaskFission, new LambdaQueryWrapper<WeTaskFission>()
                .in(WeTaskFission::getId, taskIds));
        weTaskFissionStaffService.delStaffByTaskIds(taskIds);
    }

    @Async
    @Override
    public void sendWeTaskFission(WeAddTaskFissionQuery query) {
        //海报路径
        String postersPath = query.getPostersUrl();
        //目标员工id
        String fissStaffId = query.getFissionTargetId();
        //H5生成海报页面路径
        String pageUrlStr = StringUtils.format(linkWeChatConfig.getTaskFissionUrl(), query.getId());

        WeAddGroupMessageQuery messageQuery = new WeAddGroupMessageQuery();
        messageQuery.setBusinessId(query.getId());
        messageQuery.setChatType(1);
        messageQuery.setSource(1);
        messageQuery.setIsAll(false);
        messageQuery.setContent(query.getTaskName());
        WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
        weMessageTemplate.setMsgType(MessageType.LINK.getMessageType());
        weMessageTemplate.setMediaId(postersPath);
        weMessageTemplate.setDescription(query.getFissInfo());
        weMessageTemplate.setLinkUrl(pageUrlStr);
        weMessageTemplate.setTitle(query.getTaskName());
        weMessageTemplate.setPicUrl(postersPath);
        messageQuery.setAttachmentsList(ListUtil.toList(weMessageTemplate));

        if (query.getStartTime() != null && query.getStartTime().getTime() <= System.currentTimeMillis()) {
            messageQuery.setIsTask(0);
        } else {
            messageQuery.setIsTask(1);
            messageQuery.setSendTime(query.getStartTime());
        }
        try {
            List<WeTaskFissionStaff> weTaskFissionStaffList = query.getTaskFissionStaffs();
            //获取部门id
            String departmentIds = Optional.ofNullable(weTaskFissionStaffList).orElseGet(ArrayList::new).stream().filter(weTaskFissionStaff ->
                            WeConstans.USE_SCOP_BUSINESSID_TYPE_ORG.equals(weTaskFissionStaff.getStaffType()))
                    .map(WeTaskFissionStaff::getStaffId).collect(Collectors.joining(","));
            //获取成员id
            String userIds = Optional.ofNullable(weTaskFissionStaffList).orElseGet(ArrayList::new).stream().filter(weTaskFissionStaff ->
                            WeConstans.USE_SCOP_BUSINESSID_TYPE_USER.equals(weTaskFissionStaff.getStaffType()))
                    .map(WeTaskFissionStaff::getStaffId).collect(Collectors.joining(","));
            List<WeCustomer> weCustomerList = weCustomerService.getCustomerListByCondition(WeCustomersQuery.builder()
                    .deptIds(departmentIds)
                    .userIds(userIds)
                    .tagIds(query.getCustomerTagId().replaceAll("all", ""))
                    .build());
            if (weCustomerList != null) {
                Map<String, Set<String>> userAndCustomerMap = weCustomerList.stream().collect(Collectors.groupingBy(WeCustomer::getAddUserId, Collectors.mapping(WeCustomer::getExternalUserid, Collectors.toSet())));
                List<WeAddGroupMessageQuery.SenderInfo> senderList = new ArrayList<>();
                userAndCustomerMap.forEach((userId, customerIds) -> {
                    WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
                    senderInfo.setUserId(userId);
                    senderInfo.setCustomerList(new ArrayList<>(customerIds));
                    senderList.add(senderInfo);
                });
                messageQuery.setSenderList(senderList);
            }
            weGroupMessageTemplateService.addGroupMsgTemplate(messageQuery);
        } catch (Exception e) {
            WeTaskFission weTaskFission = new WeTaskFission();
            weTaskFission.setId(query.getId());
            weTaskFission.setFissStatus(-1);
            weTaskFission.setRemark(e.getMessage());
            updateById(weTaskFission);
            throw new WeComException(e.getMessage());
        }
    }

    @Override
    public String fissionPosterGenerate(WeTaskFissionQuery query) {
        WeTaskFission weTaskFission = getById(query.getTaskFissionId());
        //任务表添加当前客户任务
        WeTaskFissionRecord record = weTaskFissionRecordService.getTaskFissionRecord(query.getTaskFissionId(), SecurityUtils.getWxLoginUser().getUnionId(), SecurityUtils.getWxLoginUser().getNickName());
        String posterUrl = record.getPoster();
        if (StringUtils.isBlank(posterUrl)) {
            JSONObject qrcode = getPosterQRCode(weTaskFission, record);
            if (qrcode == null || qrcode.get("qrCode") == null) {
                throw new WeComException("生成的二维码为空");
            }
            WeMaterial material = materialService.getById(weTaskFission.getPostersId());
            if (StringUtils.isNotEmpty(material.getPosterSubassembly())) {
                List<WePosterSubassembly> wePosterSubassemblies = JSONArray.parseArray(material.getPosterSubassembly(), WePosterSubassembly.class);
                wePosterSubassemblies.stream().filter(Objects::nonNull)
                        .filter(wePosterSubassembly -> wePosterSubassembly.getType() == 3).forEach(wePosterSubassembly -> {
                            wePosterSubassembly.setImgPath(qrcode.getString("qrCode"));
                        });
                WePoster wePoster = BeanUtil.copyProperties(material, WePoster.class);
                wePoster.setTitle(material.getMaterialName());
                wePoster.setSampleImgPath(material.getMaterialUrl());
                wePoster.setBackgroundImgPath(material.getBackgroundImgUrl());
                wePoster.setPosterSubassemblyList(wePosterSubassemblies);
                WeMaterial weMaterial = materialService.generateSimpleImg(wePoster);
                posterUrl = weMaterial.getMaterialUrl();
            } else {
                throw new WeComException("生成海报错误，无二维码位置");
            }
            if (StringUtils.isBlank(posterUrl)) {
                throw new WeComException("生成的海报为空");
            }
            record.setQrCode(qrcode.getString("qrCode"));
            record.setConfigId(qrcode.getString("configId"));
            record.setPoster(posterUrl);
            weTaskFissionRecordService.updateWeTaskFissionRecord(record);
        }
        return posterUrl;
    }

    @Override
    public WeTaskFissionProgressVo getTaskProgress(WeTaskFissionQuery query) {
        WeTaskFissionVo weTaskFissionVo = selectWeTaskFissionById(query.getTaskFissionId());

        WeTaskFissionProgressVo weTaskFissionProgressVo = weTaskFissionRecordService
                .getTaskProgress(query.getTaskFissionId(), SecurityUtils.getWxLoginUser().getUnionId());
        if (weTaskFissionProgressVo == null) {
            throw new WeComException("任务信息不存在");
        }
        Date completeTime = weTaskFissionProgressVo.getCompleteTime();
        if (completeTime == null) {
            weTaskFissionProgressVo.setRewardUrl(null);
            weTaskFissionProgressVo.setRewardImageUrl(null);
        }

        Long recordId = weTaskFissionProgressVo.getRecordId();
        List<WeTaskFissionCompleteRecord> customerList = weTaskFissionCompleteRecordService.list(new LambdaQueryWrapper<WeTaskFissionCompleteRecord>()
                .eq(WeTaskFissionCompleteRecord::getFissionRecordId, recordId)
                .eq(WeTaskFissionCompleteRecord::getTaskFissionId,query.getTaskFissionId())
                .eq(WeTaskFissionCompleteRecord::getDelFlag,0));
        if (CollectionUtils.isNotEmpty(customerList)) {
            List<WeTaskFissionCustomerVo> fissionCustomerList = customerList.stream().map(customerInfo -> {
                WeTaskFissionCustomerVo fissionCustomerVo = new WeTaskFissionCustomerVo();
                fissionCustomerVo.setCustomerName(customerInfo.getCustomerName());
                fissionCustomerVo.setAvatar(customerInfo.getCustomerAvatar());
                fissionCustomerVo.setAddTime(customerInfo.getCreateTime());
                return fissionCustomerVo;
            }).collect(Collectors.toList());
            weTaskFissionProgressVo.setCustomerList(fissionCustomerList);
        }

        return weTaskFissionProgressVo;

    }

    @Override
    public WeTaskFissionStatisticVo taskFissionStatistic(Long taskFissionId, Date startTime, Date endTime) {
        WeTaskFissionStatisticVo vo = new WeTaskFissionStatisticVo();
        WeTaskFission taskFission = getById(taskFissionId);
        if (taskFission == null) {
            throw new WeComException("任务数据不存在");
        }
        vo.setTaskFissionId(taskFissionId);
        vo.setTaskName(taskFission.getTaskName());
        vo.setStartTime(startTime);
        vo.setEndTime(endTime);

        List<WeTaskFissionRecord> fissionRecordList = weTaskFissionRecordService.statisticRecords(taskFissionId, startTime, endTime);
        if (CollectionUtil.isEmpty(fissionRecordList)) {
            fissionRecordList = new ArrayList<>();
        }
        List<String> stateList = fissionRecordList.stream().map(item -> {
            return WeConstans.FISSION_PREFIX + item.getId();
        }).collect(Collectors.toList());

        Map<String, Long> customerMap = new HashMap<>();
        if(TaskFissionType.GROUP_FISSION.getCode().equals(taskFission.getFissionType())){
            List<WeCustomer> customerList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                    .in(CollectionUtil.isNotEmpty(stateList),WeCustomer::getState, stateList)
                    .apply("DATE_FORMAT(add_time,'%Y-%m-%d') >= '" + DateUtil.formatDate(startTime) + "'")
                    .apply("DATE_FORMAT(add_time,'%Y-%m-%d') <= '" + DateUtil.formatDate(endTime) + "'")
                    .eq(WeCustomer::getDelFlag, 0));
            Map<String, Long> customerMap1 = Optional.ofNullable(customerList).orElseGet(ArrayList::new).parallelStream().filter(Objects::nonNull).collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getAddTime()), Collectors.counting()));
            customerMap.putAll(customerMap1);
        }if(TaskFissionType.GROUP_FISSION.getCode().equals(taskFission.getFissionType())){
            List<WeGroupMember> weGroupMemberList = weGroupMemberService.list(new LambdaQueryWrapper<WeGroupMember>()
                    .in(CollectionUtil.isNotEmpty(stateList),WeGroupMember::getState, stateList)
                    .apply("DATE_FORMAT(join_time,'%Y-%m-%d') >= '" + DateUtil.formatDate(startTime) + "'")
                    .apply("DATE_FORMAT(join_time,'%Y-%m-%d') <= '" + DateUtil.formatDate(endTime) + "'")
                    .eq(WeGroupMember::getDelFlag, 0));
            Map<String, Long> customerMap2 = Optional.ofNullable(weGroupMemberList).orElseGet(ArrayList::new).parallelStream().filter(Objects::nonNull).collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getJoinTime()), Collectors.counting()));
            customerMap.putAll(customerMap2);
        }

        Map<String, List<WeTaskFissionRecord>> recordsMap = fissionRecordList.parallelStream().filter(Objects::nonNull).collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getCreateTime())));
        List<WeTaskFissionDailyDataVo> dailyDataList = new LinkedList<>();
        DateUtils.findDates(startTime, endTime).stream().map(d -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, d))
                .forEach(date -> {
                    WeTaskFissionDailyDataVo v = new WeTaskFissionDailyDataVo();
                    v.setDay(date);
                    Long increase = customerMap.get(date);
                    v.setIncrease(increase != null ? increase.intValue() : 0);
                    List<WeTaskFissionRecord> recordsList = recordsMap.get(date);
                    if (CollectionUtils.isNotEmpty(recordsList)) {
                        v.setAttend(recordsList.size());
                        int completeSize = (int) recordsList.stream().filter(r -> r.getCompleteTime() != null).count();
                        v.setComplete(completeSize);
                    }
                    dailyDataList.add(v);
                });
        vo.setData(dailyDataList);
        return vo;
    }

    @Async
    @Override
    public void addCustomerHandler(String externalUserId, String userId, String fissionRecordId) {
        try {
            log.info("裂变任务处理  >>>>>>>>>>start");
            WeTaskFissionRecord weTaskFissionRecord = weTaskFissionRecordService.getById(Long.valueOf(fissionRecordId));
            if (weTaskFissionRecord != null) {
                //查询完成记录中是否存在
                WeTaskFissionCompleteRecord completeRecord = weTaskFissionCompleteRecordService.getOne(new LambdaQueryWrapper<WeTaskFissionCompleteRecord>()
                        .eq(WeTaskFissionCompleteRecord::getTaskFissionId, weTaskFissionRecord.getTaskFissionId())
                        .eq(WeTaskFissionCompleteRecord::getFissionRecordId, fissionRecordId)
                        .eq(WeTaskFissionCompleteRecord::getCustomerId, externalUserId)
                        .eq(WeTaskFissionCompleteRecord::getDelFlag, 0));
                if(completeRecord != null){
                    return;
                }else {
                    //查询客户数据
                    WeCustomerDetailInfoVo weCustomerDetail = weCustomerService.findWeCustomerDetail(externalUserId, userId, 0);
                    if(weCustomerDetail == null){
                        return;
                    }
                    WeTaskFissionCompleteRecord addCompleteRecord = new WeTaskFissionCompleteRecord();
                    addCompleteRecord.setTaskFissionId(weTaskFissionRecord.getTaskFissionId());
                    addCompleteRecord.setFissionRecordId(weTaskFissionRecord.getId());
                    addCompleteRecord.setCustomerId(externalUserId);
                    addCompleteRecord.setCustomerName(weCustomerDetail.getCustomerName());
                    addCompleteRecord.setCustomerAvatar(weCustomerDetail.getAvatar());
                    weTaskFissionCompleteRecordService.save(addCompleteRecord);
                }
                //查询裂变任务详情
                WeTaskFission weTaskFission = selectWeTaskFissionById(weTaskFissionRecord.getTaskFissionId());
                Integer fissNum = weTaskFissionRecord.getFissNum();
                fissNum = fissNum + 1;
                weTaskFissionRecord.setFissNum(fissNum);
                log.info("查询裂变任务详情  >>>>>>>>>>{}", JSONObject.toJSONString(weTaskFission));

                //裂变数量完成任务处理,消费兑换码
                if (fissNum >= weTaskFission.getFissNum()) {
                    log.info("裂变数量完成任务处理,消费兑换码  >>>>>>>>>>{}", fissNum);
                    if (weTaskFissionRecord.getCompleteTime() == null) {
                        weTaskFissionRecord.setCompleteTime(new Date());
                    }
                    WeTaskFissionReward reward = new WeTaskFissionReward();
                    reward.setTaskFissionId(weTaskFissionRecord.getTaskFissionId());
                    reward.setRewardCodeStatus(0);
                    List<WeTaskFissionReward> weTaskFissionRewardList = weTaskFissionRewardService.getRewardList(reward);
                    if (CollectionUtils.isNotEmpty(weTaskFissionRewardList)) {
                        WeTaskFissionReward fissionReward = weTaskFissionRewardList.get(0);
                        fissionReward.setRewardUser(weTaskFissionRecord.getCustomerName());
                        fissionReward.setRewardUserId(weTaskFissionRecord.getCustomerId());
                        weTaskFissionRewardService.saveOrUpdate(fissionReward);
                    }
                }
                weTaskFissionRecordService.updateWeTaskFissionRecord(weTaskFissionRecord);
                log.info("裂变任务处理  >>>>>>>>>>end");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("裂变任务添加客户处理异常 fissionRecordId：{}", fissionRecordId, e);
        }
    }

    @Async
    @Override
    public void groupFissionEnterCheck(String chatId, Integer joinScene, Long createTime, Integer memChangeCnt) {
        log.info("群裂变客户入群校验>>>>>>>> chatId:{},joinScene:{}, createTime:{},memChangeCnt:{}",chatId,joinScene,createTime,memChangeCnt);
        if(joinScene != 3){
            return;
        }
        List<WeGroupMember> groupMemberList = weGroupMemberService.list(new LambdaQueryWrapper<WeGroupMember>()
                .eq(WeGroupMember::getChatId,chatId)
                .eq(WeGroupMember::getJoinScene,joinScene)
                .le(WeGroupMember::getJoinTime,DateUtil.formatDateTime(new Date(createTime * 1000)))
                .orderByDesc(WeGroupMember::getJoinTime));
        if(CollectionUtil.isNotEmpty(groupMemberList)){
            for (WeGroupMember weGroupMember : groupMemberList) {
                String state = weGroupMember.getState();
                if(StringUtils.isNotEmpty(state) && state.startsWith(WeConstans.FISSION_PREFIX)){
                    String fissionRecordId = state.substring(WeConstans.FISSION_PREFIX.length());
                    WeTaskFissionRecord weTaskFissionRecord = weTaskFissionRecordService.getById(Long.valueOf(fissionRecordId));
                    if (weTaskFissionRecord != null) {

                        //查询完成记录中是否存在
                        WeTaskFissionCompleteRecord completeRecord = weTaskFissionCompleteRecordService.getOne(new LambdaQueryWrapper<WeTaskFissionCompleteRecord>()
                                .eq(WeTaskFissionCompleteRecord::getTaskFissionId, weTaskFissionRecord.getTaskFissionId())
                                .eq(WeTaskFissionCompleteRecord::getFissionRecordId, fissionRecordId)
                                .eq(WeTaskFissionCompleteRecord::getCustomerId, weGroupMember.getUserId())
                                .eq(WeTaskFissionCompleteRecord::getDelFlag, 0));
                        if(completeRecord != null){
                            return;
                        }else {
                            WeTaskFissionCompleteRecord addCompleteRecord = new WeTaskFissionCompleteRecord();
                            addCompleteRecord.setTaskFissionId(weTaskFissionRecord.getTaskFissionId());
                            addCompleteRecord.setFissionRecordId(weTaskFissionRecord.getId());
                            addCompleteRecord.setCustomerId(weGroupMember.getUserId());
                            addCompleteRecord.setCustomerName(weGroupMember.getName());
                            weTaskFissionCompleteRecordService.save(addCompleteRecord);
                        }
                        //查询裂变任务详情
                        WeTaskFission weTaskFission = selectWeTaskFissionById(weTaskFissionRecord.getTaskFissionId());
                        Integer fissNum = weTaskFissionRecord.getFissNum();
                        fissNum = fissNum + 1;
                        weTaskFissionRecord.setFissNum(fissNum);
                        log.info("查询群裂变任务详情  >>>>>>>>>>{}", JSONObject.toJSONString(weTaskFission));

                        //裂变数量完成任务处理,消费兑换码
                        if (fissNum >= weTaskFission.getFissNum()) {
                            log.info("群裂变数量完成任务处理,消费兑换码  >>>>>>>>>>{}", fissNum);
                            if (weTaskFissionRecord.getCompleteTime() == null) {
                                weTaskFissionRecord.setCompleteTime(new Date());
                            }
                            WeTaskFissionReward reward = new WeTaskFissionReward();
                            reward.setTaskFissionId(weTaskFissionRecord.getTaskFissionId());
                            reward.setRewardCodeStatus(0);
                            List<WeTaskFissionReward> weTaskFissionRewardList = weTaskFissionRewardService.getRewardList(reward);
                            if (CollectionUtils.isNotEmpty(weTaskFissionRewardList)) {
                                WeTaskFissionReward fissionReward = weTaskFissionRewardList.get(0);
                                fissionReward.setRewardUser(weTaskFissionRecord.getCustomerName());
                                fissionReward.setRewardUserId(weTaskFissionRecord.getCustomerId());
                                weTaskFissionRewardService.saveOrUpdate(fissionReward);
                            }
                        }
                        weTaskFissionRecordService.updateWeTaskFissionRecord(weTaskFissionRecord);
                    }
                }
            }
        }
        log.info("群裂变任务处理  >>>>>>>>>>end");
    }



    /*************************************** private functions **************************************/

    private JSONObject getPosterQRCode(WeTaskFission taskFission, WeTaskFissionRecord record) {
        JSONObject qrCode = new JSONObject();
        if (StringUtils.isBlank(record.getQrCode())) {
            Long taskFissionId = record.getTaskFissionId();
            Integer taskFissionType = taskFission.getFissionType();
            if (TaskFissionType.USER_FISSION.getCode().equals(taskFissionType)) {
                qrCode = getUserFissionQrcode(taskFission.getFissionTargetId(), record);
            } else if (TaskFissionType.GROUP_FISSION.getCode().equals(taskFissionType)) {
                qrCode = getGroupFissionQrcode(taskFission, record);
            } else {
                throw new WeComException("错误的任务类型");
            }
        }else {
            qrCode.put("qrCode",record.getQrCode());
            qrCode.put("configId",record.getConfigId());
        }
        return qrCode;
    }



    private JSONObject getUserFissionQrcode(String fissionTargetId, WeTaskFissionRecord record) {
        //获取二维码
        JSONObject qrcode = new JSONObject();
        WeAddWayQuery contactWay = new WeAddWayQuery(1,2,WeConstans.FISSION_PREFIX + record.getId(),String.valueOf(record.getTaskFissionId()));
        contactWay.setUser(Lists.newArrayList(fissionTargetId));
        WeAddWayVo data = qwCustomerClient.addContactWay(contactWay).getData();
        if (data != null && StringUtils.isNotEmpty(data.getQrCode())) {
            qrcode.put("qrCode",data.getQrCode());
            qrcode.put("configId",data.getConfigId());
           return qrcode;
        }
        return qrcode;
    }

    private JSONObject getGroupFissionQrcode(WeTaskFission weTaskFission, WeTaskFissionRecord record) {
        JSONObject qrcode = new JSONObject();
        //配置进群方式
        WeGroupChatAddJoinWayVo data = qwCustomerClient.addJoinWayForGroupChat(
                WeGroupChatAddJoinWayQuery.builder()
                        .scene(2)
                        .room_base_name(weTaskFission.getTaskName())
                        .room_base_id(1)
                        .chat_id_list(Collections.singletonList(weTaskFission.getFissionTargetId()))
                        .state(WeConstans.FISSION_PREFIX + record.getId())
                        .build()
        ).getData();
        if (data != null && StringUtils.isNotEmpty(data.getConfig_id())) {
            //获取进群二维码
            WeGroupChatGetJoinWayVo joinWayVo = qwCustomerClient.getJoinWayForGroupChat(WeGroupChatJoinWayQuery.builder()
                    .config_id(data.getConfig_id())
                    .build()).getData();
            if(null != joinWayVo && null != joinWayVo.getJoin_way()
                    && StringUtils.isNotEmpty(joinWayVo.getJoin_way().getQr_code())){
                qrcode.put("configId",data.getConfig_id());
                qrcode.put("qrCode",joinWayVo.getJoin_way().getQr_code());
            }
        }
        return qrcode;
    }


    /**
     * 更新过期任务
     *
     * @return
     */
    @Override
    public void updateExpiredWeTaskFission() {
        this.baseMapper.updateExpiredWeTaskFission();
    }
}
