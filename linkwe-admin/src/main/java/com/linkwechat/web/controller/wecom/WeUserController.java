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
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.service.IWeUserService;


/**
 * 通讯录相关客户Controller
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
@RestController
@RequestMapping("/wecom/user")
public class WeUserController extends BaseController
{
    @Autowired
    private IWeUserService weUserService;

    /**
     * 查询通讯录相关客户列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeUser weUser)
    {
        startPage();
        List<WeUser> list = weUserService.selectWeUserList(weUser);
        return getDataTable(list);
    }

    /**
     * 导出通讯录相关客户列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:export')")
    @Log(title = "通讯录相关客户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeUser weUser)
    {
        List<WeUser> list = weUserService.selectWeUserList(weUser);
        ExcelUtil<WeUser> util = new ExcelUtil<WeUser>(WeUser.class);
        return util.exportExcel(list, "user");
    }

    /**
     * 获取通讯录相关客户详细信息
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(weUserService.selectWeUserById(id));
    }

    /**
     * 新增通讯录相关客户
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:add')")
    @Log(title = "通讯录相关客户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WeUser weUser)
    {
        return toAjax(weUserService.insertWeUser(weUser));
    }

    /**
     * 修改通讯录相关客户
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:edit')")
    @Log(title = "通讯录相关客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WeUser weUser)
    {
        return toAjax(weUserService.updateWeUser(weUser));
    }

    /**
     * 删除通讯录相关客户
     */
    @PreAuthorize("@ss.hasPermi('wecom:user:remove')")
    @Log(title = "通讯录相关客户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weUserService.deleteWeUserByIds(ids));
    }
}
