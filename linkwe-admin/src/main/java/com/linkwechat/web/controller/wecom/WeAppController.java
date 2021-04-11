package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.wecom.domain.WeApp;
import com.linkwechat.wecom.service.IWeAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wecom/weapp")
public class WeAppController extends BaseController {

    @Autowired
    IWeAppService iWeAppService;


    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(
                iWeAppService.list()
        );
    }



    @PutMapping("/updateWeApp")
    public AjaxResult updateWeApp(@RequestBody WeApp weApp){

        iWeAppService.updateById(weApp);

        return AjaxResult.success();
    }








}
