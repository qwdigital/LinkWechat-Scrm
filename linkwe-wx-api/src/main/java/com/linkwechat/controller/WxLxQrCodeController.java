package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.qr.query.WxLxQrQuery;
import com.linkwechat.domain.qr.vo.WxLxQrCodeVo;
import com.linkwechat.service.IWeLxQrCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public AjaxResult receiveAward(@RequestBody @Validated WxLxQrQuery query) throws Exception {
        weLxQrCodeService.receiveAward(query);
        return AjaxResult.success();
    }

    /**
     * 校验客户是否领取红包或卡券
     * @param query 入参
     * @return
     */
    @ApiOperation(value = "校验客户是否领取红包或卡券", httpMethod = "POST")
    @PostMapping("/checkIsReceive")
    public AjaxResult checkIsReceive(@RequestBody WxLxQrQuery query){
        Boolean result = weLxQrCodeService.checkIsReceive(query);
        return AjaxResult.success(result);
    }

    /**
     * 红包领取记录
     * @param query
     * @return
     */
    @ApiOperation(value = "红包领取记录", httpMethod = "POST")
    @PostMapping("/getReceiveList")
    public AjaxResult getReceiveList(@RequestBody WxLxQrQuery query){
        weLxQrCodeService.getReceiveList(query);
        return AjaxResult.success();
    }

}
