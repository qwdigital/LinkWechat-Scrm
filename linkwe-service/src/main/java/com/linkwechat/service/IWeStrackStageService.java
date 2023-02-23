package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeStrackStage;

public interface IWeStrackStageService extends IService<WeStrackStage> {

    /**
     * 初始化商阶
     */
    void initStrackStage();
}
