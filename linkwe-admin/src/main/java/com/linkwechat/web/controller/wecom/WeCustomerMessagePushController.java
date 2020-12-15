package com.linkwechat.web.controller.wecom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import com.linkwechat.wecom.service.IWeCustomerMessagePushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 企业微信 群发消息 controller
 * @author: KeWen
 * @create: 2020-12-01 20:23
 **/
@Slf4j
@RestController
@RequestMapping("/wecom/customerMessagePush")
public class WeCustomerMessagePushController extends BaseController {


    @Autowired
    private IWeCustomerMessagePushService weCustomerMessagePushService;

    /**
     * 新增群发消息发送
     */
    @PreAuthorize("@ss.hasPermi('system:push:add')")
    @Log(title = "新增群发消息发送", businessType = BusinessType.INSERT)
    @PostMapping(value = "add")
    public AjaxResult add(CustomerMessagePushDto customerMessagePushDto)  {
        try {
            weCustomerMessagePushService.addWeCustomerMessagePush(customerMessagePushDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return AjaxResult.error("群发失败");
        }
        return AjaxResult.success();
    }

}
