package com.linkwechat.wecom.client;

import com.dtflys.forest.annotation.*;
import com.linkwechat.domain.wecom.query.WeBaseQuery;
import com.linkwechat.domain.wecom.query.customer.UpdateCustomerRemarkQuery;
import com.linkwechat.domain.wecom.query.customer.WeBatchCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.WeCustomerListQuery;
import com.linkwechat.domain.wecom.query.customer.WeCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.groupchat.*;
import com.linkwechat.domain.wecom.query.customer.link.WeLinkCustomerCountQuery;
import com.linkwechat.domain.wecom.query.customer.link.WeLinkCustomerListsQuery;
import com.linkwechat.domain.wecom.query.customer.link.WeLinkCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.moment.WeAddMomentQuery;
import com.linkwechat.domain.wecom.query.customer.moment.WeMomentQuery;
import com.linkwechat.domain.wecom.query.customer.moment.WeMomentResultQuery;
import com.linkwechat.domain.wecom.query.customer.msg.*;
import com.linkwechat.domain.wecom.query.customer.state.WeGroupChatStatisticQuery;
import com.linkwechat.domain.wecom.query.customer.state.WeUserBehaviorDataQuery;
import com.linkwechat.domain.wecom.query.customer.strategy.WeAddCustomerStrategyQuery;
import com.linkwechat.domain.wecom.query.customer.strategy.WeCustomerStrategyQuery;
import com.linkwechat.domain.wecom.query.customer.strategy.WeUpdateCustomerStrategyQuery;
import com.linkwechat.domain.wecom.query.customer.tag.WeAddCorpTagQuery;
import com.linkwechat.domain.wecom.query.customer.tag.WeCorpTagListQuery;
import com.linkwechat.domain.wecom.query.customer.tag.WeMarkTagQuery;
import com.linkwechat.domain.wecom.query.customer.tag.WeUpdateCorpTagQuery;
import com.linkwechat.domain.wecom.query.customer.transfer.WeTransferCustomerQuery;
import com.linkwechat.domain.wecom.query.customer.transfer.WeTransferGroupChatQuery;
import com.linkwechat.domain.wecom.query.customer.transfer.WeUnassignedQuery;
import com.linkwechat.domain.wecom.query.product.QwAddProductQuery;
import com.linkwechat.domain.wecom.query.product.QwProductListQuery;
import com.linkwechat.domain.wecom.query.product.QwProductQuery;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeContactWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.customer.WeBatchCustomerDetailVo;
import com.linkwechat.domain.wecom.vo.customer.WeCustomerDetailVo;
import com.linkwechat.domain.wecom.vo.customer.WeCustomerListVo;
import com.linkwechat.domain.wecom.vo.customer.WeFollowUserListVo;
import com.linkwechat.domain.wecom.vo.customer.groupchat.*;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkCustomerAcquisitionQuotaVo;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkCustomerListsVo;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkCustomerVo;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkWecustomerCountVo;
import com.linkwechat.domain.wecom.vo.customer.moment.*;
import com.linkwechat.domain.wecom.vo.customer.msg.WeAddCustomerMsgVo;
import com.linkwechat.domain.wecom.vo.customer.msg.WeGroupMsgListVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwAddProductVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductListVo;
import com.linkwechat.domain.wecom.vo.customer.product.QwProductVo;
import com.linkwechat.domain.wecom.vo.customer.state.WeGroupChatStatisticVo;
import com.linkwechat.domain.wecom.vo.customer.state.WeUserBehaviorDataVo;
import com.linkwechat.domain.wecom.vo.customer.strategy.WeCustomerStrategyDetailVo;
import com.linkwechat.domain.wecom.vo.customer.strategy.WeCustomerStrategyListVo;
import com.linkwechat.domain.wecom.vo.customer.strategy.WeCustomerStrategyRangeVo;
import com.linkwechat.domain.wecom.vo.customer.strategy.WeCustomerStrategyVo;
import com.linkwechat.domain.wecom.vo.customer.tag.WeCorpTagListVo;
import com.linkwechat.domain.wecom.vo.customer.tag.WeCorpTagVo;
import com.linkwechat.domain.wecom.vo.customer.transfer.WeTransferCustomerVo;
import com.linkwechat.domain.wecom.vo.customer.transfer.WeUnassignedVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.domain.wecom.vo.qr.WeContactWayListVo;
import com.linkwechat.domain.wecom.vo.qr.WeContactWayVo;
import com.linkwechat.wecom.interceptor.WeAccessTokenInterceptor;
import com.linkwechat.wecom.interceptor.WeContactTokenInterceptor;
import com.linkwechat.wecom.retry.WeCommonRetryWhen;

