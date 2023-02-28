package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeStrackStage;

public interface IWeStrackStageService extends IService<WeStrackStage> {

    /**
     * 初始化商阶
     */
    void initStrackStage();


    /**
     * 新增商阶
     * @param weStrackStage
     */
    void add(WeStrackStage weStrackStage);


    /**
     * 删除商阶
     * @param id
     * @param growStageVal
     */
    void remove(String id,Integer growStageVal);
}
