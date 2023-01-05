package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.wecom.TicketUtils;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.fegin.QwTicketClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author danmo
 * @description h5 ticket校验接口
 * @date 2021/1/6 11:23
 **/
@Api(tags = " h5 ticket校验接口")
@Slf4j
@RequestMapping(value = "ticket")
@RestController
public class WeTicketController extends BaseController {

    @Resource
    private QwTicketClient qwTicketClient;

    /**
     * 获取微信应用相关Ticket
     * @return
     */
    @GetMapping("/getWxTicket")
    public AjaxResult getWxTicket(String url){
        return AjaxResult.success(TicketUtils.getSignatureMap(
                qwTicketClient.getWxTicket().getData()
                ,url));
    }


}
