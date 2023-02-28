package com.linkwechat.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.strategiccrowd.CorpAddStateEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.spring.SpringUtils;
import com.linkwechat.domain.WeStrategicCrowd;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.strategic.crowd.query.WeAddStrategicCrowdQuery;
import com.linkwechat.domain.strategic.crowd.query.WeCorpStateTagSourceQuery;
import com.linkwechat.domain.strategic.crowd.query.WeStrategicCrowdListQuery;
import com.linkwechat.domain.strategic.crowd.query.WeStrategicCrowdQuery;
import com.linkwechat.domain.strategic.crowd.vo.WeStrategicCrowdAnalyzelVo;
import com.linkwechat.domain.strategic.crowd.vo.WeStrategicCrowdDetailVo;
import com.linkwechat.service.IWeStrategicCrowdService;
import com.linkwechat.service.IWeStrategicCrowdStateTagService;
import com.linkwechat.service.impl.WeCategoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author danmo
 * @description 策略人群
 * @date 2022/07/05 18:22
 **/

@RestController
@RequestMapping(value = "strategic/crowd")
@Api(tags = "策略人群")
public class WeStrategicCrowdController extends BaseController {

    @Autowired
    private IWeStrategicCrowdService weStrategicCrowdService;


    @ApiOperation(value = "获取策略下拉选项", httpMethod = "POST")
    @PostMapping("/getDownOptions")
    public AjaxResult<Map<String, Object>> getDownOptions(@RequestBody List<String> enumNames) {
        return AjaxResult.success(weStrategicCrowdService.getDownOptions(enumNames));
    }

    @ApiOperation(value = "获取策略渠道来源", httpMethod = "GET")
    @GetMapping("/getCorpStateTagSource")
    public TableDataInfo getCorpStateTagSource(WeCorpStateTagSourceQuery query) {
        CorpAddStateEnum corpAddStateEnum = CorpAddStateEnum.parseEnum(query.getCode());
        if(corpAddStateEnum == null){
            throw new WeComException("无效CODE");
        }
        String method = corpAddStateEnum.getMethod();
        startPage();
        List<Map<String,Object>> stateTagSourceList = SpringUtils.getBean(method, IWeStrategicCrowdStateTagService.class).getStateTagSourceList(query);
        return getDataTable(stateTagSourceList);
    }


}
