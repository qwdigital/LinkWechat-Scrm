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
import com.linkwechat.wecom.domain.WeGroupCode;
import com.linkwechat.wecom.service.IWeGroupCodeService;


/**
 * 客户群活码Controller
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
@RestController
@RequestMapping("/wecom/groupCode")
public class WeGroupCodeController extends BaseController
{
    @Autowired
    private IWeGroupCodeService weGroupCodeService;

    /**
     * 查询客户群活码列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeGroupCode weGroupCode)
    {
        startPage();
        List<WeGroupCode> list = weGroupCodeService.selectWeGroupCodeList(weGroupCode);
        return getDataTable(list);
    }

    /**
     * 导出客户群活码列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:export')")
    @Log(title = "客户群活码", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeGroupCode weGroupCode)
    {
        List<WeGroupCode> list = weGroupCodeService.selectWeGroupCodeList(weGroupCode);
        ExcelUtil<WeGroupCode> util = new ExcelUtil<WeGroupCode>(WeGroupCode.class);
        return util.exportExcel(list, "code");
    }

    /**
     * 获取客户群活码详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weGroupCodeService.selectWeGroupCodeById(id));
    }

    /**
     * 新增客户群活码
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:add')")
    @Log(title = "客户群活码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeGroupCode weGroupCode)
    {
        return toAjax(weGroupCodeService.insertWeGroupCode(weGroupCode));
    }

    /**
     * 修改客户群活码
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:edit')")
    @Log(title = "客户群活码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeGroupCode weGroupCode)
    {
        return toAjax(weGroupCodeService.updateWeGroupCode(weGroupCode));
    }

    /**
     * 删除客户群活码
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:remove')")
    @Log(title = "客户群活码", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weGroupCodeService.deleteWeGroupCodeByIds(ids));
    }
}
