package com.linkwechat.controller;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.material.entity.WeCategory;
import com.linkwechat.domain.material.vo.WeCategoryNewVo;
import com.linkwechat.domain.material.vo.WeCategoryVo;
import com.linkwechat.service.IWeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 企业微信素材分类Controller
 *
 * @author leejoker
 * @date 2022-04-30
 */
@RestController
@RequestMapping("category")
@Api(tags = "企业微信素材分类")
public class WeCategoryController extends BaseController {

    @Resource
    private IWeCategoryService weCategoryService;

    /**
     * 类目树
     *
     * @param mediaType 媒体类型 {@link com.linkwechat.common.enums.CategoryMediaType}
     * @param scene     应用场景 默认值-1, 1朋友圈
     * @return {@link AjaxResult< WeCategoryVo>}
     * @author WangYX
     * @date 2023/06/27 16:17
     */
    @GetMapping("/list")
    @ApiOperation("类目树")
    public AjaxResult<WeCategoryVo> list(@RequestParam("mediaType") String mediaType, @RequestParam(value = "scene", required = false, defaultValue = "-1") Integer scene) {
        return AjaxResult.success(weCategoryService.findWeCategoryByMediaType(mediaType, scene));
    }

    /**
     * 通过id查询类目详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("通过id查询类目详细信息")
    public AjaxResult<WeCategoryVo> getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weCategoryService.getById(id));
    }

    /**
     * 添加类目
     */
    @Log(title = "添加类目", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("添加类目")
    public AjaxResult add(@Validated @RequestBody WeCategory category) {
        weCategoryService.insertWeCategory(category);
        return AjaxResult.success();
    }

    /**
     * 更新目录
     */
    @Log(title = "更新目录", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("更新目录")
    public AjaxResult edit(@Validated @RequestBody WeCategory category) {
        weCategoryService.updateWeCategory(category);
        return AjaxResult.success();
    }


    /**
     * 删除类目
     */
    @Log(title = "删除类目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除类目")
    public AjaxResult remove(@PathVariable Long[] ids) {
        weCategoryService.deleteWeCategoryByIds(ids);
        return AjaxResult.success();
    }

    @Log(title = "批量删除或移动", businessType = BusinessType.DELETE)
    @DeleteMapping("/delOrMuchMove")
    @ApiOperation("批量删除或移动")
    public AjaxResult delOrMuchMove(@RequestBody WeCategoryNewVo weCategoryNewVo) {
        weCategoryService.delOrMuchMove(weCategoryNewVo);
        return AjaxResult.success();
    }


}
