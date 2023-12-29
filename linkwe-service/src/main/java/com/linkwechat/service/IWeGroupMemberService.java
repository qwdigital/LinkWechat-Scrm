package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeGroupMember;
import com.linkwechat.domain.groupchat.vo.WeCustomerDeduplicationVo;
import com.linkwechat.domain.groupchat.vo.WeGroupChannelCountVo;
import com.linkwechat.domain.groupchat.vo.WeGroupMemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 企业微信群成员(WeGroupMember)
 *
 * @author danmo
 * @since 2022-04-02 13:35:14
 */
public interface IWeGroupMemberService extends IService<WeGroupMember> {

    /**
     * 获取群成员列表（分页）
     * @param weGroupMember
     * @return
     */
    List<WeGroupMember> getPageList(WeGroupMember weGroupMember);

    /**
     * 插入客户
     * @param weGroupMembers
     */
    void insertBatch(List<WeGroupMember> weGroupMembers);

    /**
     * 获取群成员列表
     * @param chatId 群聊ID
     * @return
     */
    List<WeGroupMemberVo> selectGroupMemberListByChatId(String chatId);

    /**
     * 获取群成员列表
     * @param chatIds 群聊ID
     * @return
     */
    List<WeGroupMemberVo> selectGroupMemberListByChatIds(List<String> chatIds);

    /**
     * 退群数据更新
     * @param userId
     * @param chatId
     */
    void quitGroup(Integer quitScene,String userId,String chatId);


    /**
     * 物理删除群成员
     * @param chatId
     * @param userId
     */
    void physicalDelete(String chatId,String userId);

    List<WeGroupChannelCountVo> getMemberNumByState(String state, Date startTime, Date endTime);


    /**
     * 获取去重客户列表
     * @param customerName
     * @return
     */
    List<WeCustomerDeduplicationVo> findWeCustomerDeduplication(String customerName);

    /**
     * 群去重提醒
     * @param externalUserids
     */
    void remindDuplicateMembers(String externalUserids);
}
