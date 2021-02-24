package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeGroupStatistic;

import java.util.List;

/**
 * 群聊数据统计数据
Service接口
 *
 * @author ruoyi
 * @date 2021-02-24
 */
public interface IWeGroupStatisticService extends IService<WeGroupStatistic> {

    /**
     * 查询列表
     */
    List<WeGroupStatistic> queryList(WeGroupStatistic weGroupStatistic);
}
