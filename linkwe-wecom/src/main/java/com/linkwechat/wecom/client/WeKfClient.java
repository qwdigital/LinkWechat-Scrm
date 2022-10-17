package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.kf.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.kf.*;
import com.linkwechat.wecom.interceptor.WeKfAccessTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @Description 客服管理
 * @date 2021/12/13 10:23
 **/
@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeKfAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeKfClient {

    /**
     * 添加客服
     *
     * @param query in {@link WeKfAddQuery}
     * @return {@link WeAddKfVo}
     */
    @Post(url = "/kf/account/add")
    WeAddKfVo add(@JSONBody WeKfAddQuery query);

    /**
     * 删除客服
     *
     * @param query in {@link WeKfQuery}
     * @return {@link WeResultVo}
     */
    @Post(url = "/kf/account/del")
    WeResultVo delete(@JSONBody WeKfQuery query);

    /**
     * 修改客服信息
     *
     * @param query in {@link WeKfQuery}
     * @return {@link WeResultVo}
     */
    @Post(url = "/kf/account/update")
    WeResultVo update(@JSONBody WeKfQuery query);

    /**
     * 获取客服帐号列表
     *
     * @param query in {@link WeBaseQuery}
     * @return {@link WeKfListVo}
     */
    @Post(url = "/kf/account/list")
    WeKfListVo list(@JSONBody QwKfListQuery query);

    /**
     * 获取客服帐号链接
     *
     * @param query in {@link WeKfQuery}
     * @return {@link WeKfDetailVo}
     */
    @Post(url = "/kf/add_contact_way")
    WeKfDetailVo addContactWay(@JSONBody WeKfQuery query);

    /**
     * 添加接待人员
     *
     * @param query in {@link WeKfQuery}
     * @return {@link WeKfUserListVo}
     */
    @Post(url = "/kf/servicer/add")
    WeKfUserListVo addServicer(@JSONBody WeKfQuery query);

    /**
     * 删除接待人员
     *
     * @param query in {@link WeKfQuery}
     * @return {@link WeKfUserListVo}
     */
    @Post(url = "/kf/servicer/del")
    WeKfUserListVo delServicer(@JSONBody WeKfQuery query);

    /**
     * 获取接待人员列表
     *
     * @param query in {@link WeKfQuery}
     * @return {@link WeKfUserListVo}
     */
    @Get(url = "/kf/servicer/list?open_kfid=${query.open_kfid}")
    WeKfUserListVo servicerList(@Var("query") WeKfQuery query);


    /**
     * 获取会话状态
     *
     * @param query in {@link WeKfStateQuery}
     * @return {@link WeKfStateVo}
     */
    @Post(url = "/kf/service_state/get")
    WeKfStateVo getServiceState(@JSONBody WeKfStateQuery query);

    /**
     * 变更会话状态
     *
     * @param query in {@link WeKfStateQuery}
     * @return {@link WeResultVo}
     */
    @Post(url = "/kf/service_state/trans")
    WeKfStateVo transServiceState(@JSONBody WeKfStateQuery query);


    /**
     * 发送消息
     *
     * @param query in {@link WeKfMsgQuery}
     * @return {@link WeKfMsgVo}
     */
    @Post(url = "/kf/send_msg")
    WeKfMsgVo sendMsg(@JSONBody WeKfMsgQuery query);

    /**
     * 读取消息
     *
     * @param query in {@link WeKfGetMsgQuery}
     * @return {@link WeKfGetMsgVo}
     */
    @Post(url = "/kf/sync_msg")
    WeKfGetMsgVo syncMsg(@JSONBody WeKfGetMsgQuery query);

    /**
     * 客户基本信息获取
     *
     * @param query in {@link WeKfCustomerQuery}
     * @return {@link WeCustomerInfoVo}
     */
    @Post(url = "/kf/customer/batchget")
    WeCustomerInfoVo getKfCustomerBatch(@JSONBody WeKfCustomerQuery query);

    /**
     * 获取配置的专员与客户群
     *
     * @param query in {@link WeBaseQuery}
     * @return {@link WeUpgradeServiceConfigVo}
     */
    @Get(url = "/kf/customer/get_upgrade_service_config")
    WeUpgradeServiceConfigVo getUpgradeServiceConfig(@JSONBody WeBaseQuery query);

    /**
     * 为客户升级为专员或客户群服务
     *
     * @param query in {@link WeUpgradeServiceQuery}
     * @return {@link WeResultVo}
     */
    @Post(url = "/kf/customer/upgrade_service")
    WeResultVo customerUpgradeService(@JSONBody WeUpgradeServiceQuery query);

    /**
     * 为客户取消推荐
     *
     * @param query in {@link WeUpgradeServiceQuery}
     * @return {@link WeResultVo}
     */
    @Post(url = "/kf/customer/cancel_upgrade_service")
    WeResultVo customerCancelUpgradeService(@JSONBody WeUpgradeServiceQuery query);


    /**
     * 发送欢迎语等事件响应消息
     *
     * @param query
     * @return
     */
    @Post(url = "/kf/send_msg_on_event")
    WeKfMsgVo sendMsgOnEvent(@JSONBody WeKfMsgQuery query);

    /**
     * 获取「客户数据统计」企业汇总数据
     *
     * @param query
     * @return
     */
    @Post(url = "/kf/get_corp_statistic")
    WeKfStatisticListVo getCorpStatistic(@JSONBody WeKfGetStatisticQuery query);

    /**
     * 获取「客户数据统计」接待人员明细数据
     *
     * @param query
     * @return
     */
    @Post(url = "/kf/get_servicer_statistic")
    WeKfStatisticListVo getServicerStatistic(@JSONBody WeKfGetStatisticQuery query);

    /**
     * 知识库新增分组
     * @param query
     * @return
     */
    @Post(url = "/kf/knowledge/add_group")
    WeKfAddKnowledgeGroupVo addKnowledgeGroup(@JSONBody WeKfAddKnowledgeGroupQuery query);


    /**
     * 知识库删除分组
     * @param query
     * @return
     */
    @Post(url = "/kf/knowledge/del_group")
    WeResultVo delKnowledgeGroup(@JSONBody WeKfKnowledgeGroupQuery query);

    /**
     * 知识库修改分组
     * @param query
     * @return
     */
    @Post(url = "/kf/knowledge/mod_group")
    WeResultVo modKnowledgeGroup(@JSONBody WeKfKnowledgeGroupQuery query);

    /**
     * 知识库分组列表
     * @param query
     * @return
     */
    @Post(url = "/kf/knowledge/list_group")
    WeKfKnowledgeGroupListVo getKnowledgeGroupList(@JSONBody WeKfKnowledgeGroupQuery query);
}
