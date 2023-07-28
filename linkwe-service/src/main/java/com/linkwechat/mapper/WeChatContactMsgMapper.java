package com.linkwechat.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.msgaudit.query.WeChatContactMsgQuery;
import com.linkwechat.domain.msgaudit.vo.WeChatContactMsgVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeChatContactMsg;

/**
 * 会话消息(WeChatContactMsg)
 *
 * @author danmo
 * @since 2022-05-06 11:54:51
 */

@Repository()
@Mapper
public interface WeChatContactMsgMapper extends BaseMapper<WeChatContactMsg> {

    /**
     * 外部联系人 会话列表
     * @param fromId 消息发送人id
     * @return 会话列表
     */
    List<WeChatContactMsgVo> selectExternalChatList(@Param("fromId") String fromId);

    /**
     * 单聊 会话列表
     * @param weChatContactMsg 入参
     * @return 会话列表
     */
    List<WeChatContactMsgVo> selectAloneChatList(WeChatContactMsg weChatContactMsg);

    /**
     * 内部联系人 会话列表
     * @param fromId 消息发送人id
     * @return 会话列表
     */
    List<WeChatContactMsgVo> selectInternalChatList(@Param("fromId") String fromId);

    /**
     * 群聊 会话列表
     * @param fromId 消息发送人id
     * @return 会话列表
     */
    List<WeChatContactMsgVo> selectGroupChatList(@Param("fromId") String fromId);

    /**
     * 全文检索 会话列表
     * @param query 检索条件
     * @return 会话列表
     */
    List<WeChatContactMsgVo> selectFullSearchChatList(WeChatContactMsgQuery query);

}

