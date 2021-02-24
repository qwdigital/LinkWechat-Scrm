package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeUserBehaviorData;

import java.util.List;

/**
 * 联系客户统计数据 Service接口
 *
 * @author ruoyi
 * @date 2021-02-24
 */
public interface IWeUserBehaviorDataService extends IService<WeUserBehaviorData> {

    /**
     * 查询列表
     */
    List<WeUserBehaviorData> queryList(WeUserBehaviorData weUserBehaviorData);
}
