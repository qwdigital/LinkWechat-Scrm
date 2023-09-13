package com.linkwechat.fegin;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.customer.*;
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
import com.linkwechat.domain.wecom.vo.customer.*;
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
import com.linkwechat.fallback.QwCustomerFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author danmo
 * @description 企微客户接口
 * @date 2022/3/13 20:54
 **/
@FeignClient(value = "${wecom.serve.linkwe-wecom}", fallback = QwCustomerFallbackFactory.class)
public interface QwCustomerClient {

    /**
     * 获取配置了客户联系功能的成员列表
     *
     * @param query corpid
     * @return
     */
    @PostMapping("/customer/getFollowerList")
    public AjaxResult<WeFollowUserListVo> getFollowUserList(@RequestBody WeBaseQuery query);

    /**
     * 配置客户联系「联系我」方式
     *
     * @param query 入参
     * @return
     */
    @PostMapping("/customer/addContactWay")
    public AjaxResult<WeAddWayVo> addContactWay(@RequestBody WeAddWayQuery query);

    /**
     * 获取企业已配置的「联系我」方式
     *
     * @param query 入参
     * @return
     */
    @PostMapping("/customer/getContactWay")
    public AjaxResult<WeContactWayVo> getContactWay(@RequestBody WeContactWayQuery query);

    /**
     * 获取企业已配置的「联系我」列表
     *
     * @param query 入参
     * @return
     */
    @PostMapping("/customer/getContactWayList")
    public AjaxResult<WeContactWayListVo> getContactWayList(@RequestBody WeContactWayQuery query);

    /**
     * 更新联系方式
     *
     * @param query 入参
     * @return
     */
    @PostMapping("/customer/updateContactWay")
    public AjaxResult<WeResultVo> updateContactWay(@RequestBody WeAddWayQuery query);

    /**
     * 删除联系方式
     *
     * @param query 入参
     * @return
     */
    @PostMapping("/customer/delContactWay")
    public AjaxResult<WeResultVo> delContactWay(@RequestBody WeContactWayQuery query);


    /**
     * 获取客户群列表
     *
     * @param query
     * @return WeGroupChatListVo
     */
    @PostMapping("/customer/groupchat/list")
    public AjaxResult<WeGroupChatListVo> getGroupChatList(@RequestBody WeGroupChatListQuery query);

    /**
     * 获取客户群详情
     *
     * @param query
     * @return WeGroupChatDetailVo
     */
    @PostMapping("/customer/groupchat/get")
    public AjaxResult<WeGroupChatDetailVo> getGroupChatDetail(@RequestBody WeGroupChatDetailQuery query);


    /**
     * 获取企业标签库
     *
     * @param query
     * @return
     */
    @GetMapping("/customer/getCorpTagList")
    public AjaxResult<WeCorpTagListVo> getCorpTagList(@SpringQueryMap WeCorpTagListQuery query);

    /**
     * 添加企业客户标签
     *
     * @param query
     * @return
     */
    @PostMapping("/customer/addCorpTag")
    public AjaxResult<WeCorpTagVo> addCorpTag(@RequestBody WeAddCorpTagQuery query);


    /**
     * 删除企业客户标签
     *
     * @param query
     * @return
     */
    @DeleteMapping("/customer/delCorpTag")
    public AjaxResult<WeResultVo> delCorpTag(@RequestBody WeCorpTagListQuery query);


    /**
     * 编辑客户标签
     *
     * @param weMarkTagQuery
     * @return
     */
    @PostMapping("/customer/makeCustomerLabel")
    public AjaxResult makeCustomerLabel(@RequestBody WeMarkTagQuery weMarkTagQuery);

    /**
     * 分配在职成员的客户
     *
     * @param query
     * @return
     */
    @PostMapping("/customer/transferCustomer")
    public AjaxResult<WeTransferCustomerVo> transferCustomer(@RequestBody WeTransferCustomerQuery query);


    /**
     * 查询客户接替状态
     *
     * @param query
     * @return
     */
    @PostMapping("/customer/transferResult")
    public AjaxResult<WeTransferCustomerVo> transferResult(@RequestBody WeTransferCustomerQuery query);

