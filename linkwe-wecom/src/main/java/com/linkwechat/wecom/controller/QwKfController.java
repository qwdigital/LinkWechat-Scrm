package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.department.WeDeptQuery;
import com.linkwechat.domain.wecom.query.kf.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.department.WeDeptVo;
import com.linkwechat.domain.wecom.vo.kf.*;
import com.linkwechat.wecom.service.IQwDeptService;
import com.linkwechat.wecom.service.IQwKfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author danmo
 * @date 2022/4/18 21:46
 **/
@Api(tags = "客服接口管理")
@Slf4j
@RestController
@RequestMapping("kf")
public class QwKfController {
    @Resource
    private IQwKfService kfService;

    /**
     * 添加客服账号
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "添加客服账号",httpMethod = "POST")
    @PostMapping("/account/add")
    public AjaxResult<WeAddKfVo> addAccount(@RequestBody WeKfAddQuery query) {
        WeAddKfVo weAddKfVo = kfService.addAccount(query);
        return AjaxResult.success(weAddKfVo);
    }

    /**
     * 删除客服账号
     * @param query
     * @return
     */
    @ApiOperation(value = "删除客服账号",httpMethod = "POST")
    @PostMapping("/account/del")
    public AjaxResult<WeResultVo> delAccount(@RequestBody WeKfQuery query) {
        WeResultVo resultVo = kfService.delAccount(query);
        return AjaxResult.success(resultVo);
    }

    /**
     * 修改客服账号
     * @param query
     * @return
     */
    @ApiOperation(value = "修改客服账号",httpMethod = "POST")
    @PostMapping("/account/update")
    public AjaxResult<WeResultVo> updateAccount(@RequestBody WeKfQuery query) {
        WeResultVo resultVo = kfService.updateAccount(query);
        return AjaxResult.success(resultVo);
    }

    /**
     * 获取客服账号列表
     * @param query
     * @return
     */
    @ApiOperation(value = "获取客服账号列表",httpMethod = "POST")
    @PostMapping("/account/list")
    public AjaxResult<WeKfListVo> getAccountList(@RequestBody WeBaseQuery query) {
        WeKfListVo weKfListVo = kfService.getAccountList(query);
        return AjaxResult.success(weKfListVo);
    }

    /**
     * 获取客服帐号链接
     * @param query
     * @return
     */
    @ApiOperation(value = "获取客服帐号链接",httpMethod = "POST")
    @PostMapping("/account/addContactWay")
    public AjaxResult<WeKfDetailVo> addContactWay(@RequestBody WeKfQuery query) {
        WeKfDetailVo kfDetailVo = kfService.addContactWay(query);
        return AjaxResult.success(kfDetailVo);
    }

    /**
     * 添加客服接待人员
     * @param query
     * @return
     */
    @ApiOperation(value = "添加客服接待人员",httpMethod = "POST")
    @PostMapping("/servicer/add")
    public AjaxResult<WeKfUserListVo> addServicer(@RequestBody WeKfQuery query) {
        WeKfUserListVo weKfUserListVo = kfService.addServicer(query);
        return AjaxResult.success(weKfUserListVo);
    }

    /**
     * 删除客服接待人员
     * @param query
     * @return
     */
    @ApiOperation(value = "删除客服接待人员",httpMethod = "POST")
    @PostMapping("/servicer/del")
    public AjaxResult<WeKfUserListVo> delServicer(@RequestBody WeKfQuery query) {
        WeKfUserListVo weKfUserListVo = kfService.delServicer(query);
        return AjaxResult.success(weKfUserListVo);
    }

    /**
     * 获取客服接待人员列表
     * @param query
     * @return
     */
    @ApiOperation(value = "获取客服接待人员列表",httpMethod = "POST")
    @PostMapping("/servicer/list")
    public AjaxResult<WeKfUserListVo> getServicerList(@RequestBody WeKfQuery query) {
        WeKfUserListVo weKfUserListVo = kfService.getServicerList(query);
        return AjaxResult.success(weKfUserListVo);
    }

    /**
     * 获取会话状态
     * @param query
     * @return
     */
    @ApiOperation(value = "获取会话状态",httpMethod = "POST")
    @PostMapping("/session/getStatus")
    public AjaxResult<WeKfStateVo> getSessionStatus(@RequestBody WeKfStateQuery query) {
        WeKfStateVo weKfStateVo = kfService.getSessionStatus(query);
        return AjaxResult.success(weKfStateVo);
    }

    /**
     * 修改会话状态
     * @param query
     * @return
     */
    @ApiOperation(value = "修改会话状态",httpMethod = "POST")
    @PostMapping("/session/updateStatus")
    public AjaxResult<WeKfStateVo> updateSessionStatus(@RequestBody WeKfStateQuery query) {
        WeKfStateVo resultVo = kfService.updateSessionStatus(query);
        return AjaxResult.success(resultVo);
    }

    /**
     * 发送会话消息
     * @param query
     * @return
     */
    @ApiOperation(value = "发送会话消息",httpMethod = "POST")
    @PostMapping("/session/sendMsg")
    public AjaxResult<WeKfMsgVo> sendSessionMsg(@RequestBody WeKfMsgQuery query) {
        WeKfMsgVo kfMsgVo = kfService.sendSessionMsg(query);
        return AjaxResult.success(kfMsgVo);
    }

    /**
     * 读取会话消息
     * @param query
     * @return
     */
    @ApiOperation(value = "读取会话消息",httpMethod = "POST")
    @PostMapping("/session/getMsg")
    public AjaxResult<WeKfGetMsgVo> getSessionMsg(@RequestBody WeKfGetMsgQuery query) {
        WeKfGetMsgVo kfGetMsgVo = kfService.getSessionMsg(query);
        return AjaxResult.success(kfGetMsgVo);
    }

