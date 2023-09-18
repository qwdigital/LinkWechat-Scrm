package com.linkwechat.wecom.service;



import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Var;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.customer.UpdateCustomerRemarkQuery;
import com.linkwechat.domain.wecom.query.customer.WeBatchCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.WeCustomerListQuery;
import com.linkwechat.domain.wecom.query.customer.WeCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.*;
import com.linkwechat.domain.wecom.query.customer.link.WeLinkCustomerCountQuery;
import com.linkwechat.domain.wecom.query.customer.link.WeLinkCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.msg.*;
import com.linkwechat.domain.wecom.query.customer.state.WeGroupChatStatisticQuery;
import com.linkwechat.domain.wecom.query.customer.state.WeUserBehaviorDataQuery;
import com.linkwechat.domain.wecom.query.customer.tag.WeAddCorpTagQuery;
import com.linkwechat.domain.wecom.query.customer.tag.WeCorpTagListQuery;
import com.linkwechat.domain.wecom.query.customer.tag.WeMarkTagQuery;
import com.linkwechat.domain.wecom.query.customer.tag.WeUpdateCorpTagQuery;
import com.linkwechat.domain.wecom.query.customer.transfer.WeTransferCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.transfer.WeTransferGroupChatQuery;
import com.linkwechat.domain.wecom.query.groupmsg.WeGroupMsgQuery;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeContactWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.WeBatchCustomerDetailVo;
import com.linkwechat.domain.wecom.vo.customer.WeCustomerDetailVo;
import com.linkwechat.domain.wecom.vo.customer.WeCustomerListVo;
import com.linkwechat.domain.wecom.vo.customer.WeFollowUserListVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatAddJoinWayVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatDetailVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatGetJoinWayVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatListVo;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkCustomerAcquisitionQuotaVo;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkCustomerVo;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkWecustomerCountVo;
import com.linkwechat.domain.wecom.vo.customer.msg.WeAddCustomerMsgVo;
import com.linkwechat.domain.wecom.vo.customer.msg.WeGroupMsgListVo;
import com.linkwechat.domain.wecom.vo.customer.state.WeGroupChatStatisticVo;
import com.linkwechat.domain.wecom.vo.customer.state.WeUserBehaviorDataVo;
import com.linkwechat.domain.wecom.vo.customer.tag.WeCorpTagListVo;
import com.linkwechat.domain.wecom.vo.customer.tag.WeCorpTagVo;
import com.linkwechat.domain.wecom.vo.customer.transfer.WeTransferCustomerVo;
import com.linkwechat.domain.wecom.vo.goupmsg.WeGroupMsgTplVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.domain.wecom.vo.qr.WeContactWayListVo;
import com.linkwechat.domain.wecom.vo.qr.WeContactWayVo;

/**
 * @author danmo
 * @Description 客户业务接口
 * @date 2022/03/16 17:12
 **/
public interface IQwCustomerService {

    /**
     * 获取配置了客户联系功能的成员列表
     *
     * @param query code和corpid
     * @return
     */
    WeFollowUserListVo getFollowUserList(WeBaseQuery query);

    /**
     * 配置客户联系「联系我」方式
     *
     * @param query 入参
     * @return
     */
    WeAddWayVo addContactWay(WeAddWayQuery query);

    /**
     * 获取企业已配置的「联系我」方式
     *
     * @param query 入参
     * @return
     */
    WeContactWayVo getContactWay(WeContactWayQuery query);

    /**
     * 获取企业已配置的「联系我」列表
     *
     * @param query 入参
     * @return
     */
    WeContactWayListVo getContactWayList(WeContactWayQuery query);

    /**
     * 更新联系方式
     *
     * @param query 入参
     * @return
     */
    WeResultVo updateContactWay(WeAddWayQuery query);

    /**
     * 删除联系方式
     *
     * @param query 入参
     * @return
     */
    WeResultVo delContactWay(WeContactWayQuery query);

    /**
     * 获取客户群列表
     *
     * @param query
     * @return WeGroupChatListVo
     */
    WeGroupChatListVo getGroupChatList(WeGroupChatListQuery query);

    /**
     * 获取客户群详情
     *
     * @param query
     * @return WeGroupChatDetailVo
     */
    WeGroupChatDetailVo getGroupChatDetail(WeGroupChatDetailQuery query);

    /**
     * 获取企业标签库
     *
     * @param query
     * @return WeCorpTagListVo
     */
    WeCorpTagListVo getCorpTagList(WeCorpTagListQuery query);


    /**
     * 添加企业客户标签
     *
     * @param query
     * @return WeCorpTagVo
     */
    WeCorpTagVo addCorpTag(WeAddCorpTagQuery query);


    /**
     * 删除企业客户标签
     *
     * @param query
     * @return WeResultVo
     */
    WeResultVo delCorpTag(WeCorpTagListQuery query);

    /**
     * 创建企业群发
     *
     * @param query
     * @return
     */
    WeAddCustomerMsgVo addMsgTemplate(WeAddCustomerMsgQuery query);

    /**
     * 获取群发记录列表
     *
     * @param query
     * @return
     */
    WeGroupMsgListVo getGroupMsgList(WeGroupMsgListQuery query);

