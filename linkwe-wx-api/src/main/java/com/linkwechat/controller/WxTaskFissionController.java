package com.linkwechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.fission.WeFissionInviterPoster;
import com.linkwechat.domain.taskfission.query.WeTaskFissionQuery;
import com.linkwechat.domain.taskfission.vo.WeTaskFissionProgressVo;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeFissionService;
import com.linkwechat.service.IWeTaskFissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 任务宝Controller
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Api(tags = "任务宝Controller")
@RestController
@RequestMapping("/fission")
public class WxTaskFissionController extends BaseController {

    @Autowired
    private IWeTaskFissionService weTaskFissionService;


    @Autowired
    private IWeFissionService iWeFissionService;


    /**
     * 生成带二维码的海报
     */
    @ApiOperation(value = "生成带二维码的海报", httpMethod = "POST")
    @PostMapping("/poster")
    public AjaxResult<JSONObject> posterGenerate(@RequestBody WeTaskFissionQuery query) {
        String posterUrl = weTaskFissionService.fissionPosterGenerate(query);
        JSONObject json = new JSONObject();
        json.put("posterUrl", posterUrl);
        return AjaxResult.success(json);
    }

    @ApiOperation(value = "根据任务id获取任务进度", httpMethod = "POST")
    @PostMapping("/progress")
    public AjaxResult<WeTaskFissionProgressVo> getTaskProgress(@RequestBody WeTaskFissionQuery query) {
        WeTaskFissionProgressVo taskProgress = weTaskFissionService.getTaskProgress(query);
        return AjaxResult.success(taskProgress);
    }


    /**
     * 获取生成指定客户的裂变海报
     * @param unionid
     * @param fissionId
     * @return
     */
    @GetMapping("/findFissionPoster")
    public AjaxResult<WeFissionInviterPoster> findFissionPoster(String unionid, String fissionId){


        return AjaxResult.success(
                iWeFissionService.findFissionPoster(unionid, fissionId)
        );

    }



}
