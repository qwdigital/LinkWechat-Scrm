package com.linkwechat.controller;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.qr.query.WeQrAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkQuery;
import com.linkwechat.service.IWeShortLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author danmo
 * @description 短链管理
 * @date 2022/12/18 18:22
 **/

@RestController
@RequestMapping(value = "/short/link")
@Api(tags = "短链管理")
public class WeShortLinkController extends BaseController {

    @Autowired
    private IWeShortLinkService weShortLinkService;

    @ApiOperation(value = "新增短链", httpMethod = "POST")
    @Log(title = "新增短链", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addShortLink(@RequestBody @Validated WeShortLinkAddQuery query) {
        weShortLinkService.addShortLink(query);
        return AjaxResult.success();
    }


    @ApiOperation(value = "修改短链", httpMethod = "PUT")
    @Log(title = "修改短链", businessType = BusinessType.UPDATE)
    @PutMapping("/update/{id}")
    public AjaxResult updateShortLink(@PathVariable("id") Long id, @RequestBody @Validated WeShortLinkAddQuery query) {
        query.setId(id);
        weShortLinkService.updateShortLink(query);
        return AjaxResult.success();
    }


    @ApiOperation(value = "删除短链", httpMethod = "DELETE")
    @Log(title = "删除短链", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult deleteShortLink(@PathVariable("ids") List<Long> ids) {
        weShortLinkService.deleteShortLink(ids);
        return AjaxResult.success();
    }

    @ApiOperation(value = "短链详情", httpMethod = "GET")
    @Log(title = "短链详情", businessType = BusinessType.SELECT)
    @GetMapping("/get/{id}")
    public AjaxResult getShortLinkInfo(@PathVariable("id") Long id) {
        weShortLinkService.getShortLinkInfo(id);
        return AjaxResult.success();
    }


    @ApiOperation(value = "短链列表", httpMethod = "GET")
    @Log(title = "短链列表", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    public AjaxResult getShortLinkList(WeShortLinkQuery query) {
        weShortLinkService.getShortLinkList(query);
        return AjaxResult.success();
    }

}
