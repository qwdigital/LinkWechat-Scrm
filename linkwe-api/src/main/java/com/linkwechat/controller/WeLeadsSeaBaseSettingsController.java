package com.linkwechat.controller;


import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.leads.sea.query.WeLeadsSeaBaseSettingsRequest;
import com.linkwechat.domain.leads.sea.vo.WeLeadsSeaBaseSettingsVO;
import com.linkwechat.service.IWeLeadsSeaBaseSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 公海基础配置表(WeSeaBaseSettings)表控制层
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/17 17:12
 */
@Api(tags = "公海基础配置")
@Validated
@RestController
@RequestMapping(value = "/sea/Base/settings")
public class WeLeadsSeaBaseSettingsController {

    @Resource
    private IWeLeadsSeaBaseSettingsService weLeadsSeaBaseSettingsService;

    @ApiOperation(value = "查询")
    @GetMapping(value = "")
    public AjaxResult<WeLeadsSeaBaseSettingsVO> query() {
        return AjaxResult.success(weLeadsSeaBaseSettingsService.queryBaseSetting());
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "")
    public AjaxResult update(@RequestBody @Validated WeLeadsSeaBaseSettingsRequest param) {
        return AjaxResult.success(weLeadsSeaBaseSettingsService.updateBaseSetting(param));
    }
}

