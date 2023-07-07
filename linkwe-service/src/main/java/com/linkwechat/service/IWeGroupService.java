package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.WeCustomerTrajectory;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.customer.vo.WeCustomerAddGroupVo;
import com.linkwechat.domain.groupchat.query.WeGroupChatQuery;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatListVo;
import com.linkwechat.domain.groupchat.vo.LinkGroupChatVo;
import com.linkwechat.domain.wecom.query.customer.groupchat.WeGroupChatListQuery;
import com.linkwechat.domain.wecom.vo.customer.groupchat.WeGroupChatListVo;

import java.util.List;

/**
 * 企业微信群(WeGroup)
 *
 * @author danmo
 * @since 2022-04-02 13:35:13
 */
public interface IWeGroupService extends IService<WeGroup> {

    /**
     * 获取群列表
     * @param query
     */
    List<LinkGroupChatListVo> getPageList(WeGroupChatQuery query);

    /**
     *  应用获取客群数据
     * @param query
     * @return
     */
    List<LinkGroupChatListVo> selectWeGroupListByApp(WeGroupChatQuery query);

    /**
     * 获取群详情
     * @param id
     */
    LinkGroupChatListVo getInfo(Long id);

    LinkGroupChatListVo getInfo(String chatId);

    /**
     * 同步客户群
     */
    void synchWeGroup();

    /**
     * 同步客户群
     * @param msg
     */
    void synchWeGroupHandler(String msg);


    /**
     * 跟进条件同步客群
     * @param chatListQuery
     * @return
     */
    List<WeGroupChatListVo.GroupChat> synchWeGroup(WeGroupChatListQuery chatListQuery);



    public List<WeGroup> findGroupInfoFromWechat(WeGroupChatListQuery chatListQuery);


    /**
     * 从企业微信端获取的群信息
     * @param groupChatList
     * @param chatListQuery
     */
    void getGroupChatList(List<WeGroupChatListVo.GroupChat> groupChatList, WeGroupChatListQuery chatListQuery);

    /**
     * 创建客群
     * @param chatId
     * @param corpId
     * @param isCallBack 是否回掉 true回掉调用该方法,false非回掉触发改方法
     */
    void createWeGroup(String chatId, String corpId,boolean isCallBack);

    /**
     * 解散客群
     * @param chatId
     */
    void deleteWeGroup(String chatId);


    /**
     * 获取客户添加的群
     * @param userId
     * @param externalUserid
     * @return
     */
    List<WeCustomerAddGroupVo> findWeGroupByCustomer(String userId, String externalUserid);

    /**
     * 成员入群
     * @param chatId 群ID
     * @param joinScene 入群方式
     * @param memChangeCnt 成员变更数量
     */
    void addMember(String chatId, Integer joinScene, Integer memChangeCnt);

    /**
     * 成员退群
     * @param chatId 群ID
     * @param quitScene 入群方式
     * @param memChangeCnt 成员变更数量
     */
    void delMember(String chatId, Integer quitScene, Integer memChangeCnt);

    /**
     * 群信息变更
     * @param chatId 群ID
     * @param updateDetail 变更类型
     */
    void changeGroup(String chatId, String updateDetail);




}
