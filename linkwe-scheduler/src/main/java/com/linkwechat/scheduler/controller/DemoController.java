package com.linkwechat.scheduler.controller;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.scheduler.service.SopTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private SopTaskService sopTaskService;


    @GetMapping("/getXXX")
    public AjaxResult getXXX(){
        sopTaskService.handleChangeSop();
        return AjaxResult.success();
    }



}
