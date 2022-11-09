package com.linkwechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.agent.vo.LwAgentListVo;
import com.linkwechat.domain.robot.query.WeRobotAddQuery;
import com.linkwechat.domain.robot.query.WeRobotQuery;
import com.linkwechat.service.IWeGroupRobotInfoService;
import com.linkwechat.service.IWeGroupRobotMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author danmo
 * @description 群机器人管理
 * @date 2022/11/03 18:22
 **/
@Slf4j
@RestController
@RequestMapping(value = "group/robot")
@Api(tags = "群机器人管理")
public class WeGroupRobotController extends BaseController {

    @Autowired
    private IWeGroupRobotInfoService weGroupRobotInfoService;

    @Autowired
    private IWeGroupRobotMsgService weGroupRobotMsgService;


    @ApiOperation(value = "新增群机器人", httpMethod = "POST")
    @PostMapping("/add")
    public AjaxResult addRobot(@RequestBody @Validated WeRobotAddQuery query) {
        log.info("新增群机器人入参query:{}", JSONObject.toJSONString(query));
        Long id = weGroupRobotInfoService.addGroupRobot(query);
        return AjaxResult.success(id);
    }

    @ApiOperation(value = "编辑群机器人", httpMethod = "PUT")
    @PutMapping("/update/{id}")
    public AjaxResult updateRobot(@PathVariable("id") Long id, @Validated @RequestBody WeRobotAddQuery query) {
        query.setId(id);
        log.info("编辑群机器人入参query:{}", JSONObject.toJSONString(query));
        weGroupRobotInfoService.updateRobot(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取机器人列表", httpMethod = "GET")
    @GetMapping("/list")
    public AjaxResult<LwAgentListVo> getList(WeRobotQuery query) {
        log.info("获取机器人列表接口");
        //List<LwAgentListVo> list = weGroupRobotInfoService.getList(query);
        return AjaxResult.success();
    }

    /*





    @ApiOperation(value = "删除应用", httpMethod = "DELETE")
    @DeleteMapping("/delete/{id}")
    public AjaxResult<LwAgentListVo> deleteAgent(@PathVariable("id") Integer id) {
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

   */

}