/**
 * @author danmo
 * @Description 客户接口
 * @date 2021/12/2 16:46
 **/

@BaseRequest(baseURL = "${weComServerUrl}", interceptor = WeAccessTokenInterceptor.class)
@Retry(maxRetryCount = "3", maxRetryInterval = "1000", condition = WeCommonRetryWhen.class)
public interface WeCustomerClient {

    /**
     * 获取配置了客户联系功能的成员列表
     *
     * @param query
     * @return WeFollowUserListVo
     */
    @Request(url = "/externalcontact/get_follow_user_list", type = "GET")
    WeFollowUserListVo getFollowUserList(@JSONBody WeBaseQuery query);

    /**
     * 配置客户联系「联系我」方式
     *
     * @param query
     * @return WeAddWayVo
     */
    @Request(url = "/externalcontact/add_contact_way", type = "POST")
    WeAddWayVo addContactWay(@JSONBody WeAddWayQuery query);

    /**
     * 获取企业已配置的「联系我」方式
     *
     * @param query
     * @return WeContactWayVo
     */
    @Request(url = "/externalcontact/get_contact_way", type = "POST")
    WeContactWayVo getContactWay(@JSONBody WeContactWayQuery query);


    /**
     * 获取企业已配置的「联系我」列表
     *
     * @param query
     * @return WeContactWayListVo
     */
    @Request(url = "/externalcontact/list_contact_way", type = "POST")
    WeContactWayListVo getContactWayList(@JSONBody WeContactWayQuery query);

    /**
     * 更新联系方式
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/externalcontact/update_contact_way", type = "POST")
    WeResultVo updateContactWay(@JSONBody WeAddWayQuery query);

    /**
     * 删除联系方式
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/externalcontact/del_contact_way", type = "POST")
    WeResultVo delContactWay(@JSONBody WeContactWayQuery query);


    /**
     * 获取客户列表
     *
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/list?userid=${query.userid}", type = "GET")
    WeCustomerListVo getCustomerList(@Var("query") WeCustomerListQuery query);

    /**
     * 根据客户id获取客户详情
     *
     * @param query
     * @return WeCustomerDetailVo
     */
    @Get(url = "/externalcontact/get?external_userid=${query.external_userid}")
    WeCustomerDetailVo getCustomerDetail(@Var("query") WeCustomerQuery query);

    /**
     * 批量获取客户详情
     *
     * @param query
     * @return WeCustomerDetailVo
     */
    @Request(url = "/externalcontact/batch/get_by_user", type = "POST")
    WeBatchCustomerDetailVo getBatchCustomerDetail(@JSONBody WeBatchCustomerQuery query);

    /**
     * 修改客户备注信息
     *
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/remark", type = "POST")
    WeResultVo updateCustomerRemark(@JSONBody UpdateCustomerRemarkQuery query);


    /**
     * 获取规则组列表
     *
     * @param query
     * @return WeCustomerStrategyListVo
     */
    @Request(url = "/externalcontact/customer_strategy/list", type = "POST")
    WeCustomerStrategyListVo getCustomerStrategyList(@JSONBody WeCustomerStrategyQuery query);

    /**
     * 获取规则组详情
     *
     * @param query
     * @return WeCustomerStrategyDetailVo
     */
    @Request(url = "/externalcontact/customer_strategy/get", type = "POST")
    WeCustomerStrategyDetailVo getCustomerStrategyDetail(@JSONBody WeCustomerStrategyQuery query);

    /**
     * 获取规则组管理范围
     *
     * @param query
     * @return WeCustomerStrategyRangeVo
     */
    @Request(url = "/externalcontact/customer_strategy/get_range", type = "POST")
    WeCustomerStrategyRangeVo getCustomerStrategyRange(@JSONBody WeCustomerStrategyQuery query);

    /**
     * 创建新的规则组
     *
     * @param query
     * @return WeCustomerStrategyVo
     */
    @Request(url = "/externalcontact/customer_strategy/create", type = "POST")
    WeCustomerStrategyVo addCustomerStrategy(@JSONBody WeAddCustomerStrategyQuery query);

