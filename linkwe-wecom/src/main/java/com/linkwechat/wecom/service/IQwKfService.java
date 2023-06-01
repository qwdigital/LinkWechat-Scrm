package com.linkwechat.wecom.service;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.kf.*;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.kf.*;

/**
 * @author danmo
 * @version 1.0
 * @date 2022/04/16 21:10
 */
public interface IQwKfService {

    /**
     * 创建客服账号
     * @param query
     * @return
     */
    WeAddKfVo addAccount(WeKfAddQuery query);

    /**
     * 删除客服账号
     * @param query
     * @return
     */
    WeResultVo delAccount(WeKfQuery query);

    /**
     * 修改客服账号
     * @param query
     * @return
     */
    WeResultVo updateAccount(WeKfQuery query);

    /**
     * 获取客服账号列表
     * @param query
     * @return
     */
    WeKfListVo getAccountList(WeBaseQuery query);

    /**
     * 获取客服账号链接
     * @param query
     * @return
     */
    WeKfDetailVo addContactWay(WeKfQuery query);

    /**
     * 添加客服接待人员
     * @param query
     * @return
     */
    WeKfUserListVo addServicer(WeKfQuery query);

    /**
     * 删除客服接待人员
     * @param query
     * @return
     */
    WeKfUserListVo delServicer(WeKfQuery query);

    /**
     * 获取客服接待人员列表
     * @param query
     * @return
     */
    WeKfUserListVo getServicerList(WeKfQuery query);

    /**
     * 获取会话状态
     * @param query
     * @return
     */
    WeKfStateVo getSessionStatus(WeKfStateQuery query);

    /**
     * 变更会话状态
     * @param query
     * @return
     */
    WeKfStateVo updateSessionStatus(WeKfStateQuery query);

    /**
     * 发送会话消息
     * @param query
     * @return
     */
    WeKfMsgVo sendSessionMsg(WeKfMsgQuery query);

    /**
     * 读取会话消息
     * @param query
     * @return
     */
    WeKfGetMsgVo getSessionMsg(WeKfGetMsgQuery query);

    /**
     * 客户基本信息获取
     * @param query
     * @return
     */
    WeCustomerInfoVo getCustomerInfos(WeKfCustomerQuery query);

    /**
     * 获取配置的专员与客户群
     * @param query
     * @return
     */
    WeUpgradeServiceConfigVo getUpgradeServiceConfig(WeBaseQuery query);

    /**
     * 为客户升级为专员或客户群服务
     * @param query
     * @return
     */
    WeResultVo customerUpgradeService(WeUpgradeServiceQuery query);

    /**
     * 为客户取消推荐
     * @param query
     * @return
     */
    WeResultVo cancelUpgradeService(WeUpgradeServiceQuery query);

    /**
     * 发送欢迎语等事件响应消息
     * @param query
     * @return
     */
    WeKfMsgVo sendMsgOnEvent(WeKfMsgQuery query);

    /**
     * 获取「客户数据统计」企业汇总数据
     * @param query
     * @return
     */
    WeKfStatisticListVo getCorpStatistic(WeKfGetStatisticQuery query);

    /**
     * 获取「客户数据统计」接待人员明细数据
     * @param query
     * @return
     */
    WeKfStatisticListVo getServicerStatistic(WeKfGetStatisticQuery query);

    /**
     * 知识库新增分组
     * @param query
     * @return
     */
    WeKfAddKnowledgeGroupVo addKnowledgeGroup(WeKfAddKnowledgeGroupQuery query);

    /**
     * 知识库删除分组
     * @param query
     * @return
     */
    WeResultVo delKnowledgeGroup(WeKfKnowledgeGroupQuery query);

    /**
     * 知识库修改分组
     * @param query
     * @return
     */
    WeResultVo modKnowledgeGroup(WeKfKnowledgeGroupQuery query);

    /**
     * 知识库分组列表
     * @param query
     * @return
     */
    WeKfKnowledgeGroupListVo getKnowledgeGroupList(WeKfKnowledgeGroupQuery query);
}
