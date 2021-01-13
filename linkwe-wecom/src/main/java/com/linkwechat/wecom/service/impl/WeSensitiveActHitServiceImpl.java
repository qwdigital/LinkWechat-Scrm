package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.wecom.domain.WeSensitiveActHit;
import com.linkwechat.wecom.mapper.WeSensitiveActHitMapper;
import com.linkwechat.wecom.service.IWeSensitiveActHitService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author leejoker <1056650571@qq.com>
 * @version 1.0
 * @date 2021/1/13 9:05
 */
@Service
public class WeSensitiveActHitServiceImpl extends ServiceImpl<WeSensitiveActHitMapper, WeSensitiveActHit> implements IWeSensitiveActHitService {
    @Override
    public WeSensitiveActHit selectWeSensitiveActHitById(Long id) {
        return getById(id);
    }

    @Override
    public List<WeSensitiveActHit> selectWeSensitiveActHitList(WeSensitiveActHit weSensitiveActHit) {
        LambdaQueryWrapper<WeSensitiveActHit> lambda = Wrappers.lambdaQuery();
        lambda.orderByDesc(WeSensitiveActHit::getCreateTime);
        return list(lambda);
    }

    @Override
    public boolean insertWeSensitiveActHit(WeSensitiveActHit weSensitiveActHit) {
        weSensitiveActHit.setCreateBy(SecurityUtils.getUsername());
        weSensitiveActHit.setCreateTime(DateUtils.getNowDate());
        return saveOrUpdate(weSensitiveActHit);
    }
}
