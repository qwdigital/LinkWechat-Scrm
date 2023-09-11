package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.sea.entity.WeLeadsSeaBaseSettings;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaBaseSettingsRequest;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaBaseSettingsVO;


/**
 * 公海基础配置 服务接口
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/11 16:33
 */
public interface IWeLeadsSeaBaseSettingsService extends IService<WeLeadsSeaBaseSettings> {

    /**
     * 查询公海基础配置，这里表里只有一条数据
     *
     * @return 公海基础配置
     */
    WeLeadsSeaBaseSettingsVO queryBaseSetting();

    /**
     * 更新公海基础配置
     *
     * @param request 前端入参
     * @return 成功或者失败
     */
    boolean updateBaseSetting(WeLeadsSeaBaseSettingsRequest request);
}

