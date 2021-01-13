package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeChatSide;
import com.linkwechat.wecom.service.IWeChatSideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天工具侧边栏
 * @author kewen
 */
@RequestMapping(value = "/wecom/chat/side")
@RestController
public class WeChatSideController extends BaseController {


    @Autowired
    public IWeChatSideService weChatSideService;

    /**
     * 群发侧边栏列表
     */
   // @PreAuthorize("@ss.hasPermi('chat:side:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "h5") String h5) {
        startPage();
        List<WeChatSide> weChatSides = weChatSideService.chatSides(h5);
        return getDataTable(weChatSides);
    }

    /**
     * 更新侧边栏信息
     */
    @PreAuthorize("@ss.hasPermi('chat:side:edit')")
    @Log(title = "更新侧边栏信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeChatSide weChatSide) {
        return toAjax(weChatSideService.updateWeChatSide(weChatSide));
    }

}
