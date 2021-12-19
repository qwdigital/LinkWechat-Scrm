package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.quartz.task.PageHomeDataTask;
import com.linkwechat.wecom.domain.dto.WePageStaticDataDto;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import com.linkwechat.wecom.service.IWeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author danmo
 * @description 首页统计
 * @date 2021/2/23 15:30
 **/
@Api(tags = "首页统计")
@Slf4j
@RestController
@RequestMapping("wecom/page/")
public class WePageDateContraller {
   @Autowired
   private RedisCache redisCache;
   @Autowired
   private PageHomeDataTask pageHomeDataTask;

    /**
     *
     */
    @ApiOperation(value = "数据总览controller",httpMethod = "GET")
    //  @PreAuthorize("@ss.hasPermi('wecom:page:getCorpBasicData')")
    @GetMapping("/getCorpBasicData")
    public AjaxResult getCorpBasicData(){
        return AjaxResult.success(redisCache.getCacheMap("getCorpBasicData"));
    }

    @ApiOperation(value = "实时数据controller",httpMethod = "GET")
    //  @PreAuthorize("@ss.hasPermi('wecom:page:getCorpRealTimeData')")
    @GetMapping("/getCorpRealTimeData")
    public AjaxResult<WePageStaticDataDto> getCorpRealTimeData(){
        WePageStaticDataDto wePageStaticDataDto = redisCache.getCacheObject("getCorpRealTimeData");
        return AjaxResult.success(wePageStaticDataDto);
    }

    @ApiOperation(value = "实时数据刷新",httpMethod = "GET")
    @GetMapping("/refresh")
    public AjaxResult refresh(){
        pageHomeDataTask.getPageHomeDataData();
        return AjaxResult.success();
    }
}
