package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.service.IWeLxQrCodeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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


}
