package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.fission.WeFissionInviterPoster;
import com.linkwechat.domain.fission.vo.WeFissionProgressVo;
import com.linkwechat.service.IWeFissionInviterRecordSubService;
import com.linkwechat.service.IWeFissionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 任务裂变(微信)
 *
 * @author leejoker
 * @date 2021-01-20
 */
@RestController
@RequestMapping("/fission")
public class WxTaskFissionController extends BaseController {



    @Autowired
    private IWeFissionService iWeFissionService;


    @Autowired
    private IWeFissionInviterRecordSubService iWeFissionInviterRecordSubService;


    /**
     * 获取生成指定客户的裂变海报
     * @param unionid 微信客户unionid
     * @param fissionId 裂变id
     * @return
     */
    @GetMapping("/findFissionPoster")
    public AjaxResult<WeFissionInviterPoster> findFissionPoster(String unionid, String fissionId) throws Exception {




        return AjaxResult.success(
                iWeFissionService.findFissionPoster(unionid, fissionId)
        );

    }


    /**
     * 获取裂变进度
     * @param unionid 微信客户unionid
     * @param fissionId 裂变id
     * @return
     */
    @GetMapping("/findWeFissionProgress")
    public AjaxResult<WeFissionProgressVo> findWeFissionProgress(String unionid, String fissionId){

        return AjaxResult.success(
                iWeFissionInviterRecordSubService.findWeFissionProgress(unionid, fissionId)
        );
    }






}
