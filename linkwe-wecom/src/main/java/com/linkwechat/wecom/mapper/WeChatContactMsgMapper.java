package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeChatContactMsg;
import com.linkwechat.wecom.domain.vo.WeChatContactMsgVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会话消息Mapper接口
 *
 * @author ruoyi
 * @date 2021-07-28
 */
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
     * @param weChatContactMsg 检索条件
     * @return 会话列表
     */
    List<WeChatContactMsgVo> selectFullSearchChatList(WeChatContactMsg weChatContactMsg);
}
