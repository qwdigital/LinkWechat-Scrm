package com.linkwechat.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.domain.leads.leads.entity.WeLeadsAutoRecovery;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSea;
import com.linkwechat.mapper.WeLeadsAutoRecoveryMapper;
import com.linkwechat.mapper.WeLeadsSeaMapper;
import com.linkwechat.service.IWeLeadsAutoRecoveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    private WeLeadsSeaMapper weLeadsSeaMapper;

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
}