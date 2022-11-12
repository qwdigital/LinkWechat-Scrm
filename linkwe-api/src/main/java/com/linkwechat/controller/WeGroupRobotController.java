package com.linkwechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.WeGroupRobotInfo;
import com.linkwechat.domain.WeGroupRobotMsg;
import com.linkwechat.domain.robot.query.WeRobotAddQuery;
import com.linkwechat.domain.robot.query.WeRobotMsgAddQuery;
import com.linkwechat.domain.robot.query.WeRobotMsgListQuery;
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
    public TableDataInfo<WeGroupRobotInfo> getList(WeRobotQuery query) {
        log.info("获取机器人列表接口");
        super.startPage();
        List<WeGroupRobotInfo> list = weGroupRobotInfoService.getList(query);
        return getDataTable(list);
    }


    @ApiOperation(value = "删除机器人", httpMethod = "DELETE")
    @DeleteMapping("/delete/{id}")
    public AjaxResult deleteGroupRobot(@PathVariable("id") Long id) {
        log.info("删除机器人入参query:{}", id);
        weGroupRobotInfoService.deleteGroupRobot(id);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取机器人历史消息列表", httpMethod = "GET")
    @GetMapping("/msg/list")
    public TableDataInfo<WeGroupRobotMsg> getMsgList(WeRobotMsgListQuery query) {
        log.info("获取机器人历史消息列表入参query:{}", JSONObject.toJSONString(query));
        super.startPage();
        List<WeGroupRobotMsg> list = weGroupRobotMsgService.getMsgList(query);
        return getDataTable(list);
    }

    @ApiOperation(value = "发送群机器人消息", httpMethod = "POST")
    @PostMapping("/msg/add")
    public AjaxResult addRobotMsg(@Validated @RequestBody WeRobotMsgAddQuery query) {
        log.info("发送群机器人消息入参query:{}", JSONObject.toJSONString(query));
        weGroupRobotMsgService.addRobotMsg(query);
        return AjaxResult.success();
    }

}
