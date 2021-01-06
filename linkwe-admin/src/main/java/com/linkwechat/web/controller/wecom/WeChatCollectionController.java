package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.service.IWeChatCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天工具 侧边栏栏 素材收藏
 *
 * @author kwen
 */
@RequestMapping(value = "/wecom/chat/collection")
@RestController
public class WeChatCollectionController extends BaseController {


    @Autowired
    private IWeChatCollectionService weChatCollectionService;


    /**
     * 添加收藏
     */
    @PreAuthorize("@ss.hasPermi('chat:collection:add')")
    @Log(title = "添加收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addCollection(@RequestParam(value = "materialId") Long materialId, @RequestParam(value = "userId") Long userId) {
        return toAjax(weChatCollectionService.addCollection(materialId, userId));
    }


    /**
     * 取消收藏
     */
    @PreAuthorize("@ss.hasPermi('chat:collection:delete')")
    @Log(title = "取消收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult cancleCollection(@RequestParam(value = "materialId") Long materialId, @RequestParam(value = "userId") Long userId) {
        return toAjax(weChatCollectionService.cancleCollection(materialId, userId));
    }

}
