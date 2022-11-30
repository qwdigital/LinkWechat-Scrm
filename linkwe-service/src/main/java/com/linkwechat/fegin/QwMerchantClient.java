package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.merchant.WeGetBillListQuery;
import com.linkwechat.domain.wecom.vo.merchant.WeGetBillListVo;
import com.linkwechat.fallback.QwMerchantFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 对外收款
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/28 12:34
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwMerchantFallBackFactory.class, contextId = "linkwe-wecom-mrchant")
public interface QwMerchantClient {

    /**
     * 获取对外收款记录
     *
     * @param query
     * @return
     */
    @PostMapping("/merchant/bill/list")
    AjaxResult<WeGetBillListVo> getBillList(WeGetBillListQuery query);


}
