package com.linkwechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.agent.query.WeAgentAddQuery;
import com.linkwechat.domain.agent.query.WeAgentEditQuery;
import com.linkwechat.domain.agent.query.WeAgentMsgAddQuery;
import com.linkwechat.domain.agent.query.WeAgentMsgListQuery;
import com.linkwechat.domain.agent.vo.WeAgentListVo;
import com.linkwechat.domain.agent.vo.WeAgentMsgListVo;
import com.linkwechat.domain.agent.vo.WeAgentMsgVo;
import com.linkwechat.service.IWeAgentInfoService;
import com.linkwechat.service.IWeAgentMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author danmo
 * @description 应用管理
 * @date 2022/11/03 18:22
 **/
@Slf4j
@RestController
@RequestMapping(value = "agent")
@Api(tags = "应用管理")
public class WeAgentController extends BaseController {

    @Autowired
    private IWeAgentInfoService weAgentInfoService;

    @Autowired
    private IWeAgentMsgService weAgentMsgService;

    @ApiOperation(value = "拉取应用信息", httpMethod = "POST")
    @PostMapping("/pull")
    public AjaxResult pullAgent(@RequestBody @Validated WeAgentAddQuery query) {
        log.info("拉取应用信息入参query:{}", JSONObject.toJSONString(query));
        weAgentInfoService.pullAgent(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "编辑应用信息", httpMethod = "PUT")
    @PutMapping("/update/{id}")
    public AjaxResult update(@PathVariable("id") Integer id, @RequestBody WeAgentEditQuery query) {
        query.setId(id);
        log.info("编辑应用信息入参query:{}", JSONObject.toJSONString(query));
        weAgentInfoService.update(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取应用列表", httpMethod = "GET")
    @GetMapping("/list")
    public AjaxResult<WeAgentListVo> getList() {
        log.info("获取应用列表接口调用");
        List<WeAgentListVo> list = weAgentInfoService.getList();
        return AjaxResult.success(list);
    }

    @ApiOperation(value = "删除应用", httpMethod = "DELETE")
    @DeleteMapping("/delete/{id}")
    public AjaxResult<WeAgentListVo> deleteAgent(@PathVariable("id") Integer id) {
        log.info("删除应用入参query:{}", id);
        weAgentInfoService.deleteAgent(id);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取历史消息列表", httpMethod = "GET")
    @GetMapping("/msg/list")
    public AjaxResult<WeAgentMsgListVo> getMsgList(@RequestBody WeAgentMsgListQuery query) {
        return AjaxResult.success();
    }

    @ApiOperation(value = "新增应用消息", httpMethod = "POST")
    @PostMapping("/msg/add")
    public AjaxResult addMsg(@RequestBody WeAgentMsgAddQuery query) {
        log.info("新增应用消息入参query:{}", JSONObject.toJSONString(query));
        weAgentMsgService.addMsg(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "修改应用消息", httpMethod = "PUT")
    @PutMapping("/msg/update/{id}")
    public AjaxResult updateMsg(@PathVariable("id") Long id, @RequestBody WeAgentMsgAddQuery query) {
        query.setId(id);
        log.info("修改应用消息入参query:{}", JSONObject.toJSONString(query));
        weAgentMsgService.updateMsg(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "删除应用消息", httpMethod = "DELETE")
    @DeleteMapping("/msg/delete/{id}")
    public AjaxResult deleteMsg(@PathVariable("id") Long id) {
        log.info("删除应用消息入参query:{}", id);
        weAgentMsgService.deleteMsg(id);
        return AjaxResult.success();
    }

    @ApiOperation(value = "应用消息详情", httpMethod = "GET")
    @GetMapping("/msg/get/{id}")
    public AjaxResult<WeAgentMsgVo> getMsgInfo(@PathVariable("id") Long id) {
        log.info("应用消息详情入参query:{}", id);
        weAgentMsgService.getMsgInfo(id);
        return AjaxResult.success();
    }

    @ApiOperation(value = "撤销应用消息", httpMethod = "GET")
    @GetMapping("/msg/revoke/{id}")
    public AjaxResult revokeMsgInfo(@PathVariable("id") Long id) {
        log.info("撤销消息详情入参query:{}", id);
        weAgentMsgService.revokeMsgInfo(id);
        return AjaxResult.success();
    }

}
