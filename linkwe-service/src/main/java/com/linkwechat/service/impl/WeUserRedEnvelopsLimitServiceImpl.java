package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.envelopes.WeUserRedEnvelopsLimit;
import com.linkwechat.mapper.WeUserRedEnvelopsLimitMapper;
import com.linkwechat.service.IWeUserRedEnvelopsLimitService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeUserRedEnvelopsLimitServiceImpl extends ServiceImpl<WeUserRedEnvelopsLimitMapper, WeUserRedEnvelopsLimit> implements IWeUserRedEnvelopsLimitService {


    /**
     * 员工限额列表
     * @param userId
     * @return
     */
    @Override
    public List<WeUserRedEnvelopsLimit> findLimitUserRedEnvelops(String userId) {
        return this.baseMapper.findLimitUserRedEnvelops(userId);
    }
}
