package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.MessageConstants;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.common.enums.leads.record.FollowBackModeEnum;
import com.linkwechat.common.enums.message.MessageTypeEnum;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.entity.WeLeadsAutoRecovery;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.mapper.*;
import com.linkwechat.service.IWeLeadsAutoRecoveryService;
import com.linkwechat.service.IWeLeadsFollowRecordContentService;
import com.linkwechat.service.IWeMessageNotificationService;
import com.linkwechat.service.IWeTasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 线索自动回收 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:44
 */
@Slf4j
@Service
public class WeLeadsAutoRecoveryServiceImpl extends ServiceImpl<WeLeadsAutoRecoveryMapper, WeLeadsAutoRecovery> implements IWeLeadsAutoRecoveryService {
    @Resource
    private WeLeadsMapper weLeadsMapper;
    @Resource
    private IWeTasksService weTasksService;
    @Resource
    private WeLeadsSeaMapper weLeadsSeaMapper;
    @Resource
    private WeLeadsFollowerMapper weLeadsFollowerMapper;
    @Resource
    private WeLeadsFollowRecordMapper weLeadsFollowRecordMapper;
    @Resource
    private IWeLeadsFollowRecordContentService weLeadsFollowRecordContentService;
    @Resource
    private IWeMessageNotificationService weMessageNotificationService;


    @Override
    public Long save(Long leadsId, Long followerId, Long seaId) {
        WeLeadsSea sea = weLeadsSeaMapper.selectById(seaId);
        //判断是否自动回收
        if (sea.getIsAutoRecovery().equals(1)) {
            Integer first = sea.getFirst();
            DateTime dateTime = DateUtil.offsetDay(DateUtil.beginOfDay(DateUtil.date()), first);

            WeLeadsAutoRecovery autoRecovery = new WeLeadsAutoRecovery();
            autoRecovery.setId(IdUtil.getSnowflake().nextId());
            autoRecovery.setLeadsId(leadsId);
            autoRecovery.setFollowerId(followerId);
            autoRecovery.setType(0);
            autoRecovery.setExecutingState(0);
            autoRecovery.setRecoveryTime(dateTime);
            autoRecovery.setDelFlag(Constants.COMMON_STATE);
            this.baseMapper.insert(autoRecovery);
            return autoRecovery.getId();
        }
        return -1L;
    }

    @Override
    public void cancel(Long leadsId, Long followerId) {
        //取消自动回收
        WeLeadsAutoRecovery autoRecovery = new WeLeadsAutoRecovery();
        autoRecovery.setExecutingState(2);
        LambdaUpdateWrapper<WeLeadsAutoRecovery> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(WeLeadsAutoRecovery::getLeadsId, leadsId);
        updateWrapper.eq(WeLeadsAutoRecovery::getFollowerId, followerId);
        updateWrapper.eq(WeLeadsAutoRecovery::getExecutingState, 0);
        this.baseMapper.update(autoRecovery, updateWrapper);
    }

    @Override
    public Long cancelAndSave(Long leadsId, Long followerId, Long seaId) {
        //取消旧的
        this.cancel(leadsId, followerId);
        //添加新的
        return this.save(leadsId, followerId, seaId);
    }

    @Override
    public void executeAutoRecovery() {

        //获取未执行的跟进的线索
        DateTime dateTime = DateUtil.beginOfDay(DateUtil.date());
        LambdaQueryWrapper<WeLeadsAutoRecovery> wrapper = Wrappers.lambdaQuery();
        wrapper.le(WeLeadsAutoRecovery::getRecoveryTime, dateTime);
        wrapper.eq(WeLeadsAutoRecovery::getDelFlag, Constants.COMMON_STATE);
        wrapper.eq(WeLeadsAutoRecovery::getExecutingState, 0);
        List<WeLeadsAutoRecovery> list = this.baseMapper.selectList(wrapper);

        //执行回收
        if (CollectionUtil.isNotEmpty(list)) {
            for (WeLeadsAutoRecovery weLeadsAutoRecovery : list) {
                //修改线索状态并清空当前跟进人
                WeLeads weLeads = weLeadsMapper.selectById(weLeadsAutoRecovery.getLeadsId());
                if (BeanUtil.isEmpty(weLeads)) {
                    continue;
                }
                WeLeads leads = new WeLeads();
                leads.setId(weLeadsAutoRecovery.getLeadsId());
                leads.setLeadsStatus(LeadsStatusEnum.RETURNED.getCode());
                leads.setFollowerId(null);
                leads.setFollowerName(null);
                leads.setWeUserId(null);
                leads.setDeptId(null);
                leads.setRecoveryTimes(weLeads.getRecoveryTimes() + 1);
                leads.setReturnReason(FollowBackModeEnum.AUTO_RECOVERY.getCode());
                weLeadsMapper.updateById(leads);

                //更新线索跟进人
                LambdaQueryWrapper<WeLeadsFollower> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(WeLeadsFollower::getLeadsId, weLeadsAutoRecovery.getLeadsId());
                queryWrapper.eq(WeLeadsFollower::getIsCurrentFollower, 1);
                WeLeadsFollower one = weLeadsFollowerMapper.selectOne(queryWrapper);
                if (BeanUtil.isNotEmpty(one)) {
                    WeLeadsFollower weLeadsFollower = new WeLeadsFollower();
                    weLeadsFollower.setId(one.getId());
                    weLeadsFollower.setFollowerStatus(3);
                    weLeadsFollower.setFollowerEndTime(new Date());
                    weLeadsFollower.setIsCurrentFollower(0);
                    weLeadsFollower.setReturnType(FollowBackModeEnum.AUTO_RECOVERY.getCode());
                    weLeadsFollower.setReturnReason(FollowBackModeEnum.AUTO_RECOVERY.getType());
                    weLeadsFollowerMapper.updateById(weLeadsFollower);

                    //添加跟进记录
                    WeLeadsFollowRecord record = new WeLeadsFollowRecord();
                    record.setId(IdUtil.getSnowflake().nextId());
                    record.setWeLeadsId(weLeadsAutoRecovery.getLeadsId());
                    record.setSeaId(weLeads.getSeaId());
                    record.setFollowUserId(one.getId());
                    record.setRecordStatus(3);
                    record.setCreateTime(new Date());
                    weLeadsFollowRecordMapper.insert(record);

                    //添加跟进记录内容
                    weLeadsFollowRecordContentService.autoRecovery(record.getId());
                }

                //自动回收状态修改
                weLeadsAutoRecovery.setExecutingState(1);
                this.baseMapper.updateById(weLeadsAutoRecovery);

                //取消线索长时间待跟进任务
                weTasksService.cancelLeadsLongTimeNotFollowUp(weLeads.getId(), weLeads.getFollowerId());

                //消息通知
                weMessageNotificationService.saveAndSend(MessageTypeEnum.LEADS.getType(), weLeads.getWeUserId(), MessageConstants.LEADS_AUTO_RECOVERY, weLeads.getName());
            }
        }
    }
}