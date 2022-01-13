package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.enums.TaskFissionType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.QREncode;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtil;
import com.linkwechat.common.utils.img.NetFileUtils;
import com.linkwechat.wecom.client.WeExternalContactClient;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeChatUserDTO;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.domain.dto.WeTaskFissionPosterDTO;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import com.linkwechat.wecom.domain.dto.message.LinkMessageDto;
import com.linkwechat.wecom.domain.query.WeAddGroupMessageQuery;
import com.linkwechat.wecom.domain.vo.WeTaskFissionDailyDataVO;
import com.linkwechat.wecom.domain.vo.WeTaskFissionProgressVO;
import com.linkwechat.wecom.domain.vo.WeTaskFissionStatisticVO;
import com.linkwechat.wecom.mapper.WeTaskFissionMapper;
import com.linkwechat.wecom.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务宝Service业务层处理
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Slf4j
@Service
public class WeTaskFissionServiceImpl extends ServiceImpl<WeTaskFissionMapper, WeTaskFission> implements IWeTaskFissionService {
    @Autowired
    private WeTaskFissionMapper weTaskFissionMapper;
    @Autowired
    private IWeTaskFissionStaffService weTaskFissionStaffService;
    @Autowired
    private IWeCustomerMessagePushService weCustomerMessagePushService;
    @Autowired
    private IWeTaskFissionRecordService weTaskFissionRecordService;
    @Autowired
    private WeExternalContactClient weExternalContactClient;
    @Autowired
    private IWePosterService wePosterService;
    @Autowired
    private IWeGroupCodeService weGroupCodeService;
    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeTaskFissionCompleteRecordService weTaskFissionCompleteRecordService;

    @Autowired
    private IWeGroupMessageTemplateService weGroupMessageTemplateService;

    @Value("${H5.fissionUrl}")
    private String pageUrl;
    @Value("${H5.fissionGroupUrl}")
    private String pageGroupUrl;

    /**
     * 查询任务宝
     *
     * @param id 任务宝ID
     * @return 任务宝
     */
    @Override
    public WeTaskFission selectWeTaskFissionById(Long id) {
        WeTaskFission taskFission = weTaskFissionMapper.selectWeTaskFissionById(id);
        List<WeTaskFissionStaff> staffList = weTaskFissionStaffService.selectWeTaskFissionStaffByTaskId(id);
        taskFission.setTaskFissionStaffs(staffList);
        return taskFission;
    }

    /**
     * 查询任务宝列表
     *
     * @param weTaskFission 任务宝
     * @return 任务宝
     */
    @Override
    public List<WeTaskFission> selectWeTaskFissionList(WeTaskFission weTaskFission){
        return weTaskFissionMapper.selectWeTaskFissionList(weTaskFission);
    }

    /**
     * 新增任务宝
     *
     * @param weTaskFission 任务宝
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,WeComException.class})
    public void insertWeTaskFission(WeTaskFission weTaskFission) {
        weTaskFission.setFissStatus(0);
        groupQrcodeHandler(weTaskFission);
        int insertResult = weTaskFissionMapper.insertWeTaskFission(weTaskFission);
        if (insertResult > 0) {
            if (CollectionUtils.isNotEmpty(weTaskFission.getTaskFissionStaffs())) {
                if (weTaskFission.getId() != null) {
                    weTaskFission.getTaskFissionStaffs().forEach(staff -> {
                        staff.setTaskFissionId(weTaskFission.getId());
                    });
                    weTaskFissionStaffService.insertWeTaskFissionStaffList(weTaskFission.getTaskFissionStaffs());
                }
            }
            try {
                sendWeTaskFission(weTaskFission);
            } catch (Exception e) {
                throw new WeComException(e.getMessage());
            }
        }
    }

    /**
     * 修改任务宝
     *
     * @param weTaskFission 任务宝
     * @return 结果
     */
    @Override
    @Transactional
    public Long updateWeTaskFission(WeTaskFission weTaskFission) {
        weTaskFission.setUpdateTime(DateUtils.getNowDate());
        weTaskFission.setUpdateBy(SecurityUtils.getUsername());
        groupQrcodeHandler(weTaskFission);
        int updateResult = weTaskFissionMapper.updateWeTaskFission(weTaskFission);
        if (updateResult > 0) {
            if (CollectionUtils.isNotEmpty(weTaskFission.getTaskFissionStaffs())) {
                log.info("发起成员信息：【{}】", JSONObject.toJSONString(weTaskFission.getTaskFissionStaffs()));
                List<WeTaskFissionStaff> staffList = weTaskFissionStaffService.selectWeTaskFissionStaffByTaskId(weTaskFission.getId());
                if (CollectionUtils.isNotEmpty(staffList)) {
                    weTaskFissionStaffService.deleteWeTaskFissionStaffByIds(staffList.stream().map(WeTaskFissionStaff::getId).toArray(Long[]::new));
                }
                weTaskFission.getTaskFissionStaffs().forEach(staff -> {
                    staff.setTaskFissionId(weTaskFission.getId());
                });
                weTaskFissionStaffService.insertWeTaskFissionStaffList(weTaskFission.getTaskFissionStaffs());
            }
        }
        return weTaskFission.getId();
    }

