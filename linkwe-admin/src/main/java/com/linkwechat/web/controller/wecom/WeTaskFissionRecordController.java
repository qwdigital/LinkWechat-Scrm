package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeTaskFissionRecord;
import com.linkwechat.wecom.service.IWeTaskFissionRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 裂变任务完成记录Controller
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Api(description = "裂变任务完成记录Controller")
@RestController
@RequestMapping("/wecom/record")
public class WeTaskFissionRecordController extends BaseController {
    @Autowired
    private IWeTaskFissionRecordService weTaskFissionRecordService;

    /**
     * 查询裂变任务完成记录列表
     */
    @ApiOperation(value = "查询裂变任务完成记录列表",httpMethod = "GET")
    //   @PreAuthorize("@ss.hasPermi('wecom:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeTaskFissionRecord weTaskFissionRecord) {
        startPage();
        List<WeTaskFissionRecord> list = weTaskFissionRecordService.selectWeTaskFissionRecordList(weTaskFissionRecord);
        return getDataTable(list);
    }

    /**
     * 导出裂变任务完成记录列表
     */
    @ApiOperation(value = "导出裂变任务完成记录列表",httpMethod = "GET")
    //    @PreAuthorize("@ss.hasPermi('wecom:record:export')")
    @Log(title = "裂变任务完成记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeTaskFissionRecord weTaskFissionRecord) {
        List<WeTaskFissionRecord> list = weTaskFissionRecordService.selectWeTaskFissionRecordList(weTaskFissionRecord);
        ExcelUtil<WeTaskFissionRecord> util = new ExcelUtil<WeTaskFissionRecord>(WeTaskFissionRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 获取裂变任务完成记录详细信息
     */
    @ApiOperation(value = "获取裂变任务完成记录详细信息",httpMethod = "GET")
    //   @PreAuthorize("@ss.hasPermi('wecom:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weTaskFissionRecordService.selectWeTaskFissionRecordById(id));
    }

    /**
     * 新增裂变任务完成记录
     */
    @ApiOperation(value = "新增裂变任务完成记录",httpMethod = "POST")
    //   @PreAuthorize("@ss.hasPermi('wecom:record:add')")
    @Log(title = "裂变任务完成记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody WeTaskFissionRecord weTaskFissionRecord) {
        return toAjax(weTaskFissionRecordService.insertWeTaskFissionRecord(weTaskFissionRecord));
    }

    /**
     * 修改裂变任务完成记录
     */
    @ApiOperation(value = "修改裂变任务完成记录",httpMethod = "PUT")
    //   @PreAuthorize("@ss.hasPermi('wecom:record:edit')")
    @Log(title = "裂变任务完成记录", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody WeTaskFissionRecord weTaskFissionRecord) {
        return toAjax(weTaskFissionRecordService.updateWeTaskFissionRecord(weTaskFissionRecord));
    }

    /**
     * 删除裂变任务完成记录
     */
    @ApiOperation(value = "删除裂变任务完成记录",httpMethod = "DELETE")
    //   @PreAuthorize("@ss.hasPermi('wecom:record:remove')")
    @Log(title = "裂变任务完成记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(weTaskFissionRecordService.deleteWeTaskFissionRecordByIds(ids));
    }
}
