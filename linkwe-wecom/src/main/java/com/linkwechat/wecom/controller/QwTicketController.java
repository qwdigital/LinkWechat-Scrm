package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.wecom.service.IQwTicketService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author danmo
 * @description h5 ticket校验接口
 * @date 2021/1/6 11:23
 **/
@Api(tags = " h5 ticket校验接口")
@RequestMapping(value = "ticket")
@RestController
public class QwTicketController{

    @Autowired
    private IQwTicketService qwTicketService;

    @Autowired
    private IQwTicketService iQwTicketService;

    /**
     * 获取企业的jsapi_ticket
     *
     * @param query JS接口页面的完整URL
     * @return
     */
    @PostMapping("/getCorpTicket")
    public AjaxResult<String> getCorpTicket(@RequestBody WeBaseQuery query) {
        return AjaxResult.success(qwTicketService.getCorpTicket(query));
    }

    /**
     * 获取应用的jsapi_ticket
     *
     * @param query JS接口页面的完整URL
     * @return
     */
    @PostMapping("/getAgentTicket")
    public AjaxResult<String> getAgentTicket(@RequestBody WeBaseQuery query) {
        return AjaxResult.success(qwTicketService.getAgentTicket(query));
    }

    /**
     * 获取微信应用相关Ticket
     * @return
     */
    @GetMapping("/getWxTicket")
    public AjaxResult<String> getWxTicket(){

        return AjaxResult.success(
                iQwTicketService.getWxTicket()
        );
    }


}
