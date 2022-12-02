package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.merchant.WeGetBillListQuery;
import com.linkwechat.domain.wecom.vo.merchant.WeGetBillListVo;

/**
 * 对外收款
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/28 12:24
 */
public interface IQwMerchantService {
    /**
     * 获取对外收款记录
     * @param query
     * @return
     */
    WeGetBillListVo getBillList(WeGetBillListQuery query);


}
