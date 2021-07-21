package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.dto.WeChatCollectionDto;
import com.linkwechat.wecom.domain.vo.WeChatSideVo;
import com.linkwechat.wecom.service.IWeChatCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    //@PreAuthorize("@ss.hasPermi('chat:collection:add')")
    @Log(title = "添加收藏", businessType = BusinessType.INSERT)
    @PostMapping("addCollection")
    public AjaxResult addCollection(@RequestBody WeChatCollectionDto chatCollectionDto) {
        boolean b =
                weChatCollectionService.addCollection(chatCollectionDto.getMaterialId(), chatCollectionDto.getUserId());
        if(weChatCollectionService.addCollection(chatCollectionDto.getMaterialId(), chatCollectionDto.getUserId())){
           return AjaxResult.success();
        }
        return  AjaxResult.success("当前素材不可重复收藏");
    }


    /**
     * 取消收藏
     */
   // @PreAuthorize("@ss.hasPermi('chat:collection:delete')")
   // @Log(title = "取消收藏", businessType = BusinessType.UPDATE)
    @PostMapping(value = "cancleCollection")
    public AjaxResult cancleCollection(@RequestBody WeChatCollectionDto chatCollectionDto) {
        return toAjax(weChatCollectionService.cancleCollection(chatCollectionDto.getMaterialId(), chatCollectionDto.getUserId()));
    }

    /**
     * 收藏列表
     */
  //  @PreAuthorize("@ss.hasPermi('chat:collection:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "userId") String userId,@RequestParam(value = "keyword",required = false) String keyword) {
        startPage();
        List<WeChatSideVo> collections = weChatCollectionService.collections(userId,keyword);
        return getDataTable(collections);
    }

}
