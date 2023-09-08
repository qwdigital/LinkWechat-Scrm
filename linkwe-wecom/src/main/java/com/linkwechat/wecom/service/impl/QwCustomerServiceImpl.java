package com.linkwechat.wecom.service.impl;

import com.linkwechat.common.utils.StringUtils;
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
import com.linkwechat.wecom.client.WeCustomerClient;
import com.linkwechat.wecom.client.WeGroupWelcomeTplClient;
import com.linkwechat.wecom.service.IQwCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 客户业务实现类
 * @date 2022/3/25 13:47
 **/
@Slf4j
@Service
public class QwCustomerServiceImpl implements IQwCustomerService {

    @Autowired
    private WeCustomerClient weCustomerClient;

    @Autowired
    private WeGroupWelcomeTplClient weGroupWelcomeTplClient;


    @Override
    public WeFollowUserListVo getFollowUserList(WeBaseQuery query) {
        return weCustomerClient.getFollowUserList(query);
    }

    @Override
    public WeAddWayVo addContactWay(WeAddWayQuery query) {
        return weCustomerClient.addContactWay(query);
    }

    @Override
    public WeContactWayVo getContactWay(WeContactWayQuery query) {
        return weCustomerClient.getContactWay(query);
    }

    @Override
    public WeContactWayListVo getContactWayList(WeContactWayQuery query) {
        return weCustomerClient.getContactWayList(query);
    }

    @Override
    public WeResultVo updateContactWay(WeAddWayQuery query) {
        return weCustomerClient.updateContactWay(query);
    }

    @Override
    public WeResultVo delContactWay(WeContactWayQuery query) {
        return weCustomerClient.delContactWay(query);
    }

    @Override
    public WeGroupChatListVo getGroupChatList(WeGroupChatListQuery query) {
        WeGroupChatListVo groupChatList = weCustomerClient.getGroupChatList(query);
        if (groupChatList != null && groupChatList.getNextCursor() != null) {
            query.setCursor(groupChatList.getNextCursor());
            WeGroupChatListVo groupChatChildList = getGroupChatList(query);
            groupChatList.getGroupChatList().addAll(groupChatChildList.getGroupChatList());
        }
        return groupChatList;
    }

    @Override
    public WeGroupChatDetailVo getGroupChatDetail(WeGroupChatDetailQuery query) {
        return weCustomerClient.getGroupChatDetail(query);
    }

    @Override
    public WeCorpTagListVo getCorpTagList(WeCorpTagListQuery query) {
        return weCustomerClient.getCorpTagList(query);
    }

    @Override
    public WeCorpTagVo addCorpTag(WeAddCorpTagQuery query) {
        return weCustomerClient.addCorpTag(query);
    }

    @Override
    public WeResultVo delCorpTag(WeCorpTagListQuery query) {
        return weCustomerClient.delCorpTag(query);
    }

    @Override
    public WeAddCustomerMsgVo addMsgTemplate(WeAddCustomerMsgQuery query) {
        return weCustomerClient.addMsgTemplate(query);
    }

    @Override
    public WeGroupMsgListVo getGroupMsgList(WeGroupMsgListQuery query) {
        WeGroupMsgListVo groupMsgList = weCustomerClient.getGroupMsgList(query);
        if (groupMsgList != null && StringUtils.isNotEmpty(groupMsgList.getNextCursor())) {
            query.setCursor(groupMsgList.getNextCursor());
            WeGroupMsgListVo groupMsgListChild = getGroupMsgList(query);
            groupMsgList.getGroupMsgList().addAll(groupMsgListChild.getGroupMsgList());
        }
        return groupMsgList;
    }

    @Override
    public WeGroupMsgListVo getGroupMsgTask(WeGetGroupMsgListQuery query) {
        WeGroupMsgListVo groupMsgTask = weCustomerClient.getGroupMsgTask(query);
        if (groupMsgTask != null && StringUtils.isNotEmpty(groupMsgTask.getNextCursor())) {
            query.setCursor(groupMsgTask.getNextCursor());
            WeGroupMsgListVo groupMsgTaskChild = getGroupMsgTask(query);
            groupMsgTask.getTaskList().addAll(groupMsgTaskChild.getTaskList());
        }
        return groupMsgTask;
    }

    @Override
    public WeGroupMsgListVo getGroupMsgSendResult(WeGetGroupMsgListQuery query) {
        WeGroupMsgListVo groupMsgSendResult = weCustomerClient.getGroupMsgSendResult(query);
        if (groupMsgSendResult != null && StringUtils.isNotEmpty(groupMsgSendResult.getNextCursor())) {
            query.setCursor(groupMsgSendResult.getNextCursor());
            WeGroupMsgListVo groupMsgSendResultChild = getGroupMsgSendResult(query);
            groupMsgSendResult.getSendList().addAll(groupMsgSendResultChild.getSendList());
        }
        return groupMsgSendResult;
    }

    @Override
    public WeResultVo cancelGroupMsgSend(WeCancelGroupMsgSendQuery query) {
        return weCustomerClient.cancelGroupMsgSend(query);
    }

    @Override
    public WeResultVo sendWelcomeMsg(WeWelcomeMsgQuery query) {

        return weCustomerClient.sendWelcomeMsg(query);
    }

    @Override
    public WeResultVo makeCustomerLabel(WeMarkTagQuery weMarkTagQuery) {
        return weCustomerClient.makeCustomerLabel(weMarkTagQuery);
    }

