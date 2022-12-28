package com.linkwechat.controller;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.qr.query.WeQrAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkAddQuery;
import com.linkwechat.service.IWeShortLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author danmo
 * @description 短链管理
 * @date 2022/12/18 18:22
 **/

@RestController
@RequestMapping(value = "/short/link")
@Api(tags = "活码管理")
public class WeShortLinkController extends BaseController {

    @Autowired
    private IWeShortLinkService weShortLinkService;

    @ApiOperation(value = "新增短链", httpMethod = "POST")
    @Log(title = "活码管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addShortLink(@RequestBody @Validated WeShortLinkAddQuery weQrAddQuery) {
        return AjaxResult.success();
    }


}
