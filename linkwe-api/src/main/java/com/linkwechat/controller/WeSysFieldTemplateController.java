package com.linkwechat.controller;


import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.WeSysFieldTemplate;
import com.linkwechat.service.IWeSysFieldTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;


/**
 * 客户画像自定义模版
 */
@RestController
@RequestMapping(value = "field")
public class WeSysFieldTemplateController {


    @Autowired
    IWeSysFieldTemplateService iWeSysFieldTemplateService;


    /**
     * 获取列表
     * @param weSysFieldTemplate
     * @return
     */
    @GetMapping("/list")
    public AjaxResult<List<WeSysFieldTemplate>> list(WeSysFieldTemplate weSysFieldTemplate){

        return AjaxResult.success(
                iWeSysFieldTemplateService.findLists(weSysFieldTemplate)
        );
    }


    /**
     * 新增
     * @param sysFieldTemplate
     * @return
     */
    @PostMapping("/addSysField")
    public AjaxResult  addSysField(@RequestBody WeSysFieldTemplate sysFieldTemplate){
        iWeSysFieldTemplateService.addSysField(sysFieldTemplate);
        return AjaxResult.success();
    }


    /**
     * 更新
     * @param sysFieldTemplate
     * @return
     */
    @PutMapping("/updateSysField")
    public AjaxResult updateSysField(@RequestBody WeSysFieldTemplate sysFieldTemplate){
        iWeSysFieldTemplateService.updateSysField(sysFieldTemplate);
        return AjaxResult.success();
    }


    /**
     * 批量更新
     * @param sysFieldTemplates
     * @return
     */
    @PostMapping("/batchUpdateSysField")
    public AjaxResult batchUpdateSysField(@RequestBody List<WeSysFieldTemplate> sysFieldTemplates){
        iWeSysFieldTemplateService.updateBatchById(sysFieldTemplates);
        return AjaxResult.success();
    }


    /**
     * 通过id列表批量删除识客码
     *
     * @param ids id列表
     * @return 结果
     */
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteSysField(@PathVariable("ids") Long[] ids) {
        iWeSysFieldTemplateService.removeByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }








}
