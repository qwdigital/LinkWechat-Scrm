package com.linkwechat.wecom.controller;

import com.linkwechat.common.core.domain.AjaxResult;
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
import com.linkwechat.wecom.service.IQwCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author danmo
 * @description 客户接口管理
 * @date 2022/3/20 14:54
 **/
@Api(tags = "客户接口管理")
@RestController
@RequestMapping("customer")
public class QwCustomerController {

    @Autowired
    private IQwCustomerService qwCustomerService;

    /**
     * 获取配置了客户联系功能的成员列表
     *
     * @param query corpid
     * @return
     */
    @ApiOperation(value = "获取配置了客户联系功能的成员列表", httpMethod = "POST")
    @PostMapping("/getFollowerList")
    public AjaxResult<WeFollowUserListVo> getFollowUserList(@RequestBody WeBaseQuery query) {
        WeFollowUserListVo followUserList = qwCustomerService.getFollowUserList(query);
        return AjaxResult.success(followUserList);
    }

    /**
     * 配置客户联系「联系我」方式
     *
     * @param query 入参
     * @return
     */
    @ApiOperation(value = "配置客户联系「联系我」方式", httpMethod = "POST")
    @PostMapping("/addContactWay")
    public AjaxResult<WeAddWayVo> addContactWay(@RequestBody WeAddWayQuery query) {
        WeAddWayVo weAddWayVo = qwCustomerService.addContactWay(query);
        return AjaxResult.success(weAddWayVo);
    }

    /**
     * 获取企业已配置的「联系我」方式
     *
     * @param query 入参
     * @return
     */
    @ApiOperation(value = "获取企业已配置的「联系我」方式", httpMethod = "POST")
    @PostMapping("/getContactWay")
    public AjaxResult<WeContactWayVo> getContactWay(@RequestBody WeContactWayQuery query) {
        WeContactWayVo weContactWayVo = qwCustomerService.getContactWay(query);
        return AjaxResult.success(weContactWayVo);
    }

    /**
     * 获取企业已配置的「联系我」列表
     *
     * @param query 入参
     * @return
     */
    @ApiOperation(value = "获取企业已配置的「联系我」列表", httpMethod = "POST")
    @PostMapping("/getContactWayList")
    public AjaxResult<WeContactWayListVo> getContactWayList(@RequestBody WeContactWayQuery query) {
        WeContactWayListVo weContactWayVo = qwCustomerService.getContactWayList(query);
        return AjaxResult.success(weContactWayVo);
    }

    /**
     * 更新联系方式
     *
     * @param query 入参
     * @return
     */
    @ApiOperation(value = "更新联系方式", httpMethod = "POST")
    @PostMapping("/updateContactWay")
    public AjaxResult<WeResultVo> updateContactWay(@RequestBody WeAddWayQuery query) {
        WeResultVo resultVo = qwCustomerService.updateContactWay(query);
        return AjaxResult.success(resultVo);
    }

    /**
     * 删除联系方式
     *
     * @param query 入参
     * @return
     */
    @ApiOperation(value = "删除联系方式", httpMethod = "POST")
    @PostMapping("/delContactWay")
    public AjaxResult<WeResultVo> delContactWay(@RequestBody WeContactWayQuery query) {
        WeResultVo resultVo = qwCustomerService.delContactWay(query);
        return AjaxResult.success(resultVo);
    }

    /**
     * 获取客户群列表
     *
     * @param query
     * @return WeGroupChatListVo
     */
    @ApiOperation(value = "获取客户群列表", httpMethod = "POST")
    @PostMapping("/groupchat/list")
    public AjaxResult<WeGroupChatListVo> getGroupChatList(@RequestBody WeGroupChatListQuery query) {
        WeGroupChatListVo groupChatList = qwCustomerService.getGroupChatList(query);
        return AjaxResult.success(groupChatList);
    }

    /**
     * 获取客户群详情
     *
     * @param query
     * @return WeGroupChatDetailVo
     */
    @ApiOperation(value = "获取客户群详情", httpMethod = "POST")
    @PostMapping("/groupchat/get")
    public AjaxResult<WeGroupChatDetailVo> getGroupChatDetail(@RequestBody WeGroupChatDetailQuery query) {
        WeGroupChatDetailVo groupChatDetail = qwCustomerService.getGroupChatDetail(query);
        return AjaxResult.success(groupChatDetail);
    }

