package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.storecode.entity.WeStoreCodeConfig;
import com.linkwechat.domain.storecode.query.WxStoreCodeQuery;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;

public interface IWeStoreCodeConfigService extends IService<WeStoreCodeConfig> {


    /**
     * 创建或更新门店导购码或门店群活码
     * @param storeCodeConfig
     */
    void createOrUpdate(WeStoreCodeConfig storeCodeConfig) throws IOException;


    /**
     * 获取对应的门店相关配置
     * @param wxStoreCodeQuery
     * @return
     */
    WeStoreCodeConfig getWeStoreCodeConfig(WxStoreCodeQuery wxStoreCodeQuery);

}
