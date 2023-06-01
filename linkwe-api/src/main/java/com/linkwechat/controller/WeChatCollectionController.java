package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.material.ao.WePoster;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.LinkMediaCollectQuery;
import com.linkwechat.domain.side.vo.WeChatSideVo;
import com.linkwechat.service.IWeChatCollectionService;
import com.linkwechat.service.IWeMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天工具 侧边栏 素材收藏
 *
 * @author danmo
 */
@Api(tags = "素材收藏接口")
@RequestMapping(value = "/chat/collection")
@RestController
public class WeChatCollectionController extends BaseController {


    @Autowired
    private IWeChatCollectionService weChatCollectionService;

    @Autowired
    private IWeMaterialService weMaterialService;


    /**
     * 添加收藏
     */
    @ApiOperation(value = "添加收藏",httpMethod = "POST")
    @PostMapping("addCollection")
    public AjaxResult addCollection(@RequestBody LinkMediaCollectQuery query) {
        weChatCollectionService.addCollection(query.getMaterialId());
        return  AjaxResult.success();
    }


    /**
     * 取消收藏
     */
    @ApiOperation(value = "取消收藏",httpMethod = "POST")
    @PostMapping(value = "cancleCollection")
    public AjaxResult cancleCollection(@RequestBody LinkMediaCollectQuery query) {
        weChatCollectionService.cancleCollection(query.getMaterialId());
        return  AjaxResult.success();
    }

    /**
     * 收藏列表
     */
    @ApiOperation(value = "收藏列表",httpMethod = "GET")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "keyword",required = false) String keyword) {
        startPage();
        List<WeChatSideVo> collections = weChatCollectionService.collections(SecurityUtils.getUserId(),keyword);
        if(CollectionUtil.isNotEmpty(collections)){
            collections.forEach(k->{
                if(StringUtils.isEmpty(k.getMediaType())){//为空则为海报
                    WeMaterial weMaterial = weMaterialService.getById(k.getMaterialId());
                    if(null != weMaterial){
                        k.setMediaType(MediaType.POSTER.getType());
                        k.setMaterialName(weMaterial.getMaterialName());
                        k.setMaterialUrl(weMaterial.getMaterialUrl());
                    }
                }
            });
        }
        return getDataTable(collections);
    }

}
