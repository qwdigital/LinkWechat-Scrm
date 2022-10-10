package com.linkwechat.controller;

import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.config.WeSideBarConfig;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeSideBarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@RequestMapping(value = "/config")
public class WeConfigController {

    @Autowired
    LinkWeChatConfig linkWeChatConfig;


    /**
     * 获取侧边栏相关配置
     * @return
     */
    @GetMapping("/findWeSideBar")
    public AjaxResult<WeSideBarVo> findWeSideBar(){

        WeSideBarConfig weSideBarConfig = linkWeChatConfig.getWeSideBarConfig();

        return AjaxResult.success(
                WeSideBarVo.builder()
                        .materialName(weSideBarConfig.getMaterial().getMaterialName())
                        .materialUrl(weSideBarConfig.getMaterial().getMaterialUrl())
                        .wordGroupName(weSideBarConfig.getWordGroup().getWordGroupName())
                        .wordGroupUrl(weSideBarConfig.getWordGroup().getWordGroupUrl())
                        .customerPortraitName(weSideBarConfig.getCustomerPortrait().getCustomerPortraitName())
                        .customerPortraitUrl(weSideBarConfig.getCustomerPortrait().getCustomerPortraitUrl())
                        .redEnvelopesName(weSideBarConfig.getRedEnvelopes().getRedEnvelopesName())
                        .redEnvelopesUrl(weSideBarConfig.getRedEnvelopes().getRedEnvelopesUrl())
                        .build()
        );


    }


}