    /**
     * 编辑规则组及其管理范围
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/externalcontact/customer_strategy/edit", type = "POST")
    WeResultVo updateCustomerStrategy(@JSONBody WeUpdateCustomerStrategyQuery query);

    /**
     * 删除规则组
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/externalcontact/customer_strategy/del", type = "POST")
    WeResultVo delCustomerStrategy(@JSONBody WeCustomerStrategyQuery query);

    /**
     * 获取企业标签库
     * @param query
     * @return WeCorpTagListVo
     */
    @Request(url = "/externalcontact/get_corp_tag_list", type = "POST" ,interceptor =WeContactTokenInterceptor.class)
    WeCorpTagListVo getCorpTagList(@JSONBody WeCorpTagListQuery query);

    /**
     * 添加企业客户标签
     *
     * @param query
     * @return WeCorpTagVo
     */
    @Request(url = "/externalcontact/add_corp_tag", type = "POST" ,interceptor =WeContactTokenInterceptor.class)
    WeCorpTagVo addCorpTag(@JSONBody WeAddCorpTagQuery query);


    /**
     * 编辑企业客户标签
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/externalcontact/edit_corp_tag", type = "POST" ,interceptor =WeContactTokenInterceptor.class)
    WeResultVo editCorpTag(@JSONBody WeUpdateCorpTagQuery query);


    /**
     * 删除企业客户标签
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/externalcontact/del_corp_tag", type = "POST" ,interceptor =WeContactTokenInterceptor.class)
    WeResultVo delCorpTag(@JSONBody WeCorpTagListQuery query);

    /**
     * 编辑客户企业标签
     *
     * @param query
     * @return WeResultVo
     */
    @Request(url = "/externalcontact/mark_tag", type = "POST" ,interceptor =WeContactTokenInterceptor.class)
    WeResultVo markCustomerTag(@JSONBody WeMarkTagQuery query);

    /**
     * 分配在职成员的客户
     *
     * @param query
     * @return WeTransferCustomerVo
     */
    @Request(url = "/externalcontact/transfer_customer", type = "POST")
    WeTransferCustomerVo transferCustomer(@JSONBody WeTransferCustomerQuery query);

    /**
     * 查询客户接替状态
     *
     * @param query
     * @return WeTransferCustomerVo
     */
    @Request(url = "/externalcontact/transfer_result", type = "POST")
    WeTransferCustomerVo transferResult(@JSONBody WeTransferCustomerQuery query);

    /**
     * 获取待分配的离职成员列表
     *
     * @param query
     * @return WeUnassignedVo
     */
    @Request(url = "/externalcontact/get_unassigned_list", type = "POST")
    WeUnassignedVo getUnassignedList(@JSONBody WeUnassignedQuery query);

    /**
     * 分配离职成员的客户
     *
     * @param query
     * @return WeTransferCustomerVo
     */
    @Request(url = "/externalcontact/resigned/transfer_customer", type = "POST")
    WeTransferCustomerVo resignedTransferCustomer(@JSONBody WeTransferCustomerQuery query);

    /**
     * 查询离职成员客户接替状态
     *
     * @param query
     * @return WeTransferCustomerVo
     */
    @Request(url = "/externalcontact/resigned/transfer_result", type = "POST")
    WeTransferCustomerVo resignedTransferResult(@JSONBody WeTransferCustomerQuery query);

    /**
     * 分配离职成员的客户群
     *
     * @param query
     * @return WeTransferCustomerVo
     */
    @Request(url = "/externalcontact/groupchat/transfer", type = "POST")
    WeTransferCustomerVo transferGroupChat(@JSONBody WeTransferGroupChatQuery query);

    /**
     * 获取客户群列表
     *
     * @param query
     * @return WeGroupChatListVo
     */
    @Request(url = "/externalcontact/groupchat/list", type = "POST",interceptor= WeContactTokenInterceptor.class)
    WeGroupChatListVo getGroupChatList(@JSONBody WeGroupChatListQuery query);

    /**
     * 获取客户群详情
     *
     * @param query
     * @return WeGroupChatDetailVo
     */
    @Request(url = "/externalcontact/groupchat/get", type = "POST",interceptor= WeContactTokenInterceptor.class)
    WeGroupChatDetailVo getGroupChatDetail(@JSONBody WeGroupChatDetailQuery query);

