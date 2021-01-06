package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.dto.WeChatItemDto;
import com.linkwechat.wecom.service.IWeChatItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聊天工具侧边栏
 * @author kewen
 */
@RequestMapping(value = "/wecom/chat/item")
@RestController
public class WeChatItemController extends BaseController {

    @Autowired
    private IWeChatItemService weChatItemService;


    /**
     * 侧边栏抓取素材
     */
    @PreAuthorize("@ss.hasPermi('chat:item:add')")
    @Log(title = "侧边栏抓取素材", businessType = BusinessType.INSERT)
    @PutMapping
    public AjaxResult add(@RequestBody WeChatItemDto chatItemDto) {
        return toAjax(weChatItemService.checkItems(chatItemDto));
    }



}
