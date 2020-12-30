package com.linkwechat.web.controller.wecom;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.domain.ConversationArchiveQuery;
import com.linkwechat.wecom.service.IWeConversationArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sxw
 * @description 会话存档controller
 * @date 2020/12/19 13:51
 **/
@Slf4j
@RestController
@RequestMapping("/wecom/finance")
public class WeConversationArchiveController {
    @Autowired
    private IWeConversationArchiveService weConversationArchiveService;

    /**
     * 获取单聊会话数据接口
     *
     * @param query      入参
     * @param /fromId    发送人id
     * @param /reveiceId 接收人id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('conversationArchive:chatContact:list')")
    @GetMapping("/getChatContactList")
    public R<PageInfo<JSONObject>> getChatContactList(ConversationArchiveQuery query) {
        return R.ok(weConversationArchiveService.getChatContactList(query));
    }


    /**
     * 获取群聊会话数据接口
     *
     * @param query   入参
     * @param /fromId 发送人id
     * @param /room   接收人id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('conversationArchive:chatRoomContact:list')")
    @GetMapping("/getChatRoomContactList")
    public R<PageInfo<JSONObject>> getChatRoomContactList(ConversationArchiveQuery query) {
        return R.ok(weConversationArchiveService.getChatRoomContactList(query));
    }


}