    /**
     * 获取企业标签库
     *
     * @param query
     * @return
     */
    @GetMapping("/getCorpTagList")
    public AjaxResult<WeCorpTagListVo> getCorpTagList(WeCorpTagListQuery query) {
        return AjaxResult.success(qwCustomerService.getCorpTagList(query));
    }

    /**
     * 添加企业客户标签
     *
     * @param query
     * @return
     */
    @PostMapping("/addCorpTag")
    public AjaxResult<WeCorpTagVo> addCorpTag(@RequestBody WeAddCorpTagQuery query) {

        return AjaxResult.success(
                qwCustomerService.addCorpTag(query)
        );

    }


    /**
     * 删除企业客户标签
     *
     * @param query
     * @return
     */
    @DeleteMapping("/delCorpTag")
    public AjaxResult<WeResultVo> delCorpTag(@RequestBody WeCorpTagListQuery query) {

        return AjaxResult.success(
                qwCustomerService.delCorpTag(query)
        );
    }


    /**
     * 编辑客户标签
     *
     * @param weMarkTagQuery
     * @return
     */
    @PostMapping("/makeCustomerLabel")
    public AjaxResult makeCustomerLabel(@RequestBody WeMarkTagQuery weMarkTagQuery) {
        qwCustomerService.makeCustomerLabel(weMarkTagQuery);

        return AjaxResult.success();
    }


    /**
     * 分配在职成员的客户
     *
     * @param query
     * @return
     */
    @PostMapping("/transferCustomer")
    public AjaxResult<WeTransferCustomerVo> transferCustomer(@RequestBody WeTransferCustomerQuery query) {

        return AjaxResult.success(qwCustomerService.transferCustomer(query));
    }


    /**
     * 查询客户接替状态
     *
     * @param query
     * @return
     */
    @PostMapping("/transferResult")
    public AjaxResult<WeTransferCustomerVo> transferResult(@RequestBody WeTransferCustomerQuery query) {

        return AjaxResult.success(
                qwCustomerService.transferResult(query)
        );
    }


    /**
     * 获取客户详情
     *
     * @param query
     * @return
     */
    @PostMapping("/getCustomerDetail")
    public AjaxResult<WeCustomerDetailVo> getCustomerDetail(@RequestBody WeCustomerQuery query) {
        return AjaxResult.success(qwCustomerService.getCustomerDetail(query));
    }

    /**
     * 批量获取客户详情
     *
     * @param query
     * @return
     */
    @PostMapping("/getBatchCustomerDetail")
    public AjaxResult<WeBatchCustomerDetailVo> getBatchCustomerDetail(@RequestBody WeBatchCustomerQuery query) {
        return AjaxResult.success(qwCustomerService.getBatchCustomerDetail(query));

    }

    /**
     * 创建企业群发
     *
     * @param query
     * @return WeAddCustomerMsgVo
     */
    @PostMapping("/group/msg/add")
    public AjaxResult<WeAddCustomerMsgVo> addMsgTemplate(@RequestBody WeAddCustomerMsgQuery query) {
        return AjaxResult.success(qwCustomerService.addMsgTemplate(query));
    }

    /**
     * 获取群发记录列表
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @PostMapping("/group/msg/getList")
    public AjaxResult<WeGroupMsgListVo> getGroupMsgList(@RequestBody WeGroupMsgListQuery query) {
        return AjaxResult.success(qwCustomerService.getGroupMsgList(query));
    }

    /**
     * 获取群发成员发送任务列表
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @PostMapping("/group/msg/getTask")
    public AjaxResult<WeGroupMsgListVo> getGroupMsgTask(@RequestBody WeGetGroupMsgListQuery query) {
        return AjaxResult.success(qwCustomerService.getGroupMsgTask(query));
    }

    /**
     * 获取企业群发成员执行结果
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @PostMapping("/group/msg/getSendResult")
    public AjaxResult<WeGroupMsgListVo> getGroupMsgSendResult(@RequestBody WeGetGroupMsgListQuery query) {
        return AjaxResult.success(qwCustomerService.getGroupMsgSendResult(query));
    }

    /**
     * 停止企业群发
     *
     * @param query
     * @return
     */
    @PostMapping("/group/msg/cancelGroupMsgSend")
    public AjaxResult<WeResultVo> cancelGroupMsgSend(@RequestBody WeCancelGroupMsgSendQuery query) {
        return AjaxResult.success(qwCustomerService.cancelGroupMsgSend(query));
    }