    /**
     * 客户群opengid转换
     *
     * @param query
     * @return WeGroupChatVo
     */
    @Request(url = "/externalcontact/opengid_to_chatid", type = "POST")
    WeGroupChatVo opengIdToChatId(@JSONBody WeGroupOpenGidQuery query);


    /**
     * 创建客户朋友圈的发表任务
     *
     * @param query
     * @return WeAddMomentVo
     */
    @Request(url = "/externalcontact/add_moment_task", type = "POST")
    WeAddMomentVo addMomentTask(@JSONBody WeAddMomentQuery query);

    /**
     * 获取任务创建结果
     *
     * @param query
     * @return WeAddMomentVo
     */
    @Request(url = "/externalcontact/get_moment_task_result?jobid=${query.jobid}", type = "GET")
    WeMomentResultVo getMomentTaskResult(@Var("query") WeMomentResultQuery query);


    /**
     * 获取企业全部的发表列表
     *
     * @param query
     * @return WeMomentListVo
     */
    @Request(url = "/externalcontact/get_moment_list", type = "POST")
    WeMomentListVo getMomentList(@JSONBody WeMomentQuery query);

    /**
     * 获取客户朋友圈企业发表的列表
     *
     * @param query
     * @return WeMomentTaskVo
     */
    @Request(url = "/externalcontact/get_moment_task", type = "POST")
    WeMomentTaskVo getMomentTask(@JSONBody WeMomentQuery query);

    /**
     * 获取客户朋友圈发表时选择的可见范围
     *
     * @param query
     * @return WeMomentCustomerListVo
     */
    @Request(url = "/externalcontact/get_moment_customer_list", type = "POST")
    WeMomentCustomerListVo getMomentCustomerList(@JSONBody WeMomentQuery query);

    /**
     * 获取客户朋友圈发表时选择的可见范围
     *
     * @param query
     * @return WeMomentCustomerListVo
     */
    @Request(url = "/externalcontact/get_moment_send_result", type = "POST")
    WeMomentCustomerListVo getMomentSendResult(@JSONBody WeMomentQuery query);

    /**
     * 获取客户朋友圈的互动数据
     *
     * @param query
     * @return WeMomentCommentListVo
     */
    @Request(url = "/externalcontact/get_moment_comments", type = "POST")
    WeMomentCommentListVo getMomentComments(@JSONBody WeMomentQuery query);

    /**
     * 创建企业群发
     *
     * @param query
     * @return WeAddCustomerMsgVo
     */
    @Request(url = "/externalcontact/add_msg_template", type = "POST")
    WeAddCustomerMsgVo addMsgTemplate(@JSONBody WeAddCustomerMsgQuery query);

    /**
     * 获取群发记录列表
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @Request(url = "/externalcontact/get_groupmsg_list_v2", type = "POST")
    WeGroupMsgListVo getGroupMsgList(@JSONBody WeGroupMsgListQuery query);

    /**
     * 获取群发成员发送任务列表
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @Request(url = "/externalcontact/get_groupmsg_task", type = "POST")
    WeGroupMsgListVo getGroupMsgTask(@JSONBody WeGetGroupMsgListQuery query);


    /**
     * 获取企业群发成员执行结果
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @Request(url = "/externalcontact/get_groupmsg_send_result", type = "POST")
    WeGroupMsgListVo getGroupMsgSendResult(@JSONBody WeGetGroupMsgListQuery query);

    /**
     * 停止企业群发
     *
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/cancel_groupmsg_send", type = "POST")
    WeResultVo cancelGroupMsgSend(@JSONBody WeCancelGroupMsgSendQuery query);


    /**
     * 发送新客户欢迎语
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @Request(url = "/externalcontact/send_welcome_msg", type = "POST")
    WeResultVo sendWelcomeMsg(@JSONBody WeWelcomeMsgQuery query);

    /**
     * 联系客户统计
     *
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/get_user_behavior_data", type = "POST")
    WeUserBehaviorDataVo getUserBehaviorData(@JSONBody WeUserBehaviorDataQuery query);

    /**
     * 群聊数据统计（按群主聚合的方式）
     *
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/groupchat/statistic", type = "POST")
    WeGroupChatStatisticVo getGroupChatStatistic(@JSONBody WeGroupChatStatisticQuery query);

    /**
     * 群聊数据统计(按自然日聚合的方式)
     *
     * @param query
     * @return
     */
    @Request(url = "/externalcontact/groupchat/statistic_group_by_day", type = "POST")
    WeGroupChatStatisticVo getGroupChatStatisticGroupByDay(@JSONBody WeGroupChatStatisticQuery query);

