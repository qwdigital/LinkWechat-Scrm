package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.ConversationArchiveQuery;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.wecom.service.IWeConversationArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author danmo
 * @description 会话存档controller
 * @date 2020/12/19 13:51
 **/
@Slf4j
@RestController
@RequestMapping("/wecom/finance")
public class WeConversationArchiveController extends BaseController {
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
    public TableDataInfo getChatContactList(ConversationArchiveQuery query) {
        return getDataTable(weConversationArchiveService.getChatContactList(query));
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
    public TableDataInfo getChatRoomContactList(ConversationArchiveQuery query) {
        return getDataTable(weConversationArchiveService.getChatRoomContactList(query));
    }


    /**
     * 获取全局会话数据接口
     *
     * @param query      入参
     * @return
     */
    @PreAuthorize("@ss.hasPermi('conversationArchive:chatAllContact:list')")
    @GetMapping("/getChatAllList")
    public TableDataInfo getChatAllList(ConversationArchiveQuery query) {
        return getDataTable(weConversationArchiveService.getChatAllList(query));
    }

}
