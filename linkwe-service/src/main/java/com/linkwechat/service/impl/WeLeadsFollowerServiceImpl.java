package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.common.enums.leads.record.FollowBackModeEnum;
import com.linkwechat.common.enums.leads.record.FollowModeEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerNumVO;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerVO;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordAttachment;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordContent;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecordCooperateUser;
import com.linkwechat.domain.leads.record.query.WeLeadsAddFollowRequest;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordAttachmentRequest;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordCooperateUserRequest;
import com.linkwechat.domain.leads.record.query.WeLeadsFollowRecordRequest;
import com.linkwechat.domain.task.query.WeTasksRequest;
import com.linkwechat.mapper.WeLeadsFollowRecordContentMapper;
import com.linkwechat.mapper.WeLeadsFollowRecordMapper;
import com.linkwechat.mapper.WeLeadsFollowerMapper;
import com.linkwechat.mapper.WeLeadsMapper;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 线索跟进人 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:44
 */
@Slf4j
@Service
public class WeLeadsFollowerServiceImpl extends ServiceImpl<WeLeadsFollowerMapper, WeLeadsFollower> implements IWeLeadsFollowerService {
    @Resource
    private WeLeadsMapper weLeadsMapper;
    @Resource
    private IWeTasksService weTasksService;
    @Resource
    private WeLeadsFollowerMapper weLeadsFollowerMapper;
    @Resource
    private WeLeadsFollowRecordMapper weLeadsFollowRecordMapper;
    @Resource
    private IWeLeadsAutoRecoveryService weLeadsAutoRecoveryService;
    @Resource
    private WeLeadsFollowRecordContentMapper weLeadsFollowRecordContentMapper;
    @Resource
    private IWeLeadsFollowRecordAttachmentService weLeadsFollowRecordAttachmentService;
    @Resource
    private IWeLeadsFollowRecordCooperateUserService weLeadsFollowRecordCooperateUserService;


