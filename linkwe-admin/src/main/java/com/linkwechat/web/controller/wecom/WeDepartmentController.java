package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.service.IWeDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业微信组织架构相关Controller
 *
 * @author ruoyi
 * @date 2020-09-01
 */
@Api(tags = "微信组织架构相关接口")
@RestController
@RequestMapping("/wecom/department")
public class WeDepartmentController extends BaseController {
    @Autowired
    private IWeDepartmentService weDepartmentService;


    /**
     * 查询企业微信组织架构相关列表
     */
    //  @PreAuthorize("@ss.hasPermi('contacts:organization:list')")
    @GetMapping("/list")
    @ApiOperation("获取部门列表")
    public AjaxResult<List<WeDepartment>> list() {
        List<WeDepartment> list = weDepartmentService.getList();
        return AjaxResult.success(list);
    }


    /**
     * 新增企业微信组织架构相关
     */
    // @PreAuthorize("@ss.hasPermi('contacts:organization:addMember')")
    @Log(title = "企业微信组织架构相关", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("添加部门")
    public AjaxResult add(@Validated @RequestBody WeDepartment weDepartment) {
        weDepartmentService.insert(weDepartment);
        return AjaxResult.success();
    }

    /**
     * 修改企业微信组织架构相关
     */
    //  @PreAuthorize("@ss.hasPermi('contacts:organization:editDep')")
    @Log(title = "企业微信组织架构相关", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("更新部门")
    public AjaxResult edit(@RequestBody WeDepartment weDepartment) {
        weDepartmentService.update(weDepartment);
        return AjaxResult.success();
    }

    /**
     * 删除企业微信组织架构相关
     */
    //  @PreAuthorize("@ss.hasPermi('wecom:department:remove')")
    @Log(title = "企业微信组织架构相关", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {

        weDepartmentService.deleteByIds(ids);

        return AjaxResult.success();
    }

}
