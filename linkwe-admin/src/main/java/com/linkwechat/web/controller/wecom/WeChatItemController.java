package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.wecom.domain.WeChatItem;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.dto.WeChatItemDto;
import com.linkwechat.wecom.domain.vo.WeChatSideVo;
import com.linkwechat.wecom.service.IWeChatItemService;
import com.linkwechat.wecom.service.IWeMaterialService;
import com.linkwechat.wecom.service.IWePosterService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 聊天工具侧边栏
 *
 * @author kewen
 */
@RequestMapping(value = "/wecom/chat/item")
@RestController
public class WeChatItemController extends BaseController {

    @Autowired
    private IWeChatItemService weChatItemService;

    @Autowired
    private IWeMaterialService materialService;

    @Resource
    private IWePosterService wePosterService;





    //   @PreAuthorize("@ss.hasPermi('wecom:material:list')")
    @GetMapping("/mList")
    @ApiOperation("获取侧边栏选中或者未选中素材")
    public TableDataInfo mList(@RequestParam(value = "categoryId", required = false) String categoryId
            , @RequestParam(value = "search", required = false) String search,@RequestParam(value = "mediaType") String mediaType) {
        startPage();
        List<WeMaterial> list;
        if(StringUtils.isNotBlank(mediaType) && mediaType.equals(MediaType.POSTER.getType())){
            list = wePosterService.list(StringUtils.isBlank(categoryId)?null:Long.valueOf(categoryId),search).stream().map(wePoster -> {
                WeMaterial weMaterial = new WeMaterial();
                weMaterial.setMaterialName(wePoster.getTitle());
                weMaterial.setMaterialUrl(wePoster.getSampleImgPath());
                weMaterial.setCategoryId(wePoster.getCategoryId());
                weMaterial.setId(wePoster.getId());
                return weMaterial;
            }).collect(Collectors.toList());
        }else {
            list = materialService.findWeMaterials(categoryId, search,mediaType);
        }

        if(CollectionUtil.isNotEmpty(list)){
            List<WeChatItem> weChatItems = weChatItemService.list(new LambdaQueryWrapper<WeChatItem>()
                    .in(WeChatItem::getMaterialId,list.stream().map(WeMaterial::getId).collect(Collectors.toList())));
            if(CollectionUtil.isNotEmpty(weChatItems)){
                list.stream().forEach(k->{
                    k.setIsCheck(
                            weChatItems.stream().filter(item -> item.getMaterialId()
                                    .equals(k.getId())).findFirst().isPresent()?true:false
                    );
                });
            }
        }

        return getDataTable(list);
    }


    /**
     * 侧边栏抓取素材
     */
//    @PreAuthorize("@ss.hasPermi('chat:item:add')")
   @Log(title = "侧边栏抓取素材", businessType = BusinessType.INSERT)
    @PutMapping
    public AjaxResult add(@RequestBody WeChatItemDto chatItemDto) {
        return toAjax(weChatItemService.checkItems(chatItemDto));
    }

    /**
     * h5素材列表
     */
    //@PreAuthorize("@ss.hasPermi('chat:item:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "sideId") Long sideId
            , @RequestParam(value = "keyword", required = false) String keyword,@RequestParam(value = "mediaType") String mediaType,@RequestParam(value = "userId") String userId) {
        startPage();
        List<WeChatSideVo> weChatSideVos;
        if(mediaType.equals("5")){
            weChatSideVos=weChatItemService.findChatPostsItems(sideId,keyword,mediaType,userId);
        }else{
            weChatSideVos
                    = weChatItemService.chatItems(sideId,keyword,mediaType,userId);
        }
        return getDataTable(weChatSideVos);
    }





}
