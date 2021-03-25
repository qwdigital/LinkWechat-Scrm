package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeTaskFissionStaff;
import com.linkwechat.wecom.service.IWeTaskFissionStaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 裂变任务员工列Controller
 *
 * @author leejoker
 * @date 2021-01-20
 */
@Api(description = "裂变任务员工列Controller")
@RestController
@RequestMapping("/wecom/staff")
public class WeTaskFissionStaffController extends BaseController {
    @Autowired
    private IWeTaskFissionStaffService weTaskFissionStaffService;

    /**
     * 查询裂变任务员工列列表
     */
    @ApiOperation(value = "查询裂变任务员工列列表",httpMethod = "GET")
    //  @PreAuthorize("@ss.hasPermi('wecom:staff:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeTaskFissionStaff weTaskFissionStaff) {
        startPage();
        List<WeTaskFissionStaff> list = weTaskFissionStaffService.selectWeTaskFissionStaffList(weTaskFissionStaff);
        return getDataTable(list);
    }

    /**
     * 导出裂变任务员工列列表
     */
    @ApiOperation(value = "导出裂变任务员工列列表",httpMethod = "GET")
    //   @PreAuthorize("@ss.hasPermi('wecom:staff:export')")
    @Log(title = "裂变任务员工列", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeTaskFissionStaff weTaskFissionStaff) {
        List<WeTaskFissionStaff> list = weTaskFissionStaffService.selectWeTaskFissionStaffList(weTaskFissionStaff);
        ExcelUtil<WeTaskFissionStaff> util = new ExcelUtil<WeTaskFissionStaff>(WeTaskFissionStaff.class);
        return util.exportExcel(list, "staff");
    }

    /**
     * 获取裂变任务员工列详细信息
     */
    @ApiOperation(value = "获取裂变任务员工列详细信息",httpMethod = "GET")
    //   @PreAuthorize("@ss.hasPermi('wecom:staff:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weTaskFissionStaffService.selectWeTaskFissionStaffById(id));
    }

    /**
     * 新增裂变任务员工列
     */
    @ApiOperation(value = "新增裂变任务员工列",httpMethod = "POST")
    //   @PreAuthorize("@ss.hasPermi('wecom:staff:add')")
    @Log(title = "裂变任务员工列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody WeTaskFissionStaff weTaskFissionStaff) {
        return toAjax(weTaskFissionStaffService.insertWeTaskFissionStaff(weTaskFissionStaff));
    }

    /**
     * 修改裂变任务员工列
     */
    @ApiOperation(value = "修改裂变任务员工列",httpMethod = "PUT")
    //   @PreAuthorize("@ss.hasPermi('wecom:staff:edit')")
    @Log(title = "裂变任务员工列", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody WeTaskFissionStaff weTaskFissionStaff) {
        return toAjax(weTaskFissionStaffService.updateWeTaskFissionStaff(weTaskFissionStaff));
    }

    /**
     * 删除裂变任务员工列
     */
    @ApiOperation(value = "删除裂变任务员工列",httpMethod = "DELETE")
    //   @PreAuthorize("@ss.hasPermi('wecom:staff:remove')")
    @Log(title = "裂变任务员工列", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(weTaskFissionStaffService.deleteWeTaskFissionStaffByIds(ids));
    }
}
