package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.merchant.WeGetBillListQuery;
import com.linkwechat.domain.wecom.vo.merchant.WeGetBillListVo;
import com.linkwechat.wecom.service.IQwMerchantService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 对外收款
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/11/28 12:27
 */
@RestController
@RequestMapping("merchant")
public class QwMerchantController {

    @Resource
    private IQwMerchantService qwMerchantService;

    /**
     * 获取对外收款记录
     *
     * @param query
     * @return
     */
    @PostMapping("/bill/list")
    public AjaxResult<WeGetBillListVo> getbillList(@RequestBody WeGetBillListQuery query) {
        WeGetBillListVo billList = qwMerchantService.getBillList(query);
        return AjaxResult.success(billList);
    }


}