    /**
     * 批量删除任务宝
     *
     * @param ids 需要删除的任务宝ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionByIds(Long[] ids) {
        return weTaskFissionMapper.deleteWeTaskFissionByIds(ids);
    }

    /**
     * 删除任务宝信息
     *
     * @param id 任务宝ID
     * @return 结果
     */
    @Override
    public int deleteWeTaskFissionById(Long id) {
        return weTaskFissionMapper.deleteWeTaskFissionById(id);
    }


    @Override
    public void sendWeTaskFission(WeTaskFission weTaskFission) throws Exception {
        //海报路径
        String postersPath = weTaskFission.getPostersUrl();
        //目标员工id
        String fissStaffId = weTaskFission.getFissionTargetId();
        //H5生成海报页面路径
        String pageUrlStr = StringUtils.format(pageUrl,weTaskFission.getId(),fissStaffId,weTaskFission.getPostersId());

        WeAddGroupMessageQuery messageQuery = new WeAddGroupMessageQuery();
        messageQuery.setBusinessId(weTaskFission.getId());
        messageQuery.setChatType(1);
        messageQuery.setSource(1);
        messageQuery.setIsAll(false);
        messageQuery.setContent(weTaskFission.getTaskName());
        WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
        weMessageTemplate.setMsgType(MessageType.LINK.getMessageType());
        weMessageTemplate.setMediaId(postersPath);
        weMessageTemplate.setDescription(weTaskFission.getFissInfo());
        weMessageTemplate.setLinkUrl(pageUrlStr);
        weMessageTemplate.setTitle(weTaskFission.getTaskName());
        weMessageTemplate.setPicUrl(postersPath);
        messageQuery.setAttachmentsList(ListUtil.toList(weMessageTemplate));

        if (weTaskFission.getStartTime() != null && weTaskFission.getStartTime().getTime() <= System.currentTimeMillis()) {
            messageQuery.setIsTask(0);
        }else {
            messageQuery.setIsTask(1);
            messageQuery.setSendTime(weTaskFission.getStartTime());
        }
        List<WeTaskFissionStaff> weTaskFissionStaffList = weTaskFission.getTaskFissionStaffs();
        //获取部门id
        String departmentIds = Optional.ofNullable(weTaskFissionStaffList).orElseGet(ArrayList::new).stream().filter(weTaskFissionStaff ->
                WeConstans.USE_SCOP_BUSINESSID_TYPE_ORG.equals(weTaskFissionStaff.getStaffType()))
                .map(WeTaskFissionStaff::getStaffId).collect(Collectors.joining(","));
        //获取成员id
        String userIds = Optional.ofNullable(weTaskFissionStaffList).orElseGet(ArrayList::new).stream().filter(weTaskFissionStaff ->
                WeConstans.USE_SCOP_BUSINESSID_TYPE_USER.equals(weTaskFissionStaff.getStaffType()))
                .map(WeTaskFissionStaff::getStaffId).collect(Collectors.joining(","));
        List<WeCustomerList> weCustomerList = weCustomerService.findWeCustomerList(WeCustomerList.builder()
                .userIds(userIds)
                .tagIds(weTaskFission.getCustomerTagId().replaceAll("all",""))
                .departmentIds(departmentIds)
                .build(),null);
        if(weCustomerList != null){
            Map<String, Set<String>> userAndCustomerMap = weCustomerList.stream().collect(Collectors.groupingBy(WeCustomerList::getFirstUserId, Collectors.mapping(WeCustomerList::getExternalUserid, Collectors.toSet())));
            List<WeAddGroupMessageQuery.SenderInfo> senderList = new ArrayList<>();
            userAndCustomerMap.forEach((userId,customerIds) ->{
                WeAddGroupMessageQuery.SenderInfo senderInfo = new WeAddGroupMessageQuery.SenderInfo();
                senderInfo.setUserId(userId);
                if (!ObjectUtil.equal("all",weTaskFission.getCustomerTagId())) {
                    senderInfo.setCustomerList(new ArrayList<>(customerIds));
                }
                senderList.add(senderInfo);
            });
            messageQuery.setSenderList(senderList);
        }
        weGroupMessageTemplateService.addGroupMsgTemplate(messageQuery);
    }

