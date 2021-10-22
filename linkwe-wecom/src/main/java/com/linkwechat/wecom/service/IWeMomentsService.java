package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeMoments;
import com.linkwechat.wecom.mapper.WeMomentsMapper;

public interface IWeMomentsService extends IService<WeMoments> {

    void addOrUpdateMoments(WeMoments weMoments);
}
