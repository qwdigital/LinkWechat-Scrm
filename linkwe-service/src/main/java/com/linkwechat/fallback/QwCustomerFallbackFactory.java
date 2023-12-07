package com.linkwechat.fallback;

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
import com.linkwechat.fegin.QwCustomerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 客户接口回调
 * @date 2022/3/25 23:03
 **/
@Component
@Slf4j
public class QwCustomerFallbackFactory implements QwCustomerClient {
    @Override
    public AjaxResult<WeFollowUserListVo> getFollowUserList(WeBaseQuery query) {

        return null;
    }

    @Override
    public AjaxResult<WeAddWayVo> addContactWay(WeAddWayQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeContactWayVo> getContactWay(WeContactWayQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeContactWayListVo> getContactWayList(WeContactWayQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> updateContactWay(WeAddWayQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> delContactWay(WeContactWayQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupChatListVo> getGroupChatList(WeGroupChatListQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupChatDetailVo> getGroupChatDetail(WeGroupChatDetailQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeCorpTagListVo> getCorpTagList(WeCorpTagListQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeCorpTagVo> addCorpTag(WeAddCorpTagQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> delCorpTag(WeCorpTagListQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeAddCustomerMsgVo> addMsgTemplate(WeAddCustomerMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupMsgListVo> getGroupMsgList(WeGroupMsgListQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupMsgListVo> getGroupMsgTask(WeGetGroupMsgListQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupMsgListVo> getGroupMsgSendResult(WeGetGroupMsgListQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> cancelGroupMsgSend(WeCancelGroupMsgSendQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> sendWelcomeMsg(WeWelcomeMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeUserBehaviorDataVo> getUserBehaviorData(WeUserBehaviorDataQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupChatStatisticVo> getGroupChatStatistic(WeGroupChatStatisticQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupChatStatisticVo> getGroupChatStatisticByDay(WeGroupChatStatisticQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupChatAddJoinWayVo> addJoinWayForGroupChat(WeGroupChatAddJoinWayQuery joinWayQuery) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupChatGetJoinWayVo> getJoinWayForGroupChat(WeGroupChatJoinWayQuery joinWayQuery) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> delJoinWayForGroupChat(WeGroupChatJoinWayQuery joinWayQuery) {
        return null;
    }

    @Override
    public AjaxResult<WeTransferCustomerVo> resignedTransferCustomer(WeTransferCustomerQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeTransferCustomerVo> transferGroupChat(WeTransferGroupChatQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> updateJoinWayForGroupChat(WeGroupChatUpdateJoinWayQuery query) {
        return null;
    }

    @Override
    public AjaxResult<UnionidToExternalUserIdVo> unionIdToExternalUserId3rd(UnionidToExternalUserIdQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> editCorpTag(WeUpdateCorpTagQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeGroupMsgTplVo> addWeGroupMsg(WeGroupMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> updateWeGroupMsg(WeGroupMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> delWeGroupMsg(WeGroupMsgQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeLinkCustomerVo> createCustomerLink(WeLinkCustomerQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeLinkCustomerVo> updateCustomerLink(WeLinkCustomerQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> deleteCustomerLink(WeLinkCustomerQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeLinkCustomerAcquisitionQuotaVo> customerAcquisitionQuota(WeBaseQuery weBaseQuery) {
        return null;
    }

    @Override
    public AjaxResult<WeLinkWecustomerCountVo> customerLinkCount(WeLinkCustomerCountQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeResultVo> updateCustomerRemark(UpdateCustomerRemarkQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeCustomerListVo> getCustomerList(WeCustomerListQuery query) {
        return null;
    }

    @Override
    public AjaxResult makeCustomerLabel(WeMarkTagQuery weMarkTagQuery) {
        return null;
    }

    @Override
    public AjaxResult<WeTransferCustomerVo> transferCustomer(WeTransferCustomerQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeTransferCustomerVo> transferResult(WeTransferCustomerQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeCustomerDetailVo> getCustomerDetail(WeCustomerQuery query) {
        return null;
    }

    @Override
    public AjaxResult<WeBatchCustomerDetailVo> getBatchCustomerDetail(WeBatchCustomerQuery query) {
        return null;
    }
}
