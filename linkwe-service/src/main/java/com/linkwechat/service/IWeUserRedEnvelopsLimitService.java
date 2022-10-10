package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.envelopes.WeUserRedEnvelopsLimit;

import java.util.List;

public interface IWeUserRedEnvelopsLimitService extends IService<WeUserRedEnvelopsLimit> {
    List<WeUserRedEnvelopsLimit> findLimitUserRedEnvelops(String userId);
}
