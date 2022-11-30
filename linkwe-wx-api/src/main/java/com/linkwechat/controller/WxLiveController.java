package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.live.WeLive;
import com.linkwechat.domain.live.query.WeLiveQuery;
import com.linkwechat.domain.live.vo.WeLinveUserVo;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.IWeLiveService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Api(tags = "客服管理")
@RestController
@RequestMapping("/live/")
public class WxLiveController extends BaseController {


    @Autowired
    IWeLiveService iWeLiveService;



    @Autowired
    QwSysUserClient qwSysUserClient;






    /**
     * 获取直播凭证
     * @param liveQuery
     * @return
     */
    @GetMapping("/getLivingCode")
    public AjaxResult getLivingCode(WeLiveQuery liveQuery){
        WeLinveUserVo weLinveUserVo=new WeLinveUserVo();

        WeLive weLive = iWeLiveService.findLiveDetail(liveQuery.getLivingId());

        if(null != weLive){


            weLinveUserVo.setWeLive(weLive);
            weLinveUserVo.setLiveCode(iWeLiveService.getLivingCode(weLive.getLivingId(), liveQuery.getOpenid()));
        }


        return AjaxResult.success(
                weLinveUserVo
        );
    }
}
