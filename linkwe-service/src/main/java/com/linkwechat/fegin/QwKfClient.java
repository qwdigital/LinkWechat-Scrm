package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.kf.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.kf.*;
import com.linkwechat.fallback.QwKfFallbackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @date 2022/3/29 23:00
 */
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwKfFallbackFactory.class, contextId = "linkwe-wecom-kf")
public interface QwKfClient {
    /**
     * 添加客服账号
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "添加客服账号",httpMethod = "POST")
    @PostMapping("/kf/account/add")
    public AjaxResult<WeAddKfVo> addAccount(@RequestBody WeKfAddQuery query);

    /**
     * 删除客服账号
     * @param query
     * @return
     */
    @ApiOperation(value = "删除客服账号",httpMethod = "POST")
    @PostMapping("/kf/account/del")
    public AjaxResult<WeResultVo> delAccount(@RequestBody WeKfQuery query);

    /**
     * 修改客服账号
     * @param query
     * @return
     */
    @ApiOperation(value = "修改客服账号",httpMethod = "POST")
    @PostMapping("/kf/account/update")
    public AjaxResult<WeResultVo> updateAccount(@RequestBody WeKfQuery query);

    /**
     * 获取客服账号列表
     * @param query
     * @return
     */
    @ApiOperation(value = "获取客服账号列表",httpMethod = "POST")
    @PostMapping("/kf/account/list")
    public AjaxResult<WeKfListVo> getAccountList(@RequestBody WeBaseQuery query);

    /**
     * 获取客服帐号链接
     * @param query
     * @return
     */
    @ApiOperation(value = "获取客服帐号链接",httpMethod = "POST")
    @PostMapping("/kf/account/addContactWay")
    public AjaxResult<WeKfDetailVo> addContactWay(@RequestBody WeKfQuery query);

    /**
     * 添加客服接待人员
     * @param query
     * @return
     */
    @ApiOperation(value = "添加客服接待人员",httpMethod = "POST")
    @PostMapping("/kf/servicer/add")
    public AjaxResult<WeKfUserListVo> addServicer(@RequestBody WeKfQuery query);

    /**
     * 删除客服接待人员
     * @param query
     * @return
     */
    @ApiOperation(value = "删除客服接待人员",httpMethod = "POST")
    @PostMapping("/kf/servicer/del")
    public AjaxResult<WeKfUserListVo> delServicer(@RequestBody WeKfQuery query);

    /**
     * 获取客服接待人员列表
     * @param query
     * @return
     */
    @ApiOperation(value = "获取客服接待人员列表",httpMethod = "POST")
    @PostMapping("/kf/servicer/list")
    public AjaxResult<WeKfUserListVo> getServicerList(@RequestBody WeKfQuery query);

    /**
     * 获取会话状态
     * @param query
     * @return
     */
    @ApiOperation(value = "获取会话状态",httpMethod = "POST")
    @PostMapping("/kf/session/getStatus")
    public AjaxResult<WeKfStateVo> getSessionStatus(@RequestBody WeKfStateQuery query);

    /**
     * 修改会话状态
     * @param query
     * @return
     */
    @ApiOperation(value = "修改会话状态",httpMethod = "POST")
    @PostMapping("/kf/session/updateStatus")
    public AjaxResult<WeKfStateVo> updateSessionStatus(@RequestBody WeKfStateQuery query);

    /**
     * 发送会话消息
     * @param query
     * @return
     */
    @ApiOperation(value = "发送会话消息",httpMethod = "POST")
    @PostMapping("/kf/session/sendMsg")
    public AjaxResult<WeKfMsgVo> sendSessionMsg(@RequestBody WeKfMsgQuery query);

    /**
     * 读取会话消息
     * @param query
     * @return
     */
    @ApiOperation(value = "读取会话消息",httpMethod = "POST")
    @PostMapping("/kf/session/getMsg")
    public AjaxResult<WeKfGetMsgVo> getSessionMsg(@RequestBody WeKfGetMsgQuery query);

