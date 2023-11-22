package com.linkwechat.scheduler.controller;

import com.linkwechat.service.IWeMomentsTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private IWeMomentsTaskService iWeMomentsTaskService;

    @GetMapping("/getXXX")
    public void getXX(){

        iWeMomentsTaskService.syncWeMomentsHandler(null);

    }
}