    /**
     * 获取客户详情
     *
     * @param query
     * @return
     */
    @PostMapping("/customer/getCustomerDetail")
    public AjaxResult<WeCustomerDetailVo> getCustomerDetail(@RequestBody WeCustomerQuery query);

    /**
     * 批量获取客户详情
     *
     * @param query
     * @return
     */
    @PostMapping("/customer/getBatchCustomerDetail")
    public AjaxResult<WeBatchCustomerDetailVo> getBatchCustomerDetail(@RequestBody WeBatchCustomerQuery query);


    /**
     * 创建企业群发
     *
     * @param query
     * @return WeAddCustomerMsgVo
     */
    @PostMapping("/customer/group/msg/add")
    public AjaxResult<WeAddCustomerMsgVo> addMsgTemplate(@RequestBody WeAddCustomerMsgQuery query);

    /**
     * 获取群发记录列表
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @PostMapping("/customer/group/msg/getList")
    public AjaxResult<WeGroupMsgListVo> getGroupMsgList(@RequestBody WeGroupMsgListQuery query);

    /**
     * 获取群发成员发送任务列表QwCustomerServiceImpl
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @PostMapping("/customer/group/msg/getTask")
    public AjaxResult<WeGroupMsgListVo> getGroupMsgTask(@RequestBody WeGetGroupMsgListQuery query);

    /**
     * 获取企业群发成员执行结果
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @PostMapping("/customer/group/msg/getSendResult")
    public AjaxResult<WeGroupMsgListVo> getGroupMsgSendResult(@RequestBody WeGetGroupMsgListQuery query);

    /**
     * 停止企业群发
     * @param query
     * @return
     */
    @PostMapping("/customer/group/msg/cancelGroupMsgSend")
    public AjaxResult<WeResultVo> cancelGroupMsgSend(@RequestBody WeCancelGroupMsgSendQuery query);

    /**
     * 发送新客户欢迎语
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @PostMapping("/customer/sendWelcomeMsg")
    public AjaxResult<WeResultVo> sendWelcomeMsg(@RequestBody WeWelcomeMsgQuery query);

    /**
     * 联系客户统计
     *
     * @param query
     * @return WeUserBehaviorDataVo
     */
    @PostMapping("/customer/getUserBehaviorData")
    public AjaxResult<WeUserBehaviorDataVo> getUserBehaviorData(@RequestBody WeUserBehaviorDataQuery query);


    /**
     * 群聊数据统计（按群主聚合的方式）
     *
     * @param query
     * @return WeGroupChatStatisticVo
     */
    @PostMapping("/customer/getGroupChatStatistic")
    public AjaxResult<WeGroupChatStatisticVo> getGroupChatStatistic(@RequestBody WeGroupChatStatisticQuery query);

    /**
     * 群聊数据统计（按日期聚合的方式）
     *
     * @param query
     * @return WeGroupChatStatisticVo
     */
    @PostMapping("/customer/getGroupChatStatisticByDay")
    public AjaxResult<WeGroupChatStatisticVo> getGroupChatStatisticByDay(@RequestBody WeGroupChatStatisticQuery query);

    /**
     * 配置客户群进群方式
     *
     * @param joinWayQuery
     * @return
     */
    @PostMapping("/customer/addJoinWayForGroupChat")
    public AjaxResult<WeGroupChatAddJoinWayVo> addJoinWayForGroupChat(@RequestBody WeGroupChatAddJoinWayQuery joinWayQuery);


    /**
     * 获取客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    @GetMapping("/customer/getJoinWayForGroupChat")
    public AjaxResult<WeGroupChatGetJoinWayVo> getJoinWayForGroupChat(@SpringQueryMap WeGroupChatJoinWayQuery joinWayQuery);

    /**
     * 删除客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    @PostMapping("/customer/delJoinWayForGroupChat")
    public AjaxResult<WeResultVo> delJoinWayForGroupChat(@RequestBody WeGroupChatJoinWayQuery joinWayQuery);

    /**
     * 分配离职成员的客户
     *
     * @param query
     * @return
     */
    @PostMapping("/customer/resignedTransferCustomer")
    public AjaxResult<WeTransferCustomerVo> resignedTransferCustomer(@RequestBody WeTransferCustomerQuery query);