    @Override
    public WeLeadsFollowerNumVO getLeadsFollowerNum(Long seaId) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        List<WeLeadsFollower> list = weLeadsFollowerMapper.getLeadsFollower(sysUser.getUserId());
        String dateStr = DateUtil.date().toDateStr();
        long count = list.stream().filter(i -> i.getSeaId().equals(seaId)).filter(i -> DateUtil.date(i.getFollowerStartTime()).toDateStr().equals(dateStr)).count();
        return WeLeadsFollowerNumVO.builder().maxClaim(count).stockMaxClaim(Long.valueOf(list.size())).build();
    }

    @Override
    public List<WeLeadsFollowerVO> getFollowerList(Long leadsId) {
        LambdaQueryWrapper<WeLeadsFollower> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WeLeadsFollower::getLeadsId, leadsId);
        wrapper.orderByDesc(WeLeadsFollower::getFollowerStartTime);
        List<WeLeadsFollower> list = this.baseMapper.selectList(wrapper);
        List<WeLeadsFollowerVO> result = new ArrayList<>();
        list.forEach(i -> {
            WeLeadsFollowerVO build = WeLeadsFollowerVO.builder()
                    .id(i.getId())
                    .followerId(i.getFollowerId())
                    .followUserName(i.getFollowerName())
                    .followUserDeptName(i.getDeptName())
                    .followerStatus(i.getFollowerStatus())
                    .followerStatusStr(LeadsStatusEnum.ofByCode(i.getFollowerStatus()).getStatusCn())
                    .build();
            if (i.getReturnType() != null) {
                build.setBackMode(i.getReturnType());
                build.setBackModeStr(FollowBackModeEnum.ofByCode(i.getReturnType()).getType());
            }
            result.add(build);
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFollow(WeLeadsAddFollowRequest request) {
        //获取线索的当前跟进人
        LambdaQueryWrapper<WeLeadsFollower> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WeLeadsFollower::getLeadsId, request.getWeLeadsId());
        wrapper.eq(WeLeadsFollower::getFollowerId, SecurityUtils.getLoginUser().getSysUser().getUserId());
        wrapper.eq(WeLeadsFollower::getFollowerStatus, LeadsStatusEnum.BE_FOLLOWING_UP.getCode());
        wrapper.eq(WeLeadsFollower::getIsCurrentFollower, 1);
        WeLeadsFollower weLeadsFollower = weLeadsFollowerMapper.selectOne(wrapper);
        if (BeanUtil.isEmpty(weLeadsFollower)) {
            throw new ServiceException("不是当前跟进人，无法跟进！");
        }

        WeLeads weLeads = weLeadsMapper.selectById(request.getWeLeadsId());
        if (BeanUtil.isEmpty(weLeads)) {
            throw new ServiceException("线索不存在！");
        }

        //添加线索的跟进记录
        WeLeadsFollowRecord record = new WeLeadsFollowRecord();
        record.setId(IdUtil.getSnowflake().nextId());
        record.setWeLeadsId(request.getWeLeadsId());
        record.setSeaId(weLeads.getSeaId());
        record.setFollowUserId(weLeadsFollower.getId());
        record.setRecordStatus(1);
        record.setCreateTime(new Date());
        weLeadsFollowRecordMapper.insert(record);

        //添加线索的跟进记录内容
        addRecordContent(IdUtil.getSnowflake().nextId(), record.getId(), "跟进方式", FollowModeEnum.of(request.getFollowMode()).getDesc(), 0, 0L, 0);
        if (BeanUtil.isNotEmpty(request.getCooperateTime())) {
            addRecordContent(IdUtil.getSnowflake().nextId(), record.getId(), "协定日期", DateUtil.format(request.getCooperateTime(), DatePattern.NORM_DATETIME_PATTERN), 1, 0L, 0);
        }
        long id = IdUtil.getSnowflake().nextId();
        addRecordContent(id, record.getId(), "跟进内容", request.getRecordContent(), 2, 0L, CollectionUtil.isNotEmpty(request.getAttachmentList()) ? 1 : 0);

        //添加线索的跟进记录附件
        List<WeLeadsFollowRecordAttachmentRequest> attachmentList = request.getAttachmentList();
        if (CollectionUtil.isNotEmpty(attachmentList)) {
            List<WeLeadsFollowRecordAttachment> collect = attachmentList.stream().map(i -> WeLeadsFollowRecordAttachment.builder()
                    .id(IdUtil.getSnowflake().nextId())
                    .contentId(id)
                    .type(i.getAttachmentType())
                    .title(i.getAttachmentName())
                    .url(i.getAttachmentAddress())
                    .build()).collect(Collectors.toList());
            weLeadsFollowRecordAttachmentService.saveBatch(collect);
        }

        //添加线索的跟进记录协作成员
        List<WeLeadsFollowRecordCooperateUserRequest> cooperateUsers = request.getCooperateUsers();
        if (CollectionUtil.isNotEmpty(cooperateUsers)) {
            List<WeLeadsFollowRecordCooperateUser> collect = cooperateUsers.stream().map(i -> WeLeadsFollowRecordCooperateUser.builder()
                    .id(IdUtil.getSnowflake().nextId())
                    .contentId(id)
                    .userId(i.getUserId())
                    .weUserId(i.getWeUserId())
                    .userName(i.getUserName())
                    .build()).collect(Collectors.toList());
            weLeadsFollowRecordCooperateUserService.saveBatch(collect);
        }

        //取消并添加新的自动回收机制
        weLeadsAutoRecoveryService.cancelAndSave(record.getWeLeadsId(), weLeadsFollower.getFollowerId(), weLeads.getSeaId());

        //取消并添加新的待办任务(线索长时间待跟进)
        weTasksService.cancelAndAddLeadsLongTimeNotFollowUp(weLeads.getId(), weLeads.getName(), SecurityUtils.getLoginUser().getSysUser().getUserId(),
                SecurityUtils.getLoginUser().getSysUser().getWeUserId(), weLeads.getSeaId());

        //待办任务
        if (BeanUtil.isNotEmpty(request.getCooperateTime())) {
            //线索约定事项待跟进
            WeTasksRequest build = WeTasksRequest.builder()
                    .userId(SecurityUtils.getLoginUser().getSysUser().getUserId())
                    .weUserId(SecurityUtils.getLoginUser().getSysUser().getWeUserId())
                    .leadsId(request.getWeLeadsId())
                    .recordId(record.getId())
                    .cooperateTime(request.getCooperateTime())
                    .build();
            weTasksService.appointItemWaitFollowUp(build);

            //成员的线索约定事项待处理
            if (CollectionUtil.isNotEmpty(cooperateUsers)) {
                build.setCooperateUsers(cooperateUsers);
                weTasksService.userAppointItemWaitFollowUp(build);
            }
        }

        if (CollectionUtil.isNotEmpty(cooperateUsers)) {
            //有成员的线索跟进@了你
            WeTasksRequest build = WeTasksRequest.builder()
                    .userId(SecurityUtils.getLoginUser().getSysUser().getUserId())
                    .weUserId(SecurityUtils.getLoginUser().getSysUser().getWeUserId())
                    .userName(SecurityUtils.getLoginUser().getSysUser().getUserName())
                    .leadsId(request.getWeLeadsId())
                    .recordId(record.getId())
                    .cooperateTime(request.getCooperateTime())
                    .cooperateUsers(cooperateUsers).build();
            weTasksService.userFollowUp2You(build);
        }
    }

    @Override
    public void list(WeLeadsFollowRecordRequest request) {


    }

    @Override
    public List<WeLeadsFollowerVO> userStatistic(List<Long> userIds) {
        return this.baseMapper.userStatistic(userIds);
    }

    /**
     * 添加跟进记录内容
     *
     * @param id         主键Id，不传时，自动生成
     * @param recordId   跟进记录Id
     * @param itemKey    记录项目名
     * @param itemValue  记录项目值
     * @param rank       排序
     * @param parentId   父类Id，默认0L
     * @param attachment 是否存在附件： 0否 1是 默认0
     * @return
     * @author WangYX
     * @date 2023/07/21 18:25
     */
    private void addRecordContent(Long id, Long recordId, String itemKey, String itemValue, Integer rank, Long parentId, Integer attachment) {
        WeLeadsFollowRecordContent content = WeLeadsFollowRecordContent.builder()
                .id(id == null ? IdUtil.getSnowflakeNextId() : id)
                .recordId(recordId)
                .itemKey(itemKey)
                .itemValue(itemValue)
                .rank(rank)
                .parentId(parentId == null ? 0L : parentId)
                .attachment(attachment == null ? 0 : attachment)
                .visible(0)
                .createTime(new Date())
                .build();
        weLeadsFollowRecordContentMapper.insert(content);
    }
}