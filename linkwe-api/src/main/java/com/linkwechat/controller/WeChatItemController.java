package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.LinkMediaQuery;
import com.linkwechat.domain.side.WeChatItem;
import com.linkwechat.domain.side.dto.WeChatItemDto;
import com.linkwechat.domain.side.query.WeChatItemQuery;
import com.linkwechat.domain.side.vo.WeChatSideVo;
import com.linkwechat.service.IWeChatItemService;
import com.linkwechat.service.IWeMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 聊天工具侧边栏
 *
 * @author kewen
 */
@RestController
@RequestMapping(value = "/chat/item")
public class WeChatItemController extends BaseController {

    @Autowired
    private IWeChatItemService weChatItemService;

    @Autowired
    private IWeMaterialService materialService;


    /**
     * 获取侧边栏选中或者未选中素材
     * @param query
     * @return
     */
    @GetMapping("/mList")
    public TableDataInfo mList(WeChatItemQuery query) {
        startPage();
        List<WeMaterial> list=materialService.findWeMaterials(LinkMediaQuery.builder()
                .categoryId(query.getCategoryId())
                .search(query.getSearch())
                .mediaType(query.getMediaType())
                .build());
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
    @Log(title = "侧边栏抓取素材", businessType = BusinessType.INSERT)
    @PutMapping
    public AjaxResult add(@RequestBody WeChatItemDto chatItemDto) {
        weChatItemService.checkItems(chatItemDto);
        return AjaxResult.success();
    }

    /**
     * h5素材列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WeChatItemQuery query) {
        startPage();
        return getDataTable(
                weChatItemService.chatItems(query.getSideId(),query.getKeyword(),query.getMediaType(),query.getUserId())
        );
    }





}