    /**
     * 获取客服客户信息
     * @param query
     * @return
     */
    @ApiOperation(value = "获取客服客户信息",httpMethod = "POST")
    @PostMapping("/customer/batchGet")
    public AjaxResult<WeCustomerInfoVo> getCustomerInfos(@RequestBody WeKfCustomerQuery query) {
        WeCustomerInfoVo customerInfos = kfService.getCustomerInfos(query);
        return AjaxResult.success(customerInfos);
    }

    /**
     * 获取配置的专员与客户群
     * @param query
     * @return
     */
    @ApiOperation(value = "获取配置的专员与客户群",httpMethod = "POST")
    @PostMapping("/customer/getUpgradeServiceConfig")
    public AjaxResult<WeUpgradeServiceConfigVo> getUpgradeServiceConfig(@RequestBody WeBaseQuery query) {
        WeUpgradeServiceConfigVo serviceConfig = kfService.getUpgradeServiceConfig(query);
        return AjaxResult.success(serviceConfig);
    }

    /**
     * 为客户升级为专员或客户群服务
     * @param query
     * @return
     */
    @ApiOperation(value = "为客户升级为专员或客户群服务",httpMethod = "POST")
    @PostMapping("/customer/customerUpgradeService")
    public AjaxResult<WeResultVo> customerUpgradeService(@RequestBody WeUpgradeServiceQuery query) {
        WeResultVo resultVo = kfService.customerUpgradeService(query);
        return AjaxResult.success(resultVo);
    }

    /**
     * 为客户取消推荐
     * @param query
     * @return
     */
    @ApiOperation(value = "为客户取消推荐",httpMethod = "POST")
    @PostMapping("/customer/cancelUpgradeService")
    public AjaxResult<WeResultVo> cancelUpgradeService(@RequestBody WeUpgradeServiceQuery query) {
        WeResultVo resultVo = kfService.cancelUpgradeService(query);
        return AjaxResult.success(resultVo);
    }

    /**
     * 发送欢迎语等事件响应消息
     * @param query
     * @return
     */
    @ApiOperation(value = "发送欢迎语等事件响应消息",httpMethod = "POST")
    @PostMapping("/sendMsgOnEvent")
    public AjaxResult<WeKfMsgVo> sendMsgOnEvent(@RequestBody WeKfMsgQuery query) {
        WeKfMsgVo kfMsgVo = kfService.sendMsgOnEvent(query);
        return AjaxResult.success(kfMsgVo);
    }

    /**
     * 获取「客户数据统计」企业汇总数据
     * @param query
     * @return
     */
    @ApiOperation(value = "获取「客户数据统计」企业汇总数据",httpMethod = "POST")
    @PostMapping("/getCorpStatistic")
    public AjaxResult<WeKfStatisticListVo> getCorpStatistic(@RequestBody WeKfGetStatisticQuery query) {
        WeKfStatisticListVo weKfStatisticList = kfService.getCorpStatistic(query);
        return AjaxResult.success(weKfStatisticList);
    }

    /**
     * 获取「客户数据统计」接待人员明细数据
     * @param query
     * @return
     */
    @ApiOperation(value = "获取「客户数据统计」接待人员明细数据",httpMethod = "POST")
    @PostMapping("/getServicerStatistic")
    public AjaxResult<WeKfStatisticListVo> getServicerStatistic(@RequestBody WeKfGetStatisticQuery query) {
        WeKfStatisticListVo weKfStatisticList = kfService.getServicerStatistic(query);
        return AjaxResult.success(weKfStatisticList);
    }

    /**
     * 知识库新增分组
     * @param query
     * @return
     */
    @ApiOperation(value = "知识库新增分组",httpMethod = "POST")
    @PostMapping("/addKnowledgeGroup")
    public AjaxResult<WeKfAddKnowledgeGroupVo> addKnowledgeGroup(@RequestBody WeKfAddKnowledgeGroupQuery query) {
        WeKfAddKnowledgeGroupVo groupVo = kfService.addKnowledgeGroup(query);
        return AjaxResult.success(groupVo);
    }

    /**
     * 知识库删除分组
     * @param query
     * @return
     */
    @ApiOperation(value = "知识库删除分组",httpMethod = "POST")
    @PostMapping("/delKnowledgeGroup")
    public AjaxResult<WeResultVo> delKnowledgeGroup(@RequestBody WeKfKnowledgeGroupQuery query) {
        WeResultVo weResult = kfService.delKnowledgeGroup(query);
        return AjaxResult.success(weResult);
    }

    /**
     * 知识库修改分组
     * @param query
     * @return
     */
    @ApiOperation(value = "知识库修改分组",httpMethod = "POST")
    @PostMapping("/modKnowledgeGroup")
    public AjaxResult<WeResultVo> modKnowledgeGroup(@RequestBody WeKfKnowledgeGroupQuery query) {
        WeResultVo weResult = kfService.modKnowledgeGroup(query);
        return AjaxResult.success(weResult);
    }

    /**
     * 知识库分组列表
     * @param query
     * @return
     */
    @ApiOperation(value = "知识库修改分组",httpMethod = "POST")
    @PostMapping("/getKnowledgeGroupList")
    public AjaxResult<WeKfKnowledgeGroupListVo> getKnowledgeGroupList(@RequestBody WeKfKnowledgeGroupQuery query) {
        WeKfKnowledgeGroupListVo weKfKnowledgeGroupList = kfService.getKnowledgeGroupList(query);
        return AjaxResult.success(weKfKnowledgeGroupList);
    }
}
