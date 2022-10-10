package com.linkwechat.controller;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.side.WeChatSide;
import com.linkwechat.service.IWeChatSideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 聊天工具侧边栏
 *
 * @author kewen
 */
@RestController
@RequestMapping(value = "/chat/side")
public class WeChatSideController extends BaseController {


    @Autowired
    public IWeChatSideService weChatSideService;

    /**
     * 群发侧边栏列表
     */
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<WeChatSide> weChatSides = weChatSideService.chatSides("0");
        return getDataTable(weChatSides);
    }

    /**
     * 更新侧边栏信息
     */
    @Log(title = "更新侧边栏信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeChatSide weChatSide) {
        weChatSideService.updateWeChatSide(weChatSide);
        return AjaxResult.success();
    }

    /**
     * 群发侧边栏列表
     */
    @GetMapping("/h5List")
    public TableDataInfo h5List() {
        startPage();
        List<WeChatSide> weChatSides = weChatSideService.chatSides("1");
        return getDataTable(weChatSides);
    }

}

