package com.linkwechat.web.controller.wecom;

import java.util.List;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.linkwechat.wecom.domain.WeGroupCodeActual;
import com.linkwechat.wecom.service.IWeGroupCodeActualService;

/**
 * 实际群码Controller
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
@RestController
@RequestMapping("/wecom/actual")
public class WeGroupCodeActualController extends BaseController
{
    @Autowired
    private IWeGroupCodeActualService weGroupCodeActualService;

    /**
     * 查询实际群码列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:actual:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeGroupCodeActual weGroupCodeActual)
    {
        startPage();
        List<WeGroupCodeActual> list = weGroupCodeActualService.selectWeGroupCodeActualList(weGroupCodeActual);
        return getDataTable(list);
    }

    /**
     * 导出实际群码列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:actual:export')")
    @Log(title = "实际群码", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeGroupCodeActual weGroupCodeActual)
    {
        List<WeGroupCodeActual> list = weGroupCodeActualService.selectWeGroupCodeActualList(weGroupCodeActual);
        ExcelUtil<WeGroupCodeActual> util = new ExcelUtil<WeGroupCodeActual>(WeGroupCodeActual.class);
        return util.exportExcel(list, "actual");
    }

    /**
     * 获取实际群码详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:actual:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weGroupCodeActualService.selectWeGroupCodeActualById(id));
    }

    /**
     * 新增实际群码
     */
    @PreAuthorize("@ss.hasPermi('wecom:actual:add')")
    @Log(title = "实际群码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeGroupCodeActual weGroupCodeActual)
    {
        return toAjax(weGroupCodeActualService.insertWeGroupCodeActual(weGroupCodeActual));
    }

    /**
     * 修改实际群码
     */
    @PreAuthorize("@ss.hasPermi('wecom:actual:edit')")
    @Log(title = "实际群码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeGroupCodeActual weGroupCodeActual)
    {
        return toAjax(weGroupCodeActualService.updateWeGroupCodeActual(weGroupCodeActual));
    }

    /**
     * 删除实际群码
     */
    @PreAuthorize("@ss.hasPermi('wecom:actual:remove')")
    @Log(title = "实际群码", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weGroupCodeActualService.deleteWeGroupCodeActualByIds(ids));
    }
}
