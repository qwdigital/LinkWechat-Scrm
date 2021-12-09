package com.linkwechat.web.controller.wecom;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.Tree;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeCategory;
import com.linkwechat.wecom.domain.vo.WeCategoryVo;
import com.linkwechat.wecom.service.IWeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业微信素材分类Controller
 *
 * @author KEWEN
 * @date 2020-10-08
 */
@RestController
@RequestMapping("/wecom/category")
@Api(tags = "企业微信素材分类")
public class WeCategoryController extends BaseController {

    @Autowired
    private IWeCategoryService weCategoryService;



//    @PreAuthorize("@ss.hasPermi('wechat:category:list')")
    @GetMapping("/list")
    @ApiOperation("类目树")
    public AjaxResult<WeCategoryVo> list(@RequestParam("mediaType") String mediaType) {
        return AjaxResult.success(weCategoryService.findWeCategoryByMediaType(mediaType));
    }

    /**
     * 通过id查询类目详细信息
     */
//    @PreAuthorize("@ss.hasPermi('wechat:category:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("通过id查询类目详细信息")
    public AjaxResult<WeCategoryVo> getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weCategoryService.getById(id));
    }

    /**
     * 添加类目
     */
//    @PreAuthorize("@ss.hasPermi('wechat:category:add')")
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
//    @PreAuthorize("@ss.hasPermi('wechat:category:edit')")
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
//    @PreAuthorize("@ss.hasPermi('wechat:category:remove')") Constants.DELETE_CODE
    @Log(title = "删除类目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除类目")
    public AjaxResult remove(@PathVariable Long[] ids) {
        weCategoryService.deleteWeCategoryById(ids);
//        List<WeCategory> categorys=new ArrayList<>();
//        for (Long id:ids) {
//            categorys.add(
//                    WeCategory.builder()
//                            .id(id)
//                            .delFlag("55")
//                            .build()
//            );
//        }
//        weCategoryService.updateBatchById(categorys);

        return AjaxResult.success();
    }



}
