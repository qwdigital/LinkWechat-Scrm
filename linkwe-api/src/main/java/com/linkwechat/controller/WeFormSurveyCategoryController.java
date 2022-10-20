package com.linkwechat.controller;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.material.entity.WeCategory;
import com.linkwechat.service.IWeFormSurveyCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 智能表单分组
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/13 9:33
 */
@RestController
@RequestMapping("/form/category")
public class WeFormSurveyCategoryController {

    @Resource
    private IWeFormSurveyCategoryService weFormSurveyCategoryService;

    /**
     * 添加类目
     */
    @Log(title = "添加类目", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("添加类目")
    public AjaxResult add(@Validated @RequestBody WeCategory category) {
        weFormSurveyCategoryService.insertWeCategory(category);
        return AjaxResult.success();
    }

    /**
     * 更新目录
     */
    @Log(title = "更新目录", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("更新目录")
    public AjaxResult edit(@Validated @RequestBody WeCategory category) {
        weFormSurveyCategoryService.updateWeCategory(category);
        return AjaxResult.success();
    }

    /**
     * 删除类目
     */
    @Log(title = "删除类目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除类目")
    public AjaxResult remove(@PathVariable Long[] ids) {
        weFormSurveyCategoryService.deleteWeCategoryById(ids);
        return AjaxResult.success();
    }


}