    @Override
    public WeTransferCustomerVo transferCustomer(WeTransferCustomerQuery query) {
        return weCustomerClient.transferCustomer(query);
    }

    @Override
    public WeTransferCustomerVo transferResult(WeTransferCustomerQuery query) {
        return weCustomerClient.transferResult(query);
    }

    @Override
    public WeBatchCustomerDetailVo getBatchCustomerDetail(WeBatchCustomerQuery query) {
        WeBatchCustomerDetailVo customerDetail = weCustomerClient.getBatchCustomerDetail(query);
        if (customerDetail != null && StringUtils.isNotEmpty(customerDetail.getNextCursor())) {
            query.setCursor(customerDetail.getNextCursor());
            WeBatchCustomerDetailVo customerDetailChild = weCustomerClient.getBatchCustomerDetail(query);
            customerDetail.getExternalContactList().addAll(customerDetailChild.getExternalContactList());
        }
        return customerDetail;
    }

    @Override
    public WeCustomerDetailVo getCustomerDetail(WeCustomerQuery query) {
        WeCustomerDetailVo customerDetail = weCustomerClient.getCustomerDetail(query);
        if (customerDetail != null && StringUtils.isNotEmpty(customerDetail.getNextCursor())) {
            query.setCursor(customerDetail.getNextCursor());
            WeCustomerDetailVo customerDetailChild = weCustomerClient.getCustomerDetail(query);
            customerDetail.getFollowUser().addAll(customerDetailChild.getFollowUser());
        }
        return customerDetail;
    }

    @Override
    public WeUserBehaviorDataVo getUserBehaviorData(WeUserBehaviorDataQuery query) {
        return weCustomerClient.getUserBehaviorData(query);
    }

    @Override
    public WeGroupChatStatisticVo getGroupChatStatistic(WeGroupChatStatisticQuery query) {
        return weCustomerClient.getGroupChatStatistic(query);
    }

    @Override
    public WeGroupChatStatisticVo getGroupChatStatisticByDay(WeGroupChatStatisticQuery query) {
        return weCustomerClient.getGroupChatStatisticGroupByDay(query);
    }

    @Override
    public WeGroupChatAddJoinWayVo addJoinWayForGroupChat(WeGroupChatAddJoinWayQuery joinWayQuery) {
        return weCustomerClient.addJoinWayForGroupChat(joinWayQuery);
    }

    @Override
    public WeGroupChatGetJoinWayVo getJoinWayForGroupChat(WeGroupChatJoinWayQuery joinWayQuery) {
        return weCustomerClient.getJoinWayForGroupChat(joinWayQuery);
    }

    @Override
    public WeResultVo delJoinWayForGroupChat(WeGroupChatJoinWayQuery joinWayQuery) {
        return weCustomerClient.delJoinWayForGroupChat(joinWayQuery);
    }

    @Override
    public WeTransferCustomerVo resignedTransferCustomer(WeTransferCustomerQuery query) {
        return weCustomerClient.resignedTransferCustomer(query);
    }

    @Override
    public WeTransferCustomerVo transferGroupChat(WeTransferGroupChatQuery query) {
        return weCustomerClient.transferGroupChat(query);
    }

    @Override
    public WeResultVo updateJoinWayForGroupChat(WeGroupChatUpdateJoinWayQuery joinWayQuery) {
        return weCustomerClient.updateJoinWayForGroupChat(joinWayQuery);
    }

    @Override
    public WeResultVo editCorpTag(WeUpdateCorpTagQuery query) {
        return weCustomerClient.editCorpTag(query);
    }

    @Override
    public WeGroupMsgTplVo addWeGroupMsg(WeGroupMsgQuery query) {
        return weGroupWelcomeTplClient.addWeGroupMsg(query);
    }

    @Override
    public WeResultVo updateWeGroupMsg(WeGroupMsgQuery query) {
        return weGroupWelcomeTplClient.updateWeGroupMsg(query);
    }

    @Override
    public WeResultVo delWeGroupMsg(WeGroupMsgQuery query) {
        return weGroupWelcomeTplClient.delWeGroupMsg(query);
    }

    @Override
    public WeLinkCustomerVo createCustomerLink(WeLinkCustomerQuery query) {
        return weCustomerClient.createCustomerLink(query);
    }

    @Override
    public WeResultVo updateCustomerLink(WeLinkCustomerQuery query) {
        return weCustomerClient.updateCustomerLink(query);
    }

    @Override
    public WeResultVo deleteCustomerLink(WeLinkCustomerQuery query) {
        return weCustomerClient.deleteCustomerLink(query);
    }

    @Override
    public WeLinkWecustomerCountVo customerLinkCount(WeLinkCustomerCountQuery query) {
        return weCustomerClient.customerLinkCount(query);
    }

    @Override
    public WeLinkCustomerAcquisitionQuotaVo customerAcquisitionQuota(WeBaseQuery query) {
        return weCustomerClient.customerAcquisitionQuota(query);
    }

    @Override
    public WeResultVo updateCustomerRemark(UpdateCustomerRemarkQuery query) {
        return weCustomerClient.updateCustomerRemark(query);
    }

    @Override
    public WeCustomerListVo getCustomerList(WeCustomerListQuery query) {
        return weCustomerClient.getCustomerList(query);
    }


}