    @Override
    @Transactional
    public String fissionPosterGenerate(WeTaskFissionPosterDTO weTaskFissionPosterDTO) {
        WeCustomer weCustomer = weCustomerService.getOne(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getUnionid, weTaskFissionPosterDTO.getUnionId()).last("limit 1"));
        if (weCustomer != null) {
            //任务表添加当前客户任务
            WeTaskFissionRecord record = getTaskFissionRecordId(weTaskFissionPosterDTO.getTaskFissionId(), weCustomer.getUnionid(), weCustomer.getCustomerName());
            String posterUrl = record.getPoster();
            if (StringUtils.isBlank(posterUrl)) {
                String qrcode = getPosterQRCode(weTaskFissionPosterDTO.getFissionTargetId(), record, weCustomer);
                if (StringUtils.isBlank(qrcode)) {
                    throw new WeComException("生成的二维码为空");
                }
                WePoster poster = wePosterService.selectOne(weTaskFissionPosterDTO.getPosterId());
                poster.getPosterSubassemblyList().stream().filter(Objects::nonNull)
                        .filter(wePosterSubassembly -> wePosterSubassembly.getType() == 3).forEach(wePosterSubassembly -> {
                    wePosterSubassembly.setImgPath(qrcode);
                });
                posterUrl = wePosterService.generateSimpleImg(poster);
                if (StringUtils.isBlank(posterUrl)) {
                    throw new WeComException("生成的海报为空");
                }
                record.setQrCode(qrcode);
                record.setPoster(posterUrl);
                weTaskFissionRecordService.updateWeTaskFissionRecord(record);
            }
            return posterUrl;
        } else {
            throw new WeComException("客户信息不存在");
        }
    }

    @Override
    public void completeFissionRecord(Long taskFissionId, Long taskFissionRecordId, WeChatUserDTO weChatUserDTO) {
        WeTaskFissionCompleteRecord wfcr = new WeTaskFissionCompleteRecord();
        wfcr.setTaskFissionId(taskFissionId);
        wfcr.setFissionRecordId(taskFissionRecordId);
        wfcr.setCustomerId(weChatUserDTO.getUnionid());
        wfcr.setCustomerName(weChatUserDTO.getName());
        List<WeTaskFissionCompleteRecord> list = weTaskFissionCompleteRecordService.selectWeTaskFissionCompleteRecordList(wfcr);
        if (CollectionUtils.isEmpty(list)) {
            wfcr.setCreateTime(new Date());
            wfcr.setCustomerAvatar(weChatUserDTO.getAvatar());
            weTaskFissionCompleteRecordService.insertWeTaskFissionCompleteRecord(wfcr);
        }
    }

    @Override
    public List<WeCustomer> getCustomerListById(String unionId, String fissionId) {
        List<WeCustomer> customerList = new LinkedList<>();
        if (StringUtils.isEmpty(unionId)) {
            List<WeTaskFissionRecord> weTaskFissionRecords = weTaskFissionRecordService.list(new LambdaQueryWrapper<WeTaskFissionRecord>().eq(WeTaskFissionRecord::getTaskFissionId, fissionId));
            if(CollectionUtil.isNotEmpty(weTaskFissionRecords)){
                weTaskFissionRecords.forEach(record -> {
//                    WeCustomer weCustomer = weCustomerService.selectWeCustomerById(record.getCustomerId());
//                    customerList.add(weCustomer);

                    customerList.addAll(
                            weCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                                    .eq(WeCustomer::getExternalUserid,record.getCustomerId()))
                    );
                });
            }
        } else {
            WeTaskFissionRecord weTaskFissionRecord = weTaskFissionRecordService
                    .selectWeTaskFissionRecordByIdAndCustomerId(Long.valueOf(fissionId), unionId);
            Optional.ofNullable(weTaskFissionRecord).orElseThrow(() -> new WeComException("任务记录信息不存在"));


            List<WeCustomer> weCustomers = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                    .eq(WeCustomer::getState, WeConstans.FISSION_PREFIX + weTaskFissionRecord.getId()));


            List<String> eidList = Optional.ofNullable(weCustomers).orElseGet(ArrayList::new).stream()
                    .map(WeCustomer::getExternalUserid).collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(eidList)) {
                customerList.addAll(weCustomerService.listByIds(eidList));
            }
        }
        return customerList;
    }

    @Override
    public WeTaskFissionStatisticVO taskFissionStatistic(Long taskFissionId, Date startTime, Date endTime) {
        WeTaskFissionStatisticVO vo = new WeTaskFissionStatisticVO();
        WeTaskFission taskFission = weTaskFissionMapper.selectWeTaskFissionById(taskFissionId);
        if (taskFission == null) {
            throw new WeComException("任务数据不存在");
        }
        vo.setTaskFissionId(taskFissionId);
        vo.setTaskName(taskFission.getTaskName());
        vo.setStartTime(startTime);
        vo.setEndTime(endTime);
        Map<String, List<WeTaskFissionCompleteRecord>> completeRecordsMap = weTaskFissionCompleteRecordService.statisticCompleteRecords(taskFissionId, startTime, endTime).parallelStream().filter(Objects::nonNull).collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getCreateTime())));
        Map<String, List<WeTaskFissionRecord>> recordsMap = weTaskFissionRecordService.statisticRecords(taskFissionId, startTime, endTime).parallelStream().filter(Objects::nonNull).collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getCreateTime())));
        List<WeTaskFissionDailyDataVO> dailyDataList = Lists.newArrayList();
        DateUtils.findDates(startTime, endTime).stream().map(d -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, d))
                .forEach(date -> {
                    WeTaskFissionDailyDataVO v = new WeTaskFissionDailyDataVO();
                    v.setDay(date);
                    List<WeTaskFissionCompleteRecord> completeList = completeRecordsMap.get(date);
                    List<WeTaskFissionRecord> recordsList = recordsMap.get(date);
                    if (CollectionUtils.isNotEmpty(completeList)) {
                        v.setIncrease(completeList.size());
                    }
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

    @Override
    public WeTaskFissionProgressVO getCustomerTaskProgress(WeTaskFission taskFission, String unionId) {
        long total = taskFission.getFissNum();
        List<WeCustomer> list = new ArrayList<>();
        if (taskFission.getFissionType() == 1) {
            List<WeCustomer> customerList = getCustomerListById(unionId, String.valueOf(taskFission.getId()));
            if(CollectionUtil.isNotEmpty(customerList)){
                list.addAll(customerList);
            }
        } else {
            List<WeTaskFissionCompleteRecord> completeRecordList = weTaskFissionCompleteRecordService.getCompleteListByTaskId(taskFission.getId());
            if (CollectionUtil.isNotEmpty(completeRecordList)) {
                completeRecordList.forEach(completeRecord -> {
                    WeCustomer weCustomer = new WeCustomer();
                    weCustomer.setAvatar(completeRecord.getCustomerAvatar());
                    weCustomer.setUnionid(completeRecord.getCustomerId());
                    weCustomer.setCustomerName(completeRecord.getCustomerName());
                    list.add(weCustomer);
                });
            }
        }
        return WeTaskFissionProgressVO.builder().total(total).completed((long) list.size()).customers(list).build();
    }

    /**
     * 更新过期任务
     *
     * @return
     */
    @Override
    public void updateExpiredWeTaskFission() {
        weTaskFissionMapper.updateExpiredWeTaskFission();
    }


    /**
     * 根据群活码id 查询任务列表
     *
     * @param groupCodeId 群活码id
     * @return
     */
    @Override
    public List<WeTaskFission> getTaskFissionListByGroupCodeId(Long groupCodeId) {
        return this.list(new LambdaQueryWrapper<WeTaskFission>()
                .eq(WeTaskFission::getFissionTargetId, groupCodeId)
                .eq(WeTaskFission::getFissionType, 2)
                .eq(WeTaskFission::getFissStatus, 1));
    }

    /*************************************** private functions **************************************/

    private String getPosterQRCode(String fissionTargetId, WeTaskFissionRecord record, WeCustomer weCustomer) {
        String qrCode = record.getQrCode();
        if (StringUtils.isBlank(qrCode)) {
            Long taskFissionId = record.getTaskFissionId();
            WeTaskFission taskFission = weTaskFissionMapper.selectWeTaskFissionById(taskFissionId);
            Integer taskFissionType = taskFission.getFissionType();
            if (TaskFissionType.USER_FISSION.getCode().equals(taskFissionType)) {
                qrCode = getUserFissionQrcode(fissionTargetId, record);
            } else if (TaskFissionType.GROUP_FISSION.getCode().equals(taskFissionType)) {
                qrCode = getGroupFissionQrcode(taskFissionId, record, weCustomer);
            } else {
                throw new WeComException("错误的任务类型");
            }
        }
        return qrCode;
    }

    private String getUserFissionQrcode(String fissionTargetId, WeTaskFissionRecord record) {
        //获取二维码
        String qrcode = null;
        WeExternalContactDto.WeContactWay contactWay = posterContactWay(fissionTargetId, record.getId());
        WeExternalContactDto dto = weExternalContactClient.addContactWay(contactWay);
        if (dto != null) {
            qrcode = dto.getQr_code();
        }
        return qrcode;
    }

    private String getGroupFissionQrcode(Long taskFissionId, WeTaskFissionRecord record, WeCustomer weCustomer) {
        if (weCustomer != null) {
            String avatarUrl = weCustomer.getAvatar();
            String content = StringUtils.format(pageGroupUrl, taskFissionId, record.getId());
            BufferedImage bufferedImage = QREncode.crateQRCode(content, avatarUrl);
            if (bufferedImage != null) {
                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                    ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                    NetFileUtils.StreamMultipartFile streamMultipartFile = new NetFileUtils.StreamMultipartFile(System.currentTimeMillis() + ".jpg", byteArrayOutputStream.toByteArray());
                    JSONObject fileInfo = FileUtil.upload(streamMultipartFile);
                    log.info(">>>>>>>>>>>>fileInfo:{}",fileInfo.toJSONString());
                    return fileInfo.getString("imgUrlPrefix")+fileInfo.getString("fileName");
                } catch (Exception e) {
                    log.warn("生成海报二维码异常, record={}, customer={}, exception={}", JSONObject.toJSONString(record), JSONObject.toJSONString(weCustomer), ExceptionUtils.getStackTrace(e));
                    throw new WeComException("生成二维码异常");
                }
            }
            throw new WeComException("生成二维码异常");
        } else {
            throw new WeComException("生成二维码异常,用户信息不存在");
        }
    }

    private WeExternalContactDto.WeContactWay posterContactWay(String fissionTargetId, Long recordId) {
        WeExternalContactDto.WeContactWay wcw = new WeExternalContactDto.WeContactWay();
        wcw.setScene(2);
        wcw.setType(1);
        wcw.setUser(new String[]{fissionTargetId});
        wcw.setState(WeConstans.FISSION_PREFIX + recordId);
        return wcw;
    }

    private WeTaskFissionRecord getTaskFissionRecordId(Long taskFissionId, String customerId, String customerName) {
        WeTaskFissionRecord record = WeTaskFissionRecord.builder()
                .taskFissionId(taskFissionId)
                .customerId(customerId)
                .customerName(customerName)
                .createTime(new Date()).build();
        List<WeTaskFissionRecord> searchExists = weTaskFissionRecordService.selectWeTaskFissionRecordList(record);
        WeTaskFissionRecord recordInfo;
        if (CollectionUtils.isNotEmpty(searchExists)) {
            recordInfo = searchExists.get(0);
        } else {
            int insertRows = weTaskFissionRecordService.insertWeTaskFissionRecord(record);
            if (insertRows > 0) {
                recordInfo = record;
            } else {
                throw new WeComException("生成海报异常：插入裂变记录失败");
            }
        }
        return recordInfo;
    }

    private void groupQrcodeHandler(WeTaskFission weTaskFission) {
        String groupQrcodeId = weTaskFission.getFissionTargetId();
        if (weTaskFission.getFissionType() != null && weTaskFission.getFissionType().equals(TaskFissionType.GROUP_FISSION.getCode())
                && StringUtils.isNotBlank(groupQrcodeId) && StringUtils.isBlank(weTaskFission.getFissQrcode())) {
            WeGroupCode groupCode = weGroupCodeService.getById(Long.parseLong(groupQrcodeId));
            if (groupCode != null) {
                String qrcodeUrl = groupCode.getCodeUrl();
                weTaskFission.setFissQrcode(qrcodeUrl);
            }
        }
    }
}