    /**
     * 获取客服客户信息
     * @param query
     * @return
     */
    @ApiOperation(value = "获取客服客户信息",httpMethod = "POST")
    @PostMapping("/kf/customer/batchGet")
    public AjaxResult<WeCustomerInfoVo> getCustomerInfos(@RequestBody WeKfCustomerQuery query);

    /**
     * 获取配置的专员与客户群
     * @param query
     * @return
     */
    @ApiOperation(value = "获取配置的专员与客户群",httpMethod = "POST")
    @PostMapping("/kf/customer/getUpgradeServiceConfig")
    public AjaxResult<WeUpgradeServiceConfigVo> getUpgradeServiceConfig(@RequestBody WeBaseQuery query);

    /**
     * 为客户升级为专员或客户群服务
     * @param query
     * @return
     */
    @ApiOperation(value = "为客户升级为专员或客户群服务",httpMethod = "POST")
    @PostMapping("/kf/customer/customerUpgradeService")
    public AjaxResult<WeResultVo> customerUpgradeService(@RequestBody WeUpgradeServiceQuery query);

    /**
     * 为客户取消推荐
     * @param query
     * @return
     */
    @ApiOperation(value = "为客户取消推荐",httpMethod = "POST")
    @PostMapping("/kf/customer/cancelUpgradeService")
    public AjaxResult<WeResultVo> cancelUpgradeService(@RequestBody WeUpgradeServiceQuery query);

    /**
     * 发送欢迎语等事件响应消息
     * @param query
     * @return
     */
    @ApiOperation(value = "发送欢迎语等事件响应消息",httpMethod = "POST")
    @PostMapping("/kf/sendMsgOnEvent")
    public AjaxResult<WeKfMsgVo> sendMsgOnEvent(@RequestBody WeKfMsgQuery query);

    /**
     * 获取「客户数据统计」企业汇总数据
     * @param query
     * @return
     */
    @ApiOperation(value = "获取「客户数据统计」企业汇总数据",httpMethod = "POST")
    @PostMapping("/kf/getCorpStatistic")
    AjaxResult<WeKfStatisticListVo> getCorpStatistic(@RequestBody WeKfGetStatisticQuery query);

    /**
     * 获取「客户数据统计」接待人员明细数据
     * @param query
     * @return
     */
    @ApiOperation(value = "获取「客户数据统计」接待人员明细数据",httpMethod = "POST")
    @PostMapping("/kf/getServicerStatistic")
    AjaxResult<WeKfStatisticListVo> getServicerStatistic(@RequestBody WeKfGetStatisticQuery query);

    /**
     * 知识库新增分组
     * @param query
     * @return
     */
    @ApiOperation(value = "知识库新增分组",httpMethod = "POST")
    @PostMapping("/kf/addKnowledgeGroup")
    AjaxResult<WeKfAddKnowledgeGroupVo> addKnowledgeGroup(@RequestBody WeKfAddKnowledgeGroupQuery query);

    /**
     * 知识库删除分组
     * @param query
     * @return
     */
    @ApiOperation(value = "知识库删除分组",httpMethod = "POST")
    @PostMapping("/kf/delKnowledgeGroup")
    AjaxResult<WeResultVo> delKnowledgeGroup(@RequestBody WeKfKnowledgeGroupQuery query);

    /**
     * 知识库修改分组
     * @param query
     * @return
     */
    @ApiOperation(value = "知识库修改分组",httpMethod = "POST")
    @PostMapping("/kf/modKnowledgeGroup")
    AjaxResult<WeResultVo> modKnowledgeGroup(@RequestBody WeKfKnowledgeGroupQuery query);

    /**
     * 知识库分组列表
     * @param query
     * @return
     */
    @ApiOperation(value = "知识库修改分组",httpMethod = "POST")
    @PostMapping("/kf/getKnowledgeGroupList")
    AjaxResult<WeKfKnowledgeGroupListVo> getKnowledgeGroupList(@RequestBody WeKfKnowledgeGroupQuery query);
}
