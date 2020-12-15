package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCustomerMessageOriginal;
/**
 * 群发消息 原始数据信息表 we_customer_messageOriginal
 *
 * @author kewen
 * @date 2020-12-12
 */
public interface IWeCustomerMessageOriginalService extends IService<WeCustomerMessageOriginal> {
    /**
     * 保存原始数据信息
     * @param weCustomerMessageOriginal 原始数据信息
     * @return
     */
    int saveWeCustomerMessageOriginal(WeCustomerMessageOriginal weCustomerMessageOriginal);
}