    /**
     * 发送新客户欢迎语
     *
     * @param query
     * @return WeGroupMsgListVo
     */
    @PostMapping("/sendWelcomeMsg")
    public AjaxResult<WeResultVo> sendWelcomeMsg(@RequestBody WeWelcomeMsgQuery query) {
        return AjaxResult.success(qwCustomerService.sendWelcomeMsg(query));
    }

    /**
     * 联系客户统计
     *
     * @param query
     * @return WeUserBehaviorDataVo
     */
    @PostMapping("/getUserBehaviorData")
    public AjaxResult<WeUserBehaviorDataVo> getUserBehaviorData(@RequestBody WeUserBehaviorDataQuery query) {
        return AjaxResult.success(qwCustomerService.getUserBehaviorData(query));
    }


    /**
     * 群聊数据统计（按群主聚合的方式）
     *
     * @param query
     * @return WeGroupChatStatisticVo
     */
    @PostMapping("/getGroupChatStatistic")
    public AjaxResult<WeGroupChatStatisticVo> getGroupChatStatistic(@RequestBody WeGroupChatStatisticQuery query) {
        return AjaxResult.success(qwCustomerService.getGroupChatStatistic(query));
    }

    /**
     * 群聊数据统计（按日期聚合的方式）
     *
     * @param query
     * @return WeGroupChatStatisticVo
     */
    @PostMapping("/getGroupChatStatisticByDay")
    public AjaxResult<WeGroupChatStatisticVo> getGroupChatStatisticByDay(@RequestBody WeGroupChatStatisticQuery query) {
        return AjaxResult.success(qwCustomerService.getGroupChatStatisticByDay(query));
    }


    /**
     * 配置客户群进群方式
     *
     * @param joinWayQuery
     * @return
     */
    @PostMapping("/addJoinWayForGroupChat")
    public AjaxResult<WeGroupChatAddJoinWayVo> addJoinWayForGroupChat(@RequestBody WeGroupChatAddJoinWayQuery joinWayQuery) {

        return AjaxResult.success(
                qwCustomerService.addJoinWayForGroupChat(joinWayQuery)
        );
    }


    /**
     * 获取客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    @GetMapping("/getJoinWayForGroupChat")
    public AjaxResult<WeGroupChatGetJoinWayVo> getJoinWayForGroupChat(WeGroupChatJoinWayQuery joinWayQuery) {

        return AjaxResult.success(
                qwCustomerService.getJoinWayForGroupChat(joinWayQuery)
        );
    }


    /**
     * 删除客户群进群方式配置
     *
     * @param joinWayQuery
     * @return
     */
    @PostMapping("/delJoinWayForGroupChat")
    public AjaxResult<WeResultVo> delJoinWayForGroupChat(@RequestBody WeGroupChatJoinWayQuery joinWayQuery) {

        return AjaxResult.success(
                qwCustomerService.delJoinWayForGroupChat(joinWayQuery)
        );

    }

    /**
     * 分配离职成员的客户
     *
     * @param query
     * @return
     */
    @PostMapping("/resignedTransferCustomer")
    public AjaxResult<WeTransferCustomerVo> resignedTransferCustomer(@RequestBody WeTransferCustomerQuery query) {
        return AjaxResult.success(
                qwCustomerService.resignedTransferCustomer(query)
        );
    }


    /**
     * 分配离职成员的客户群
     *
     * @param query
     * @return
     */
    @PostMapping("/transferGroupChat")
    public AjaxResult<WeTransferCustomerVo> transferGroupChat(@RequestBody WeTransferGroupChatQuery query) {
        return AjaxResult.success(
                qwCustomerService.transferGroupChat(query)
        );

    }