    /**
     * 编辑客户标签
     *
     * @return
     */
    @Request(url = "/externalcontact/mark_tag",
            type = "POST"
    )
    WeResultVo makeCustomerLabel(@JSONBody WeMarkTagQuery weMarkTagQuery);


    /**
     * 配置客户群进群方式
     *
     * @param joinWayQuery
     * @return
     */
    @Post("/externalcontact/groupchat/add_join_way")
    WeGroupChatAddJoinWayVo addJoinWayForGroupChat(@JSONBody WeGroupChatAddJoinWayQuery joinWayQuery);


    /**
     * 获取客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    @Post("/externalcontact/groupchat/get_join_way")
    WeGroupChatGetJoinWayVo getJoinWayForGroupChat(@JSONBody WeGroupChatJoinWayQuery joinWayQuery);


    /**
     * 删除客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    @Post("/externalcontact/groupchat/del_join_way")
    WeResultVo delJoinWayForGroupChat(@JSONBody WeGroupChatJoinWayQuery joinWayQuery);


    /**
     * 更新客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    @Post("/externalcontact/groupchat/update_join_way")
    WeResultVo updateJoinWayForGroupChat(@JSONBody WeGroupChatUpdateJoinWayQuery joinWayQuery);

    /**
     * 创建商品图册
     *
     * @param query
     * @return
     */
    @Post("/externalcontact/add_product_album")
    QwAddProductVo addProductAlbum(@JSONBody QwAddProductQuery query);

    /**
     * 编辑商品图册
     *
     * @param query
     * @return
     */
    @Post("/externalcontact/update_product_album")
    WeResultVo updateProductAlbum(@JSONBody QwAddProductQuery query);

    /**
     * 删除商品图册
     *
     * @param query
     * @return
     */
    @Post("/externalcontact/delete_product_album")
    WeResultVo delProductAlbum(@JSONBody QwProductQuery query);

    /**
     * 获取商品图册
     *
     * @param query
     * @return
     */
    @Post("/externalcontact/get_product_album")
    QwProductVo getProductAlbum(@JSONBody QwProductQuery query);

    /**
     * 获取商品图册列表
     *
     * @param query
     * @return
     */
    @Post("/externalcontact/get_product_album_list")
    QwProductListVo getProductAlbumList(@JSONBody QwProductListQuery query);


    /**
     * 创建获客链接
     * @param query
     * @return
     */
    @Post(url="/externalcontact/customer_acquisition/create_link")
    WeLinkCustomerVo createCustomerLink(@JSONBody WeLinkCustomerQuery query);




    /**
     * 编辑获客链接
     * @param query
     * @return
     */
    @Post(url="/externalcontact/customer_acquisition/update_link")
    WeResultVo updateCustomerLink(@JSONBody WeLinkCustomerQuery query);




    /**
     * 删除获客链接
     * @param query
     * @return
     */
    @Post(url="/externalcontact/customer_acquisition/delete_link")
    WeResultVo deleteCustomerLink(@JSONBody WeLinkCustomerQuery query);


    /**
     * 获取获客列表
     * @return
     */
    @Post(url="/externalcontact/customer_acquisition/customer")
    WeLinkWecustomerCountVo customerLinkCount(@JSONBody WeLinkCustomerCountQuery query);


    /**
     * 企业可通过此接口查询当前剩余的使用量。
     * @return
     */
    @Get(url="/externalcontact/customer_acquisition_quota")
    WeLinkCustomerAcquisitionQuotaVo customerAcquisitionQuota(@JSONBody WeBaseQuery query);


    /**
     * 获取获客链接列表
     * @param customerListsQuery
     * @return
     */
    @Post(url = "/externalcontact/customer_acquisition/list_link")
    WeLinkCustomerListsVo listCustomerLink(@JSONBody WeLinkCustomerListsQuery customerListsQuery);





}
