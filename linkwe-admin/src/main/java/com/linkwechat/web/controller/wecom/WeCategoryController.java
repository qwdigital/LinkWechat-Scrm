package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeCategory;
import com.linkwechat.wecom.service.IWeCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业微信素材分类Controller
 *
 * @author KEWEN
 * @date 2020-10-08
 */
@RestController
@RequestMapping("/wecom/category")
public class WeCategoryController extends BaseController {

    @Autowired
    private IWeCategoryService weCategoryService;

    /**
     * 类目树
     */
//    @PreAuthorize("@ss.hasPermi('wechat:category:list')")
    @GetMapping("/list")
    @ApiOperation("类目树")
    public AjaxResult list(@RequestParam("mediaType") String mediaType) {
        return AjaxResult.success(weCategoryService.findWeCategoryByMediaType(mediaType));
    }

    /**
     * 通过id查询类目详细信息
     */
//    @PreAuthorize("@ss.hasPermi('wechat:category:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("通过id查询类目详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weCategoryService.findWeCategoryById(id));
    }

    /**
     * 添加类目
     */
//    @PreAuthorize("@ss.hasPermi('wechat:category:add')")
    @Log(title = "添加类目", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("添加类目")
    public AjaxResult add(@RequestBody WeCategory category) {
        return toAjax(weCategoryService.insertWeCategory(category));
    }

    /**
     * 更新目录
     */
//    @PreAuthorize("@ss.hasPermi('wechat:category:edit')")
    @Log(title = "更新目录", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("更新目录")
    public AjaxResult edit(@RequestBody WeCategory category) {
        return toAjax(weCategoryService.updateWeCategory(category));
    }


    /**
     * 删除类目
     */
//    @PreAuthorize("@ss.hasPermi('wechat:category:remove')")
    @Log(title = "删除类目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除类目")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(weCategoryService.deleteWeCategoryByIds(ids));
    }

}
