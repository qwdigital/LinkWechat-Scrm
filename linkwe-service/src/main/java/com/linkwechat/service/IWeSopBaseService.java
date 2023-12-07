package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.domain.sop.WeSopBase;
import com.linkwechat.domain.sop.vo.*;
import com.linkwechat.domain.sop.vo.content.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author robin
 * @description 针对表【we_sop_base(Sop base表)】的数据库操作Service
 * @createDate 2022-09-06 10:33:34
 */
public interface IWeSopBaseService extends IService<WeSopBase> {

    /**
     * 创建sop
     * @param weSopBase
     */
    void createWeSop(WeSopBase weSopBase);



    /**
     * 更新sop
     * @param weSopBase
     */
    void updateWeSop(WeSopBase weSopBase);


    /**
     * 根据sopBaseId获取相关详情
     * @param sopBaseId
     * @return
     */
    WeSopBase findWeSopBaseBySopBaseId(Long sopBaseId);


    /**
     * 根据sopid移除sop
     * @param sopId
     */
    void removeWeSoPBySopId(List<Long> sopId);


    /**
     * 获取sop列表
     * @return
     */
    TableDataInfo<List<WeSopListsVo>> findWeSopListsVo(WeSopBase weSopBase);







    /**
     * sop统计详情tab
     * @param sopBaseId
     * @return
     */
    WeSopDetailTabVo findWeSopDetailTabVo(String sopBaseId);


    /**
     * sop统计详情客群列表
     * @param sopBaseId
     * @param groupName
     * @param executeState
     * @param weUserId
     * @return
     */
    List<WeSopDetailGroupVo> findWeSopDetailGroup(String sopBaseId,String groupName,Integer executeState, String weUserId);



    /**
     *  sop统计详情客户列表
     * @return
     */
    List<WeSopDetailCustomerVo>  findWeSopDetailCustomer(String sopBaseId, String customerName,Integer executeState,String weUserId);


    /**
     * 获取客户内容执行列表
     * @param executeWeUserId
     * @param targetId
     * @param executeSubState
     * @param sopBaseId
     * @param executeTargetId
     * @return
     */
    List<WeCustomerSopContentVo> findCustomerExecuteContent(String executeWeUserId, String targetId, Integer executeSubState, String sopBaseId,String executeTargetId);


    /**
     * 获取指定客户需要执行的sop
     * @param executeWeUserId
     * @param targetId
     * @param executeSubState
     * @return
     */
    WeSendCustomerSopContentVo findCustomerSopContent(String executeWeUserId, String targetId, Integer executeSubState);



    /**
     * 获取客群内容执行列表
     * @param chatId
     * @param executeState
     * @param sopBaseId
     * @return
     */
    List<WeGroupSopContentVo> findGroupExecuteContent(String chatId, Integer executeState, String sopBaseId,String executeTargetId);


    /**
     * 获取指定客群需要执行的sop
     * @param chatId
     * @param executeSubState
     * @return
     */
    WeSendGroupSopContentVo findGroupSopContent(String chatId, Integer executeSubState);

    /**
     * 获取今日客户发送的sop相关消息
     * @return
     */
    List<WeCustomerSopToBeSentVo> findWeCustomerSopToBeSent(boolean isExpiringSoon);

    /**
     * 获取今日客群发送的sop相关消息
     * @return
     */
    List<WeGroupSopToBeSentVo> findWeGroupSopToBeSent(boolean isExpiringSoon);


    /**
     * 执行sop(执行结束添加动作)
     * @param executeTargetAttachId
     */
    void executeSop(String executeTargetAttachId);


    /**
     * 构建生效群(key:执行成员weUserId;value:执行员工作为群主的群)
     * @param weSopBase
     * @param chatId 群id，主要用于新群
     * @return
     */
    Map<String,List<LinkGroupChatListVo>> builderExecuteGroup(WeSopBase weSopBase,String chatId);




    /**
     * 构建任务执行对象(即执行sop任务的企业员工)
     * @param executeWeUser
     * @return
     */
    Set<String> builderExecuteWeUserIds(WeSopExecuteUserConditVo executeWeUser);


    /**
     * 构建客户sop执行计划
     * @param weSopBase
     * @param executeWeCustomers
     * @param isCreateOrUpdate
     * @param buildXkSopPlan 是否构建新客执行计划 true构建，false不构建
     */
    void builderExecuteCustomerSopPlan(WeSopBase weSopBase, Map<String, List<WeCustomersVo>> executeWeCustomers, boolean isCreateOrUpdate, boolean buildXkSopPlan);


    /**
     * 构建客群sop执行计划
     * @param weSopBase
     * @param executeGroups
     * @param isCreateOrUpdate
     * @param buildXkSopPlan
     */
    void builderExecuteGroupSopPlan(WeSopBase weSopBase, Map<String, List<LinkGroupChatListVo>> executeGroups, boolean isCreateOrUpdate,boolean buildXkSopPlan);



    /**
     * 企微发送方式结果同步
     * @param sopBaseId
     */
    void synchSopExecuteResultForWeChatPushType(String sopBaseId);



    /**
     * 更新sop状态
     * @param sopId
     * @param sopState
     */
    void  updateSopState(String sopId,Integer sopState);

}

