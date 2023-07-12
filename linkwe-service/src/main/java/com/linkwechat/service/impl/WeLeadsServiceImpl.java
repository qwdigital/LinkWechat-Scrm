package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.LeadsCenterConstants;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.entity.WeLeadsFollower;
import com.linkwechat.domain.leads.leads.vo.WeLeadsFollowerNumVO;
import com.linkwechat.domain.leads.record.entity.WeLeadsFollowRecord;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaBaseSettingsVO;
import com.linkwechat.mapper.WeLeadsFollowRecordMapper;
import com.linkwechat.mapper.WeLeadsMapper;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 线索 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 14:44
 */
@Slf4j
@Service
public class WeLeadsServiceImpl extends ServiceImpl<WeLeadsMapper, WeLeads> implements IWeLeadsService {

    @Resource
    private IWeLeadsSeaBaseSettingsService weLeadsSeaBaseSettingsService;
    @Resource
    private IWeLeadsFollowerService weLeadsFollowerService;
    @Resource
    private WeLeadsFollowRecordMapper weLeadsFollowRecordMapper;
    @Resource
    private IWeLeadsFollowRecordContentService weLeadsFollowRecordContentService;
    @Resource
    private IWeLeadsAutoRecoveryService weLeadsAutoRecoveryService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveLeads(Long leadsId) {
        //判断线索是否存在
        WeLeads weLeads = this.getById(leadsId);
        if (BeanUtil.isEmpty(weLeads)) {
            throw new ServiceException("线索不存在！");
        }
        //判断线索是否已分配
        if (!(weLeads.getLeadsStatus().equals(LeadsStatusEnum.WAIT_FOR_DISTRIBUTION.getCode())
                || weLeads.getLeadsStatus().equals(LeadsStatusEnum.RETURNED.getCode()))) {
            throw new ServiceException("线索已分配", HttpStatus.CONFLICT);
        }
        //检查领取数量是否超过基础配置
        checkReceiveNum(weLeads.getSeaId());

        String key = LeadsCenterConstants.LEADS + leadsId;
        RLock lock = redissonClient.getLock(key);
        try {
            //尝试获取锁，等待5s
            boolean b = lock.tryLock(5, 5, TimeUnit.SECONDS);
            if (b) {
                //1.再次判断线索所属状态
                WeLeads leads = this.getById(leadsId);
                if (!(leads.getLeadsStatus().equals(LeadsStatusEnum.WAIT_FOR_DISTRIBUTION.getCode())
                        || leads.getLeadsStatus().equals(LeadsStatusEnum.RETURNED.getCode()))) {
                    throw new ServiceException("线索已分配", HttpStatus.CONFLICT);
                }
                //执行领取操作
                receiveLeads(weLeads);
            }
        } catch (InterruptedException e) {
            log.error("领取线索时，尝试获取分布式锁失败！");
            e.printStackTrace();
        }
    }

    /**
     * 主动领取
     *
     * @author WangYX
     * @date 2023/07/12 11:18
     * @version 1.0.0
     */
    private void receiveLeads(WeLeads weLeads) {
        //修改线索状态和添加当前跟进人信息
        LambdaUpdateWrapper<WeLeads> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(WeLeads::getId, weLeads.getId());
        wrapper.set(WeLeads::getLeadsStatus, SecurityUtils.getLoginUser().getSysUser().getUserId());
        wrapper.set(WeLeads::getFollowerId, SecurityUtils.getLoginUser().getSysUser().getWeUserId());
        wrapper.set(WeLeads::getWeUserId, LeadsStatusEnum.BE_FOLLOWING_UP.getCode());
        wrapper.set(WeLeads::getFollowerName, SecurityUtils.getLoginUser().getSysUser().getUserName());
        this.update(wrapper);

        //添加线索跟进人记录
        WeLeadsFollower weLeadsFollower = new WeLeadsFollower();
        weLeadsFollower.setId(IdUtil.getSnowflake().nextId());
        weLeadsFollower.setLeadsId(weLeads.getId());
        weLeadsFollower.setFollowerId(SecurityUtils.getLoginUser().getSysUser().getUserId());
        weLeadsFollower.setFollowerWeUserId(SecurityUtils.getLoginUser().getSysUser().getWeUserId());
        weLeadsFollower.setFollowerName(SecurityUtils.getLoginUser().getSysUser().getUserName());
        weLeadsFollower.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        weLeadsFollower.setDeptName(SecurityUtils.getLoginUser().getSysUser().getDeptName());
        weLeadsFollower.setGetType(1);
        weLeadsFollower.setFollowerStatus(1);
        weLeadsFollower.setFollowerStartTime(new Date());
        weLeadsFollower.setIsCurrentFollower(1);
        weLeadsFollowerService.save(weLeadsFollower);

        //添加跟进记录
        WeLeadsFollowRecord record = new WeLeadsFollowRecord();
        record.setId(IdUtil.getSnowflake().nextId());
        record.setWeLeadsId(weLeads.getId());
        record.setSeaId(weLeads.getSeaId());
        record.setFollowUserId(weLeadsFollower.getId());
        record.setRecordStatus(0);
        record.setCreateTime(new Date());
        weLeadsFollowRecordMapper.insert(record);

        //添加跟进记录内容
        weLeadsFollowRecordContentService.memberToReceive(record.getId(), weLeads.getSeaId());

        //自动回收
        weLeadsAutoRecoveryService.save(weLeads.getId(), SecurityUtils.getLoginUser().getSysUser().getUserId(), weLeads.getSeaId());
    }


    /**
     * 检查领取数量是否超过基础配置
     *
     * @param seaId 公海Id
     * @author WangYX
     * @date 2023/07/11 17:51
     */
    private void checkReceiveNum(Long seaId) {
        //公海基础配置
        WeLeadsSeaBaseSettingsVO weLeadsSeaBaseSettingsVO = weLeadsSeaBaseSettingsService.queryBaseSetting();
        if (BeanUtil.isEmpty(weLeadsSeaBaseSettingsVO)) {
            throw new ServiceException("公海基础配置未设置！");
        }

        WeLeadsFollowerNumVO leadsFollowerNum = weLeadsFollowerService.getLeadsFollowerNum(seaId);
        if (leadsFollowerNum.getMaxClaim() >= weLeadsSeaBaseSettingsVO.getMaxClaim()) {
            throw new ServiceException("今日领取数量已超过员工每日领取上限");
        }
        if (leadsFollowerNum.getStockMaxClaim() >= weLeadsSeaBaseSettingsVO.getStockMaxClaim()) {
            throw new ServiceException("累计领取数量超过员工客户存量上限");
        }
    }
}