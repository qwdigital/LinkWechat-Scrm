package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.WePosterQuery;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionVo;
import com.linkwechat.service.IWeMaterialService;
import com.linkwechat.service.IWeShortLinkPromotionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 短链推广
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/07 15:27
 */
@Api(tags = "短链推广")
@RestController
@RequestMapping("/short/link/promotion")
public class WeShortLinkPromotionController extends BaseController {

    @Resource
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeMaterialService weMaterialService;

    /**
     * 短链推广列表
     *
     * @param query 查询条件
     * @return
     */
    @ApiOperation(value = "短链推广列表", httpMethod = "GET")
    @Log(title = "短链推广列表", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    public TableDataInfo<WeShortLinkPromotionVo> list(WeShortLinkPromotionQuery query) {
        startPage();
        List<WeShortLinkPromotionVo> list = weShortLinkPromotionService.selectList(query);
        return getDataTable(list);
    }


    @ApiOperation(value = "新增短链推广", httpMethod = "POST")
    @Log(title = "新增短链推广", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody WeShortLinkPromotionAddQuery query) {
        try {
            Long id = weShortLinkPromotionService.add(query);
            return AjaxResult.success(id);
        } catch (IOException e) {
            logger.error("新建短链推广失败：{}", e.getMessage());
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    @Log(title = "暂存短链推广", businessType = BusinessType.INSERT)
    @ApiOperation(value = "暂存短链推广", httpMethod = "POST")
    @PostMapping("/ts")
    public AjaxResult temporaryStorage(@Validated @RequestBody WeShortLinkPromotionAddQuery query) {
        try {
            Long id = weShortLinkPromotionService.ts(query);
            return AjaxResult.success(id);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("暂存推广短链失败：{}", e.getMessage());
            return AjaxResult.error();
        }
    }

    @Log(title = "修改短链推广", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改", httpMethod = "PUT")
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody WeShortLinkPromotionUpdateQuery query) {
        weShortLinkPromotionService.edit(query);
        return AjaxResult.success();
    }

    @Log(title = "删除短链推广", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @PutMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        LambdaUpdateWrapper<WeShortLinkPromotion> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(WeShortLinkPromotion::getId, id);
        updateWrapper.set(WeShortLinkPromotion::getDelFlag, 1);
        boolean update = weShortLinkPromotionService.update(updateWrapper);
        return AjaxResult.success();
    }

    @Log(title = "删除短链推广", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @PutMapping("/batch/delete")
    public AjaxResult batchDelete(List<Long> ids) {
        LambdaUpdateWrapper<WeShortLinkPromotion> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.in(WeShortLinkPromotion::getId, ids);
        updateWrapper.set(WeShortLinkPromotion::getDelFlag, 1);
        boolean update = weShortLinkPromotionService.update(updateWrapper);
        return AjaxResult.success();
    }


    @ApiOperation(value = "获取包含占位码组件的海报", httpMethod = "GET")
    @Log(title = "获取包含占位码组件的海报", businessType = BusinessType.SELECT)
    @GetMapping("/poster/list")
    public TableDataInfo<WeMaterialVo> posterList(WePosterQuery query) {
        startPage();
        LambdaQueryWrapper<WeMaterial> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(WeMaterial::getId, WeMaterial::getMaterialUrl, WeMaterial::getMaterialName);
        //只取带有占位码组件的海报
        queryWrapper.eq(WeMaterial::getPosterQrType, 3);
        queryWrapper.eq(WeMaterial::getDelFlag, 0);
        queryWrapper.like(StrUtil.isNotBlank(query.getTitle()), WeMaterial::getMaterialName, query.getTitle());
        List<WeMaterial> list = weMaterialService.list(queryWrapper);
        List<WeMaterialVo> weMaterialVos = BeanUtil.copyToList(list, WeMaterialVo.class);
        return getDataTable(weMaterialVos);
    }

    @ApiOperation(value = "推广", httpMethod = "GET")
    @GetMapping("/spread")
    public AjaxResult promotion() {

        return AjaxResult.success();
    }


}

