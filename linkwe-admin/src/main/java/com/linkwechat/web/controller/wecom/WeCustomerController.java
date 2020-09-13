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
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.service.IWeCustomerService;

/**
 * 企业微信客户Controller
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
@RestController
@RequestMapping("/wecom/customer")
public class WeCustomerController extends BaseController
{
    @Autowired
    private IWeCustomerService weCustomerService;

    /**
     * 查询企业微信客户列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeCustomer weCustomer)
    {
        startPage();
        List<WeCustomer> list = weCustomerService.selectWeCustomerList(weCustomer);
        return getDataTable(list);
    }

    /**
     * 导出企业微信客户列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:customer:export')")
    @Log(title = "企业微信客户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeCustomer weCustomer)
    {
        List<WeCustomer> list = weCustomerService.selectWeCustomerList(weCustomer);
        ExcelUtil<WeCustomer> util = new ExcelUtil<WeCustomer>(WeCustomer.class);
        return util.exportExcel(list, "customer");
    }

    /**
     * 获取企业微信客户详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:customer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weCustomerService.selectWeCustomerById(id));
    }

    /**
     * 新增企业微信客户
     */
    @PreAuthorize("@ss.hasPermi('wecom:customer:add')")
    @Log(title = "企业微信客户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeCustomer weCustomer)
    {
        return toAjax(weCustomerService.insertWeCustomer(weCustomer));
    }

    /**
     * 修改企业微信客户
     */
    @PreAuthorize("@ss.hasPermi('wecom:customer:edit')")
    @Log(title = "企业微信客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeCustomer weCustomer)
    {
        return toAjax(weCustomerService.updateWeCustomer(weCustomer));
    }

    /**
     * 删除企业微信客户
     */
    @PreAuthorize("@ss.hasPermi('wecom:customer:remove')")
    @Log(title = "企业微信客户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weCustomerService.deleteWeCustomerByIds(ids));
    }
}
