package com.linkwechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.kf.query.WeKfAddMsgQuery;
import com.linkwechat.domain.kf.vo.WeKfEvaluationVo;
import com.linkwechat.service.IWeKfPoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author danmo
 * @description 客服管理
 * @date 2022/1/18 21:44
 **/
@Slf4j
@Api(tags = "客服管理")
@RestController
@RequestMapping("/kf/")
public class WxKfController extends BaseController {

    @Autowired
    private IWeKfPoolService weKfPoolService;

    @ApiOperation("查询评价")
    @GetMapping("/msg/evaluation/get")
    public AjaxResult<WeKfEvaluationVo> getEvaluation(WeKfAddMsgQuery query) {
        if (Objects.isNull(query)) {
            throw new WeComException("参数不能为空");
        }
        if (StringUtils.isEmpty(query.getPoolId())) {
            throw new WeComException("客服ID不能为空");
        }
        WeKfEvaluationVo customerMsg = weKfPoolService.getEvaluation(query);
        return AjaxResult.success(customerMsg);
    }

    @ApiOperation("新增评价语")
    @PostMapping("/msg/evaluation/add")
    public AjaxResult addEvaluation(@Validated @RequestBody WeKfAddMsgQuery query) {
        log.info("新增评价语入参：param:{}", JSONObject.toJSONString(query));
        weKfPoolService.addEvaluation(query);
        return AjaxResult.success();
    }

}
