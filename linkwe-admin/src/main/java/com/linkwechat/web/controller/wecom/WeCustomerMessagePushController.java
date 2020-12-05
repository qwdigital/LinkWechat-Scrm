package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.wecom.service.IWeCustomerMessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 企业微信 群发消息 controller
 * @author: KeWen
 * @create: 2020-12-01 20:23
 **/
@RestController
@RequestMapping("/wecom/customerMessagePush")
public class WeCustomerMessagePushController extends BaseController {

    @Autowired
    private IWeCustomerMessagePushService weCustomerMessagePushService;


}
