package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.qr.query.WxLxQrQuery;
import com.linkwechat.domain.qr.vo.WxLxQrCodeVo;
import com.linkwechat.service.IWeLxQrCodeService;
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
 * @description 拉新活码管理
 * @date 2023/03/06 18:22
 **/

@RestController
@RequestMapping(value = "lxqr")
@Api(tags = "拉新活码管理")
public class WxLxQrCodeController extends BaseController {

    @Autowired
    private IWeLxQrCodeService weLxQrCodeService;


    @ApiOperation(value = "获取活码", httpMethod = "POST")
    @PostMapping("/getQrcode")
    public AjaxResult<WxLxQrCodeVo> getQrcode(@RequestBody @Validated WxLxQrQuery query) {
        WxLxQrCodeVo lxQrCode = weLxQrCodeService.getQrcode(query);
        return AjaxResult.success(lxQrCode);
    }

    @ApiOperation(value = "领取红包或卡券", httpMethod = "POST")
    @PostMapping("/receive/award")
    public AjaxResult<WxLxQrCodeVo> receiveAward(@RequestBody @Validated WxLxQrQuery query) {
        WxLxQrCodeVo lxQrCode = weLxQrCodeService.getQrcode(query);
        return AjaxResult.success(lxQrCode);
    }

}
