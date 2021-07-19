package com.linkwechat.web.controller.wecom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.dto.message.AsyncResultDto;
import com.linkwechat.wecom.domain.dto.message.CustomerMessagePushDto;
import com.linkwechat.wecom.domain.vo.CustomerMessagePushVo;
import com.linkwechat.wecom.domain.vo.WeCustomerMessageResultVo;
import com.linkwechat.wecom.service.IWeCustomerMessageOriginalService;
import com.linkwechat.wecom.service.IWeCustomerMessagePushService;
import com.linkwechat.wecom.service.IWeCustomerMessgaeResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

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

    @Autowired
    private IWeCustomerMessageOriginalService weCustomerMessageOriginalService;

    @Autowired
    private IWeCustomerMessgaeResultService weCustomerMessgaeResultService;


    /**
     * 新增群发消息发送
     */
    //   @PreAuthorize("@ss.hasPermi('customerMessagePush:push:add')")
    @Log(title = "新增群发消息发送", businessType = BusinessType.INSERT)
    @PostMapping(value = "add")
    public AjaxResult add(@RequestBody CustomerMessagePushDto customerMessagePushDto) {
        try {

            weCustomerMessagePushService.addWeCustomerMessagePush(customerMessagePushDto);

        } catch (JsonProcessingException | ParseException | CloneNotSupportedException e) {
            e.printStackTrace();
            return AjaxResult.error("群发失败");
        }
        return AjaxResult.success();
    }


    /**
     * 群发消息列表
     */
    //   @PreAuthorize("@ss.hasPermi('customerMessagePush:push:list')")
    @GetMapping(value = "/list")
    public TableDataInfo list(@RequestParam(value = "sender", required = false) String sender
            , @RequestParam(value = "content", required = false) String content
            , @RequestParam(value = "pushType", required = false) String pushType
            , @RequestParam(value = "beginTime", required = false) String beginTime
            , @RequestParam(value = "endTime", required = false) String endTime) {
        startPage();
        List<CustomerMessagePushVo> list = weCustomerMessagePushService.customerMessagePushs(sender, content, pushType, beginTime, endTime);
        return getDataTable(list);
    }

    /**
     * 群发消息详情
     */
    //   @PreAuthorize("@ss.hasPermi('customerMessagePush:push:view')")
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestParam(value = "messageId") Long messageId) {
        CustomerMessagePushVo customerMessagePushVo = weCustomerMessageOriginalService.CustomerMessagePushDetail(messageId);
        return AjaxResult.success(customerMessagePushVo);
    }

    /**
     * 群发消息结果列表
     */
    //   @PreAuthorize("@ss.hasPermi('customerMessagePush:push:pushResults')")
    @GetMapping(value = "/pushResults")
    public TableDataInfo customerMessagePushResult(@RequestParam(value = "messageId") Long messageId, @RequestParam(value = "status") String status) {
        startPage();
        List<WeCustomerMessageResultVo> weCustomerMessageResuls = weCustomerMessgaeResultService.customerMessagePushs(messageId, status);
        return getDataTable(weCustomerMessageResuls);
    }

    /**
     * 同步消息发送结果
     */
    //   @PreAuthorize("@ss.hasPermi('customerMessagePush:push:asyncResult')")
    @PostMapping(value = "asyncResult")
    public AjaxResult asyncResult(@RequestBody AsyncResultDto asyncResultDto) throws JsonProcessingException {
        weCustomerMessageOriginalService.asyncResult(asyncResultDto);
        return AjaxResult.success();
    }

}
