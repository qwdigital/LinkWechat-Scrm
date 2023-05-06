package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.qirule.query.WeQiRuleAddQuery;
import com.linkwechat.domain.qirule.query.WeQiRuleListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleDetailVo;
import com.linkwechat.domain.qirule.vo.WeQiRuleListVo;
import com.linkwechat.service.IWeQiRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会话质检管理
 *
 * @author danmo
 * @date 2023/05/05 18:22
 **/

@RestController
@RequestMapping(value = "qi")
@Api(tags = "会话质检管理")
public class WeQiRuleController extends BaseController {

    @Autowired
    private IWeQiRuleService weQiRuleService;

    @ApiOperation(value = "新增质检规则", httpMethod = "POST")
    @PostMapping("/add")
    public AjaxResult addQiRule(@RequestBody @Validated WeQiRuleAddQuery query) {
        weQiRuleService.addQiRule(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "修改质检规则", httpMethod = "PUT")
    @PutMapping("/update/{id}")
    public AjaxResult updateQiRule(@PathVariable("id") Long id, @RequestBody @Validated WeQiRuleAddQuery query) {
        query.setQiId(id);
        weQiRuleService.updateQiRule(query);
        return AjaxResult.success();
    }


    @ApiOperation(value = "获取质检规则详情", httpMethod = "GET")
    @GetMapping("/get/{id}")
    public AjaxResult<WeQiRuleDetailVo> getQiRuleDetail(@PathVariable("id") Long id) {
        WeQiRuleDetailVo detail = weQiRuleService.getQiRuleDetail(id);
        return AjaxResult.success(detail);
    }

    @ApiOperation(value = "获取质检规则列表", httpMethod = "GET")
    @GetMapping("/list")
    public TableDataInfo<WeQiRuleListVo> getQiRuleList(WeQiRuleListQuery query) {
        startPage();
        List<WeQiRuleListVo> qrCodeList = weQiRuleService.getQiRuleList(query);
        return getDataTable(qrCodeList);
    }

    @ApiOperation(value = "删除质检规则", httpMethod = "DELETE")
    @DeleteMapping("/del/{ids}")
    public AjaxResult delQiRule(@PathVariable("ids") List<Long> ids) {
        weQiRuleService.delQiRule(ids);
        return AjaxResult.success();
    }
}
