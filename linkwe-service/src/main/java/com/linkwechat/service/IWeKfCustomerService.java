package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeKfCustomer;

/**
 * 客服客户表(WeKfCustomer)
 *
 * @author danmo
 * @since 2022-04-15 15:53:34
 */
public interface IWeKfCustomerService extends IService<WeKfCustomer> {

    /**
     * 保存客户信息
     * @param corpId 企业ID
     * @param externalUserId 客户id
     */
    void saveCustomerInfo(String corpId, String externalUserId);

    WeKfCustomer getCustomerInfo(String corpId, String externalUserId);

} 
