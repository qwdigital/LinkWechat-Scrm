package com.linkwechat.controller;


import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.WeFormSurveyCatalogue;
import com.linkwechat.domain.form.query.WeAddFormSurveyCatalogueQuery;
import com.linkwechat.domain.form.query.WeFormSurveyCatalogueQuery;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 智能表单基本目录表Controller
 *
 * @author HXD
 * @date 2022-05-27 10:08
 */
@RestController
@RequestMapping("/form/survey")
@Api(tags = "智能表单接口")
@Slf4j
public class WeFormSurveyCatalogueController extends BaseController {

    @Autowired
    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;


    @PostMapping(value = "/add")
    @ApiOperation(value = "新增表单基本信息", httpMethod = "POST")
    public AjaxResult add(@RequestBody @Validated WeAddFormSurveyCatalogueQuery query) {
        return AjaxResult.success(weFormSurveyCatalogueService.add(query));
    }

    /**
     * 删除表单
     */
    @DeleteMapping(value = "/delete/{ids}")
    @ApiOperation(value = "删除表单", httpMethod = "DELETE")
    public AjaxResult deleteSurvey(@PathVariable("ids") List<Long> ids) {
        weFormSurveyCatalogueService.deleteSurvey(ids);
        return AjaxResult.success();
    }

    /**
     * 更新表单
     */
    @PutMapping(value = "/update")
    @ApiOperation(value = "更新表单", httpMethod = "PUT")
    public AjaxResult updateSurvey(@RequestBody @Validated WeAddFormSurveyCatalogueQuery query) {
        weFormSurveyCatalogueService.updateSurvey(query);
        return AjaxResult.success();
    }

    /**
     * 查询表单详情
     */
    @GetMapping(value = "/getInfo/{id}")
    @ApiOperation(value = "查询表单详情", httpMethod = "GET")
    public AjaxResult<WeFormSurveyCatalogue> getInfo(@PathVariable("id") Long id) {
        WeFormSurveyCatalogue info = weFormSurveyCatalogueService.getInfo(id,null,null,false);
        return AjaxResult.success(info);
    }

    /**
     * 查询表单列表
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "查询表单列表", httpMethod = "GET")
    public TableDataInfo<WeFormSurveyCatalogue> getList(WeFormSurveyCatalogueQuery query) {
        startPage();
        List<WeFormSurveyCatalogue> list = weFormSurveyCatalogueService.getList(query);
        return getDataTable(list);
    }

    /**
     * 更新表单状态（
     * 1.启用 2.暂停 3.结束）
     */
    @PutMapping(value = "/updateStatus")
    @ApiOperation(value = "更新表单状态", httpMethod = "PUT")
    public AjaxResult updateStatus(@RequestBody WeFormSurveyCatalogueQuery query) {
        weFormSurveyCatalogueService.updateStatus(query);
        return AjaxResult.success();
    }


    @ApiOperation(value = "删除表单分组", httpMethod = "GET")
    @GetMapping("/deleteGroup")
    public AjaxResult deleteFormSurveyGroup(@RequestParam("groupId") Long groupId) {
        weFormSurveyCatalogueService.deleteFormSurveyGroup(groupId);
        return AjaxResult.success();
    }
}
