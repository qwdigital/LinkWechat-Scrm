package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSeaVisibleRange;
import com.linkwechat.mapper.WeLeadsSeaVisibleRangeMapper;
import com.linkwechat.service.IWeLeadsSeaVisibleRangeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 公海可见范围 服务实现类
 * </p>
 *
 * @author WangYX
 * @since 2023-04-03
 */
@Service
public class WeLeadsSeaVisibleRangeServiceImpl extends ServiceImpl<WeLeadsSeaVisibleRangeMapper, WeLeadsSeaVisibleRange> implements IWeLeadsSeaVisibleRangeService {

    @Resource
    private WeLeadsSeaVisibleRangeMapper weLeadsSeaVisibleRangeMapper;

    @Override
    public List<WeLeadsSeaVisibleRange> getVisibleRange(Long userId, String weUserId) {
        return weLeadsSeaVisibleRangeMapper.getVisibleRange(userId, weUserId);
    }
}
