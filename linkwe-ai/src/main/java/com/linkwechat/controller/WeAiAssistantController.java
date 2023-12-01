package com.linkwechat.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.WeAiMsgQuery;
import com.linkwechat.service.IWeAiSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/assistant")
@Api(tags = "AI助手管理")
public class WeAiAssistantController {

    @Autowired
    private IWeAiSessionService iWeAiSessionService;


    @ApiOperation(value = "创建连接",httpMethod = "GET")
    @GetMapping("/create/connect")
    public AjaxResult createSseConnect() {
        iWeAiSessionService.createSseConnect();
        return AjaxResult.success();
    }

    @ApiOperation(value = "关闭连接",httpMethod = "GET")
    @GetMapping("/close/connect/{sessionId}")
    public AjaxResult closeSseConnect(@PathVariable("sessionId") String sessionId) {
        iWeAiSessionService.closeSseConnect(sessionId);
        return AjaxResult.success();
    }

    @ApiOperation(value = "发送消息",httpMethod = "POST")
    @PostMapping("/send/msg")
    public AjaxResult sendMsg(@RequestBody WeAiMsgQuery query){
        iWeAiSessionService.sendMsg(query);
        return AjaxResult.success();
    }

}
