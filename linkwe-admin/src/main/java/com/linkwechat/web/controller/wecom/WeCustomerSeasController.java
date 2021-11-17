package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.wecom.service.IWeCustomerSeasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户公海相关
 */
@RestController
@RequestMapping("/wecom/seas")
public class WeCustomerSeasController extends BaseController {

    @Autowired
    private IWeCustomerSeasService iWeCustomerSeasService;








}
