package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.MediaType;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WePoster;
import com.linkwechat.wecom.domain.WePosterSubassembly;
import com.linkwechat.wecom.service.IWePosterFontService;
import com.linkwechat.wecom.service.IWePosterService;
import com.linkwechat.wecom.service.IWePosterSubassemblyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ws
 */
@RestController
@RequestMapping(value = "wecom/poster/")
@Api(description = "海报")
public class WePosterController extends BaseController {


    @Resource
    private IWePosterService wePosterService;

    @Resource
    private IWePosterSubassemblyService wePosterSubassemblyService;

    @Resource
    private IWePosterFontService wePosterFontService;

    @PostMapping(value = "insert")
    @ApiOperation("创建海报")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult insert(@RequestBody WePoster poster) {
        wePosterService.generateSimpleImg(poster);
        poster.setMediaType(MediaType.POSTER.getType());
        wePosterService.saveOrUpdate(poster);
        if(CollectionUtil.isNotEmpty(poster.getPosterSubassemblyList())) {
            poster.getPosterSubassemblyList().forEach(wePosterSubassembly -> {
                wePosterSubassembly.setPosterId(poster.getId());
            });
            wePosterSubassemblyService.saveBatch(poster.getPosterSubassemblyList());
        }
        return AjaxResult.success("创建成功");
    }


    @PutMapping(value = "update")
    @ApiOperation("修改海报")
    //  @PreAuthorize("@ss.hasAnyPermi('wecom:poster:update')")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult update(@RequestBody WePoster poster) {
        if (poster.getId() == null) {
            return AjaxResult.error("id为空");
        }
        poster.setMediaType(null);
        wePosterService.generateSimpleImg(poster);
        wePosterService.saveOrUpdate(poster);
        List<WePosterSubassembly> posterSubassemblyList = wePosterSubassemblyService.lambdaQuery().eq(WePosterSubassembly::getPosterId, poster.getId()).eq(WePosterSubassembly::getDelFlag, 0).list();
        Map<Long, WePosterSubassembly> posterSubassemblyMap = posterSubassemblyList.stream().collect(Collectors.toMap(WePosterSubassembly::getId, p -> p));
        if(!CollectionUtils.isEmpty(poster.getPosterSubassemblyList())) {
            List<WePosterSubassembly> insertList = new ArrayList<>();
            List<WePosterSubassembly> updateList = new ArrayList<>();
            poster.getPosterSubassemblyList().forEach(wePosterSubassembly -> {
                if (wePosterSubassembly.getId() == null) {
                    wePosterSubassembly.setPosterId(poster.getId());
                    insertList.add(wePosterSubassembly);
                } else {
                    posterSubassemblyMap.remove(wePosterSubassembly.getId());
                    updateList.add(wePosterSubassembly);
                }
            });
            if (!CollectionUtils.isEmpty(insertList)) {
                wePosterSubassemblyService.saveBatch(insertList);
            }
            if (!CollectionUtils.isEmpty(updateList)) {
                wePosterSubassemblyService.updateBatchById(updateList);
            }
        }
        List<WePosterSubassembly> deleteList = new ArrayList<>(posterSubassemblyMap.values());
        if (!CollectionUtils.isEmpty(deleteList)) {
            wePosterSubassemblyService.update(Wrappers.lambdaUpdate(WePosterSubassembly.class).set(WePosterSubassembly::getDelFlag,1).in(WePosterSubassembly::getId,deleteList.stream().map(WePosterSubassembly::getId).collect(Collectors.toList())));
        }
        return AjaxResult.success("修改成功");
    }



    @GetMapping(value = "entity/{id}")
    //    @PreAuthorize("@ss.hasAnyPermi('wecom:poster:entity')")
    @ApiOperation("查询海报详情")
    public AjaxResult entity(@PathVariable Long id) {
        return AjaxResult.success(wePosterService.selectOne(id));
    }

    @GetMapping(value = "page")
    @ApiOperation("分页查询海报")
    //    @PreAuthorize("@ss.hasAnyPermi('wecom:poster:page')")
    public AjaxResult page(Long categoryId, String name) {
        startPage();
        List<WePoster> fontList = wePosterService.lambdaQuery()
                .eq(WePoster::getDelFlag, 0)
                .eq(categoryId != null, WePoster::getCategoryId, categoryId)
                .like(StringUtils.isNotBlank(name), WePoster::getTitle, name)
                .orderByDesc(WePoster::getCreateTime)
                .list();
        return AjaxResult.success(getDataTable(fontList));
    }

    @DeleteMapping(value = "delete/{id}")
    @ApiOperation(value = "删除海报")
    //    @PreAuthorize("@ss.hasAnyPermi('wecom:poster:delete')")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult deletePosterFont(@PathVariable Long id) {
        wePosterService.update(
                Wrappers.lambdaUpdate(WePoster.class).set(WePoster::getDelFlag, 1).eq(WePoster::getId, id));
        wePosterSubassemblyService.update(
                Wrappers.lambdaUpdate(WePosterSubassembly.class).set(WePosterSubassembly::getDelFlag, 1).eq(WePosterSubassembly::getPosterId, id)
        );
        return AjaxResult.success("删除成功");
    }


}
