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
import com.linkwechat.mapper.WeLeadsFollowRecordContentMapper;
import com.linkwechat.mapper.WeLeadsFollowRecordMapper;
import com.linkwechat.mapper.WeLeadsFollowerMapper;
import com.linkwechat.mapper.WeLeadsMapper;
import com.linkwechat.service.IWeLeadsAutoRecoveryService;
import com.linkwechat.service.IWeLeadsFollowRecordAttachmentService;
import com.linkwechat.service.IWeLeadsFollowRecordCooperateUserService;
import com.linkwechat.service.IWeLeadsFollowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private WeLeadsFollowerMapper weLeadsFollowerMapper;
    @Resource
    private WeLeadsFollowRecordMapper weLeadsFollowRecordMapper;
    @Resource
    private WeLeadsMapper weLeadsMapper;
    @Resource
    private WeLeadsFollowRecordContentMapper weLeadsFollowRecordContentMapper;
    @Resource
    private IWeLeadsFollowRecordAttachmentService weLeadsFollowRecordAttachmentService;
    @Resource
    private IWeLeadsFollowRecordCooperateUserService weLeadsFollowRecordCooperateUserService;
    @Resource
    private IWeLeadsAutoRecoveryService weLeadsAutoRecoveryService;


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
                    .followUserName(i.getFollowerName())
                    .followUserDeptName(i.getDeptName())
                    .followStatus(i.getFollowerStatus())
                    .followStatusStr(LeadsStatusEnum.ofByCode(i.getFollowerStatus()).getStatusCn())
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
        record.setWeLeadsId(record.getWeLeadsId());
        record.setSeaId(weLeads.getSeaId());
        record.setFollowUserId(weLeadsFollower.getId());
        record.setRecordStatus(1);
        record.setCreateTime(new Date());
        weLeadsFollowRecordMapper.insert(record);

        //添加线索的跟进记录内容
        WeLeadsFollowRecordContent content1 = WeLeadsFollowRecordContent.builder()
                .id(IdUtil.getSnowflake().nextId())
                .recordId(record.getId())
                .itemKey("跟进方式")
                .itemValue(FollowModeEnum.of(request.getFollowMode()).getDesc())
                .rank(0).attachment(0).visible(0).build();
        weLeadsFollowRecordContentMapper.insert(content1);

        if (BeanUtil.isNotEmpty(request.getCooperateTime())) {
            WeLeadsFollowRecordContent content2 = WeLeadsFollowRecordContent.builder()
                    .id(IdUtil.getSnowflakeNextId())
                    .recordId(record.getId())
                    .itemKey("协定日期")
                    .itemValue(DateUtil.format(request.getCooperateTime(), DatePattern.NORM_DATETIME_PATTERN))
                    .rank(1).attachment(0).visible(0).build();
            weLeadsFollowRecordContentMapper.insert(content2);
        }

        long id = IdUtil.getSnowflake().nextId();
        WeLeadsFollowRecordContent content3 = WeLeadsFollowRecordContent.builder()
                .id(id).recordId(record.getId()).itemKey("跟进内容")
                .itemValue(request.getRecordContent())
                .rank(2).parentId(0L)
                .attachment(request.getAttachmentList().size() > 0 ? 1 : 0)
                .visible(0).build();
        weLeadsFollowRecordContentMapper.insert(content3);

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
    }

    @Override
    public void list(WeLeadsFollowRecordRequest request) {


    }
}