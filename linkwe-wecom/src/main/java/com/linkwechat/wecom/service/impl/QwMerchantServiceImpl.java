package com.linkwechat.wecom.service.impl;

import com.linkwechat.domain.wecom.query.merchant.WeGetBillListQuery;
import com.linkwechat.domain.wecom.vo.merchant.WeGetBillListVo;
import com.linkwechat.wecom.client.WeMerchantClient;
import com.linkwechat.wecom.service.IQwMerchantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class QwMerchantServiceImpl implements IQwMerchantService {

    @Resource
    private WeMerchantClient weMerchantClient;

    @Override
    public WeGetBillListVo getBillList(WeGetBillListQuery query) {
        return weMerchantClient.getBillList(query);
    }
}
