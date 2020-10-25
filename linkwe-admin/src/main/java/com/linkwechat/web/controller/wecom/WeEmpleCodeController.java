package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.ListUtil;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.service.IWeEmpleCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 员工活码Controller
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@RestController
@RequestMapping("/wecom/code")
public class WeEmpleCodeController extends BaseController
{
    @Autowired
    private IWeEmpleCodeService weEmpleCodeService;

    /**
     * 查询员工活码列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeEmpleCode weEmpleCode)
    {
        startPage();
        List<WeEmpleCode> list = weEmpleCodeService.selectWeEmpleCodeList(weEmpleCode);
        return getDataTable(list);
    }


    /**
     * 获取员工活码详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weEmpleCodeService.selectWeEmpleCodeById(id));
    }

    /**
     * 新增员工活码
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:add')")
    @Log(title = "员工活码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeEmpleCode weEmpleCode)
    {
        return toAjax(weEmpleCodeService.insertWeEmpleCode(weEmpleCode));
    }

    /**
     * 修改员工活码
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:edit')")
    @Log(title = "员工活码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeEmpleCode weEmpleCode)
    {
        return toAjax(weEmpleCodeService.updateWeEmpleCode(weEmpleCode));
    }

    /**
     * 删除员工活码
     */
    @PreAuthorize("@ss.hasPermi('wecom:code:remove')")
    @Log(title = "员工活码", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(weEmpleCodeService.batchRemoveWeEmpleCodeIds(ListUtil.toList(ids)));
    }
}
