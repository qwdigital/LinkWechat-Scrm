package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.WeTaskFission;
import com.linkwechat.wecom.service.IWeTaskFissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 任务宝Controller
 *
 * @author leejoker
 * @date 2021-01-20
 */
@RestController
@RequestMapping("/wecom/fission")
public class WeTaskFissionController extends BaseController {
    @Autowired
    private IWeTaskFissionService weTaskFissionService;

    /**
     * 查询任务宝列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:fission:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeTaskFission weTaskFission) {
        startPage();
        List<WeTaskFission> list = weTaskFissionService.selectWeTaskFissionList(weTaskFission);
        return getDataTable(list);
    }

    /**
     * 导出任务宝列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:fission:export')")
    @Log(title = "任务宝", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeTaskFission weTaskFission) {
        List<WeTaskFission> list = weTaskFissionService.selectWeTaskFissionList(weTaskFission);
        ExcelUtil<WeTaskFission> util = new ExcelUtil<WeTaskFission>(WeTaskFission.class);
        return util.exportExcel(list, "fission");
    }

    /**
     * 获取任务宝详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:fission:query')")
    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weTaskFissionService.selectWeTaskFissionById(id));
    }

    /**
     * 新增任务宝
     */
    @PreAuthorize("@ss.hasPermi('wecom:fission:add')")
    @Log(title = "任务宝", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeTaskFission weTaskFission) {
        return toAjax(weTaskFissionService.insertWeTaskFission(weTaskFission));
    }

    /**
     * 修改任务宝
     */
    @PreAuthorize("@ss.hasPermi('wecom:fission:edit')")
    @Log(title = "任务宝", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeTaskFission weTaskFission) {
        return toAjax(weTaskFissionService.updateWeTaskFission(weTaskFission));
    }

    /**
     * 删除任务宝
     */
    @PreAuthorize("@ss.hasPermi('wecom:fission:remove')")
    @Log(title = "任务宝", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(weTaskFissionService.deleteWeTaskFissionByIds(ids));
    }

    /**
     * 发送裂变任务
     */
    @PreAuthorize("@ss.hasPermi('wecom:fission:send')")
    @Log(title = "发送裂变任务", businessType = BusinessType.OTHER)
    @GetMapping("/{id}")
    public AjaxResult send(@PathVariable Long id) {
        weTaskFissionService.sendWeTaskFission(id);
        return AjaxResult.success();
    }
}
