package com.linkwechat.controller;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.domain.msgtlp.query.WeMsgTlpAddQuery;
import com.linkwechat.domain.msgtlp.query.WeMsgTlpQuery;
import com.linkwechat.domain.msgtlp.vo.WeMsgTlpVo;
import com.linkwechat.service.IWeMsgTlpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sxw
 * @description 欢迎语管理
 * @date 2022/3/26 10:29
 **/
@RestController
@RequestMapping(value = "tlp")
@Api(tags = "欢迎语管理")
public class WeMsgTlpController extends BaseController {

    @Autowired
    private IWeMsgTlpService weMsgTlpService;

    @ApiOperation(value = "新增欢迎语模板", httpMethod = "POST")
    @Log(title = "欢迎语管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addMsgTlp(@RequestBody @Validated WeMsgTlpAddQuery query) {
        weMsgTlpService.addMsgTlp(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "修改欢迎语模板", httpMethod = "PUT")
    @Log(title = "欢迎语管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateMsgTlp(@RequestBody @Validated WeMsgTlpAddQuery query) {
        weMsgTlpService.updateMsgTlp(query);
        return AjaxResult.success();
    }

    @ApiOperation(value = "删除欢迎语模板", httpMethod = "DELETE")
    @Log(title = "欢迎语管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/del/{ids}")
    public AjaxResult delMsgTlp(@PathVariable("ids") List<Long> ids) {
        weMsgTlpService.delMsgTlp(ids);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取欢迎语模板列表", httpMethod = "GET")
    @Log(title = "欢迎语管理", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    public TableDataInfo getList(WeMsgTlpQuery query) {
        startPage();
        List<WeMsgTlpVo> weMsgTlpVoList = weMsgTlpService.getList(query);
        return getDataTable(weMsgTlpVoList);
    }

    @ApiOperation(value = "获取欢迎语模板详情", httpMethod = "GET")
    @Log(title = "欢迎语管理", businessType = BusinessType.SELECT)
    @GetMapping("/get/{id}")
    public AjaxResult<WeMsgTlpVo> getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weMsgTlpService.getInfo(id));
    }
}
