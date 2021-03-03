package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.wecom.service.IWeCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 客户画像相关controller
 * @author: HaoN
 * @create: 2021-03-03 15:10
 **/
@RestController
@RequestMapping("/wecom/portrait")
public class WeCustomerPortraitController extends BaseController {


    @Autowired
    private IWeCustomerService iWeCustomerService;


    /**
     * 根据客户id和当前企业员工id获取员工详细信息
     * @param externalUserid
     * @param operUserid
     * @return
     */
    @GetMapping(value = "/findWeCustomerInfo")
    public AjaxResult findWeCustomerInfo(String externalUserid, String operUserid){


        return AjaxResult.success(
                iWeCustomerService.findCustomerByOperUseridAndCustomerId(externalUserid,operUserid)
        );
    }



}
