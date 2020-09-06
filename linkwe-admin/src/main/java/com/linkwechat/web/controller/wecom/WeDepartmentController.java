package com.linkwechat.web.controller.wecom;

import java.util.List;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.service.IWeDepartmentService;

/**
 * 企业微信组织架构相关Controller
 * 
 * @author ruoyi
 * @date 2020-09-01
 */
@Api("微信组织架构相关接口")
@RestController
@RequestMapping("/wecom/department")
public class WeDepartmentController extends BaseController
{
    @Autowired
    private IWeDepartmentService weDepartmentService;

    /**
     * 查询企业微信组织架构相关列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:department:list')")
    @GetMapping("/list")
    @ApiOperation("获取部门列表")
    public TableDataInfo list()
    {
        List<WeDepartment> list = weDepartmentService.selectWeDepartmentList();
        return getDataTable(list);
    }
//
//
//
//    /**
//     * 获取企业微信组织架构相关详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('wecom:department:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return AjaxResult.success(weDepartmentService.selectWeDepartmentById(id));
//    }

    /**
     * 新增企业微信组织架构相关
     */
    @PreAuthorize("@ss.hasPermi('wecom:department:add')")
    @Log(title = "企业微信组织架构相关", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("添加部门")
    public AjaxResult add(@RequestBody WeDepartment weDepartment)
    {
        return toAjax(weDepartmentService.insertWeDepartment(weDepartment));
    }

    /**
     * 修改企业微信组织架构相关
     */
    @PreAuthorize("@ss.hasPermi('wecom:department:edit')")
    @Log(title = "企业微信组织架构相关", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("更新部门")
    public AjaxResult edit(@RequestBody WeDepartment weDepartment)
    {
        return toAjax(weDepartmentService.updateWeDepartment(weDepartment));
    }
//
//    /**
//     * 删除企业微信组织架构相关
//     */
//    @PreAuthorize("@ss.hasPermi('wecom:department:remove')")
//    @Log(title = "企业微信组织架构相关", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(weDepartmentService.deleteWeDepartmentByIds(ids));
//    }
}