    /**
     * 更新客户群进群方式配置
     *
     * @param query
     * @return
     */
    @PostMapping("/updateJoinWayForGroupChat")
    public AjaxResult<WeResultVo> updateJoinWayForGroupChat(@RequestBody WeGroupChatUpdateJoinWayQuery query) {

        return AjaxResult.success(
                qwCustomerService.updateJoinWayForGroupChat(query)
        );
    }

    /**
     * 编辑标签或标签组名称
     *
     * @param query
     * @return
     */
    @PostMapping("/editCorpTag")
    public AjaxResult<WeResultVo> editCorpTag(@RequestBody WeUpdateCorpTagQuery query) {

        return AjaxResult.success(
                qwCustomerService.editCorpTag(query)
        );
    }


    /**
     * 添加入群欢迎语素材
     * @param query
     * @return
     */
    @PostMapping("/addWeGroupMsg")
    public AjaxResult<WeGroupMsgTplVo>  addWeGroupMsg(@RequestBody WeGroupMsgQuery query){


        return AjaxResult.success(
                qwCustomerService.addWeGroupMsg(query)
        );

    }


    /**
     * 编辑入群欢迎语素材
     * @param query
     * @return
     */
    @PostMapping("/updateWeGroupMsg")
    public AjaxResult<WeResultVo>  updateWeGroupMsg(@RequestBody WeGroupMsgQuery query){


        return AjaxResult.success(
                qwCustomerService.updateWeGroupMsg(query)
        );

    }


    /**
     *  删除入群欢迎语素材
     * @param query
     * @return
     */
    @PostMapping("/delWeGroupMsg")
    public AjaxResult<WeResultVo> delWeGroupMsg(@RequestBody WeGroupMsgQuery query){


        return AjaxResult.success(
                qwCustomerService.delWeGroupMsg(query)
        );

    }


    /**
     * 创建获客链接
     * @param query
     * @return
     */
    @PostMapping("/createCustomerLink")
    public AjaxResult<WeLinkCustomerVo> createCustomerLink(@RequestBody WeLinkCustomerQuery query){

        return AjaxResult.success(
                qwCustomerService.createCustomerLink(query)
        );

    }



    /**
     * 更新获客链接
     * @param query
     * @return
     */
    @PostMapping("/updateCustomerLink")
    public AjaxResult<WeLinkCustomerVo> updateCustomerLink(@RequestBody WeLinkCustomerQuery query){

        return AjaxResult.success(
                qwCustomerService.updateCustomerLink(query)
        );

    }


    /**
     * 删除获客链接
     * @param query
     * @return
     */
    @PostMapping("/deleteCustomerLink")
    public AjaxResult<WeResultVo>  deleteCustomerLink(@RequestBody WeLinkCustomerQuery query){


        return AjaxResult.success(
                qwCustomerService.deleteCustomerLink(query)
        );

    }


    /**
     * 获取由获客链接添加的客户信息
     * @param query
     * @return
     */
    @PostMapping("/customerLinkCount")
    public AjaxResult<WeLinkWecustomerCountVo> customerLinkCount(@RequestBody WeLinkCustomerCountQuery query){


        return AjaxResult.success(
                qwCustomerService.customerLinkCount(query)
        );

    }


    /**
     * 获客助手查询剩余使用量
     * @param weBaseQuery
     * @return
     */
    @PostMapping("/customerAcquisitionQuota")
    public AjaxResult<WeLinkCustomerAcquisitionQuotaVo> customerAcquisitionQuota(@RequestBody WeBaseQuery weBaseQuery){


        return AjaxResult.success(
                qwCustomerService.customerAcquisitionQuota(weBaseQuery)
        );
   }

    /**
     * 更员工相关备注
     * @param query
     * @return
     */
    @PostMapping("/updateCustomerRemark")
    public AjaxResult<WeResultVo> updateCustomerRemark(@RequestBody UpdateCustomerRemarkQuery query){

        return AjaxResult.success(
                qwCustomerService.updateCustomerRemark(query)
        );
    }


    /**
     * 获取客户列表
     * @param query
     * @return
     */
    @PostMapping("/getCustomerList")
    public AjaxResult<WeCustomerListVo> getCustomerList(@RequestBody WeCustomerListQuery query){


        return AjaxResult.success(
                qwCustomerService.getCustomerList(query)
        );

    }






}
