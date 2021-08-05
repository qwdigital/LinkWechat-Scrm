package com.linkwechat.wecom.service;

import com.linkwechat.wecom.domain.WeChatContactMsg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.vo.WeChatContactMsgVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会话消息Service接口
 *
 * @author ruoyi
 * @date 2021-07-28
 */
public interface IWeChatContactMsgService extends IService<WeChatContactMsg> {

    /**
     * 查询列表
     */
    List<WeChatContactMsg> queryList(WeChatContactMsg weChatContactMsg);

    /**
     * 外部联系人 会话列表
     * @param fromId 消息发送人id
     * @return 会话列表
     */
    List<WeChatContactMsgVo> selectExternalChatList(String fromId);

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
    List<WeChatContactMsgVo> selectInternalChatList(String fromId);

    /**
     * 群聊 会话列表
     * @param fromId 消息发送人id
     * @return 会话列表
     */
    List<WeChatContactMsgVo> selectGroupChatList(String fromId);

    /**
     * 全文检索 会话列表
     * @param weChatContactMsg 检索条件
     * @return 会话列表
     */
    List<WeChatContactMsgVo> selectFullSearchChatList(WeChatContactMsg weChatContactMsg);
}
