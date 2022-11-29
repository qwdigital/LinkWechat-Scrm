package com.linkwechat.fallback;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.merchant.WeGetBillListQuery;
import com.linkwechat.domain.wecom.vo.merchant.WeGetBillListVo;
import com.linkwechat.fegin.QwMerchantClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 对外收款
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/28 12:35
 */
@Slf4j
@Component
public class QwMerchantFallBackFactory implements QwMerchantClient {

    @Override
    public AjaxResult<WeGetBillListVo> getBillList(WeGetBillListQuery query) {
        return null;
    }
}