    /**
     * 分配离职成员的客户群
     *
     * @param query
     * @return
     */
    @PostMapping("/customer/transferGroupChat")
    public AjaxResult<WeTransferCustomerVo> transferGroupChat(@RequestBody WeTransferGroupChatQuery query);


    /**
     * 更新客户群进群方式配置
     *
     * @param query
     * @return
     */
    @PostMapping("/customer/updateJoinWayForGroupChat")
    public AjaxResult<WeResultVo> updateJoinWayForGroupChat(@RequestBody WeGroupChatUpdateJoinWayQuery query);


    @PostMapping("/customer/unionIdToExternalUserId3rd")
    public AjaxResult<UnionidToExternalUserIdVo> unionIdToExternalUserId3rd(@RequestBody UnionidToExternalUserIdQuery query);

    /**
     * 编辑标签或标签组名称
     *
     * @param query
     * @return
     */
    @PostMapping("/customer/editCorpTag")
    public AjaxResult<WeResultVo> editCorpTag(@RequestBody WeUpdateCorpTagQuery query);



    /**
     * 添加入群欢迎语素材
     * @param query
     * @return
     */
    @PostMapping("/customer/addWeGroupMsg")
    public AjaxResult<WeGroupMsgTplVo>  addWeGroupMsg(@RequestBody WeGroupMsgQuery query);

    /**
     * 编辑入群欢迎语素材
     * @param query
     * @return
     */
    @PostMapping("/customer/updateWeGroupMsg")
    public AjaxResult<WeResultVo>  updateWeGroupMsg(@RequestBody WeGroupMsgQuery query);


    /**
     *  删除入群欢迎语素材
     * @param query
     * @return
     */
    @PostMapping("/customer/delWeGroupMsg")
    public AjaxResult<WeResultVo> delWeGroupMsg(@RequestBody WeGroupMsgQuery query);



    /**
     * 创建获客链接
     * @param query
     * @return
     */
    @PostMapping("/customer/createCustomerLink")
    AjaxResult<WeLinkCustomerVo> createCustomerLink(@RequestBody WeLinkCustomerQuery query);


    /**
     * 更新获客链接
     * @param query
     * @return
     */
    @PostMapping("/customer/updateCustomerLink")
    AjaxResult<WeLinkCustomerVo> updateCustomerLink(@RequestBody WeLinkCustomerQuery query);



    /**
     * 删除获客链接
     * @param query
     * @return
     */
    @PostMapping("/customer/deleteCustomerLink")
    AjaxResult<WeResultVo>  deleteCustomerLink(@RequestBody WeLinkCustomerQuery query);


    /**
     * 获客助手查询剩余使用量
     * @param weBaseQuery
     * @return
     */
    @PostMapping("/customer/customerAcquisitionQuota")
    public AjaxResult<WeLinkCustomerAcquisitionQuotaVo> customerAcquisitionQuota(@RequestBody WeBaseQuery weBaseQuery);



    /**
     * 获取由获客链接添加的客户信息
     * @param query
     * @return
     */
    @PostMapping("/customer/customerLinkCount")
    public AjaxResult<WeLinkWecustomerCountVo> customerLinkCount(@RequestBody WeLinkCustomerCountQuery query);


    /**
     * 更新客户备注
     * @param query
     * @return
     */
    @PostMapping("/customer/updateCustomerRemark")
    public AjaxResult<WeResultVo> updateCustomerRemark(@RequestBody UpdateCustomerRemarkQuery query);


    /**
     * 获取客户列表
     * @param query
     * @return
     */
    @PostMapping("/customer/getCustomerList")
    public AjaxResult<WeCustomerListVo> getCustomerList(@RequestBody WeCustomerListQuery query);
}