    /**
     * 获取群发成员发送任务列表
     *
     * @param query
     * @return
     */
    WeGroupMsgListVo getGroupMsgTask(WeGetGroupMsgListQuery query);

    /**
     * 获取企业群发成员执行结果
     *
     * @param query
     * @return
     */
    WeGroupMsgListVo getGroupMsgSendResult(WeGetGroupMsgListQuery query);

    /**
     * 停止企业群发
     *
     * @param query
     * @return
     */
    WeResultVo cancelGroupMsgSend(WeCancelGroupMsgSendQuery query);

    /**
     * 发送新客户欢迎语
     *
     * @param query
     * @return
     */
    WeResultVo sendWelcomeMsg(WeWelcomeMsgQuery query);

    /**
     * 编辑客户标签
     *
     * @param weMarkTagQuery
     * @return
     */
    WeResultVo makeCustomerLabel(WeMarkTagQuery weMarkTagQuery);


    /**
     * 分配在职成员的客户
     *
     * @param query
     * @return
     */
    WeTransferCustomerVo transferCustomer(WeTransferCustomerQuery query);


    /**
     * 查询客户接替状态
     *
     * @param query
     * @return
     */
    WeTransferCustomerVo transferResult(WeTransferCustomerQuery query);


    /**
     * 批量获取客户详情
     *
     * @param query
     * @return
     */
    WeBatchCustomerDetailVo getBatchCustomerDetail(WeBatchCustomerQuery query);

    /**
     * 获取客户详情
     *
     * @param query
     * @return
     */
    WeCustomerDetailVo getCustomerDetail(WeCustomerQuery query);

    /**
     * 联系客户统计
     *
     * @param query 入参
     * @return WeUserBehaviorDataVo
     */
    WeUserBehaviorDataVo getUserBehaviorData(WeUserBehaviorDataQuery query);

    /**
     * 群聊数据统计（按群主聚合的方式）
     *
     * @param query 入参
     * @return WeGroupChatStatisticVo
     */
    WeGroupChatStatisticVo getGroupChatStatistic(WeGroupChatStatisticQuery query);

    /**
     * 群聊数据统计（按日期聚合的方式）
     *
     * @param query
     * @return
     */
    WeGroupChatStatisticVo getGroupChatStatisticByDay(WeGroupChatStatisticQuery query);

    /**
     * 配置客户群进群方式
     *
     * @param joinWayQuery
     * @return
     */
    WeGroupChatAddJoinWayVo addJoinWayForGroupChat(WeGroupChatAddJoinWayQuery joinWayQuery);


    /**
     * 获取客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    WeGroupChatGetJoinWayVo getJoinWayForGroupChat(WeGroupChatJoinWayQuery joinWayQuery);


    /**
     * 删除客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    WeResultVo delJoinWayForGroupChat(WeGroupChatJoinWayQuery joinWayQuery);


    /**
     * 分配离职成员的客户
     *
     * @param query
     * @return
     */
    WeTransferCustomerVo resignedTransferCustomer(WeTransferCustomerQuery query);


    /**
     * 分配离职成员的客户群
     *
     * @param query
     * @return WeTransferCustomerVo
     */
    WeTransferCustomerVo transferGroupChat(WeTransferGroupChatQuery query);


    /**
     * 更新客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    WeResultVo updateJoinWayForGroupChat(WeGroupChatUpdateJoinWayQuery joinWayQuery);

    /**
     * 编辑标签或标签组名称
     *
     * @param query
     * @return
     */
    WeResultVo editCorpTag(WeUpdateCorpTagQuery query);




    /**
     * 添加入群欢迎语素材
     * @param query
     * @return
     */
    WeGroupMsgTplVo addWeGroupMsg(WeGroupMsgQuery query);


    /**
     * 编辑入群欢迎语素材
     * @param query
     * @return
     */
    WeResultVo updateWeGroupMsg(WeGroupMsgQuery query);



    /**
     * 编辑入群欢迎语素材
     * @param query
     * @return
     */
    WeResultVo delWeGroupMsg(WeGroupMsgQuery query);

    /**
     * 创建获客链接
     * @param query
     * @return
     */
    WeLinkCustomerVo createCustomerLink(WeLinkCustomerQuery query);




    /**
     * 编辑获客链接
     * @param query
     * @return
     */
    WeResultVo updateCustomerLink(WeLinkCustomerQuery query);




    /**
     * 删除获客链接
     * @param query
     * @return
     */
    WeResultVo deleteCustomerLink(WeLinkCustomerQuery query);


    /**
     * 获取由获客链接添加的客户信息
     * @param query
     * @return
     */
    WeLinkWecustomerCountVo customerLinkCount(WeLinkCustomerCountQuery query);


    /**
     * 企业可通过此接口查询当前剩余的使用量。
     * @return
     */
    WeLinkCustomerAcquisitionQuotaVo customerAcquisitionQuota(WeBaseQuery query);


    /**
     * 更新客户备注
     * @param query
     * @return
     */
    WeResultVo updateCustomerRemark(UpdateCustomerRemarkQuery query);


    /**
     * 根据员工id获取客户列表id
     * @param query
     * @return
     */
    WeCustomerListVo getCustomerList(WeCustomerListQuery query);
}
